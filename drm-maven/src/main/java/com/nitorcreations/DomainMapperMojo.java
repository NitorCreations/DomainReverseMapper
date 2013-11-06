package com.nitorcreations;

import java.io.File;
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

@Mojo(name = "map", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class DomainMapperMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;
    @Component
    private MavenProject project;
    private DomainMapperFactory factory = new DomainMapperFactory();
    private SafeWriter writer = new SafeWriter();

    @Override
    public void execute() throws MojoExecutionException {
        try {
            List<URL> projectClasspathList = getClasspathUrls();
            DomainMapper mapper = factory.create(new String[] { "com.nitorcreations.test" }, new URLClassLoader(projectClasspathList.toArray(new URL[0])));
            File outputFile = new File(outputDirectory, "domainmap.dot");
            writer.writeToFile(outputFile, mapper.describeDomain());
        } catch (ClassNotFoundException | DependencyResolutionRequiredException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<URL> getClasspathUrls() throws DependencyResolutionRequiredException, MojoExecutionException {
        List<URL> projectClasspathList = new ArrayList<URL>();
        for (String element : (List<String>) project.getCompileClasspathElements()) {
            try {
                projectClasspathList.add(new File(element).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new MojoExecutionException(element + " is an invalid classpath element", e);
            }
        };
        return projectClasspathList;
    }
}
