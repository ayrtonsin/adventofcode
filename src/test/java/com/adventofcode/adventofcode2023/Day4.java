package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day4 {

  record Pile(List<ScratchCard> cards) {

  }

  record ScratchCard(
      int index,
      Set<Integer> winningNrs,
      List<Integer> myNrs
  ) {

    public int calculateWinCount() {
      return (int) myNrs.stream().filter(winningNrs::contains).count();

    }

    public int calculateScore() {
      var myWinningNrsSize = calculateWinCount();
      return (int) Math.pow(2, myWinningNrsSize - 1);

    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay4-1.txt");

    var sampleResult = calculateWinningScore(sampleInput);

    assertThat(sampleResult).isEqualTo(13);

    var input = getInput("inputDay4.txt");
    var result = calculateWinningScore(input);
    //
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput = getInput("sampleDay4-1.txt");

    var sampleResult = calculateScratchCardsCount(sampleInput);

    assertThat(sampleResult).isEqualTo(30);

    var input = getInput("inputDay4.txt");
    var result = calculateScratchCardsCount(input);
    //
    log.info("result is {}", result);
  }

  private int calculateWinningScore(List<String> sampleInput) {
    return IntStream.range(0, sampleInput.size())
        .mapToObj(index -> toScratchCard(index, sampleInput.get(index)))
        .mapToInt(ScratchCard::calculateScore)
        .sum();
  }

  private int calculateScratchCardsCount(List<String> sampleInput) {
    var allCards = IntStream.range(0, sampleInput.size())
        .mapToObj(index -> toScratchCard(index, sampleInput.get(index)))
        .toList();

    //start recursive
    return sampleInput.size() + recurs(allCards, allCards);

  }

  private int recurs(List<ScratchCard> allCards, List<ScratchCard> cardsToCalculate) {

    var newPrices = new ArrayList<ScratchCard>();
    for (int i = 0; i < cardsToCalculate.size(); i++) {
      var card = cardsToCalculate.get(i);
      var prizes = getPrizes(allCards, card);
      newPrices.addAll(prizes);
    }

    if (newPrices.isEmpty()) {
      return 0;
    }

    return newPrices.size() + recurs(allCards, newPrices);
  }

  private List<ScratchCard> getPrizes(List<ScratchCard> allCards, ScratchCard card) {
    return allCards.subList(card.index() + 1, card.index() + card.calculateWinCount() + 1);

  }

  private ScratchCard toScratchCard(int index, String line) {
    var winningNrs = Arrays.stream(line.split(":")[1].split(Pattern.quote("|"))[0].split(" ")).filter(s -> !s.isBlank()).map(Integer::parseInt).collect(Collectors.toSet());
    var myNrs = Arrays.stream(line.split(":")[1].split(Pattern.quote("|"))[1].split(" ")).filter(s -> !s.isBlank()).map(Integer::parseInt).toList();
    return new ScratchCard(index, winningNrs, myNrs);
  }

}



