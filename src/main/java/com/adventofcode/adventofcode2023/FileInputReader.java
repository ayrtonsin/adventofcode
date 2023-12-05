package com.adventofcode.adventofcode2023;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class FileInputReader {

  private FileInputReader() {
  }

  public static List<String> getInput(String fileName) throws IOException {
    var pathname = "src/main/resources/" + fileName;
    var sampleInput = Files.readAllLines(new File(pathname).toPath(), Charset.defaultCharset());
    return sampleInput;
  }

}
