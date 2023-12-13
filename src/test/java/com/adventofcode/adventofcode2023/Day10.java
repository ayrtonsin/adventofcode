package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day10 {

//  public enum

  @Data
  static class Tile {

    Coordinates coordinates;
    char symbol;

    public Tile(Coordinates coordinates, char symbol) {
      this.coordinates = coordinates;
      this.symbol = symbol;
    }

    Tile previous;
    Tile next;

    void setPrevious(Tile previous) {
      this.previous = previous;
    }

    void setNext(Tile next) {
      this.next = next;
      next.setPrevious(this);
    }

    public boolean isHorizontalBorder() {
      return symbol == '-'
          || symbol == 'L'
          || symbol == 'S'
          || symbol == 'F'
          || symbol == '7'
          || symbol == 'J';
    }

    public boolean isVerticalBorder() {
      return symbol == '|'
          || symbol == 'L'
          || symbol == 'S'
          || symbol == 'F'
          || symbol == '7'
          || symbol == 'J';
    }
  }

  record Coordinates(int x, int y) {

    Coordinates getLeft() {
      return new Coordinates(x() - 1, y());
    }

    Coordinates getRight() {
      return new Coordinates(x() + 1, y());
    }

    Coordinates getTop() {
      return new Coordinates(x(), y() - 1);
    }

    Coordinates getBottom() {
      return new Coordinates(x(), y() + 1);
    }
  }

  record Maze(char[][] matrix, Tile start, Set<Coordinates> pathCoordinates) {

    private static char[][] getMatrix(List<String> lines) {
      char[][] matrix = new char[lines.size()][lines.getFirst().length()];
      for (int y = 0; y < lines.size(); y++) {
        matrix[y] = lines.get(y).toCharArray();
      }
      return matrix;
    }

    static Maze fromInput(List<String> lines) {
      var matrix = getMatrix(lines);
      var start = findStart(matrix);
      var tile = setFirstLinkedTile(matrix, start);
      var pathCoordinates = new HashSet<Coordinates>();
      pathCoordinates.add(tile.getCoordinates());
      var count = 1;
      while (tile.symbol != 'S') {
        tile = generateNext(matrix, tile);
        pathCoordinates.add(tile.getCoordinates());
        count++;
      }
      System.out.println("path length: " + count);
      return new Maze(matrix, start, pathCoordinates);
    }

    private static Tile generateNext(char[][] matrix, Tile tile) {
      var previousCoordinates = tile.getPrevious().getCoordinates();
      var coordinates = tile.getCoordinates();
      var nextCoordinates = switch (tile.symbol) {
        case '-' -> Optional.of(coordinates.getRight()).filter(c -> !c.equals(previousCoordinates)).orElseGet(coordinates::getLeft);
        case 'F' -> Optional.of(coordinates.getRight()).filter(c -> !c.equals(previousCoordinates)).orElseGet(coordinates::getBottom);
        case 'J' -> Optional.of(coordinates.getTop()).filter(c -> !c.equals(previousCoordinates)).orElseGet(coordinates::getLeft);
        case '|' -> Optional.of(coordinates.getTop()).filter(c -> !c.equals(previousCoordinates)).orElseGet(coordinates::getBottom);
        case 'L' -> Optional.of(coordinates.getTop()).filter(c -> !c.equals(previousCoordinates)).orElseGet(coordinates::getRight);
        case '7' -> Optional.of(coordinates.getLeft()).filter(c -> !c.equals(previousCoordinates)).orElseGet(coordinates::getBottom);
        default -> throw new IllegalStateException("Unexpected value: " + tile.symbol);
      };
      var nextSymbol = matrix[nextCoordinates.y()][nextCoordinates.x()];
      var next = new Tile(nextCoordinates, nextSymbol);
      tile.setNext(next);
      return next;
    }

    private static Tile setFirstLinkedTile(char[][] matrix, Tile inputTile) {
      var startCoordinates = inputTile.getCoordinates();

      var nextTile = Optional.of(startCoordinates.getLeft())
          .filter(c -> c.x() >= 0)
          .map(c -> new Tile(c, matrix[c.y()][c.x()]))
          .filter(t -> t.getSymbol() == 'F' || t.getSymbol() == '-' || t.getSymbol() == 'L')

          .or(() -> Optional.of(startCoordinates.getRight())
              .filter(c -> c.x() < matrix[c.y()].length)
              .map(c -> new Tile(c, matrix[c.y()][c.x()]))
              .filter(t -> t.getSymbol() == '7' || t.getSymbol() == '-' || t.getSymbol() == 'J'))

          .or(() -> Optional.of(startCoordinates.getTop())
              .filter(c -> c.y() >= 0)
              .map(c -> new Tile(c, matrix[c.y()][c.x()]))
              .filter(t -> t.getSymbol() == '7' || t.getSymbol() == '|' || t.getSymbol() == 'F'))
          .orElseThrow();

      inputTile.setNext(nextTile);
      return nextTile;
    }

    private static Tile findStart(char[][] matrix) {
      for (int y = 0; y < matrix.length; y++) {
        for (int x = 0; x < matrix[y].length; x++) {
          var cur = matrix[y][x];
          if (cur == 'S') {
            return new Tile(new Coordinates(x, y), 'S');
          }
        }
      }
      throw new RuntimeException("start not found");
    }

    public int getFurthestDistance() {
      int distance = 0;
      var tile = start();
      do {
        distance++;
        tile = tile.getNext();

      } while (tile.symbol != 'S');

      return distance / 2;
    }

    public int getEnclosedCount() {
      var count = 0;

      for (int y = 1; y < matrix.length - 1; y++) {
        for (int x = 1; x < matrix[y].length - 1; x++) {
          var tile = new Tile(new Coordinates(x, y), matrix[y][x]);
          if (!isPartOfPath(tile) && hasUnevenBoundaries(tile)) {
            count++;
          }
        }
      }
      return count;
    }

    private boolean hasUnevenBoundaries(Tile tile) {
      return //hasUnevenTop(tile)
          // || hasUnevenBottom(tile)
          // || hasUnevenLeft(tile)
          //  ||
          hasUnevenRight(tile);
    }

//    private boolean hasUnevenTop(Tile tile) {
//      return isUnevenHorizontal(tile, IntStream.range(0, tile.getCoordinates().y()));
//    }
//
//    private boolean hasUnevenBottom(Tile tile) {
//      return isUnevenHorizontal(tile, IntStream.range(tile.getCoordinates().y() + 1, matrix().length));
//    }

//    private boolean hasUnevenLeft(Tile tile) {
//      return isUnevenVertical(tile, IntStream.range(0, tile.getCoordinates().x()));
//    }

    private boolean hasUnevenRight(Tile tile) {
      return isUnevenVertical(tile, IntStream.range(tile.getCoordinates().x() + 1, matrix()[tile.getCoordinates().y()].length));
    }

//    private boolean isUnevenHorizontal(Tile tile, IntStream stream) {
//      return stream.mapToObj(y -> new Coordinates(tile.getCoordinates().x(), y))
//          .map(c -> new Tile(c, matrix()[c.y()][c.x()]))
//          .filter(Tile::isHorizontalBorder)
//          .filter(this::isPartOfPath)
//          .count() % 2 == 1;
//    }

    private boolean isUnevenVertical(Tile tile, IntStream stream) {
      var borders = stream.mapToObj(x -> new Coordinates(x, tile.getCoordinates().y()))
          .map(c -> new Tile(c, matrix()[c.y()][c.x()]))
          .filter(Tile::isVerticalBorder)
          .filter(this::isPartOfPath)
          .map(Tile::getSymbol)
          .toList();

      Character previousCorner = null;
      int borderCount = 0;
      for (char c : borders) {
        if (c == '|') {
          borderCount++;
          previousCorner = null;
        }
        if (c == 'F' || c == 'L') {
          if (previousCorner != null) {
            throw new RuntimeException("wierd, previous corner already set");
          }
          previousCorner = c;
          borderCount++;
        }
        if (c == '7' || c == 'J' || c == 'S') {
          if (previousCorner == null) {
            throw new RuntimeException("wierd, previous corner NOT set");
          }

          if (previousCorner == 'F' && (c == '7' || c == 'S')) {
            borderCount++;
          } else if (previousCorner == 'L' && c == 'J') {
            borderCount++;
          }
          previousCorner = null;
        }
      }
      return borderCount % 2 == 1;
    }

    private boolean isPartOfPath(Tile t) {
      return pathCoordinates().contains(t.getCoordinates());
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay10-1.txt");

    var sampleResult = Maze.fromInput(sampleInput).getFurthestDistance();
    assertThat(sampleResult).isEqualTo(8);

    var input = getInput("inputDay10.txt");
    var result = Maze.fromInput(input).getFurthestDistance();
    //6956
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
//    var sampleInput1 = getInput("sampleDay10-1.txt");
//    var sampleResult1 = Maze.fromInput(sampleInput1).getEnclosedCount();
//    assertThat(sampleResult1).isEqualTo(0);
//
//    var sampleInput2 = getInput("sampleDay10-2.txt");
//    var sampleResult2 = Maze.fromInput(sampleInput2).getEnclosedCount();
//    assertThat(sampleResult2).isEqualTo(5);

//s is a F
//    var sampleInput3 = getInput("sampleDay10-3.txt");
//    var sampleResult3 = Maze.fromInput(sampleInput3).getEnclosedCount();
//    assertThat(sampleResult3).isEqualTo(8);

    //s is a 7
    var input = getInput("inputDay10.txt");
    var result = Maze.fromInput(input).getEnclosedCount();
    //45 nok
    //4 nok
    //3589 too high
    //364 nok
    //134 nok
    // 1376 nok
    // 455 WOEHOE
    log.info("result is {}", result);
  }

  @Test
  void extra() {

  }
}



