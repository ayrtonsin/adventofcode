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
      return getHorizontalResult() + getVerticalResult();
    }

    private int getVerticalResult() {

      int firstReflection = getFirstReflection();
      return firstReflection;
    }

    private int getFirstReflection(char[][] matrix) {
      int firstReflection = -1;
      for (int i = 0; i < matrix.length - 1; i++) {
        var x = matrix[i];
        var y = matrix[i + 1];
        if (Arrays.equals(x, y)) {
          return i;
        }
      }
      return -1;
    }

    private int getHorizontalResult() {
      return 0;
    }
  }

  record Valley(List<Pattern> patterns) {

    public static Valley fromInput(List<String> lines) {
      List<Pattern> patterns = new ArrayList<>();
      int start = 0;
      for (int i = 0; i < lines.size(); i++) {
        if (lines.get(0).isBlank()) {
          patterns.add(Pattern.fromInput(lines.subList(start, i)));
          start = i + 1;
        }
      }
      return new Valley(patterns);
    }

    public int getResult() {
      return patterns().stream().mapToInt(Pattern::getResult).sum();
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay13-1.txt");

    var sampleResult = Valley.fromInput(sampleInput).getResult();
    assertThat(sampleResult).isEqualTo(405);

    var input = getInput("inputDay13.txt");
    var result = Valley.fromInput(sampleInput).getResult();
    //
    log.info("result is {}", result);
  }

}



