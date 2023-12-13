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

    private static char[][] getMatrix(List<String> lines) {
      char[][] matrix = new char[lines.size()][lines.getFirst().length()];
      for (int y = 0; y < lines.size(); y++) {
        matrix[y] = lines.get(y).toCharArray();
      }
      return matrix;
    }

    private static char[][] transposeMatrix(char[][] matrix) {
      int m = matrix.length;
      int n = matrix[0].length;

      char[][] transposedMatrix = new char[n][m];

      for (int x = 0; x < n; x++) {
        for (int y = 0; y < m; y++) {
          transposedMatrix[x][y] = matrix[y][x];
        }
      }
      return transposedMatrix;
    }

    public static Pattern fromInput(List<String> lines) {
      var matrix = getMatrix(lines);
      var transposed = transposeMatrix(matrix);
      return new Pattern(matrix, transposed);
    }

    public int getResult() {
      return getVerticalResult() + getHorizontalResult();
    }

    private int getVerticalResult() {

      return getAllReflections(transposed())
              .stream()
              .peek(System.out::println)
              .mapToInt(firstReflection ->firstReflection + 1)
//              .mapToInt(firstReflection -> Math.min(firstReflection + 1, this.transposed().length - firstReflection + 1))
              .max().orElse(0);
    }

    private int getHorizontalResult() {
       return getAllReflections(matrix())
               .stream()
               .mapToInt(firstReflection -> (firstReflection + 1) * 100)
//               .mapToInt(firstReflection -> Math.min(firstReflection + 1, this.matrix().length - firstReflection +1) * 100)
               .max().orElse(0);
    }

    private List<Integer> getAllReflections(char[][] matrix) {
      List<Integer> reflections = new ArrayList<>();
      for (int i = 0; i < matrix.length - 1; i++) {
        var x = matrix[i];
        var y = matrix[i + 1];
        if (Arrays.equals(x, y) && ensureMiddle(matrix, i)) {
          reflections.add(i);
        }
      }
      return reflections;
    }

    private boolean ensureMiddle(char[][] matrix, int middleX) {
      for (int i = 0; i < matrix.length; i++) {
        int middleToBegin = middleX - i / 2;
        int middleToEnd = 1 + middleX + i / 2;

        if (middleToEnd >= matrix.length || middleToBegin < 0){
          return true;
        }
        var x = matrix[middleToBegin];
        var y = matrix[middleToEnd];
        if(!Arrays.equals(x, y)){
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
      return 0;//TODO
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
    //2336 lower wtf
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
    var result = Valley.fromInput(input).getResult();
    //3248 too low
    //2336 lower wtf
    //3548 too low
    // 16376 too low
    //30802
    log.info("result is {}", result);
  }

}



