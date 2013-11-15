package com.nitorcreations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Test;

public class SafeWriterTest {
    SafeWriter writer;

    @Before
    public void setup() {
        writer = new SafeWriter();
    }

    @Test
    public void writesToCorrectFile() throws MojoExecutionException, IOException {
        File tempFile = createTempFile();
        writer.writeToFile(tempFile, "foo");
        String line = readLine(tempFile);
        assertThat(line, is("foo"));
    }

    @Test
    public void createsDirectoryIfNeeded() throws MojoExecutionException, IOException {
        File tempFile = createTempFileInNonExistingDirectory();
        assertThat(tempFile.exists(), is(false));
        writer.writeToFile(tempFile, "foo");
        assertThat(tempFile.exists(), is(true));
        tempFile.delete();
        tempFile.getParentFile().delete(); // remove temporary directory too
    }

    private String readLine(final File tempFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(tempFile);
        String line = scanner.next();
        scanner.close();
        return line;
    }

    private File createTempFile() throws IOException {
        File tempFile = File.createTempFile("bar", "baz");
        tempFile.deleteOnExit();
        return tempFile;
    }

    private File createTempFileInNonExistingDirectory() throws IOException {
        return new File("newTemp/tempFile.txt");
    }
}
