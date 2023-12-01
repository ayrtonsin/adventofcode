package com.adventofcode.adventofcode2023;

import java.util.List;

//@FeignClient(name = "adventClient", url = "https://adventofcode.com/2023/day/")
public interface AdventInputClient {

  //  @GetMapping("1/input")
  List<String> getDay1Input();

}
