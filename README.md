# DABACOG

Dabacog is the acronym for '<ins>Da</ins>ta<ins>ba</ins>se <ins>Co</ins>de <ins>G</ins>eneration'
and comes as a command line interface (CLI).

            ____        __
           / __ \____ _/ /_  ____ __________  ____ _
          / / / / __ `/ __ \/ __ `/ ___/ __ \/ __ `/
         / /_/ / /_/ / /_/ / /_/ / /__/ /_/ / /_/ /
        /_____/\__,_/_,___/\__,_/\___/\____/\__, /
                                           /____/
        Version 0.1.0 - Alpha

        Usage: dabacog [-hnvV] -s=<source> [-t=<targets> [<targets> [<targets>]]]...
        Example: dabacog --source ./app.dxd --targets sql code --verbose-logging

        Dabacog CLI arguments:

          -h, --help              Show this help message and exit.
          -n, --no-logging        Disables logging to the console.
          -s, --source=<source>   Defines the description (Dxd) source file path.
          -t, --targets=<targets> [<targets> [<targets>]]
                                  Defines the targets to generate. Use 'd' or
                                    'diagram', 's' or 'sql' and 'c' or 'code' as
                                    values. The values can be combined space separated.
                                    If no target is defined all targets are generated.
          -v, --verbose-logging   Enables verbose logging. Overrides the no-logging
                                    option.
          -V, --version           Print version information and exit.

Most software projects have a data storage, and a data access layer where the access layer
needs to communicate with the storage layer (typically a database) to perform
[CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations. These operations,
as well as the data access layer designs, are usually simple, repetitive and can be easily derived
from a model describing the required data structures and their relations.
Software developers often spend up to 30% (and more) of their time within a project with tasks
related to [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) and
surrounding problems as testing, setting up the database schema, mapping database tables to
entities, mapping entities to DTOs, and more...

All this makes the described problem an optimal candidate to be solved by code generation. And
exactly that is what Dabacog is intending to achieve. To stay flexible, as not all problems are
generic enough for a code generation approach, there will be an easy option to override or extend
the generated code, both in order to use custom code for custom non problems.

In the long term the vision is to support all major databases (with different SQL dialects) and all
major programming languages, as well as database migration strategies to regard evolving database
schema requirements in an application life-cycle.

# USAGE DEMONSTRATION

1. Create a (<ins>D</ins>abacog <ins>X</ins>ML <ins>D</ins>escription) Dxd file
    ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <!DOCTYPE dxd SYSTEM "dabacog.dtd">

   <dxd name="Dabacog - Book Library Demo">

     <config>
       <diagramsConfig
           diagramDatabaseOutputPath="./generated"
           diagramDatabaseTitle="Dabacog - Database Diagram - Book Library Demo"
           diagramDatabasePrimaryKeyFieldPorts="false"
           diagramDatabaseForeignKeyFieldPorts="true"/>
       <sqlConfig
           sqlDatabaseType="postgresql"
           sqlOutputPath="./generated"
           sqlGlobalSequence="true"
           sqlDropSchema="true"/>
       <codeConfig
           codeType="java"
           codeOutputPath="./generated"
           codePackageName="com.nilsign.dabacog.demo"/>
     </config>

     <entities>

       <class name="Genre">
         <field type="string" name="Name" unique="true" />
       </class>

       <class name="Author">
         <field type="Book" relation="n..n"/>
         <field type="string" name="Name" fts="true"/>
       </class>

       <class name="Loan">
         <field type="Book" relation="1..n"/>
         <field type="Customer" relation="1..n"/>
         <field type="date" name="Starts"/>
         <field type="date" name="Ends" indexed="true"/>
         <field type="date" name="Returns" nullable="true"/>
         <field type="boolean" name="DunningLetter" default="false"/>
       </class>

       <class name="Book">
         <field type="Author" relation="n..n"/>
         <field type="Genre" relation="n..n"/>
         <field type="Location" relation="1..n"/>
         <field type="string" name="Name" fts="true"/>
         <field type="string" name="Isbn" unique="true"/>
         <field type="double" name="Price" nullable="true"/>
       </class>

       <class name="Customer">
           <field type="Address" relation="1..1" nullable="true"/>
           <field type="Contact" relation="1..1"/>
           <field type="string" name="FirstName"/>
           <field type="string" name="LastName"/>
       </class>

       <class name="Address">
           <field type="string" name="Street"/>
           <field type="string" name="Zipcode"/>
           <field type="string" name="City"/>
       </class>

       <class name="Location">
         <field type="Book" relation="n..1"/>
         <field type="string" name="Floor" default="'warehouse'"/>
         <field type="string" name="Shelf" default="''"/>
         <field type="int" name="Position" default="-1"/>
       </class>

       <class name="BookOrder">
         <field type="Book" relation="n..1" nullable="true"/>
         <field type="string" name="Floor" default="'warehouse'"/>
         <field type="string" name="Shelf" default="''"/>
         <field type="int" name="Position" default="-1"/>
       </class>

       <class name="Employee">
         <field type="Employee" relation="1..1" nullable="true"/>
         <field type="Address" relation="1..1"/>
         <field type="string" name="FirstName" />
         <field type="string" name="LastName"/>
       </class>

       <class name="Contact">
         <field type="Customer" relation="1..1"/>
         <field type="string" name="Mobile" nullable="true"/>
         <field type="string" name="Home" nullable="true"/>
         <field type="string" name="Email"/>
       </class>

     </entities>

   </dxd>
    ```

