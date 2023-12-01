package com.adventofcode.adventofcode2023;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class Day1Puzzle1 {

  @Test
  void calibrateValues() throws IOException {

    var input = Files.readAllLines(new File("src/main/resources/inputDay1.txt").toPath(), Charset.defaultCharset());
    var sum = input
        .stream()
        .map(line -> line.toLowerCase().replaceAll("[a-z]", ""))
        .map(this::keepFirstLast)
        .mapToInt(Integer::parseInt)
        .sum();
    log.info("sum is {}", sum);
    assertThat(sum).isEqualTo(53334);

  }

  private String keepFirstLast(String str) {
    return str.substring(0, 1) + str.substring(str.length() - 1);
  }
}
