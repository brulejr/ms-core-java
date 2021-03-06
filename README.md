# About
This repo provides common tools for building Spring / Java reactive microservices.
Currently, it extends from the Spring Boot BOM, version `2.3.9.RELEASE`.

## Getting Started
The easiest way to use this library is to import it as a Gradle source dependency.
#### 1. Update settings file
Add the following block to the `settings.gradle` file to map the repository to a
gradle dependency.
```groovy
sourceControl {
    gitRepository(uri("https://github.com/brulejr/ms-core-java.git")) {
        producesModule("io.jrb.labs:ms-core-java")
    }
}
```
#### 2. Import the particular version
This `ms-core-java` dependency may not be imported in the typical fashion within
the `build.gradle` file
```groovy
	implementation 'io.jrb.labs:ms-core-java:0.2.0'
```

## Getting Support
- To file a bug; create a GitHub issue on this repo. Be sure to include details about how to replicate it.

## License
This project is covered by the [MIT License](https://en.wikipedia.org/wiki/MIT_License).
