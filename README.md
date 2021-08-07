# Zendesk
Zendesk challenge Solution.

# Software versions
This is the minimum versions of software required to run this code:
Java 1.8
Apache Maven 3.2.5 

# Instructions
To compile the code simply run the command at root of project:
	mvn clean install

This should successfully compile the code and build a jar : zendesk-1.0.0-RELEASE.jar

Code coverage can be seen via index.html that gets built within target folder.

This program expects commands to be fed from a file 

To run the code run following command where the compiled jar is located :
Compiled jar file name = zendesk-1.0.0-RELEASE.jar
Also need to include the two logging api jars are on classpath : log4j-api-2.14.0.jar and  log4j-core-2.14.0.jar

Example:
java -cp .;zendesk-1.0.0-RELEASE.jar;C:\construction\maven_repository\org\apache\logging\log4j\log4j-api\2.14.0\log4j-api-2.14.0.jar;C:\construction\maven_repository\org\apache\logging\log4j\log4j-core\2.14.0\log4j-core-2.14.0.jar;C:\construction\maven_repository\commons-io\commons-io\2.11.0\commons-io-2.11.0.jar;C:\construction\maven_repository\com\google\code\gson\gson\2.8.7\gson-2.8.7.jar;C:\construction\maven_repository\org\apache\commons\commons-lang3\3.12.0\commons-lang3-3.12.0.jar  com.zensearch.ZendeskSolution



# Assumptions

# Notes
## 1
To read json data have used GSON Library
To handle file reading we have used apache commons 

# Learnings
## 1 Inverted Index
https://medium.com/@igorkopanev/a-brief-explanation-of-the-inverted-index-f082993f8605
https://dev.to/im_bhatman/introduction-to-inverted-indexes-l04

## 2 Large file handling
https://dev.to/franzwong/process-large-json-with-limited-memory-12kb
https://newbedev.com/java-best-approach-to-parse-huge-extra-large-json-file

## 3 Scanner
Using the method next is giving wierd results for subsequent scanned inputs 
So have used 'nextLine' to capture user input
https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo



