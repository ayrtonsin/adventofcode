package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day11 {

  record Coordinates(int x, int y) {

    int distance(Coordinates other) {
      return Math.abs(x - other.x()) + Math.abs(y - other.y());
    }

  }

  record Universe(boolean[][] matrix, List<Coordinates> galaxies, Set<Integer> emptyRows, Set<Integer> emptyCols) {

    private static boolean[][] getMatrix(List<String> lines) {
      boolean[][] matrix = new boolean[lines.size()][lines.getFirst().length()];
      for (int y = 0; y < lines.size(); y++) {
        var line = lines.get(y).toCharArray();
        for (int x = 0; x < matrix[y].length; x++) {
          matrix[y][x] = line[x] == '#';
        }
      }
      return matrix;
    }

    static Universe fromInput(List<String> inputs) {
      var matrix = getMatrix(inputs);
      var galaxies = galaxiesFromMatrix(matrix);
      return new Universe(matrix, galaxies, getEmptyRows(matrix), getEmptyCols(matrix));
    }

    private static ArrayList<Coordinates> galaxiesFromMatrix(boolean[][] matrix) {
      var galaxies = new ArrayList<Coordinates>();
      for (int y = 0; y < matrix.length; y++) {
        for (int x = 0; x < matrix[y].length; x++) {
          if (matrix[y][x]) {
            galaxies.add(new Coordinates(x, y));
          }
        }
      }
      return galaxies;
    }

//    Universe expand() {
//      //expand rows
//      var expandedMatrix = expandRows(matrix());
//      expandedMatrix = expandCols(expandedMatrix);
//      return new Universe(expandedMatrix, galaxiesFromMatrix(expandedMatrix));
//    }
//
//    private boolean[][] expandRows(boolean[][] matrix) {
//      List<boolean[]> rows = new ArrayList<>();
//      boolean[] empty = new boolean[matrix[0].length];
//
//      for (int y = 0; y < matrix.length; y++) {
//        rows.add(matrix[y].clone());
//        boolean emptyRow = true;
//        for (int x = 0; x < matrix[y].length; x++) {
//          if (matrix[y][x]) {
//            emptyRow = false;
//            break;
//          }
//        }
//        if (emptyRow) {
//          rows.add(empty);
//        }
//      }
//      return rows.toArray(new boolean[0][]);
//    }
//
//    private boolean[][] expandCols(boolean[][] matrix) {
//      Set<Integer> emptyCols = getEmptyCols(matrix);
//
//      var expandedMatrix = new boolean[matrix.length][matrix[0].length + emptyCols.size()];
//      int expandedX = 0;
//      for (int x = 0; x < matrix[0].length; x++) {
//        if (emptyCols.contains(x)) {
//          for (int y = 0; y < matrix.length; y++) {
//            expandedMatrix[y][expandedX] = matrix[y][x];
//          }
//          expandedX++;
//        }
//        for (int y = 0; y < matrix.length; y++) {
//          expandedMatrix[y][expandedX] = matrix[y][x];
//        }
//        expandedX++;
//      }
//      return expandedMatrix;
//    }

    private static Set<Integer> getEmptyRows(boolean[][] matrix) {
      Set<Integer> emptyRows = new HashSet<>();
      for (int y = 0; y < matrix.length; y++) {
        boolean emptyRow = true;
        for (int x = 0; x < matrix[y].length; x++) {
          if (matrix[y][x]) {
            emptyRow = false;
            break;
          }
        }
        if (emptyRow) {
          emptyRows.add(y);
        }
      }
      return emptyRows;
    }

    private static Set<Integer> getEmptyCols(boolean[][] matrix) {
      Set<Integer> emptyCols = new HashSet<>();
      for (int x = 0; x < matrix[0].length; x++) {
        boolean emptyCol = true;
        for (int y = 0; y < matrix.length; y++) {
          if (matrix[y][x]) {
            emptyCol = false;
            break;
          }
        }
        if (emptyCol) {
          emptyCols.add(x);
        }
      }
      return emptyCols;
    }

    long distanceBetweenAllGalaxies(int expansionSize) {
      long distanceSum = 0;
      for (int i = 0; i < galaxies().size(); i++) {
        var a = galaxies.get(i);
        for (int j = i + 1; j < galaxies().size(); j++) {
          var b = galaxies().get(j);
          var emptyX = emptiesBetweenAxis(emptyCols(), a.x(), b.x()) * expansionSize;
          var emptyY = emptiesBetweenAxis(emptyRows(), a.y(), b.y()) * expansionSize;

          distanceSum += a.distance(b) + emptyY + emptyX;
        }
      }
      return distanceSum;
    }

    private long emptiesBetweenAxis(Set<Integer> empties, int a, int b) {
      return IntStream.range(Math.min(a, b), Math.max(a, b))
          .filter(empties::contains)
          .count();
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay11-1.txt");

    var sampleResult = Universe.fromInput(sampleInput).distanceBetweenAllGalaxies(1);
    assertThat(sampleResult).isEqualTo(374);

    var input = getInput("inputDay11.txt");
    var result = Universe.fromInput(input).distanceBetweenAllGalaxies(1);
    //
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput = getInput("sampleDay11-1.txt");

    var sampleResult = Universe.fromInput(sampleInput).distanceBetweenAllGalaxies(9);
    assertThat(sampleResult).isEqualTo(1030);

    var input = getInput("inputDay11.txt");
    var result = Universe.fromInput(input).distanceBetweenAllGalaxies(1000000 - 1);
    //
    log.info("result is {}", result);
  }

}



