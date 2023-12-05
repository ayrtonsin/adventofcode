package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class Day1 {

  @Test
  void calibrateValuesPart1() throws IOException {

    var input = getInput("inputDay1.txt");
    var sum = input
        .stream()
        .map(line -> line.toLowerCase().replaceAll("[a-z]", ""))
        .map(this::keepFirstLast)
        .mapToInt(Integer::parseInt)
        .sum();
    log.info("sum is {}", sum);
    assertThat(sum).isEqualTo(53334);
  }

  @Test
  void calibrateValuesPart2() throws IOException {

    var input = getInput("inputDay1.txt");
    var sum = getSumV2(input);
    assertThat(sum).isEqualTo(281);//too low 52418
  }

  private int getSumV2(List<String> input) {
    var sum = input
        .stream()
        .map(this::replaceNumberStrings)
        .map(line -> line.replaceAll("[a-z]", ""))
        .map(this::keepFirstLast)
        .mapToInt(Integer::parseInt)
        .sum();
    log.info("sum is {}", sum);
    return sum;
  }

  private String keepFirstLast(String str) {
    var fl = str.substring(0, 1) + str.substring(str.length() - 1);
    log.info("coordinates are {}", fl);
    return fl;
  }

  private static final List<String> numbersAsString = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

  private String replaceNumberStrings(String input) {
    var line = input.toLowerCase();
    var nextNumber = firstNumberString(line);
    while (nextNumber.isPresent()) {
      line = line.replaceFirst(nextNumber.orElseThrow(), String.valueOf(numbersAsString.indexOf(nextNumber.get()) + 1));
      nextNumber = firstNumberString(line);
    }

    log.info("replaced {} with {}", input, line);
    return line;
  }

  private Optional<String> firstNumberString(String input) {
    return numbersAsString.stream()
        .filter(n -> input.indexOf(n) > -1)
        .min(Comparator.comparingInt(input::indexOf));
  }
}
