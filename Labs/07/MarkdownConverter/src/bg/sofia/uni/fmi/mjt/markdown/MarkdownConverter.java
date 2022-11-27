package bg.sofia.uni.fmi.mjt.markdown;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;


public class MarkdownConverter implements MarkdownConverterAPI {


    /**
     * Converts a text in markdown format readable from {@code source} to a text
     * in corresponding HTML format written to {@code output}.
     *
     * @param source the source character-based input stream
     * @param output the output character-based output stream
     */
    @Override
    public void convertMarkdown(Reader source, Writer output) {

        SingleFileConverter s = new SingleFileConverter(source, output);
        s.executeSteps();
    }

    /**
     * Converts a text file in markdown format to a text file in corresponding HTML format.
     *
     * @param from Path of the input file
     * @param to   Path of the output file
     */
    @Override
    public void convertMarkdown(Path from, Path to) {

        SingleFileConverter s = new SingleFileConverter(from, to);
        s.executeSteps();
    }

    /**
     * Converts all markdown files in a source directory to corresponding HTML files in the target directory.
     * Each markdown file has an extension .md and is converted to an HTML file with the same name
     * and extension .html. The source directory will not contain any subdirectories.
     *
     * @param sourceDir Path of the source directory
     * @param targetDir Path of the target directory
     */
    @Override
    public void convertAllMarkdownFiles(Path sourceDir, Path targetDir) {

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir)) {

            for (Path file : stream) {

                if (file.getFileName().toString().endsWith(".md")) {
                    String fileName = file.getFileName().toString();
                    fileName = fileName.replaceFirst("\\.md", ".html");
                    Path newPath = targetDir.resolve(fileName);
                    SingleFileConverter s = new SingleFileConverter(file, newPath);
                    s.executeSteps();
                }
            }

        }
        catch (IOException | DirectoryIteratorException e) {

            throw new IllegalStateException("Cannot search in directory", e);
        }

    }
}
