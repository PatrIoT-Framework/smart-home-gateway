Smart Gateway for SmartHome
=====

This project contains Gateway modified to use with 
Virtual smart home. In order to build this application
you need:

* Java version 8
* Maven
* Docker - for containerized build

## Building and running the app

To build the application execute following command in
the sources directory
```bash
$ mvn clean package
```

The command will produce `target` directories.
```
.
|-- app
|   |-- src
|   `-- target
|-- kjar
|   |-- src
|   `-- target
`-- target
    `-- lib
```

The executable application is then located in 
`app/target/smart-home-gateway-app-1.0-SNAPSHOT.jar`

## App running

For successful execution of the Gateway, you need to 
start the app located in the `app/target` directory.

```bash
java -Diot.host=${SMART_HOME_HOST}:8282 -Diot.ws.host=${SMART_HOME_HOST}:9292 -jar app/target/smart-home-gateway-app-1.0-SNAPSHOT.jar
```

This will start the application and connect it to the running
instance of Virtual Smart Home.

__WARNING__ The application must be stored in the target
directory, because there are all the dependencies for the
classpath, if you'd need to move it, please move whole
`app/target` directory for successful starting.

 ## Build Docker image
 
 In order to build docker image you'll need to at first
 build tha app and then invoke `docker build` command
 
 ```bash
$ mvn clean package
$ docker build . --tag ${IMAGE_TAG} 
```

Docker will produce the image under your specified tag
that is ready to be connected to the `PatrIoT` simulated
environment.

## Execute docker image

In order to execute this docker image you'll need to know

* The tag you've build the image before
* IP address of the Virtual Smart Home deployment

The execution will be performed in this manner

```bash
$ docker run -e IOT_HOST="${IOT_HOST}:8282" -e IOT_WS_HOST="${IOT_HOST}:9292" ${IMAGE_TAG}
```
