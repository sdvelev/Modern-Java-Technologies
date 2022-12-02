package bg.sofia.uni.fmi.mjt.markdown;

import org.junit.jupiter.api.Test;

import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.Files.move;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MarkdownConverterTest {

    private final static String BEGINNING = "<html>" + System.lineSeparator() + "<body>" + System.lineSeparator();
    private final static String ENDING = "</body>" + System.lineSeparator() + "</html>" + System.lineSeparator();

    @Test
    void testConvertMarkdownWithReaderAndWriterHeading() {

        MarkdownConverter m = new MarkdownConverter();

        var readerToPass = new StringReader("# This is main heading");
        var writerToPass = new StringWriter();

        m.convertMarkdown(readerToPass, writerToPass);

        String expectedOutput = BEGINNING + "<h1>This is main heading</h1>" + System.lineSeparator() + ENDING;
        String output = writerToPass.toString();

        assertEquals(expectedOutput, output, "output is not the same as the expected");
    }

    @Test
    void testConvertMarkdownWithReaderAndWriterBoldText() {

        MarkdownConverter m = new MarkdownConverter();

        var readerToPass = new StringReader("I just love **bold text**.");
        var writerToPass = new StringWriter();

        m.convertMarkdown(readerToPass, writerToPass);

        String expectedOutput = BEGINNING + "I just love <strong>bold text</strong>." + System.lineSeparator() + ENDING;
        String output = writerToPass.toString();

        assertEquals(expectedOutput, output, "output is not the same as the expected");
    }

    @Test
    void testConvertMarkdownWithReaderAndWriterItalicText() {

        MarkdownConverter m = new MarkdownConverter();

        var readerToPass = new StringReader("Italicized text is the *cat's meow*.");
        var writerToPass = new StringWriter();

        m.convertMarkdown(readerToPass, writerToPass);

        String expectedOutput = BEGINNING +
            "Italicized text is the <em>cat's meow</em>." + System.lineSeparator() + ENDING;
        String output = writerToPass.toString();

        assertEquals(expectedOutput, output, "output is not the same as the expected");
    }

    @Test
    void testConvertMarkdownWithReaderAndWriterCodeText() {

        MarkdownConverter m = new MarkdownConverter();

        var readerToPass = new StringReader("Always `.close()` your streams");
        var writerToPass = new StringWriter();

        m.convertMarkdown(readerToPass, writerToPass);

        String expectedOutput = BEGINNING +
            "Always <code>.close()</code> your streams" + System.lineSeparator() + ENDING;
        String output = writerToPass.toString();

        assertEquals(expectedOutput, output, "output is not the same as the expected");
    }

    @Test
    void testConvertMarkdownWithReaderAndWriterBoldItalicCodeText() {

        MarkdownConverter m = new MarkdownConverter();

        var readerToPass = new StringReader("`.close()` *your* **eyes**");
        var writerToPass = new StringWriter();

        m.convertMarkdown(readerToPass, writerToPass);

        String expectedOutput = BEGINNING + "<code>.close()</code> <em>your</em> <strong>eyes</strong>" +
            System.lineSeparator() + ENDING;
        String output = writerToPass.toString();

        assertEquals(expectedOutput, output, "output is not the same as the expected");
    }

    @Test
    void testConvertMarkdownWithFromToTo() throws IOException {

        Path newTempDir = Files.createTempDirectory("test");
        Path newTempFileForReading = Files.createTempFile(newTempDir, "pr", "suf");
        Path newTempFileForWriting = Files.createTempFile(newTempDir, "pr", "suf");

        String toWrite = "##### Heading level 5" + System.lineSeparator() +
            "This is `code()` element" + System.lineSeparator();

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(newTempFileForReading)) {

            bufferedWriter.write(toWrite);
            bufferedWriter.flush();

        } catch (IOException e) {
            throw new IllegalStateException("Cannot write into file", e);
        }

        MarkdownConverter m = new MarkdownConverter();

        m.convertMarkdown(newTempFileForReading, newTempFileForWriting);

        String expected = BEGINNING + "<h5>Heading level 5</h5>" + System.lineSeparator() +
            "This is <code>code()</code> element" + System.lineSeparator() + ENDING;

        String readResult = "";

        try (BufferedReader bufferedReader = Files.newBufferedReader(newTempFileForWriting)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                readResult = readResult.concat(line + System.lineSeparator());

            }

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from file", e);
        }

        assertEquals(expected, readResult, "Result is not the same as the expected");

    }

    @Test
    void testConvertAllMarkdownFiles() throws IOException {

        Path newTempDirIn = createTempDirectory("test");
        Path newTempFileForReadingCorrect = Files.createTempFile(newTempDirIn, "pr", ".md");
        newTempFileForReadingCorrect = move(newTempFileForReadingCorrect,
            newTempFileForReadingCorrect.getParent().resolve("a.md"));
        Path newTempFileForReadingWrong = Files.createTempFile(newTempDirIn, "pr", ".md3");

        Path newTempDirOut = createTempDirectory("test");
        Path newTempFileForWritingCorrect = Files.createTempFile(newTempDirOut, "pr", ".md3");
        newTempFileForWritingCorrect = move(newTempFileForWritingCorrect,
            newTempFileForWritingCorrect.getParent().resolve("a.html"));


        String toWrite = "##### Heading level 5" + System.lineSeparator() +
            "This is *italic* element" + System.lineSeparator();

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(newTempFileForReadingCorrect)) {

            bufferedWriter.write(toWrite);
            bufferedWriter.flush();

        } catch (IOException e) {
            throw new IllegalStateException("Cannot write into file", e);
        }

        MarkdownConverter m = new MarkdownConverter();

        m.convertAllMarkdownFiles(newTempDirIn, newTempDirOut);

        String expected = BEGINNING + "<h5>Heading level 5</h5>" + System.lineSeparator() +
            "This is <em>italic</em> element" + System.lineSeparator() + ENDING;

        String readResult = "";

        try (BufferedReader bufferedReader = Files.newBufferedReader(newTempFileForWritingCorrect)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                readResult = readResult.concat(line + System.lineSeparator());

            }

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from file", e);
        }

        assertEquals(expected, readResult, "Result is not the same as the expected");
    }

    @Test
    void testConvertAllMarkdownFilesInvalidDirectory() {

        MarkdownConverter m = new MarkdownConverter();

        assertFalse(Files.exists(Paths.get("notExist")));

        assertThrows(IllegalStateException.class, () -> m.convertAllMarkdownFiles(Paths.get("notExist"),
            Paths.get("sample")));

    }

}
