# Zendesk
Zendesk challenge Solution.

# Software versions
This is the minimum versions of software required to run this code:
Java 1.8
Apache Maven 3.2.5 

Other dependencies are specified in pom file and will be made available when we compile and build the code.

# Instructions
To compile the code simply run the command at root of project:
	mvn clean install

This should successfully compile the code and build a jar : zendesk-1.0.0-RELEASE.jar

Code coverage can be seen via index.html that gets built within target folder.

This program expects commands to be entered at command prompt 

To run the code navigate to 'target' directory  : ( this is where Compiled jar file : zendesk-1.0.0-RELEASE.jar will be present )
We also need to include dependant jar files on classpath which are : 
log4j-api-2.14.0.jar 
log4j-core-2.14.0.jar
commons-io-2.11.0.jar
gson-2.8.7.jar
commons-lang3-3.12.0.jar

Example:
java -cp .;zendesk-1.0.0-RELEASE.jar;C:\maven_repository\log4j-api-2.14.0.jar;C:\\maven_repository\log4j-core-2.14.0.jar;C:\maven_repository\commons-io-2.11.0.jar;C:\maven_repository\gson-2.8.7.jar;C:\maven_repository\commons-lang3-3.12.0.jar  com.zensearch.ZendeskSolution



# Assumptions
1 Code will exit once a search is performed 
2 Code will not exit and start from the beginning for any invalid options entered ( other than quit ) 

# Scenarios to Unit Test / Integration Test
## Search by User attributes having tickets associated to user
1 Search by 'created_at'
2 Search by 'name'
3 Search by 'verified'
4 Search by '_id'

## Search by User attributes having NO tickets associated to user
1 Search by 'created_at'
2 Search by 'name'
3 Search by 'verified'
4 Search by '_id'

## Search by Ticket attributes having users assigned to ticket
1 Search by '_id'
2 Search by 'created_at'
3 Search by 'type'
4 Search by 'subject'
5 Search by 'assignee_id'
6 Search by 'tags'



