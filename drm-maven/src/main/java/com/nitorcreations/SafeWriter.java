package com.nitorcreations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;

public class SafeWriter {
    public void writeToFile(final File outputFile, final String domainDescription) throws MojoExecutionException {
        checkoutOutputDir(outputFile);
        try (FileWriter w = new FileWriter(outputFile)) {
            w.write(domainDescription);
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + outputFile, e);
        }
    }

    private void checkoutOutputDir(final File outputFile) {
        File f = outputFile.getParentFile();
        if (!f.exists()) {
            f.mkdirs();
        }
    }
}
