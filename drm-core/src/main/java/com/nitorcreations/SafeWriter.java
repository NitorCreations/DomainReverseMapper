package com.nitorcreations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SafeWriter {
    public void writeToFile(final File outputFile, final String domainDescription) throws IOException {
        checkoutOutputDir(outputFile);
        try (FileWriter w = new FileWriter(outputFile)) {
            w.write(domainDescription);
        } catch (IOException e) {
            throw e;
        }
    }

    private void checkoutOutputDir(final File outputFile) {
        File f = outputFile.getParentFile();
        if (!f.exists()) {
            f.mkdirs();
        }
    }
}
