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

import java.security.KeyStore;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import se.litsec.bankid.rpapi.service.BankIDClient;
import se.litsec.bankid.rpapi.service.BankIDClientImpl;
import se.litsec.bankid.rpapi.service.QRGenerator;
import se.litsec.bankid.rpapi.support.BankIDRestTemplateFactory;
import se.litsec.bankid.rpapi.support.KeyStoreFactoryBean;
import se.litsec.bankid.rpapi.support.ZxingQRGenerator;

/**
 * Configuration class for the BankID demo application.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
@Configuration
public class BankIDDemoConfiguration {

  /** The resource holding the trust anchor for TLS connections. */
  @Value("${bankid.rp-trust}")
  private Resource trustedCertificate;

  /** The resource storing the BankID Relying Party keystore, i.e., the keystore holding the RP key/certificate. */
  @Value("${bankid.rp-credentials.store}")
  private Resource keyStore;

  /** The password for the RP keystore. */
  @Value("${bankid.rp-credentials.store-password}")
  private char[] keyStorePassword;

  /** The RP keystore type (default is JKS). */
  @Value("${bankid.rp-credentials.store-type:JKS}")
  private String keyStoreType;

  /** The key password for the key in the RP keystore. */
  @Value("${bankid.rp-credentials.key-password}")
  private char[] keyPassword;

  /**
   * The key alias for the RP key/certificate. If not set, it is assumed that there is only one key entry in the
   * keystore.
   */
  @Value("${bankid.rp-credentials.key-alias:}")
  private String keyAlias;

  /**
   * The BankID Relying Party client keystore.
   * 
   * @return the RP keystore (or a factory for it, but the result will be a {@code KeyStore} bean)
   */
  @Bean(name = "rpKeyStore")
  public KeyStoreFactoryBean keyStoreFactory() {
    return new KeyStoreFactoryBean(this.keyStore, this.keyStorePassword, this.keyStoreType);
  }

  /**
   * The REST template to use while communicating with the BankID server.
   * 
   * @param keyStore
   *          the RP keystore
   * @return the REST template (or a factory, but the result will be a {@code RestTemplate} bean)
   * @throws Exception
   *           for init errors
   */
  @Bean(name = "restTemplate")
  public BankIDRestTemplateFactory restTemplateFactory(
      @Qualifier("rpKeyStore") KeyStore keyStore) throws Exception {

    BankIDRestTemplateFactory factory = new BankIDRestTemplateFactory();
    factory.setTrustedRoot(this.trustedCertificate);
    factory.setKeyStore(keyStore);
    factory.setKeyPassword(this.keyPassword);
    factory.setKeyAlias(this.keyAlias);
    return factory;
  }

  /**
   * The BankID client.
   * 
   * @param restTemplate
   *          the REST template to use for communication with the BankID server
   * @param wsEndpoint
   *          the BankID WS address
   * @return the BankID client
   */
  @Bean
  public BankIDClient bankIDClient(
      RestTemplate restTemplate,
      @Value("${bankid.ws.endpoint}") String wsEndpoint) {

    return new BankIDClientImpl(restTemplate, wsEndpoint, this.qrGenerator());
  }

  /**
   * The QR generator.
   * 
   * @return the QR generator
   */
  @Bean
  public QRGenerator qrGenerator() {
    return new ZxingQRGenerator();
  }

}
