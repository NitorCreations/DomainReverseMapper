package com.nitorcreations;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainMapperLauncher {
    private static final Logger log = LoggerFactory.getLogger(DomainMapperLauncher.class);
    private DomainMapperFactory factory = new DomainMapperFactory();

    public void run(final String[] args) throws ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException {
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
            DomainMapper domainMapper = factory.create(packages);
            if (line.hasOption('f')) {
                PrintWriter writer = new PrintWriter(line.getOptionValue('f'), "UTF-8");
                writer.println(domainMapper.describeDomain());
                writer.close();
                log.info("Wrote dot to file " + line.getOptionValue('f'));
            } else {
                log.info("GRAPHVIZ DOT FILE STARTS HERE ----:");
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
