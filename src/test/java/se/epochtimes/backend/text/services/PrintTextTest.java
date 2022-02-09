package se.epochtimes.backend.text.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import se.epochtimes.backend.text.model.Subject;
import se.epochtimes.backend.text.model.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
public class PrintTextTest {

  public static List<String> readFile() throws IOException {
    var s = File.separator;
    final String textFolder = "src"+s+"main"+s+"resources"+s+"text"+s;
    Path path = FileSystems.getDefault().getPath(textFolder + "artikel.txt");
    return Files.readAllLines(path);
  }

  @Test
  void printingHasNewLine(CapturedOutput capturedOutput) throws IOException {
    Text article = new Text(Subject.EKONOMI, readFile());
    String n = System.lineSeparator();
    String u = String.valueOf(article);
    int max = 31;
    String f = u.substring(0, max) + n + u.substring(max);
    LoggerFactory.getLogger(PrintTextTest.class).info(f);
    assertTrue(capturedOutput.getOut().trim().contains(n));
  }
}
