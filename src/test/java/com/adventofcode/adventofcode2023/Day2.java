package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day2 {

  public record Game(
      int id,
      List<Round> rounds
  ) {

  }

  public class Round {

    Map<Color, Integer> colors = new HashMap<>();

    public Round(Map<Color, Integer> colors) {
      this.colors = colors;
    }

    Stream<ColorCount> toColorCounts() {
      return colors.entrySet().stream()
          .map(entry -> new ColorCount(entry.getKey(), entry.getValue()));
    }

    void addColor(Color color, int count) {
      colors.compute(color, (c, cur) -> cur == null ? count : cur + count);
    }
  }

  public record ColorCount(Color color, int count) {

  }

  enum Color {
    BLUE, RED, GREEN;

    static Color fromStr(String color) {
      return Color.valueOf(color.toUpperCase());
    }
  }

  record Bag(int red, int green, int blue) {

    public int getColorCount(Color color) {
      return switch (color) {
        case RED -> red;
        case GREEN -> green;
        case BLUE -> blue;
      };
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay2-1.txt");
    var sampleGames = inputsToGames(sampleInput);
    var sampleBag = new Bag(12, 16, 14);

    var result = sampleGames.stream()
        .filter(game -> isPossible(game, sampleBag))
        .mapToInt(Game::id)
        .sum();
    assertThat(result).isEqualTo(8);

    var bag = new Bag(12, 13, 14);
    var input = getInput("inputDay2.txt");
    var games = inputsToGames(input);

    result = games.stream()
        .filter(game -> isPossible(game, bag))
        .mapToInt(Game::id)
        .sum();
    //2105
    log.info("result is {}", result);
  }

  boolean isPossible(Game game, Bag bag) {
    return game.rounds.stream()
        .flatMap(Round::toColorCounts)
        .allMatch(cc -> cc.count <= bag.getColorCount(cc.color));
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput = getInput("sampleDay2-1.txt");
    var sampleGames = inputsToGames(sampleInput);

    var result = sampleGames.stream()
        .map(this::toFewestBag)
        .mapToInt(bag -> bag.green * bag.blue * bag.red)
        .sum();
    assertThat(result).isEqualTo(2286);

    var input = getInput("inputDay2.txt");
    var games = inputsToGames(input);

    result = games.stream()
        .map(this::toFewestBag)
        .mapToInt(bag -> bag.green * bag.blue * bag.red)
        .sum();
    //72422
    log.info("result is {}", result);
  }

  Bag toFewestBag(Game game) {
    var colorMap = game.rounds().stream()
        .flatMap(Round::toColorCounts)
        .collect(Collectors.toMap(ColorCount::color, ColorCount::count,
            (c1, c2) -> Math.max(c1, c2)));

    return new Bag(colorMap.get(Color.RED), colorMap.get(Color.GREEN), colorMap.get(Color.BLUE));
  }

  //next methods are mapping functions from string to objects

  List<Game> inputsToGames(List<String> input) {
    return input.stream()
        .map(this::inputToGame)
        .toList();
  }

  private Game inputToGame(String s) {
    int gameId = Integer.parseInt(s.split(":")[0].split(" ")[1]);
    var rounds = roundsStrToRound(s.split(":")[1].split(";"));
    return new Game(gameId, rounds);
  }

  private List<Round> roundsStrToRound(String[] roundsStr) {
    return Arrays.stream(roundsStr)
        .map(this::roundStrToRound)
        .toList();
  }

  private Round roundStrToRound(String roundStr) {
    var colorsCountStr = roundStr.split(",");
    return new Round(Arrays.stream(colorsCountStr)
        .map(String::trim)
        .map(this::colorStrToColor)
        .collect(Collectors.toMap(ColorCount::color, ColorCount::count)));
  }

  private ColorCount colorStrToColor(String s) {
    var count = Integer.parseInt(s.split(" ")[0]);
    var color = Color.fromStr(s.split(" ")[1]);
    return new ColorCount(color, count);
  }

}



