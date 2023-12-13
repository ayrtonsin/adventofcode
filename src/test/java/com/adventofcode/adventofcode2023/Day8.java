package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day8 {

  enum Direction {LEFT, RIGHT}

  record Node(String name, String left, String right) {

    static Node fromInput(String line) {
      var name = line.split(" = ")[0];
      var left = line.split(" = ")[1].replaceAll("[() ]", "").split(",")[0];
      var right = line.split(" = ")[1].replaceAll("[() ]", "").split(",")[1];
      return new Node(name, left, right);
    }

  }

  ;

  record Plan(List<Direction> directions, Map<String, Node> nodes) {

    static Plan fromInput(List<String> input) {
      var directions = input.get(0).chars().mapToObj(c -> switch (c) {
        case 'R' -> Direction.RIGHT;
        case 'L' -> Direction.LEFT;
        default -> throw new IllegalStateException("Unexpected value: " + c);
      }).toList();

      var nodes = IntStream.range(2, input.size())
          .mapToObj(input::get)
          .map(Node::fromInput)
          .collect(Collectors.toMap(Node::name, node -> node));
      return new Plan(directions, nodes);
    }

    long walkThrough() {
      return walkThrough(0, "AAA");
    }

    private long walkThrough(int currentDirectionIdx, String currentNode) {
      var node = nodes.get(currentNode);
      System.out.println("node: " + node);
      if (node.name.equals("ZZZ")) {
        return 0;
      }
      int nextDirectionIdx = (currentDirectionIdx + 1) % directions.size();
      var direction = directions.get(currentDirectionIdx);
      if (Direction.LEFT.equals(direction)) {
        return 1 + walkThrough(nextDirectionIdx, node.left());
      } else {
        return 1 + walkThrough(nextDirectionIdx, node.right());
      }
    }

    long walkThroughSimultaneously() {
      List<String> nodeKeys = nodes.keySet().stream().filter(key -> key.endsWith("A")).toList();
      int currentDirectionIdx = 0;
      long steps = 0;
      while (true) {
//        System.out.println(steps + " " + nodeKeys);
        List<Node> nodes = nodeKeys.stream().map(this.nodes::get).toList();
        if (nodes.stream().allMatch(node -> node.name.endsWith("Z"))) {
          return steps;
        }

        var direction = directions.get(currentDirectionIdx);
        if (Direction.LEFT.equals(direction)) {
          nodeKeys = nodes.stream()
              .map(Node::left)
              .toList();
        } else {
          nodeKeys = nodes.stream()
              .map(Node::right)
              .toList();
        }

        currentDirectionIdx = (currentDirectionIdx + 1) % directions.size();
        steps++;
      }
    }

    private long walkThroughSimultaneouslyLCM() {
      var pathLengthsForEachANode = nodes.keySet().stream()
          .filter(key -> key.endsWith("A"))
          .map(this::pathToFinish)
          .toList();
      return calculateLCM(pathLengthsForEachANode);
    }

    private long pathToFinish(String start) {
      int currentDirectionIdx = 0;
      long steps = 0;
      String nodeKey = start;
      while (true) {
//        System.out.println(steps + " " + nodeKeys);
        if (nodeKey.endsWith("Z")) {
          return steps;
        }

        var direction = directions.get(currentDirectionIdx);
        var node = nodes.get(nodeKey);
        if (Direction.LEFT.equals(direction)) {
          nodeKey = node.left();
        } else {
          nodeKey = node.right();
        }

        currentDirectionIdx = (currentDirectionIdx + 1) % directions.size();
        steps++;
      }
    }

    /**
     * lcm = Least Common Multiple or "kleinste gemene deler"
     */
    public long calculateLCM(List<Long> numbers) {
      return numbers.stream()
          .reduce(1L, this::calculateLCMForTwoNumbers);
    }

    private long calculateLCMForTwoNumbers(long a, long b) {
      return a * b / calculateGCD(a, b);
    }

    private long calculateGCD(long a, long b) {
      while (b > 0) {
        long temp = b;
        b = a % b;
        a = temp;
      }
      return a;
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput1 = getInput("sampleDay8-1.txt");
    var sampleResult1 = Plan.fromInput(sampleInput1).walkThrough();
    assertThat(sampleResult1).isEqualTo(2);

    var sampleInput2 = getInput("sampleDay8-2.txt");
    var sampleResult2 = Plan.fromInput(sampleInput2).walkThrough();
    assertThat(sampleResult2).isEqualTo(6);

    System.out.println("actual#############");
    System.out.println("###################");
    var input = getInput("inputDay8.txt");
    var result = Plan.fromInput(input).walkThrough();
    //21409
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput1 = getInput("sampleDay8-1.txt");
    var sampleResult1 = Plan.fromInput(sampleInput1).walkThroughSimultaneouslyLCM();
    assertThat(sampleResult1).isEqualTo(2);

    var sampleInput2 = getInput("sampleDay8-2.txt");
    var sampleResult2 = Plan.fromInput(sampleInput2).walkThroughSimultaneouslyLCM();
    assertThat(sampleResult2).isEqualTo(6);

    var sampleInput3 = getInput("sampleDay8-3.txt");
    var sampleResult3 = Plan.fromInput(sampleInput3).walkThroughSimultaneouslyLCM();
    assertThat(sampleResult3).isEqualTo(6);

    System.out.println("actual#############");
    System.out.println("###################");
    var input = getInput("inputDay8.txt");
    var result = Plan.fromInput(input).walkThroughSimultaneouslyLCM();
    //21409
    log.info("result is {}", result);
  }

}



