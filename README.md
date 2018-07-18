Complex demo to drive Intelligent Home on DevConf 2016
===

Prerequisities:
Install JBoss A-MQ, tested with version 6.2.1.redhat-084.
Start the broker with `bin/standalone.sh`.
Add an admin user mqtt/mqtt:

```
JBossA-MQ:karaf@root> amq:create-admin-user
Please specify a user...
New user name: mqtt
Password for mqtt: 
Verify password for mqtt: 
```

Provide the following system properties either by setting the environment or by using -Dpropert=value parameters.

* iot.host - IP address and port of the RPI based services in the home
* iot.ws.host - IP address and port of the RPi based websocket service in the home that broadcasts messages on the home state, typically same as iot.host but with a different port number (9292)
* mqtt.host - IP address and port of the A-MQ broker
* mobile.host - IP address and port to which the internal REST server will be bind, this is the control interface for the mobile application

Compile with:

`mvn package`

Run the jar file in app/target. Use the following parameters to fix logging of some components and provide system properties:

`java -Diot.host=10.40.1.23:8282 -Diot.ws.host=10.40.1.23:9292 -Dmobile.host=0.0.0.0:8283 -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager -jar app/target/smart-home-gateway-app-1.0-SNAPSHOT.jar`

Overview of the demo
====

This demo covers everything that runs on OpenShift v3 in the following figure.

![Demo overview](https://raw.githubusercontent.com/px3/SilverWare-Demos/devel/demos/devconf-2016/gateway/ih-overview.png)

Details of service integration
====

All the components of the gateway are connected as shown in the following figure.

![Demo details](https://raw.githubusercontent.com/px3/SilverWare-Demos/devel/demos/devconf-2016/gateway/ih-detail.png)
