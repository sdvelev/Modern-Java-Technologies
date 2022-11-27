package bg.sofia.uni.fmi.mjt.markdown;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static bg.sofia.uni.fmi.mjt.markdown.MarkdownSymbol.STAR;

public class SingleFileConverter {

    private final static String BEGINNING = "<html>" + System.lineSeparator() + "<body>" + System.lineSeparator();
    private final static String ENDING = "</body>" + System.lineSeparator() + "</html>" + System.lineSeparator();
    private final static int FOUR_STARS = 4;
    private final static int TWO_STARS = 2;

    private Path from;
    private Path to;
    private boolean withStreams;
    private Reader reader;
    private Writer writer;

    SingleFileConverter(Path from, Path to) {
        this.from = from;
        this.to = to;
        this.withStreams = false;
    }

    SingleFileConverter(Reader reader, Writer writer) {
        this.from = null;
        this.to = null;
        this.withStreams = true;
        this.reader = reader;
        this.writer = writer;
    }

    public void executeSteps() {

        writeToFile("<html>" + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        writeToFile("<body>" + System.lineSeparator(), StandardOpenOption.APPEND);

        this.readFromFile();

        writeToFile("</body>" + System.lineSeparator(), StandardOpenOption.APPEND);
        writeToFile("</html>" + System.lineSeparator(), StandardOpenOption.APPEND);
    }

    private void writeToFile(String toWrite, StandardOpenOption openOption1, StandardOpenOption openOption2) {

        if (this.withStreams == false) {
            try (var bufferedWriter = Files.newBufferedWriter(this.getTo(), openOption1, openOption2)) {

                bufferedWriter.write(toWrite);
                bufferedWriter.flush();

            } catch (IOException e) {
                throw new IllegalStateException("Cannot write into file", e);
            }
        } else {
            try (var bufferedWriter = new BufferedWriter(writer)) {

                bufferedWriter.write(toWrite);
                bufferedWriter.flush();

            } catch (IOException e) {
                throw new IllegalStateException("Cannot write into file", e);
            }
        }
    }

    private void writeToFile(String toWrite, StandardOpenOption openOption) {

        if (this.withStreams == false) {
            try (var bufferedWriter = Files.newBufferedWriter(this.getTo(), openOption)) {

                bufferedWriter.write(toWrite);
                bufferedWriter.flush();

            } catch (IOException e) {
                throw new IllegalStateException("Cannot write into file", e);
            }
        } else {
            try (var bufferedWriter = new BufferedWriter(writer)) {

                bufferedWriter.write(toWrite);
                bufferedWriter.flush();

            } catch (IOException e) {
                throw new IllegalStateException("Cannot write into file", e);
            }
        }
    }

    private void writeHeading(String toWrite) {

        int level = 0;

        for (int i = 0; toWrite.charAt(i) == '#'; i++) {
            ++level;
        }

        String withoutSymbol = toWrite.replaceAll("#", "");
        withoutSymbol.trim();

        writeToFile("<h" + level + ">" + withoutSymbol.trim() + "</h" + level + ">" +
            System.lineSeparator(), StandardOpenOption.APPEND);
    }

    private void writeSingle(String readLine) {

        String processed = "";

        for (int i = 0; i < readLine.length(); i++) {

            if (readLine.charAt(i) == MarkdownSymbol.BACK_QUOTE.getSymbol()) {
                processed = processed.concat("<code>");
                i++;

                for (; i < readLine.length(); i++) {

                    if (readLine.charAt(i) == MarkdownSymbol.BACK_QUOTE.getSymbol()) {
                        processed = processed.concat("</code>");
                        break;
                    } else {
                        processed = processed.concat(String.valueOf(readLine.charAt(i)));
                    }
                }

            } else if (readLine.charAt(i) == STAR.getSymbol()) {

                if (readLine.charAt(i + 1) == STAR.getSymbol()) {
                    i += 2;
                    processed = processed.concat("<strong>");

                    for (; i < readLine.length(); i++) {

                        if (readLine.charAt(i) == STAR.getSymbol()) {
                            processed = processed.concat("</strong>");
                            i++;
                            break;
                        } else {
                            processed = processed.concat(String.valueOf(readLine.charAt(i)));
                        }
                    }

                } else {

                    processed = processed.concat("<em>");
                    i++;

                    for (; i < readLine.length(); i++) {

                        if (readLine.charAt(i) == STAR.getSymbol()) {
                            processed = processed.concat("</em>");
                            break;
                        } else {
                            processed = processed.concat(String.valueOf(readLine.charAt(i)));
                        }
                    }
                }

            } else {
                processed = processed.concat(String.valueOf(readLine.charAt(i)));
            }
        }

        writeToFile(processed + System.lineSeparator(), StandardOpenOption.APPEND);
    }

    private void processLine(String readLine) {

        char[] toArray = readLine.toCharArray();

        if (readLine.contains(new String(String.valueOf(MarkdownSymbol.HEADING.getSymbol())))) {
            writeHeading(readLine);
        } else if (readLine.contains(new String(String.valueOf(MarkdownSymbol.STAR.getSymbol()))) ||
            readLine.contains(new String(String.valueOf(MarkdownSymbol.BACK_QUOTE.getSymbol())))) {

            writeSingle(readLine);
        } else {

            writeToFile(readLine + System.lineSeparator(), StandardOpenOption.APPEND);
        }

    }

    private void readFromFile() {

        if (this.withStreams == false) {

            try (var bufferedReader = Files.newBufferedReader(this.getFrom())) {

                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    processLine(line);

                }

            } catch (IOException e) {
                throw new IllegalStateException("Cannot read from file", e);
            }

        } else {

            try (var bufferedReader = new BufferedReader(this.reader)) {

                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    processLine(line);
                }

            } catch (IOException e) {
                throw new IllegalStateException("Cannot write into file", e);
            }
        }
    }

    private Path getFrom() {
        return this.from;
    }

    private Path getTo() {
        return this.to;
    }
}
