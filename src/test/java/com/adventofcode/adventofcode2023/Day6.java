package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day6 {

  record Race(long time, long distance) {

    public long getMinSpeed() {
      for (long i = 1; i < time; i++) {
        if (i * (time - i) > distance) {
          return i;
        }
      }
      return 0;
    }

    public long getMaxSpeed() {
      for (long i = time - 1; i > 1; i--) {
        if (i * (time - i) > distance) {
          return i;
        }
      }
      return 0;
    }

    public Long getWinCount() {
      var first = getMinSpeed();
      var last = getMaxSpeed();

      return last - first + 1;
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay6-1.txt");

    var sampleResult = toRaces(sampleInput).stream()
        .mapToLong(Race::getWinCount)
        .reduce(Math::multiplyExact)
        .orElse(0);
    assertThat(sampleResult).isEqualTo(288);

    var input = getInput("inputDay6.txt");
    var result = toRaces(input).stream().mapToLong(Race::getWinCount).reduce(Math::multiplyExact).orElse(0);
    //449820
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput = getInput("sampleDay6-1.txt");

    var sampleResult = toRacesV2(sampleInput).getWinCount();
    assertThat(sampleResult).isEqualTo(71503);

    var input = getInput("inputDay6.txt");
    var result = toRacesV2(input).getWinCount();
    log.info("result is {}", result);
    //42250895
  }

  @Test
  void sampleTests() {
    var race1 = new Race(7, 9);
    assertThat(race1.getMinSpeed()).isEqualTo(2);
    assertThat(race1.getMaxSpeed()).isEqualTo(5);

    var race2 = new Race(15, 40);
    assertThat(race2.getMinSpeed()).isEqualTo(4);
    assertThat(race2.getMaxSpeed()).isEqualTo(11);

    var race3 = new Race(30, 200);
    assertThat(race3.getMinSpeed()).isEqualTo(11);
    assertThat(race3.getMaxSpeed()).isEqualTo(19);
  }

  List<Race> toRaces(List<String> input) {
    var races = new ArrayList<Race>();
    var times = Arrays.stream(input.get(0).split(":")[1].split(" ")).map(String::trim).filter(s -> !s.isBlank()).map(Long::parseLong).toList();
    var distances = Arrays.stream(input.get(1).split(":")[1].split(" ")).map(String::trim).filter(s -> !s.isBlank()).map(Long::parseLong).toList();
    for (int i = 0; i < times.size(); i++) {
      races.add(new Race(times.get(i), distances.get(i)));
    }
    return races;
  }

  Race toRacesV2(List<String> input) {
    var time = input.get(0).split(":")[1].replaceAll(" ", "");
    var distance = input.get(1).split(":")[1].replaceAll(" ", "");

    return new Race(Long.parseLong(time), Long.parseLong(distance));
  }
}