import com.branow.edition.FileText;
import com.branow.logger.Logger;
import com.branow.logger.Note;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import static com.branow.logger.Note.Type;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class LoggerTest {

    @Test
    public void testConsole() {
        Logger logger = new Logger();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream console = System.out;
        System.setOut(new PrintStream(out));
        Assertions.assertTrue(logger.isConsole());
        log(logger);

        System.setOut(console);
        log(logger);

        String actual = removeTextAttributes(removeDates(out.toString(StandardCharsets.UTF_8))).replaceAll("\r", "");
        String expected = getExpectedConsole();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFile() {
        FileText text = new FileText("src\\test\\java\\logs.txt");
        text.clean();
        Logger logger = new Logger(true, text);

        Assertions.assertTrue(logger.isConsole());
        Assertions.assertTrue(logger.isFile());

        log(logger);
        String actual = removeDates(text.read());
        String expected = getExpectedFile();
        Assertions.assertEquals(expected, actual);
    }

    private void log(Logger logger) {
        logger.log(getSrc().get(0));
        logger.log(getSrc().get(1).getType(), getSrc().get(1).getName(), getSrc().get(1).getDetails());
        logger.logWarning(getSrc().get(2).getName(), getSrc().get(2).getDetails());
        logger.logError(getSrcOfException());
    }

    private String getExpectedConsole() {
        return removeDates(getSrc().stream()
                .map(this::getExpectedConsole)
                .collect(Collectors.joining("\n"))
                + "\n" + getExpectedOfExceptionConsole());
    }

    private String getExpectedFile() {
        return removeDates(getSrc().stream()
                .map(this::getExpectedFile)
                .collect(Collectors.joining("#\n"))
                + "#\n" + getExpectedOfExceptionFile());
    }

    public String removeDates(String notes) {
        return notes.replaceAll("\\d{4}\\.\\d{2}\\.\\d{2} \\d{2}:\\d{2}:\\d{2}", "");
    }

    public String removeTextAttributes(String notes) {
        return notes.replaceAll("\033\\[[\\d;]*m", "");
    }


    public String getExpectedOfExceptionFile() {
        NullPointerException e = getSrcOfException();
        String name = e.getClass().getSimpleName() + " : " + e.getMessage();
        return " ~ [ERROR] ~ " + name + " ~ ";
    }

    public String getExpectedOfExceptionConsole() {
        NullPointerException e = getSrcOfException();
        String name = e.getClass().getSimpleName() + " : " + e.getMessage();
        return " [ERROR] " + name + "\n";
    }

    public NullPointerException getSrcOfException() {
        NullPointerException e = new NullPointerException("some object is null");
        e.setStackTrace(new StackTraceElement[0]);
        return e;
    }

    public String getExpectedConsole(Note note) {
        return " [" + note.getType().name() + "] " + note.getName() +
                (note.getDetails().isEmpty() ? "" : "\n\t" + note.getDetails().replaceAll("\n", "\n\t"));
    }

    public String getExpectedFile(Note note) {
        return " ~ [" + note.getType().name() + "] ~ " + note.getName() + " ~ " +
                (note.getDetails().isEmpty() ? "" : "\n\t" + note.getDetails().replaceAll("\n", "\n\t"));
    }

    public List<Note> getSrc() {
        return List.of(
                new Note(LocalDateTime.now(), Type.ERROR, "Some big error",
                "please don't worry it's only error"),
                new Note(LocalDateTime.now(), Type.INFO, "application starts",
                        "module 1 start\nmodule 2 start\nmodule 3 start"),
                new Note(LocalDateTime.now(), Type.WARNING, "user write wrong name for object"));
    }

}
