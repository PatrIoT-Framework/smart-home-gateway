/*
 * Copyright 2018 Patriot project
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
package com.redhat.patriot.smart_home_gateway.app.drools;

import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

import com.redhat.patriot.smart_home_gateway.kjar.MobileButtonAction;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.annotations.ParamName;

/**
 * Processes a mobile phone button event and creates the corresponding action on the action message topic.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class MobileGatewayMicroservice {

   private static final Logger LOGGER = LogManager.getLogger(MobileGatewayMicroservice.class);

   @Inject
   @MicroserviceReference
   private ProducerTemplate producer;

   public MobileGatewayMicroservice() {
      LOGGER.info("MobileGatewayMicroservice constructor");
   }

   public void mobileAction(@ParamName("button") final String button) {
      LOGGER.info("Mobile phone button {}", button);
      producer.sendBody("direct:actions", new MobileButtonAction(button));
   }

}
