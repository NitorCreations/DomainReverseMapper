package com.nitorcreations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * @requiresDependencyResolution compile
 */
@Mojo(name = "map", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class DomainMapperMojo extends AbstractMojo {
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;
    @Component
    private MavenProject project;

    @SuppressWarnings("unchecked")
    @Override
    public void execute() throws MojoExecutionException {
        List<String> classpathElements = null;
        try {
            classpathElements = project.getCompileClasspathElements();
            List<URL> projectClasspathList = new ArrayList<URL>();
            for (String element : classpathElements) {
                try {
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException(element + " is an invalid classpath element", e);
                }
            };
            DomainMapper mapper = DomainMapperFactory.create(new String[] { "com.nitorcreations.test" }, new URLClassLoader(projectClasspathList.toArray(new URL[0])));
            writeToFile(mapper.describeDomain());
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException(e.getMessage());
        } catch (DependencyResolutionRequiredException e) {
            new MojoExecutionException("Dependency resolution failed", e);
        }
    }

    private void writeToFile(String domainDescription) throws MojoExecutionException {
        File f = checkoutOutputDir();
        File dotFile = new File(f, "domainmap.dot");
        try (FileWriter w = new FileWriter(dotFile)) {
            w.write(domainDescription);
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + dotFile, e);
        }
    }

    private File checkoutOutputDir() {
        File f = outputDirectory;
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }
}
