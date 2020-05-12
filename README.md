# DABACOG

Dabacog is the acronym for '<ins>Da</ins>ta<ins>ba</ins>se <ins>Co</ins>de <ins>G</ins>eneration'
and comes as a command line interface (CLI).

Most software projects have a database layer and typically most (if not even all) of the database
operations are [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete)
operations. These operations are usually simple, repetitive (except of the namings) and can be
easily derived from a model which describes the required data structures and their relations.
Software developers often spend up to 30% and more of their time in a project with tasks related to
[CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) and surrounding problems as
testing, setting up the database, mapping database tables to entities or DTOs, and more... 

All this makes the described problem an optimal candidate to be solved by code generation. And
exactly that is what DABACOG is intending to achieve. To stay flexible, as not all problems are generic
enough for a static code generation approach, there will be an easy option to hook into the
generated code and inject custom code, or to even override generated code, both in order to use
custom code for custom problems.

In the long term all major databases (with different SQL dialects) and all mayor programming
languages are planned to be supported.

# SIMPLIFIED USAGE DEMONSTRATION 

1. Create an XML file (also called DXD for <ins>D</ins>abacog <ins>X</ins>ML <ins>D</ins>escription):

        <dxd>

            <meta>
                <sql traget="postgres">
                <code target="java">
                <output target="./generated"/>
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

        </dxd>

2. Execute the corresponding Dabacog CLI generation command from the directory that contains the DXD
file or just append the DXD file path as CLI argument.

        $ dabacog run

3. Dabacog generates automatically

    - A graph as png showing all database tables including their relations. Per flags fields and all
    kind of attributes can be also displayed in the graph.

    - A graph as png showing all java entities and their relations. Each entity represents all
    field of a SQL table.

    - The database with all tables, fields and attributes.

    - A database connector class

    - All entity classes (each representing an SQL table and its fields)

    - All functions for the [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete)
    operations for all entity classes

# ROADMAP

As the project has just started and as I am working on it in my spare time, do not expect a steady
or progress. Anyhow, here are the major milestones of my projects vision including an appended
progress status (DONE, DEV, PLANNED).

1. Setup project. [DONE]
2. Create an XML Parser and read the Dxd model. [DONE]
3. Generate a relational database diagram, and an entity class diagram (code) with
[Graphviz](https://www.graphviz.org/). [DEV]
4. Generate a Postgres database schema from the Dxd model. [PLANNED]
5. Generate the Java entity source code from the Dxd model. [PLANNED]
6. Generate the Java and Postgres SQL code required for all CRUD operations on all entities
7. Generate DTO classes out of many dxd model entity classes. [PLANNED]
8. Add support for database schema changes within an already running project, including support for
data migration strategies, by the use of [Flyway](https://flywaydb.org) and/or
[Liquibase](https://www.liquibase.org/). [PLANNED]
9. Support to migrate to DABACOG for already existing projects. [PLANNED]
10. Support for MySQL, MariaDB, Oracle, Microsoft SQL Server. [PLANNED]
11. Support for C#, Kotlin, C++, C, Python, Dart, Go, and more. [PLANNED]
