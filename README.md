# SigmaBank .sb Processor

A Java console application for processing .sb files according to project specifications.

## Requirements
- Java 17
- Maven 3.8+

## Quick Start
1. Build the project
```bash

mvn -q -e -DskipTests package
```
2. Place input files 
Put your .sb files in the directory from which you will run the application (usually the project root).

```bash

# No sorting, no statistics
java -jar app.jar

# Sort by name (ascending) and display statistics in console
java -jar app.jar --sort=name --order=asc --stat

# Sort by salary (descending) and save statistics to file
java -jar app.jar -s=salary --order=desc --stat -o=file --path=output/statistics.txt

```

## Notes
- Manager is always listed first in each department file.
- Only employees with valid positive salaries are considered (decimal values use a dot).
- Invalid lines are logged to output/error.log
- Department files and statistics are saved to the output/ folder