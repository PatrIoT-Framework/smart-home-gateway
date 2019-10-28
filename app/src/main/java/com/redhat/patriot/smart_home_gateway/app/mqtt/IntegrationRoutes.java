/*
 * Copyright 2019 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.redhat.patriot.smart_home_gateway.app.mqtt;

//import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 * These routes wire the individual components together. Actions go to actions topic, then to
 * business rules engine (Drools), resulting commands go to the commands queue. Also, Drools can send status updates to
 * the mobile phone. Weather sensor is periodically checked (pull model) and the corresponding actions
 * are placed in the actions topic.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class IntegrationRoutes extends RouteBuilder {

   @Override
   public void configure() throws Exception {
      final String iotHost = System.getProperty("iot.host", "127.0.0.1:8282");
      final String iotWSHost = System.getProperty("iot.ws.host", "127.0.0.1:9292");
      final String mobileHost = System.getProperty("mobile.host", "0.0.0.0:8283");

      // First, we need to start consumers from "direct" to avoid warnings while Camel processes exchange

      // process weather
      from("direct:weather").bean("weatherMicroservice", "processWeather");

      // process rfid
      from("direct:rfid").bean("weatherMicroservice", "processRfid");

      // expose REST API for the mobile phone to be able to send actions
      from("jetty:http://" + mobileHost + "/mobile").setBody().simple("${in.header.button}")
               .bean("mobileGatewayMicroservice", "mobileAction");

      // process actions in Drools
      from("direct:actions")
            .bean("droolsMicroservice", "processAction");

      // read weather from a topic deployed in the home
      from("ahc-ws://" + iotWSHost + "/weather")
            .to("direct:weather");

   }

}