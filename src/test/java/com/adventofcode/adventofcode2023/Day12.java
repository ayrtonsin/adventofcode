package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day12 {

  enum Status {
    BROKEN('#'),
    OPERATIONAL('.'),
    UNKNOWN('?');

    private final char chr;

    private Status(char chr) {
      this.chr = chr;
    }

    public static Status fromChr(char chr) {
      return Arrays.stream(Status.values()).filter(status -> status.chr == chr).findAny().orElseThrow();
    }

    @Override
    public String toString() {
      return String.valueOf(chr);
    }
  }

  record Arrangement(List<Status> springs) {

    boolean isValid(List<Integer> groups) {
      int currentGroupIdx = 0;
      int currentGroupCount = 0;
      for (Status spring : springs) {
        switch (spring) {
          case BROKEN -> currentGroupCount++;
          case OPERATIONAL -> {
            if (currentGroupCount > 0) {
              //close and evaluate current group
              if (currentGroupIdx < groups.size() && currentGroupCount == groups.get(currentGroupIdx)) {
                currentGroupCount = 0;
                currentGroupIdx++;
              } else {
                return false;
              }
            }
          }
        }
      }
//      System.out.println(springs + " " + groups);//[., #, ., ., ., #, ., ., ., ., #, #, #, .] [1, 1, 3]
      return (currentGroupIdx == groups.size() && currentGroupCount == 0)
          || (currentGroupIdx + 1 == groups.size() && currentGroupCount == groups.get(currentGroupIdx));
    }

  }

  record Row(Arrangement springs, List<Integer> groups) {

    static Row fromInput(String input) {
      return fromInput(springString(input), groupString(input));
    }

    private static String groupString(String input) {
      return input.split(" ")[1];
    }

    private static String springString(String input) {
      return input.split(" ")[0];
    }

    public static Row unfold(String input) {
      final var springString = springString(input);
      var expandedSpring = IntStream.range(0, 5).mapToObj(i -> springString).reduce((s1, s2) -> s1 + '?' + s2).orElseThrow();
      final var groupString = groupString(input);
      var expandedGroupString = IntStream.range(0, 5).mapToObj(i -> groupString).reduce((s1, s2) -> s1 + ',' + s2).orElseThrow();
      return fromInput(expandedSpring, expandedGroupString);
    }

    private static Row fromInput(String springString, String groupString) {
      var springs = springString.chars().mapToObj(c -> Status.fromChr((char) c)).toList();
      var groups = Arrays.stream(groupString.split(",")).map(Integer::parseInt).toList();
      return new Row(new Arrangement(springs), groups);
    }

    public long getPossibleArrangements() {
      return generateArrangements(springs);
    }

    private long generateArrangements(Arrangement springs) {
      return generateArrangements(springs.springs().toArray(new Status[0]), 0);
    }

    private long generateArrangements(Status[] current, int index) {
      if (index == current.length) {
        var arrangement = new Arrangement(List.of(current));
        if (arrangement.isValid(groups)) {
          return 1;
        }
        return 0;
      }

      if (Status.UNKNOWN.equals(current[index])) {
        if (Memoizer.memoize(this::isPreInValid).apply(current)) {
          return 0;
        }

        current[index] = Status.BROKEN;
        long brokenCount = generateArrangements(current, index + 1);

        current[index] = Status.OPERATIONAL;
        long operationCount = generateArrangements(current, index + 1);

        //backtrack
        current[index] = Status.UNKNOWN;

        return brokenCount + operationCount;
      } else {
        return generateArrangements(current, index + 1);
      }
    }

    private boolean isPreInValid(Status[] current) {
      int currentGroupIdx = 0;
      int currentGroupCount = 0;
      for (Status status : current) {
        switch (status) {
          case BROKEN -> currentGroupCount++;
          case OPERATIONAL -> {
            if (currentGroupCount > 0) {
              //close and evaluate current group
              if (currentGroupIdx < groups.size() && currentGroupCount == groups.get(currentGroupIdx)) {
                currentGroupCount = 0;
                currentGroupIdx++;
              } else {
                return true;
              }
            }
          }
          case UNKNOWN -> {
            return false;
          }
          default -> throw new IllegalStateException("Unexpected value: " + status);
        }
      }
      return false;

    }
  }

  record Field(List<Row> rows) {

    static Field fromInput(List<String> input) {
      return new Field(input.stream().map(Row::fromInput).toList());
    }

    static Field unfold(List<String> input) {
      return new Field(input.stream().map(Row::unfold).toList());
    }

    public long arrangementsCount() {
      return rows()
          .stream()
          .parallel()
          .mapToLong(Row::getPossibleArrangements)
          .sum();
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay12-1.txt");

    var sampleResult = Field.fromInput(sampleInput).arrangementsCount();
    assertThat(sampleResult).isEqualTo(21);

    var input = getInput("inputDay12.txt");
    var result = Field.fromInput(input).arrangementsCount();
    //
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    //increase heap -Xmx6g
    var sampleInput = getInput("sampleDay12-1.txt");

    var sampleResult = Field.unfold(sampleInput).arrangementsCount();
    assertThat(sampleResult).isEqualTo(525152);

    System.out.println("now the real stuff...");
    var input = getInput("inputDay12.txt");
    var result = Field.unfold(input).arrangementsCount();
    //
    log.info("result is {}", result);
  }

}



