package com.adventofcode.adventofcode2023;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Day5 {

  class ConversionMap {

    String mapName;

    List<Coordinates> coordinates = new ArrayList<>();

    public ConversionMap(String mapName) {
      this.mapName = mapName;
    }

    public void putConversion(long destinationStart, long sourceStart, long range) {
      coordinates.add(new Coordinates(destinationStart, sourceStart, range));
    }

    public long compute(long seed) {
      return coordinates.stream()
          .filter(c -> c.sourceStart <= seed && seed < c.sourceStart + c.range)
          .findAny()
          .map(c -> {
            var diff = seed - c.sourceStart;
            return c.destinationStart + diff;
          })
          .orElse(seed);
    }
  }

  record Coordinates(long destinationStart, long sourceStart, long range) {

  }

  record Conversion(Long seed, ConversionMap map) {

    Conversion(ConversionMap map) {
      this(null, map);
    }
  }

  record Almanac(List<Long> seeds, List<ConversionMap> transformerMaps) {

    List<Long> seedsToLocation() {
      return seeds.stream()
          .map(this::seedToLocation)
          .toList();
    }

//    Long seedRangesToMinLocation() {
//      var min = Long.MAX_VALUE;
//      for (int i = 0; i < seeds.size(); i = i + 2) {
//        var seedStart = seeds.get(i);
//        System.out.println(format("processing seed %s : %s of total %s", i, seedStart, seeds.size() / 2));
//        var seedRange = seeds.get(i + 1);
//        for (int j = 0; j < seedRange; j++) {
//          min = Math.min(min, seedToLocation(seedStart + j));
//        }
//      }
//      return min;
//    }

    long seedRangesToMinLocation() {
      return IntStream.range(0, seeds.size() / 2)
          .parallel()
          .peek(i -> System.out.println(
              String.format("processing seed %s : %s of total %s", i, seeds.get(i * 2), seeds.size() / 2)))
          .mapToLong(i -> {
            long seedStart = seeds.get(i * 2);
            long seedRange = seeds.get(i * 2 + 1);

            return IntStream.range(0, (int) seedRange)
                .parallel()
                .mapToLong(j -> seedToLocation(seedStart + j))
                .min()
                .orElse(Long.MAX_VALUE);
          })
          .min()
          .orElse(Long.MAX_VALUE);
    }

    private Long seedToLocation(Long seed) {
      return transformerMaps.stream()
          .map(Conversion::new)
          .reduce(new Conversion(seed, null), (m1, m2) -> new Conversion(m2.map.compute(m1.seed), null))
          .seed;
    }
  }

  @Test
  void puzzle1() throws IOException {
    var sampleInput = getInput("sampleDay5-1.txt");

    var sampleResult = inputToAlmanac(sampleInput).seedsToLocation().stream().mapToLong(x -> x).min().orElseThrow();
    assertThat(sampleResult).isEqualTo(35);

    var input = getInput("inputDay5.txt");
    var result = inputToAlmanac(input).seedsToLocation().stream().mapToLong(x -> x).min().orElseThrow();
    //
    log.info("result is {}", result);
  }

  @Test
  void puzzle2() throws IOException {
    var sampleInput = getInput("sampleDay5-1.txt");

    var sampleResult = inputToAlmanac(sampleInput).seedRangesToMinLocation();
    assertThat(sampleResult).isEqualTo(46);

    var input = getInput("inputDay5.txt");
    var result = inputToAlmanac(input).seedRangesToMinLocation();
    //
    log.info("result is {}", result);
  }

  public Almanac inputToAlmanac(List<String> input) {
    var seeds = Arrays.stream(input.get(0).split(": ")[1].split(" "))
        .map(String::trim)
        .map(Long::parseLong)
        .toList();

    var transfomers = new ArrayList<ConversionMap>();
    ConversionMap currentMap = null;
    for (int i = 2; i < input.size(); i++) {
      var line = input.get(i);
      if (line.contains("map:")) {
        currentMap = new ConversionMap(line.split(" map")[0]);
        transfomers.add(currentMap);
      } else if (!line.isBlank()) {
        var lineParts = line.split(" ");
        currentMap.putConversion(Long.parseLong(lineParts[0]), Long.parseLong(lineParts[1]), Long.parseLong(lineParts[2]));
      }
    }
    return new Almanac(seeds, transfomers);
  }
}



