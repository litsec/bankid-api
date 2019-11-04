/*
 * Copyright 2018 Litsec AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.litsec.bankid.demo;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of AJP for Tomcat.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
@Configuration
public class TomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

  @Value("${tomcat.ajp.port:8009}")
  private int ajpPort;

  @Value("${tomcat.ajp.remoteauthentication:false}")
  private String remoteAuthentication;

  @Value("${tomcat.ajp.enabled:false}")
  private boolean tomcatAjpEnabled;

  @Override
  public void customize(TomcatServletWebServerFactory factory) {
    if (this.tomcatAjpEnabled) {
      Connector ajpConnector = new Connector("AJP/1.3");
      ajpConnector.setPort(this.ajpPort);
      ajpConnector.setSecure(false);
      ajpConnector.setAllowTrace(false);
      ajpConnector.setScheme("http");
      factory.addAdditionalTomcatConnectors(ajpConnector);
    }
  }
}
