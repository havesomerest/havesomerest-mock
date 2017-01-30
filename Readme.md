Havesomerest-mock is a (REST) API mocking framework, currently in a proof of concept state. The tests are written in plain JSON or XML format and placed in the right place in the folder structure.

# The concept

First, please read the concept section of the [Test framework](https://github.com/havesomerest/havesomerest).

The Mocking framework uses the same structure as the test framework. It means that if a request hit the mock server and there is a matching test case for it, the framework would send back the specified response.

Because the tests and the mocks are using the same format, writing tests is basically means mocking that API as well. So if a team needs to develop against your API, you don't need to setup and maintain a full development environment, they can just use the tests as mocks. In a microservice based environment you can save tons of effort with this approach. And it has the benefit that all the additional mock scenarios the other team(s) specif(y/ies) are actual test cases for your team to fulfill! 

# Building

The project can be build with maven and requires Java 1.8.

```
./mvn clean package
```

# Running the framework

```
cp target/havesomerest-mock-1.1.0.jar . && java -jar havesomerest-mock-1.1.0.jar
```

# Planned features

Console and websocket based (G)UI, request capture, interactive mode, etc.
