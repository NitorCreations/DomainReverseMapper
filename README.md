DomainReverseMapper
===================

Automatically generate Graphviz diagram from your domain classes.

Using reflection, Domain Reverse Mapper scans your packages that contain your domain entities. It then builds a graph of entity compositions and inheritances and creates a Graphviz .dot file from that.

![domain model](https://dl.dropboxusercontent.com/u/734976/domain.png)

- black arrows describe composition (private field in one class refers to another domain class
- empty arrows show inheritance
- each package is grouped as a subgraph

## Benefits

- use code as forward thinking documentation to model your domain
- always have up-to-date diagram of your domain model
- use the diagram to help in discussions with your team and stakeholders

## Usage

This tool can be either used manually from command line or hooked as a maven plugin to your build process.

### Using from command-line

### Using with Maven

Add to your pom.xml the following:

	<build>
		<plugins>
			<plugin>
				<groupId>com.nitorcreations</groupId>
				<artifactId>drm-maven-plugin</artifactId>
				<version>1.0-SNAPSHOT</version>
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





