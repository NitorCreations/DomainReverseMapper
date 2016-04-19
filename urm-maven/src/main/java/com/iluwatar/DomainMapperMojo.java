package com.iluwatar;

import com.iluwatar.presenters.Presenter;
import com.iluwatar.presenters.Representation;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "map", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class DomainMapperMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;
    @Component
    private MavenProject project;
    @Parameter(property = "map.packages", required = true)
    private List<String> packages;
    @Parameter(property = "map.ignores", required = false)
    private List<String> ignores;
    @Parameter(property = "presenter", required = false)
    private String presenterString;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (packages.isEmpty())
            throw new MojoFailureException("No packages defined for scanning.");
        try {
            List<URL> projectClasspathList = getClasspathUrls();
            Presenter presenter = Presenter.parse(presenterString);
            DomainMapper mapper = DomainMapper.create(presenter, packages, ignores, new URLClassLoader(projectClasspathList.toArray(new URL[projectClasspathList.size()])));
            Representation representation = mapper.describeDomain();
            String fileName = "urm." + representation.getFileEnding();
            Files.write(Paths.get(outputDirectory.getPath(), fileName), representation.getContent().getBytes());
        } catch (ClassNotFoundException | DependencyResolutionRequiredException | IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<URL> getClasspathUrls() throws DependencyResolutionRequiredException, MojoExecutionException {
        List<URL> projectClasspathList = new ArrayList<>();
        for (String element : (List<String>) project.getCompileClasspathElements()) {
            try {
                projectClasspathList.add(new File(element).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new MojoExecutionException(element + " is an invalid classpath element", e);
            }
        }
        return projectClasspathList;
    }
}
