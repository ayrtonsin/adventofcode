package com.adventofcode.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static com.adventofcode.adventofcode2023.FileInputReader.getInput;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@Slf4j
public class Day15 {

  public static int hash(String input){
    int currentValue = 0;
    for(char c : input.toCharArray()){
      currentValue = currentValue + (int) c;
      currentValue = currentValue * 17;
      currentValue = currentValue % 256;
    }
    return currentValue;
  }

  private int hashInput(String input){
    return Arrays.stream(input.split(",")).mapToInt(Day15::hash).sum();
  }

  @Test
  void puzzle1() throws IOException {

    assertThat(hash("HASH")).isEqualTo(52);

    var sampleInput = getInput("sampleDay15-1.txt");
    var sampleResult = hashInput(sampleInput.getFirst());
    assertThat(sampleResult).isEqualTo(1320);

    var input = getInput("inputDay15.txt");
    var result = hashInput(input.getFirst());
    //
    log.info("result is {}", result);
  }

  record Step(String label, int boxNr, char operation, int focalLength){
    static Step fromInput(String input){
      var label = input.split("[=-]")[0];
      int focalLength = 0;
      char operation = input.indexOf('=') > 0? '=': '-';
      if(operation == '='){
        focalLength = Integer.parseInt(input.split("=")[1]);
      }
      var boxNr = Day15.hash(label);
      return new Step(label, boxNr, operation, focalLength);
    }

    Lens getLens(){
      return new Lens(label, focalLength);
    }
  }

  Map<Integer, List<Lens>> initBoxes(){
    var map = new HashMap<Integer, List<Lens>>(256);
    IntStream.range(0, 256).forEach(i -> map.put(i, new ArrayList<>()));
    return map;
  }

  int getFocusPower(int box, List<Lens> lenses){
    return IntStream.range(0, lenses.size())
            .map(slot -> (box + 1) * (slot + 1) * lenses.get(slot).getFocalLength())
            .peek(System.out::println)
            .sum();
  }

  int initializationSequence(String input){
    var steps = Arrays.stream(input.split(",")).map(Step::fromInput).toList();
    var boxes = initBoxes();

    for (Step step: steps){
      Lens lens = step.getLens();
      List<Lens> boxLenses = boxes.get(step.boxNr);
      if(step.operation() == '-'){
        boxLenses.remove(lens);
      }else if ( step.operation() == '='){
        var existingLensIdx = boxLenses.indexOf(lens);
        if(existingLensIdx == -1){
          boxLenses.add(lens);
        }else {
          boxLenses.remove(existingLensIdx);
          boxLenses.add(existingLensIdx, lens);
        }
      }

    }
    System.out.println(boxes);

//    System.out.println(Arrays.toString(boxes.entrySet().stream().mapToInt(entry -> getFocusPower(entry.getKey(), entry.getValue())).toArray()));

    return boxes.entrySet().stream().mapToInt(entry -> getFocusPower(entry.getKey(),entry.getValue())).sum();
  }

  @Test
  void puzzle2() throws IOException {


    var sampleInput = getInput("sampleDay15-1.txt");
    var sampleResult = initializationSequence(sampleInput.getFirst());
    assertThat(sampleResult).isEqualTo(145);

    var input = getInput("inputDay15.txt");
    var result = initializationSequence(input.getFirst());
    //265345
    log.info("result is {}", result);
  }

}

@EqualsAndHashCode
@AllArgsConstructor
@Getter
class Lens{
  private String label;
  @EqualsAndHashCode.Exclude
  int focalLength;

}



