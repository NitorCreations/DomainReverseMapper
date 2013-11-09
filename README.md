Domain Reverse Mapper (DRM)
===========================

Automatically generate [Graphviz](http://www.graphviz.org/) diagram from your domain classes.

Using reflection, Domain Reverse Mapper scans your packages that contain your domain entities. It then builds a graph of entity compositions and inheritances and creates a Graphviz .dot file from that.

![domain model](https://dl.dropboxusercontent.com/u/734976/domain.png)
The picture above is generated from a simple example domain with Domain Reverse Mapper. In the above picture

- black arrows describe composition (private field in one class refers to another domain class
- empty arrows show inheritance
- each package is grouped as a subgraph

## Benefits

- use code as forward thinking documentation to model your domain
- always have up-to-date diagram of your domain model
- use the diagram to help in discussions with your team and stakeholders

## Usage

This tool can be either used manually **from command line or as a maven plugin** hooked to your build process.

### Using from command-line

Download the latest executable .jar from [Releases](https://github.com/NitorCreations/DomainReverseMapper/releases/tag/drm-1.0-standalone). Run this archive in classpath that also contains your domain model classes. So let's say your domain model is in `domain.jar`, you can execute Domain Reverse Modeler with

    java -cp domain.jar:drm-dore.jar com.nitorcreations.DomainMapperCli -p com.mycompany.domain

This will scan all classes under the package `com.mycompany.domain` and output the .dot file to your console output. If you want to write it to file use switch `-f filename.dot`. If you need to scan multiple packages use format `-p "com.package1, com.package2"`.

Use the generated .dot file with your local Graphviz or any of the online Graphviz tools to show your domain diagram.

**NOTE!** Do not use `java -jar` as this will override the classpath provided by `-cp` switch so your domain classes won't get included and the produced graph will be empty.

### Using with Maven

Add to your pom.xml the following:

	<build>
		<plugins>
			<plugin>
				<groupId>com.nitorcreations</groupId>
				<artifactId>drm-maven-plugin</artifactId>
				<version>1.0</version>
				<configuration>
					<packages>
						<param>com.mycompany.domain</param>
						<param>com.mycompany.other_domain</param>
					</packages>
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

where the `packages` configuration parameter contains a list of packages that should be scanned for domain graph.

When `process-classes` life-cycle phase gets executed, your domain model graph will be saved to `/target/domainmodel.dot`. Use this file with your local Graphviz or any of the online Graphviz tools to show your domain diagram.





