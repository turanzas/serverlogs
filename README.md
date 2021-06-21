# Getting Started

## Database
In order to run the application we need a HSQLDB instance running. Here are the steps that we should follow:

* Download [HSQLDB](https://sourceforge.net/projects/hsqldb/files/latest/download) and unzip it into a folder.
* Open a console and inside the database data folder run the following command:
  java -cp ../lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:serverlogs --dbname.0 serverlogs
  
## Jar

* To create the executable jar run
  
  ```mvn package```

* To run the app 

  ``` java -jar target/server-log-1.0-shaded.jar /yourpath/```