# Transfer Service

## Requirements

For building and running the application you need:

- [JDK 1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
- [Maven 3](https://maven.apache.org)

## Technology stack & other Open-source libraries

### Data

<details open="open">
   <ul>
      <li><a href="https://www.h2database.com/html/main.html">H2 Database Engine</a> - Java SQL database. Embedded and server modes; in-memory databases</li>
   </ul>
</details>

### Server - Backend

<details open="open">
   <ul>
      <li><a href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">JDK</a> - Java™ Platform, Standard Edition Development Kit</li>
      <li><a href="https://spring.io/projects/spring-boot">Spring Boot</a> - Framework to ease the bootstrapping and development of new Spring Applications</li>
      <li><a href="https://maven.apache.org/">Maven</a> - Dependency Management</li>
   </ul>
</details>

###  Libraries and Plugins

<details open="open">
   <ul>
      <li><a href="https://projectlombok.org/">Lombok</a> - Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.</li>
   </ul>
</details>

### Others 

<details open="open">
   <ul>
      <li><a href="https://git-scm.com/">git</a> - Free and Open-Source distributed version control system</li>
   </ul>
</details>

## Installing

* When the application is running, **H2** (data in memory) will create the necessary tables along with sample data.

* 	Transfer API is accessible at : **http://localhost:8080/api/balance/transfer**

#### Running the application with IDE

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.natwest.transfer.TransferServiceApplication` class from your IDE.

* 	Download the zip or clone the Git repository.
* 	Unzip the zip file (if you downloaded one)
* 	Open Command Prompt and Change directory (cd) to folder containing pom.xml
* 	Open Eclipse
	* File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
	* Select the project
* 	Choose the Spring Boot Application file (search for @SpringBootApplication)
* 	Right Click on the file and Run as Java Application

#### Running the application with Maven

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
$ git clone https://github.com/kedarjavalkar/transfer-service.git
$ cd transfer-service
$ mvn spring-boot:run
```

#### Running the application with Executable JAR

The code can also be built into a jar and then executed/run. Once the jar is built, run the jar by double clicking on it or by using the command 

```shell
$ git clone https://github.com/kedarjavalkar/transfer-service.git
$ cd transfer-service
$ mvn package -DskipTests
$ java -jar target\transfer-service-0.0.1.jar
```

To shutdown the jar, follow the below mentioned steps on a Windows machine.

*	**Taskkill /PID PROCESS_ID_OF_RUNNING_APP /F** execute this command by replacing the **PROCESS_ID_OF_RUNNING_APP** with the actual process id of the running jar found out from executing the previous command


#### Accessing Data in H2 Database

URL to access H2 console: **http://localhost:8080/api/h2-database/**

Fill the login form as follows and click on Connect:

* 	Saved Settings: **Generic H2 (Embedded)**
* 	Setting Name: **Generic H2 (Embedded)**
* 	Driver class: **org.h2.Driver**
* 	JDBC URL: **jdbc:h2:mem:natwest_transfer**
* 	User Name: **admin**
* 	Password:

<img src="documents\h2-console-login.PNG"/>

<img src="documents\h2-console-main-view.PNG"/>

## Testing API

### Testing with Maven

```shell
$ mvn clean test
```
### Curl Request

``` shell
$curl --location --request POST 'http://localhost:8080/api/balance/transfer' \
--header 'Content-Type: application/json' \
--data-raw '{
    "fromAccount": 123456,
    "toAccount": 987654,
    "amount": "150"
}'
```

<!-- CONTACT -->
## Contact

Kedar Kishore Javalkar - kedar.javalkar@gmail.com
