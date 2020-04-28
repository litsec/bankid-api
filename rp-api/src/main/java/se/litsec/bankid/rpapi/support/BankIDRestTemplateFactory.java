/*
 * Copyright 2018-2020 Litsec AB
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
package se.litsec.bankid.rpapi.support;

import java.security.KeyStore;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Spring factory class for configuring and creating a {@link RestTemplate} instance that can be used to communicate
 * with the BankID server.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class BankIDRestTemplateFactory extends AbstractFactoryBean<RestTemplate> {

  /** The root certificate that we trust when verifying the BankID server certificate. */
  private Resource trustedRoot;

  /** The keystore holding the client TLS key and certificate (BankID relying party certificate). */
  private KeyStore keyStore;

  /** The alias to the BankID RP certificate in the keyStore property. */
  private String keyAlias;

  /** The password for the entry holding the BankID RP key and certificate. */
  private char[] keyPassword;

  /** The SSL/TLS trust store. */
  private KeyStore trustStore;

  /** {@inheritDoc} */
  @Override
  public Class<?> getObjectType() {
    return RestTemplate.class;
  }

  /** {@inheritDoc} */
  @Override
  protected RestTemplate createInstance() throws Exception {
    
    final PrivateKeyStrategy keyStrategy = (aliases, socket) -> {
      return this.keyAlias != null ? this.keyAlias : aliases.keySet().stream().findFirst().orElse(null);
    };

    final SSLContext sslContext = SSLContextBuilder.create()
      .loadKeyMaterial(this.keyStore, this.keyPassword, keyStrategy)
      .loadTrustMaterial(this.trustStore, null)
      .build();

    final CloseableHttpClient httpClient = HttpClients.custom()
      .setSSLContext(sslContext)
      .setSSLHostnameVerifier(new DefaultHostnameVerifier())
      .build();

    final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);

    return new RestTemplate(requestFactory);
  }

  /**
   * Assigns the trusted root certificate that we use when verifying the BankID server certificate.
   * 
   * @param trustedRoot
   *          resource to the trusted root certificate
   */
  public void setTrustedRoot(final Resource trustedRoot) {
    this.trustedRoot = trustedRoot;
  }

  /**
   * Assigns the keystore holding the client TLS key and certificate (BankID relying party certificate).
   * <p>
   * Note: The keystore must have been unlocked (loaded).
   * </p>
   * 
   * @param keyStore
   *          the keystore
   */
  public void setKeyStore(final KeyStore keyStore) {
    this.keyStore = keyStore;
  }

  /**
   * Assigns the alias to the BankID RP certificate in the keyStore property. If not assigned, it will be assumed that
   * only one private key exists and the first alias will be choosen.
   * 
   * @param keyAlias
   *          the key alias
   */
  public void setKeyAlias(final String keyAlias) {
    this.keyAlias = StringUtils.hasText(keyAlias) ? keyAlias.trim() : null;
  }

  /**
   * Assigns the password for the entry holding the BankID RP key and certificate.
   * 
   * @param keyPassword
   *          the key entry password
   */
  public void setKeyPassword(final char[] keyPassword) {
    Assert.notNull(keyPassword, "'keyPassword' must not be null");
    this.keyPassword = new char[keyPassword.length];
    System.arraycopy(keyPassword, 0, this.keyPassword, 0, keyPassword.length);
  }

  /** {@inheritDoc} */
  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();
    Assert.notNull(this.trustedRoot, "Property 'trustedRoot' must be assigned");
    Assert.notNull(this.keyStore, "Property 'keyStore' must be assigned");
    Assert.notNull(this.keyPassword, "Property 'keyPassword' must be assigned");

    // Load trust store ...
    //
    this.trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    this.trustStore.load(null);
    this.trustStore.setCertificateEntry("bankid-trust", CertificateFactory.getInstance("X.509")
      .generateCertificate(this.trustedRoot.getInputStream()));
  }

}
