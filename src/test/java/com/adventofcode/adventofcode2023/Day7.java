package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day7 {

  record Hand(HandType type, String hand, String handOrder, int bid) implements Comparable<Hand> {

    static Hand v1Hand(String hand, int bid) {
      return new Hand(HandType.fromV1String(hand), hand, toOrder(hand), bid);
    }

    static Hand v2Hand(String hand, int bid) {
      return new Hand(HandType.fromV2String(hand), hand, toOrder(hand).replaceAll("A", "0"), bid);
    }

    static String toOrder(String hand) {
      var x = hand.chars().mapToObj(c -> switch (c) {
        case 'A' -> 'D';
        case 'K' -> 'C';
        case 'Q' -> 'B';
        case 'J' -> 'A';
        case 'T' -> '9';
        case '9' -> '8';
        case '8' -> '7';
        case '7' -> '6';
        case '6' -> '5';
        case '5' -> '4';
        case '4' -> '3';
        case '3' -> '2';
        case '2' -> '1';
        default -> throw new IllegalStateException("Unexpected value: " + c);
      }).map(String::valueOf).collect(Collectors.joining());
      return x;
    }

    @Override
    public int compareTo(Hand o) {
      var c1 = Integer.compare(type.strength, o.type.strength);
      if (c1 == 0) {
        return handOrder.compareTo(o.handOrder);
      }
      return c1;
    }
  }

  enum HandType {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1);

    int strength;

    HandType(int strength) {
      this.strength = strength;
    }

    static HandType fromV1String(String hand) {
      Map<Character, Long> unique = hand.chars()
          .mapToObj(c -> (char) c)
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
      if (unique.size() == 1) {
        return HandType.FIVE_OF_A_KIND;
      }
      if (unique.values().stream().anyMatch(count -> count == 4)) {
        return HandType.FOUR_OF_A_KIND;
      }
      if (unique.values().stream().anyMatch(count -> count == 3)) {
        if (unique.values().stream().anyMatch(count -> count == 2)) {
          return HandType.FULL_HOUSE;
        }
        return HandType.THREE_OF_A_KIND;
      }
      var pairs = unique.values().stream().filter(count -> count == 2).count();
      if (pairs == 2) {
        return HandType.TWO_PAIR;
      } else if (pairs == 1) {
        return HandType.ONE_PAIR;
      }
      return HandType.HIGH_CARD;
    }

    static HandType fromV2String(String hand) {
      var jokerCount = hand.replaceAll("[^J]", "").length();
      if (jokerCount == 0) {
        return fromV1String(hand);
      }
      //find char with max occurrences besides J, use the fromV1String where the J is replaced to determine the handType
      return hand.replaceAll("J", "").chars()
          .mapToObj(c -> (char) c)
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
          .entrySet()
          .stream()
          .max(Map.Entry.comparingByValue())
          .map(Entry::getKey)
          .map(charWithMaxOccurrence -> fromV1String(hand.replaceAll("J", String.valueOf(charWithMaxOccurrence))))
          .orElse(HandType.FIVE_OF_A_KIND);//when entire hand is J
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay7-1.txt");

    var sampleResult = calculateScore(toV1Hands(sampleInput));
    assertThat(sampleResult).isEqualTo(6440);

    var input = getInput("inputDay7.txt");
    var result = calculateScore(toV1Hands(input));
    //253638586
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput = getInput("sampleDay7-1.txt");

    var sampleResult = calculateScore(toV2Hands(sampleInput));
    assertThat(sampleResult).isEqualTo(5905);

    var hand = Hand.v2Hand("96J56", 271);
    assertThat(Hand.v2Hand("96J56", 1).type).isEqualTo(HandType.THREE_OF_A_KIND);
    assertThat(Hand.v2Hand("JTTJT", 1).type).isEqualTo(HandType.FIVE_OF_A_KIND);
    assertThat(Hand.v2Hand("7QA5J", 1).type).isEqualTo(HandType.ONE_PAIR);
    assertThat(Hand.v2Hand("JAJJA", 1).type).isEqualTo(HandType.FIVE_OF_A_KIND);
    assertThat(Hand.v2Hand("3322J", 1).type).isEqualTo(HandType.FULL_HOUSE);
    assertThat(Hand.v2Hand("3332J", 1).type).isEqualTo(HandType.FOUR_OF_A_KIND);

    assertThat(Hand.v1Hand("KK677", 1)).isGreaterThan(Hand.v1Hand("KTJJT", 1));//2pairs greater than 2pairs but k is stronger than t
    assertThat(Hand.v2Hand("KK677", 1)).isLessThan(Hand.v2Hand("KTJJT", 1));// 2pairs less then 4of kind
    assertThat(Hand.v2Hand("AAAAA", 1)).isGreaterThan(Hand.v2Hand("JAJJA", 1));// 5of a kind vs 5 of a kind but jokers are weaker
    assertThat(Hand.v2Hand("JJJJJ", 1)).isLessThan(Hand.v2Hand("22222", 1)); // jokers are weaker

    var input = getInput("inputDay7.txt");
    var result = calculateScore(toV2Hands(input));
    //253800614 too high
    //253758187 too high
    //253300551 too high? not correct
    //253253225
    log.info("result is {}", result);
  }

  List<Hand> toV1Hands(List<String> input) {
    return input.stream().map(line -> {
          var lineA = line.trim().split(" ");
          return Hand.v1Hand(lineA[0], Integer.parseInt(lineA[1]));
        })
        .toList();
  }

  List<Hand> toV2Hands(List<String> input) {
    return input.stream().map(line -> {
          var lineA = line.trim().split(" ");
          return Hand.v2Hand(lineA[0], Integer.parseInt(lineA[1]));
        })
        .toList();
  }

  long calculateScore(List<Hand> hands) {
    var sortedHands = hands.stream().sorted().toList();
    return IntStream.range(0, sortedHands.size())
        .map(i -> sortedHands.get(i).bid * (i + 1))
        .sum();
  }
}



