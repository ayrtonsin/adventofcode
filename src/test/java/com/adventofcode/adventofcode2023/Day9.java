package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day9 {

  record History(List<Long> sequence) {

    static History fromInput(String line) {
      return new History(Arrays.stream(line.split(" "))
          .map(Long::parseLong)
          .toList());
    }

    static History fromInputReverse(String line) {
      var normal = new ArrayList<>(fromInput(line).sequence());
      Collections.reverse(normal);
      return new History(normal);
    }

    long extrapolateNext() {

      List<List<Long>> reducedList = new ArrayList<>();
      reducedList.add(this.sequence());
      var current = this.sequence();
//      System.out.println(current);
      do {
        current = extrapolateNextList(current);
//        System.out.println(current);
        reducedList.add(current);

      } while (current.stream().anyMatch(l -> l != 0));
      return reducedList.stream().mapToLong(List::getLast).sum();
    }

    static List<Long> extrapolateNextList(List<Long> seq) {
      return IntStream.range(0, seq.size() - 1)
          .mapToObj(idx -> seq.get(idx + 1) - seq.get(idx))
          .toList();
    }

  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput1 = getInput("sampleDay9-1.txt");
    var sampleResult1 = sampleInput1.stream()
        .map(History::fromInput)
        .mapToLong(History::extrapolateNext)
//        .peek(next -> System.out.println("next is " + next))
        .sum();

    assertThat(sampleResult1).isEqualTo(114);

    assertThat(new History(List.of(-2L, -4L, -6L)).extrapolateNext()).isEqualTo(-8L);
    assertThat(new History(List.of(8L, 6L, 4L)).extrapolateNext()).isEqualTo(2L);
    assertThat(new History(List.of(8L, 6L, 4L, 2L)).extrapolateNext()).isEqualTo(0L);

    var input = getInput("inputDay9.txt");
    var result = input.stream()
        .map(History::fromInput)
        .mapToLong(History::extrapolateNext)
//        .peek(next -> System.out.println("next is " + next))
        .sum();
    //1789637944 too high
    //1789635132
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput1 = getInput("sampleDay9-1.txt");
    var sampleResult1 = sampleInput1.stream()
        .map(History::fromInputReverse)
        .mapToLong(History::extrapolateNext)
//        .peek(next -> System.out.println("next is " + next))
        .sum();

    assertThat(sampleResult1).isEqualTo(2);

    var input = getInput("inputDay9.txt");
    var result = input.stream()
        .map(History::fromInputReverse)
//        .peek(next -> System.out.println("next is " + next))
        .mapToLong(History::extrapolateNext)
        .sum();
    //
    log.info("result is {}", result);
  }
}