2. Use the Dabacog CLI to generate the diagram, and the code

        java jar dabacog.jar --source [dxd-file-path] --verbose-logging

    Note, that Java 14+ needs to be used to run the dabacog.jar.

3. Dabacog generates automatically

    - A [graph as png](https://github.com/nilsign/dabacog/blob/develop/demo/generated-output/diagrams/DabacogDatabaseDiagram.png)
      showing all database tables including their relations.

    - The database schema generation [Sql script](https://github.com/nilsign/dabacog/blob/develop/demo/generated-output/sql/InitializeDatabase.sql).

    - All Java entity classes, representing all Sql tables and their fields)

    - A Java database connector class [UNDER DEVELOPMENT]

    - All Java repositories containing all [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete)
    operations of any entity class [PLANNED]

# BUILD AND EXECUTE THE DABACOG.JAR

Execute the maven 'package' phase to build a Dabacog fat jar including a valid manifest. The build
dabacog.jar file can be found in your projects build 'target' folder.

To run the Dabacog fat jar from a command line navigate into the Dabacog project´s root folder and
execute:

    java -jar target/dabacog.jar --source ./src/main/resources/dev/library.dxd -verbose-logging

Ensure that the Java version is the same as used in the pom file, which is currently Java 14.

# ROADMAP

As the project started recently and as I am working on it in my spare time, do not expect steady
progress. Anyhow, here are the major milestones of my project visions:

1. Setup project. [DONE]
2. Create a Xml parser and read the Xml model. [DONE]
3. Convert the Xml model into the Dxd model. [DONE]
4. Generate a relational database schema dot diagram and render it with [Graphviz](https://www.graphviz.org/) [DONE]
5. Generate an entity class (code) dot diagram and render it with [Graphviz](https://www.graphviz.org/) [SKIPPED]
6. Generate a Postgres database schema from the Dxd model. [DONE]
7. Generate the Java entity source code from the Dxd model. [DONE]
8. Generate the Java postgres database connector class source code for the DEV, QA and PROD environment. [PLANNED]
8. Generate the Java code and Sql queries required for all CRUD operations on all entities [PLANNED]
9. Generate DTO classes out of many dxd model entity classes. [PLANED]
10. Add support for database schema changes within an already running project, including support for
data migration strategies, by the use of [Flyway](https://flywaydb.org) and/or
[Liquibase](https://www.liquibase.org/). [PLANNED]
11. Support to migrate to Dabacog from already existing projects. [PLANNED]
12. Support for MySQL, MariaDB, Oracle, AWS Aurora, SQLite, and Microsoft SQL Server [PLANNED]
13. Support for Kotlin, C#, C++, C, Python, Go, Dart and more. [PLANNED]
