package com.iluwatar;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class DomainMapperCli {

    private static final Logger log = LoggerFactory.getLogger(DomainMapperCli.class);
    DomainMapper domainMapper;

    public static void main(final String[] args) throws ClassNotFoundException, IOException {
        new DomainMapperCli().run(args);
    }

    public void run(final String[] args) throws ClassNotFoundException, IOException {
        // create the command line parser
        CommandLineParser parser = new BasicParser();
        // create the Options
        Options options = new Options();
        options.addOption("f", "file", true, "write to file");
        options.addOption("p", "package", true, "comma separated list of domain packages");
        options.addOption(OptionBuilder.withArgName("package").hasArgs().isRequired().create('p'));
        try {
            CommandLine line = parser.parse(options, args);
            String[] packages = line.getOptionValue("p").split(",[ ]*");
            log.debug("Scanning domain for packages:");
            for (String packageName : packages) {
                log.debug(packageName);
            }
            domainMapper = DomainMapper.create(Arrays.asList(packages));
            if (line.hasOption('f')) {

                String filename = line.getOptionValue('f');
                Files.write(Paths.get(filename), domainMapper.describeDomain().getBytes());
                log.info("Wrote dot to file " + filename);
            } else {
                log.info(domainMapper.describeDomain());
            }
        } catch (ParseException exp) {
            log.info(exp.getMessage());
            // automatically generate the help statement
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar drm-core.jar", options);
        }
    }

}
