# RAML Merge
RAML Merge for Java is a simple Java project that allows you to merge RAML files with !include into a single file from the command line. This code is based of a project from [Mike Stowe that uses PHP](https://github.com/mikestowe/php-ramlMerge). The code was run through OpenAI's playground using the `code-davinci-002` model to generate the Java code. 

## Setup

Download the project from Github and uncompress the zip file. Run the following Maven command to build the jar file.

```mvn clean package```

Once built successfully, run the jar file using the following command and pass in the location of the RAML file.

```java -jar target/raml-merge-1.0-SNAPSHOT.jar <RAML file>```