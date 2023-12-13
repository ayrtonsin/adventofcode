package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day12 {

  record Arrangement(String springs) {

  }

  record Row(Arrangement springs, List<Integer> groups) {

    private static final Function<ArrangementPart, Long> memoize = Memoizer.memoize(Row::generateArrangements);
    ;

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
      var groups = Arrays.stream(groupString.split(",")).map(Integer::parseInt).toList();
      return new Row(new Arrangement(springString), groups);
    }

    public long getPossibleArrangements() {
      return generateArrangements(new ArrangementPart(springs.springs(), groups));
    }

    //private Function<ArrangementPart, Long> memoizer = Memoizer.memoize(this::generateArrangements);

    private static long generateArrangementsMemoize(ArrangementPart arrangementPart) {
      return memoize.apply(arrangementPart);
    }

    record ArrangementPart(String current, List<Integer> groups) {

    }

    private static long generateArrangements(ArrangementPart arrangementPart) {
      var current = arrangementPart.current();
      var currentGroups = arrangementPart.groups();
      if (current.isEmpty()) {
        return currentGroups.isEmpty() ? 1 : 0;
      }

      var currentSymbol = current.substring(0, 1);
      if (".".equals(currentSymbol)) {
        //operational, continue
        var newArrangement = current.substring(1);
        return generateArrangementsMemoize(new ArrangementPart(newArrangement, currentGroups));
      }

      if ("?".equals(currentSymbol)) {

        var newArrangement = current.substring(1);
        long brokenCount = generateArrangementsMemoize(new ArrangementPart("#" + newArrangement, currentGroups));
        long operationCount = generateArrangementsMemoize(new ArrangementPart("." + newArrangement, currentGroups));

        return brokenCount + operationCount;
      } else {
        if (currentGroups.isEmpty()) {
          return 0;
        }
        // # broken, reduce from current group and checks if still valid
        //next broken chars for current group should either be broken or unknown, if not then its invalid
        int nextOperational = current.indexOf('.');
        Integer firstGroup = currentGroups.getFirst();
        if (nextOperational > -1 && nextOperational < firstGroup) {
          //group doesn't fit
          return 0;
        }
        int maxGroupSize = findMaxBrokenLength(current);
        if (!currentGroups.isEmpty() && maxGroupSize >= firstGroup) {
          if (current.length() == firstGroup) {
            return currentGroups.size() == 1 ? 1 : 0;
          }
          if (current.charAt(firstGroup) == '#') {
            return 0;
          }
          //remove group + a required .
          return generateArrangementsMemoize(new ArrangementPart(current.substring(firstGroup + 1), currentGroups.subList(1, currentGroups.size())));
        } else {
          return 0;
        }
      }
    }

    public static int findMaxBrokenLength(String str) {
      int prefixLength = 0;

      for (char ch : str.toCharArray()) {
        if (ch == '#' || ch == '?') {
          prefixLength++;
        } else {
          break;  // Stop counting when a different character is encountered
        }
      }
      return prefixLength;
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
//          .parallel()
          .mapToLong(Row::getPossibleArrangements)
          .peek(counts -> System.out.println("counts: " + counts))
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
    //4232520187524
    log.info("result is {}", result);
  }

}



