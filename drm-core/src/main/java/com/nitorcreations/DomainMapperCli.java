package com.nitorcreations;

import java.io.IOException;

public class DomainMapperCli {
    private static DomainMapperLauncher launcher = new DomainMapperLauncher();

    public static void main(final String[] args) throws ClassNotFoundException, IOException {
        launcher.run(args);
    }

    public static void setLauncher(final DomainMapperLauncher launcher) {
        DomainMapperCli.launcher = launcher;
    }
}
