package com.adventofcode.adventofcode2023;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class Day14 {

  record Platform(char[][] matrix){
    public static Platform fromInput(List<String> lines) {
      var matrix = MatrixUtils.getMatrix(lines);
      return new Platform(matrix);
    }

    public Platform tiltLeft(){
      var matrix = matrix().clone();
      int rows = matrix.length;
      int cols = matrix[0].length;
      for(int x = 0; x < rows; x++){
        int countRound = 0;
        int previousStart = 0;
        for (int y = 0; y < cols; y++){
          switch (matrix[x][y]){
            case 'O' -> countRound++;
            case '.' -> {}
            case  '#' -> {
              for (int z = previousStart; z < countRound + previousStart; z++){
                matrix[x][z] = 'O';
              }
              for (int z = previousStart + countRound; z < y; z++){
                matrix[x][z] = '.';
              }
              countRound = 0;
              previousStart = y + 1;
            }
          }
        }
        for (int z = previousStart; z < countRound + previousStart; z++){
          matrix[x][z] = 'O';
        }
        for (int z = previousStart + countRound; z < cols; z++){
          matrix[x][z] = '.';
        }
      }
      return this;
    }

    public Platform tiltRight(){
      var matrix = matrix().clone();
      int rows = matrix.length;
      int cols = matrix[0].length;
      for(int x = 0; x < rows; x++){
        int countRound = 0;
        int previousStart = cols -1;
        for (int y = cols -1; y >= 0; y--){
          switch (matrix[x][y]){
            case 'O' -> countRound++;
            case '.' -> {}
            case  '#' -> {
              for (int z = previousStart; z > previousStart - countRound; z--){
                matrix[x][z] = 'O';
              }
              for (int z = previousStart - countRound; z > y; z--){
                matrix[x][z] = '.';
              }
              countRound = 0;
              previousStart = y - 1;
            }
          }
        }
        for (int z = previousStart; z > previousStart - countRound; z--){
          matrix[x][z] = 'O';
        }
        for (int z = previousStart - countRound; z >= 0; z--){
          matrix[x][z] = '.';
        }
      }
      return this;
    }

    public Platform tiltUp(){
      var matrix = matrix().clone();
      int rows = matrix.length;
      int cols = matrix[0].length;
      for(int x = 0; x < cols; x++){
        int countRound = 0;
        int previousStart = 0;
        for (int y = 0; y < rows; y++){
          switch (matrix[y][x]){
            case 'O' -> countRound++;
            case '.' -> {}
            case  '#' -> {
              for (int z = previousStart; z < countRound + previousStart; z++){
                matrix[z][x] = 'O';
              }
              for (int z = previousStart + countRound; z < y; z++){
                matrix[z][x] = '.';
              }
              countRound = 0;
              previousStart = y + 1;
            }
          }
        }
        for (int z = previousStart; z < countRound + previousStart; z++){
          matrix[z][x] = 'O';
        }
        for (int z = previousStart + countRound; z < cols; z++){
          matrix[z][x] = '.';
        }
      }
      return this;
    }

    public Platform tiltDown(){
      var matrix = matrix().clone();
      int rows = matrix.length;
      int cols = matrix[0].length;
      for(int x = 0; x < cols; x++){
        int countRound = 0;
        int previousStart = cols -1;
        for (int y = rows -1; y >= 0; y--){
          switch (matrix[y][x]){
            case 'O' -> countRound++;
            case '.' -> {}
            case  '#' -> {
              for (int z = previousStart; z > previousStart - countRound; z--){
                matrix[z][x] = 'O';
              }
              for (int z = previousStart - countRound; z > y; z--){
                matrix[z][x] = '.';
              }
              countRound = 0;
              previousStart = y - 1;
            }
          }
        }
        for (int z = previousStart; z > previousStart - countRound; z--){
          matrix[z][x] = 'O';
        }
        for (int z = previousStart - countRound; z >= 0; z--){
          matrix[z][x] = '.';
        }
      }
      return this;
    }

    private int sumRound(){
      return IntStream.range(0, matrix.length)
              .map(index -> (matrix.length - index) * countRoundBouldersOnIndex(index))
              .sum();
      }

    private int countRoundBouldersOnIndex(int index) {
      int count = 0;
      for (int i = 0; i < matrix.length; i++){
        if (matrix[index][i] == 'O'){
          count++;
        }
      }
      return count;
    }

    public Platform cycle(){
      return tiltUp().tiltLeft().tiltDown().tiltRight();
    }

    public Platform cycles(int cycles) {
      var tilted = this;
      for (int i = 0; i< cycles; i++){
        tilted = tilted.cycle();
      }
      return tilted;
    }

    record PatternLength(int length, int cyclesAdded){}

    public PatternLength findPatternLength() {
      var tilted = this;
      var cycles = 0;
      List<Integer> results = new ArrayList<>();
      do {
        results.add(sumRound());
        tilted = cycle();
        cycles++;
      } while (tilted.sumRound() != results.getFirst());

      // repeat to ensure a complete pattern
      results = new ArrayList<>();
      do {
        results.add(sumRound());
        tilted = cycle();
        cycles++;
      } while (tilted.sumRound() != results.getFirst());

      log.info(results.toString());

      return new PatternLength(results.size(), cycles);
    }

//
//
//      var tilted = this;
//      int previous = 0;
//      int current = 0;
//      long cycles = 0;
//      int stableCount = 0;
//      do{
//        previous = tilted.sumRound();
//        tilted = tilted.tiltUp().tiltLeft().tiltDown().tiltRight();
//        current = tilted.sumRound();
//        cycles++;
//        if(current == previous){
//          stableCount++;
//        }else {
//          stableCount = 0;
//        }
//      }while(stableCount < 1000);
//      return new Result(tilted, cycles - stableCount, tilted.sumRound());
//    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay14-1.txt");
    var sampleResult = Platform.fromInput(sampleInput).tiltUp().sumRound();
    assertThat(sampleResult).isEqualTo(136);

    var input = getInput("inputDay14.txt");
    var result = Platform.fromInput(input).tiltUp().sumRound();

    //106517
    log.info("result is {}", result);
  }

  @Test
  void puzzle2_preChecks() throws IOException {
    //verify tiltLeft = mirror of tilt right
    var sampleInput = getInput("sampleDay14-1.txt");
    var matrix = Platform.fromInput(sampleInput).matrix;
    var mirror = MatrixUtils.mirrorY(matrix);
    var leftTilt = new Platform(matrix).tiltLeft().matrix();
    var mirrorLTilt = MatrixUtils.mirrorY(leftTilt);
    var mirrorRightTilt = new Platform(mirror).tiltRight().matrix();
    assertThat(Arrays.deepEquals(mirrorLTilt, mirrorRightTilt)).isTrue();

    var mirrorX = MatrixUtils.mirrorX(matrix);
    var upTilt = new Platform(matrix).tiltUp().matrix();
    var mirrorUpTilt = MatrixUtils.mirrorX(upTilt);
    var mirrorDownTilt = new Platform(mirrorX).tiltDown().matrix();
    assertThat(Arrays.deepEquals(mirrorUpTilt, mirrorDownTilt)).isTrue();
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput = getInput("sampleDay14-1.txt");
    var platform = Platform.fromInput(sampleInput).cycles(1);
    var sampleResult = platform.sumRound();
    assertThat(sampleResult).isEqualTo(87);//9+18+14+18+10+4+12+2+2

    var sample100 = platform.cycles(100000).sumRound();
    assertThat(sample100).isEqualTo(64);
    platform.cycles(7);
    assertThat(platform.sumRound()).isEqualTo(64);
    platform.cycles(7);
    assertThat(platform.sumRound()).isEqualTo(64);
    platform.cycles(7);
    assertThat(platform.sumRound()).isEqualTo(64);
    platform.cycles(7);
    assertThat(platform.sumRound()).isEqualTo(64);


    int cycles = 10004;
    platform = Platform.fromInput(sampleInput).cycles(cycles);
//    var patternLength = 7;
//    assertThat(platform.findPatternLength()).isEqualTo(patternLength);
    var patternLength = platform.findPatternLength();
    cycles = cycles + patternLength.cyclesAdded;

    var totalCycles = 1000000000L;
    var repeatCount = (totalCycles - cycles) / patternLength.length;
    int mod = (int) ((totalCycles - cycles) % patternLength.length);

    log.info("mod: {}", mod);
    var currentSum = platform.sumRound();
    assertThat(platform.cycles(mod).sumRound()).isEqualTo(64);

    log.info("sample result {}", mod);
  }

}



