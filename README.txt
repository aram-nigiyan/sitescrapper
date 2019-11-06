SiteScrapper project

Target:
Grab site content.

Algorithm:
1. Collect company names and logos from search page result table.
2. Load companies ID by name for those who has logo (hereafter just companies).
3. Load companies addresses by ID.
4. Save companies to database and csv file.

Implementation details:
Test coverage of every functionality.
Selenium web driver pool is implemented for that heavy resource reusage.
Abstraction implemented to be chrome/firefox/phantomjs/etc web drivers ready.
Pools utilized: executors, http connection.
Parallel streams used to load web resources.
Configuration file eliminating any hardcoded stuff.
H2 used as database.


Main branches:
master - final stable version
release - same as master plus generated DB, csv & log files

testing:
mvn test

packaging:
mvn -Dmaven.test.skip=true package spring-boot:repackage

running:
java -jar sitescrapper-0.0.1-SNAPSHOT.jar

stats:
Executed on 4 physical cpu i7 processor; 4G network over wifi connection. Processing took 413 seconds.
