[![Build Status](https://travis-ci.org/markusmo3/uml-reverse-mapper.svg?branch=master)](https://travis-ci.org/markusmo3/uml-reverse-mapper)
[![Coverage Status](https://coveralls.io/repos/github/markusmo3/uml-reverse-mapper/badge.svg)](https://coveralls.io/github/markusmo3/uml-reverse-mapper)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.markusmo3.urm/urm-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.markusmo3.urm/urm-maven-plugin/)

UML Reverse Mapper
===========================

Automatically generate class diagram from your code.

Using reflection, UML Reverse Mapper scans your packages that contain your code. It then builds a graph of class relations and outputs either a Graphviz .dot file or a [PlantUML](http://www.plantuml.com/) .puml file.

### Using from the command-line

Build the `urm-core` project with `mvn clean package` and grab the generated artifact `urm-core.jar`. Then you need the archive that will be analyzed. In this example we use `abstract-factory.jar` and assume the package name to be `com.iluwatar.abstractfactory`. Place the jar-files in the same directory and execute the following command.

    java -cp abstract-factory.jar:urm-core.jar DomainMapperCli -p com.iluwatar.abstractfactory -i com.iluwatar.abstractfactory.Castle

This will scan all classes under the package `com.iluwatar.abstractfactory` except `Castle` that was marked to be ignored and output the .dot file to your console output. If you want to write it to file use switch `-f filename.dot`. If you need to scan multiple packages use format `-p "com.package1, com.package2"`. Note that under Windows OS the classpath separator is `;` instead of `:`

### Using the Maven plugin

Add to your pom.xml the following:
```xml
	<build>
		<plugins>
			<plugin>
				<groupId>com.github.markusmo3.urm</groupId>
				<artifactId>urm-maven-plugin</artifactId>
				<version>1.4.0</version>
				<configuration>
				    <!-- if outputDirectory is not set explicitly it will default to your build dir -->
                    <outputDirectory>${project.basedir}/etc</outputDirectory>
					<packages>
						<param>com.mycompany.mypackage</param>
						<param>com.mycompany.other_package</param>
					</packages>
					<ignores>
						<param>com.mycompany.mypackage.MyClass</param>
						<param>com.mycompany.other_package.OtherClass</param>
					</ignores>
				</configuration>
				<executions>
					<execution>
						<phase>process-classes</phase>
						<goals>
							<goal>map</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
```

where the `packages` configuration parameter contains a list of packages that should be included in the class
diagram and the `ignores` configuration parameter contains a list of types that should be excluded from the class
diagram. `Dependencies` list should contain the artifacts where the classes are found.

When `process-classes` life-cycle phase gets executed, the class diagram will be saved to
`/target/${project.name}.urm.dot` or `/target/${project.name}.urm.puml`. Use this file with your local
Graphviz or any of the online Graphviz tools to show your class diagram.

### Showcases

Here are some class diagrams generated with the `urm-maven-plugin` and the PlantUML Presenter as well as PlantUML's
free generation/hosting service.
