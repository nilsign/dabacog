# DABACOG

Dabacog is the acronym for '<ins>Da</ins>ta<ins>ba</ins>se <ins>Co</ins>de <ins>G</ins>eneration'
and comes as a command line interface (CLI).

Most software projects have a database layer and typically most (if not even all) of the database
operations are [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete)
operations. These operations are usually simple, repetitive (except of the namings) and can be
easily derived from a model that describes the required data structures and their relations.
Software developers often spend up to 30% and more of their time in a project with tasks related to
[CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) and surrounding problems as
testing, setting up the database, mapping database tables to entities or DTOs, and more... 

All this makes the described problem an optimal candidate to be solved by code generation. And
exactly that is what DABACOG is trying to achieve. To stay flexible, as not all problems are generic
enough for a static code generation approach, there will be an easy option to hook into the
generated code and inject custom code, or to even override generated code, both in order to use
custom code for custom problems.

In the long term all major databases (with different SQL dialects) and all mayor programming
languages are planned to be supported.

# SIMPLIFIED USAGE DEMONSTRATION 

1. Create a XML file (also called DXD for <ins>D</ins>abacog <ins>X</ins>ML <ins>D</ins>escription):

        <meta>
            <output path="../generated"/>
            <sql language="postgres">
            <code language="java">
        <meta/>

        <class name="Book">
            <field refersTo="Author" pointsTo="many"/>
            <field refersTo="Genre" pointsTo="one"/>
            <field name="Name" type="string"/>
        </class>

        <class name="Author">
            <field refersTo="Book" pointsTo="many"/>
            <field name="Name" type="string"/>
        </class>

        <class name="Genre">
            <field refersTo="Book" pointsTo="many"/>
            <field name="name" type="string"/>
        </class>

2. Execute the Dabacog CLI command in the same path where the DXD is located

        $ dabacog run

3. Dabacog generates automatically

    - A graph as png showing all database tables including their relations. Per flags fields and all
    kind of attributes can be also displayed in the graph.

    - A graph as png showing all java entities and their relations. Each entity represents all
    field of a SQL table.

    - The database with all tables, fields and attributes.

    - All entity java classes with all required
    [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations, as well as a
    database connector class.

# ROADMAP

The project has just started and I am working on it in my spare time, so do not expect a steady and
strong progress. Generally here are the major milestones an appended progress status (DONE, DEV,
PLANNED).

1. Setup project. [DONE]
2. Create a XML Parser and read the code generation data model. [DEV]
3. Generate a relational database diagram and an entity class diagram (code) with
[Graphviz](https://www.graphviz.org/). [PLANNED]
4. Generate a Postgres database from the data model. [PLANNED]
5. Generate a JAVA source code from the data model. [PLANNED]
6. Generate a JAVA and SQL code for database model changes and resulting database migrations by the
use of [Flyway](https://flywaydb.org) and/or [Liquibase](https://www.liquibase.org/). [PLANNED]
7. Generate DTO classes out of many entity classes. [PLANNED]
8. Support to migrate to DABACOG for already existing projects. [PLANNED]
8. Support for MySQL, MariaDB, Oracle, Microsoft SQL Server. [PLANNED]
9. Support for C#, Kotlin, C++, C, Python, Dart, Go, and more. [PLANNED]
