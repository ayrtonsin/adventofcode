package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DayTemplate {

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay1x-1.txt");

    var sampleResult = "";
    assertThat(sampleResult).isEqualTo(35);

    var input = getInput("inputDay1x.txt");
    var result = "";
    //
    log.info("result is {}", result);
  }

}



