package de.markusmo3.urm;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.net.URLClassLoader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class DomainClassFinder {

    private static final Logger logger = Logger.getLogger(DomainClassFinder.class.getName());

    private static final String URM_PACKAGE = "de.markusmo3.urm";
    private static final boolean ALLOW_FINDING_INTERNAL_CLASSES = Boolean.parseBoolean(
            System.getProperty("de.markusmo3.urm.DomainClassFinder.allowFindingInternalClasses", "false"));

    public static List<Class<?>> findClasses(final List<String> packages, List<String> ignores, final URLClassLoader classLoader) {
        return packages.stream()
                .map(packageName -> getClasses(classLoader, packageName))
                .flatMap(Collection::stream)
                .filter(DomainClassFinder::isNotPackageInfo)
                .filter(DomainClassFinder::isNotAnonymousClass)
                .filter((Class<?> clazz) -> !ignores.contains(clazz.getName()) && !ignores.contains(clazz.getSimpleName()))
                .collect(Collectors.toList());
    }

    private static boolean isNotPackageInfo(Class<?> clazz) {
        return !clazz.getSimpleName().equals("package-info");
    }

    private static boolean isNotAnonymousClass(Class<?> clazz) {
        return !clazz.getSimpleName().equals("");
    }

    private static Set<Class<?>> getClasses(URLClassLoader classLoader, String packageName) {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        classLoadersList.add(classLoader);

        FilterBuilder filter = new FilterBuilder()
                .include(FilterBuilder.prefix(packageName));
        if (!ALLOW_FINDING_INTERNAL_CLASSES) {
            filter.exclude(FilterBuilder.prefix(URM_PACKAGE));
        }

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(filter));
        return Sets.union(reflections.getSubTypesOf(Object.class),
                reflections.getSubTypesOf(Enum.class));
    }

    private DomainClassFinder() {
        // private constructor for utility class
    }
}
