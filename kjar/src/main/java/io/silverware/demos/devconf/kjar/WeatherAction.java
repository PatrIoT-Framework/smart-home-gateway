/*
 * -----------------------------------------------------------------------\
 * PerfCake
 *  
 * Copyright (C) 2010 - 2013 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -----------------------------------------------------------------------/
 */
package io.silverware.demos.devconf.kjar;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class WeatherAction extends Action {

   private Weather weather;

   public WeatherAction(final int temperature, final int humidity) {
      weather = new Weather(temperature, humidity);
   }

   public WeatherAction(final Weather weather) {
      this.weather = weather;
   }

   public Weather getWeather() {
      return weather;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final WeatherAction that = (WeatherAction) o;

      return weather.equals(that.weather);

   }

   @Override
   public int hashCode() {
      return weather.hashCode();
   }

   public String getWeatherString() {
      return weather.getTemperature() + "°C / " + weather.getHumidity() + "%";
   }

   @Override
   public String toString() {
      return "WeatherAction{" +
            "weather=" + weather +
            '}';
   }
}
