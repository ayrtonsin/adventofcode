package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day13 {

  record Pattern(char[][] matrix, char[][] transposed) {

    public static Pattern fromInput(List<String> lines) {
      var matrix = MatrixUtils.getMatrix(lines);
      var transposed = MatrixUtils.transposeMatrix(matrix);
      return new Pattern(matrix, transposed);
    }

    public int getResult() {
      return getVerticalResult(-1).or(() -> getHorizontalResult(-1)).orElseThrow();
    }

    public int getResultWithSmudge() {
      var originalHorizontal = getHorizontalResult(-1);
      var originalVertical = getVerticalResult(-1);
      int original = originalHorizontal.or(() -> originalVertical).orElseThrow();
      int originalIdx = original > 100 ? original / 100 - 1 : original - 1;

      for (int y = 0; y < matrix().length; y++) {
        for (int x = 0; x < matrix()[y].length; x++) {
          var ori = matrix()[y][x];
          var ori2 = transposed()[x][y];
          if (ori != ori2) {
            throw new RuntimeException("oei oei");
          }
          matrix()[y][x] = ori == '#' ? '.' : '#';
          transposed()[x][y] = ori == '#' ? '.' : '#';
          var smudged = getHorizontalResult(originalHorizontal.isPresent() ? originalIdx : -1)
              .filter(i -> !i.equals(original))
              .or(() -> getVerticalResult(originalVertical.isPresent() ? originalIdx : -1))
              .filter(i -> !i.equals(original));
          if (smudged.isPresent()) {
            return smudged.get();
          }
          matrix()[y][x] = ori;
          transposed()[x][y] = ori;

        }
      }

      throw new RuntimeException("oei");

    }

    private Optional<Integer> getVerticalResult(int ignoreResultIdx) {
      return getReflections(transposed(), ignoreResultIdx)
          .map(reflection -> reflection + 1);
    }

    private Optional<Integer> getHorizontalResult(int ignoreResultIdx) {
      return getReflections(matrix(), ignoreResultIdx)
          .map(reflection -> (reflection + 1) * 100);
    }

    private Optional<Integer> getReflections(char[][] matrix, int ignoreResultIdx) {
      for (int i = matrix.length - 2; i >= 0; i--) {
        var x = matrix[i];
        var y = matrix[i + 1];
        if (Arrays.equals(x, y)) {
          if (ensureMiddle(matrix, i) && ignoreResultIdx != i) {
            return Optional.of(i);
          }
        }
      }
      return Optional.empty();
    }

    private boolean ensureMiddle(char[][] matrix, int middleX) {
      for (int i = 0; i < matrix.length; i++) {
        int middleToBegin = middleX - i / 2;
        int middleToEnd = 1 + middleX + i / 2;

        if (middleToEnd >= matrix.length || middleToBegin < 0) {
          return true;
        }
        var x = matrix[middleToBegin];
        var y = matrix[middleToEnd];
        if (!Arrays.equals(x, y)) {
          return false;
        }
      }
      return true;
    }
  }

  record Valley(List<Pattern> patterns) {

    public static Valley fromInput(List<String> lines) {
      List<Pattern> patterns = new ArrayList<>();
      int start = 0;
      for (int i = 0; i < lines.size(); i++) {
        if (lines.get(i).isBlank()) {
          patterns.add(Pattern.fromInput(lines.subList(start, i)));
          start = i + 1;
        }
      }
      patterns.add(Pattern.fromInput(lines.subList(start, lines.size())));
      return new Valley(patterns);
    }

    public int getResult() {
      return patterns().stream().mapToInt(Pattern::getResult).sum();
    }

    public int getResultWithSmudge() {
      return patterns().stream().mapToInt(Pattern::getResultWithSmudge).sum();
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay13-1.txt");
    var sampleResult = Valley.fromInput(sampleInput).getResult();
    assertThat(sampleResult).isEqualTo(405);

    var sampleInput2 = getInput("sampleDay13-2.txt");
    var sampleResult2 = Valley.fromInput(sampleInput2).getResult();
    assertThat(sampleResult2).isEqualTo(709);

    var input = getInput("inputDay13.txt");
    var result = Valley.fromInput(input).getResult();
    //3248 too low
    //2336 lower wtfSys
    //3548 too low
    // 16376 too low
    //30802
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput = getInput("sampleDay13-1.txt");
    var sampleResult = Valley.fromInput(sampleInput).getResultWithSmudge();
    assertThat(sampleResult).isEqualTo(400);

    var sampleInput2 = getInput("sampleDay13-2.txt");
    var sampleResult2 = Valley.fromInput(sampleInput2).getResultWithSmudge();
    assertThat(sampleResult2).isEqualTo(1400);

    var input = getInput("inputDay13.txt");
    var result = Valley.fromInput(input).getResultWithSmudge();
    //37876
    log.info("result is {}", result);
  }

}



