package com.adventofcode.adventofcode2023;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day3 {

  public record PartNumber(int line, int startIdx, Integer number) {

    boolean isAdjacent(Symbol s) {
      var rowDiff = Math.abs(line - s.y());
      return rowDiff <= 1
          && startIdx - 1 <= s.x()
          && s.x() <= startIdx + String.valueOf(number).length();
    }

    public boolean isAdjacentofAny(List<Symbol> symbols) {
      return symbols.stream().anyMatch(this::isAdjacent);
    }
  }

  public record Symbol(char symbol, int x, int y, List<PartNumber> adjacentPartNumbers) {

    Symbol(char symbol, int x, int y) {
      this(symbol, x, y, null);
    }

    Symbol withAdjacentNumbers(List<PartNumber> partNumbers) {
      var adjacentNumbers = partNumbers.stream()
          .filter(partNumber -> partNumber.isAdjacent(this))
          .toList();
      return new Symbol(symbol, x, y, adjacentNumbers);
    }

    int combineSymbols() {
      if ('*' == symbol && adjacentPartNumbers.size() > 1) {
        return adjacentPartNumbers.stream().mapToInt(PartNumber::number).reduce(Math::multiplyExact).orElse(0);
      } else {
        return 0;
        //return adjacentPartNumbers.stream().mapToInt(PartNumber::number).sum();
      }
    }

  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("src/main/resources/sampleDay3-1.txt");

    var sampleResult = sumAllSymbols(calculateResult(sampleInput));
    assertThat(sampleResult).isEqualTo(4361);

//    var input = getInput("src/main/resources/customDay3-1.txt");
    var input = getInput("src/main/resources/inputDay3.txt");
    var result = sumAllSymbols(calculateResult(input));

    //434175 too low
    //559433 too high
    //554003
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput = getInput("src/main/resources/sampleDay3-1.txt");

    var sampleResult = sumAllSymbolsWithGears(calculateResult(sampleInput));
    assertThat(sampleResult).isEqualTo(467835);

//    var input = getInput("src/main/resources/customDay3-1.txt");
    var input = getInput("src/main/resources/inputDay3.txt");
    var result = sumAllSymbolsWithGears(calculateResult(input));

    //
    log.info("result is {}", result);
  }

  private static List<String> getInput(String pathname) throws IOException {
    var sampleInput = Files.readAllLines(new File(pathname).toPath(), Charset.defaultCharset());
    return sampleInput;
  }

  private int sumAllSymbols(Stream<Symbol> symbolsWithNumbers) {
    return symbolsWithNumbers
        .map(Symbol::adjacentPartNumbers)
        .flatMap(List::stream)
        .mapToInt(PartNumber::number)
        .sum();
  }

  private int sumAllSymbolsWithGears(Stream<Symbol> symbolsWithNumbers) {
    return symbolsWithNumbers
        .mapToInt(Symbol::combineSymbols)
        .sum();
  }

  private Stream<Symbol> calculateResult(List<String> sampleInput) {
    List<Symbol> symbols = new ArrayList<>();
    List<PartNumber> partNumbers = new ArrayList<>();
    for (int y = 0; y < sampleInput.size(); y++) {
      var line = sampleInput.get(y);
      var currentNumber = "";
      for (int x = 0; x < line.length(); x++) {
        var c = line.charAt(x);
        if ('.' == c) {
          terminateCurrentNumber(currentNumber, x, y).ifPresent(partNumbers::add);
          currentNumber = "";
        } else if (Character.isDigit(c)) {
          currentNumber = currentNumber + c;
        } else {
          symbols.add(new Symbol(c, x, y));
          terminateCurrentNumber(currentNumber, x, y).ifPresent(partNumbers::add);
          currentNumber = "";
        }
      }
      terminateCurrentNumber(currentNumber, line.length() - 1, y).ifPresent(partNumbers::add);
    }

    var filteredNumbers = partNumbers.stream()
        .filter(partNumber -> partNumber.isAdjacentofAny(symbols))
        .toList();

    return symbols.stream()
        .map(symbol -> symbol.withAdjacentNumbers(filteredNumbers));
  }

  Optional<PartNumber> terminateCurrentNumber(String currentNumber, int x, int y) {
    return Optional.of(currentNumber)
        .filter(s -> !s.isEmpty())
        .map(s -> new PartNumber(y, x - currentNumber.length(), Integer.parseInt(s)));
  }
}



