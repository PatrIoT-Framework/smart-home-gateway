FROM patriotframework/simulator-base:1.8

WORKDIR /target
COPY app/target/ ./

CMD sh -c 'java -Diot.host=${IOT_HOST} -Diot.ws.host=${IOT_WS_HOST} -jar /target/smart-home-gateway-app-1.0-SNAPSHOT.jar'