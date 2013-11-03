package com.nitorcreations;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class DomainMapperCli {
    private static DomainMapperLauncher launcher = new DomainMapperLauncher();

    public static void main(final String[] args) throws ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException {
        launcher.run(args);
    }

    public static void setLauncher(final DomainMapperLauncher launcher) {
        DomainMapperCli.launcher = launcher;
    }
}
