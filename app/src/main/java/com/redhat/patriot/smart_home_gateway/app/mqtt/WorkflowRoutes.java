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

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import com.redhat.patriot.smart_home_gateway.kjar.AirConditioningCommand;
import com.redhat.patriot.smart_home_gateway.kjar.DoorCommand;
import com.redhat.patriot.smart_home_gateway.kjar.FireplaceCommand;
import com.redhat.patriot.smart_home_gateway.kjar.LightCommand;
import com.redhat.patriot.smart_home_gateway.kjar.MediaCenterCommand;

/**
 * These routes are listening on the commands queue and executing the needed workflow to fulfill the commands.
 * The workflow is simulated in Camel routes but here we could execute any business process or Node-Red flow.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class WorkflowRoutes extends RouteBuilder {

   private final String iotHost = System.getProperty("iot.host", "127.0.0.1:8282");

   private void configureCommandsRoutes() throws Exception {
      from("direct:commands")
            .bean("cacheMicroservice", "processCommand")
               .choice()
               .when().simple("${body} is 'com.redhat.patriot.smart_home_gateway.kjar.AirConditioningCommand'")
                  .to("direct:ac")
               .when().simple("${body} is 'com.redhat.patriot.smart_home_gateway.kjar.DoorCommand'")
                  .to("direct:door")
               .when().simple("${body} is 'com.redhat.patriot.smart_home_gateway.kjar.FireplaceCommand'")
                  .to("direct:fire")
               .when().simple("${body} is 'com.redhat.patriot.smart_home_gateway.kjar.LightCommand'")
                  .to("direct:led")
               .when().simple("${body} is 'com.redhat.patriot.smart_home_gateway.kjar.BatchLightCommand'")
                  .to("direct:ledBatch")
               .when().simple("${body} is 'com.redhat.patriot.smart_home_gateway.kjar.MediaCenterCommand'")
                  .to("direct:media");
   }

   private void configureLedsRoutes() throws Exception {
      from("direct:led").choice()
            .when().simple("${body.place} == '" + LightCommand.Place.ALL + "'").to("direct:ledAll")
            .otherwise().to("direct:ledSingle");
      from("direct:ledSingle").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .setHeader("led", simple("${body.place.led}"))
            .setHeader("r", simple("${body.state.r}"))
            .setHeader("g", simple("${body.state.g}"))
            .setHeader("b", simple("${body.state.b}"))
            .setBody().constant("").to("jetty:http://" + iotHost + "/led/setrgb");
      from("direct:ledAll").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .setHeader("r", simple("${body.state.r}"))
            .setHeader("g", simple("${body.state.g}"))
            .setHeader("b", simple("${body.state.b}"))
            .setBody().constant("").to("jetty:http://" + iotHost + "/led/setrgb/all");
      from("direct:ledBatch").setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setBody().simple("${body.batch}").to("jetty:http://" + iotHost + "/led/batch");
   }

   private void configureAcRoutes() throws Exception {
      from("direct:ac").choice()
            .when().simple("${body.ac} == '" + AirConditioningCommand.Ac.NORMAL + "'")
               .to("direct:acOff")
            .otherwise()
               .to("direct:acOn");

      from("direct:acOn").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/ac/on");
      from("direct:acOff").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/ac/off");
   }

   private void configureFireplaceRoutes() throws Exception {
      from("direct:fire").choice()
            .when().simple("${body.fire} == '" + FireplaceCommand.Fire.HEAT + "'")
               .to("direct:fireOn")
            .otherwise()
               .to("direct:fireOff");
      from("direct:fireOn").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/fireplace/on");
      from("direct:fireOff").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/fireplace/off");
   }

   private void configureMediacenterRoutes() throws Exception {
      from("direct:media").choice()
            .when().simple("${body.media} == '" + MediaCenterCommand.Media.OFF + "'")
               .to("direct:tvOff")
            .when().simple("${body.media} == '" + MediaCenterCommand.Media.NEWS + "'")
               .to("direct:tvNews")
            .when().simple("${body.media} == '" + MediaCenterCommand.Media.COFFEE + "'")
               .to("direct:tvCoffee")
            .otherwise()
               .to("direct:tvRomantic");
      from("direct:tvRomantic").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/tv/romantic");
      from("direct:tvNews").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/tv/news");
      from("direct:tvCoffee").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/tv/coffee");
      from("direct:tvOff").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/tv/off");
   }

   private void configureFrontDoorRoutes() throws Exception {
      from("direct:door").choice()
            .when().simple("${body.door} == '" + DoorCommand.Door.FRONT + "'")
               .to("direct:doorFront")
            .otherwise()
               .to("direct:doorRear");
      from("direct:doorFront").choice()
            .when().simple("${body.openPercentage} == 0")
               .to("direct:doorClose")
            .otherwise()
               .to("direct:doorOpen");
      from("direct:doorOpen").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/door/open");
      from("direct:doorClose").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/door/close");
   }

   private void configureRearDoorRoutes() throws Exception {
      from("direct:doorRear").choice()
            .when().simple("${body.openPercentage} == 0")
               .to("direct:windowClose")
            .otherwise()
               .to("direct:windowOpen");
      from("direct:windowOpen").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/window/open");
      from("direct:windowClose").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("jetty:http://" + iotHost + "/window/close");
   }

   @Override
   public void configure() throws Exception {
      // where does the command belong to?
      configureCommandsRoutes();

      // led lights
      configureLedsRoutes();

      // air conditioning
      configureAcRoutes();

      // fireplace
      configureFireplaceRoutes();

      // media center
      configureMediacenterRoutes();

      // door, aka front door, aka main entrance
      configureFrontDoorRoutes();

      // window, aka rear door
      configureRearDoorRoutes();
   }

}
