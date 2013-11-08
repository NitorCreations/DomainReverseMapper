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








