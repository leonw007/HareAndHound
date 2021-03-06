
This repository contains a small Hare and Hounds web application.  It is a complete web stack from frontend to server to database.

Setup
-----

It depends only on Java 8 and Maven to build and run.

For details on installing Java 8 and an IDE that includes Maven, see the [OOSE Tools] page.


Usage
-----

The code should be directly importable as an existing Maven project into Eclipse or IntelliJ, and should directly build and run from within the IDE.  You need to set up a run configuration to run <tt>Bootstrap.main()</tt>.

If you instead want to compile and run from the command line, assuming you have the Maven command line installed:

```console
mvn package
java -jar target/hareandhounds1-1.0-SNAPSHOT.jar
```

Now simply point your browser to http://localhost:8080 to use the application.

[OOSE Tools]:http://pl.cs.jhu.edu/oose/resources/tools.shtml
[OOSE Web Java Lecture]: http://pl.cs.jhu.edu/oose/lectures/webjava.shtml

