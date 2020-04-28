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

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * An implementation of a Spring {@code FactoryBean} that loads a keystore file.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class KeyStoreFactoryBean extends AbstractFactoryBean<KeyStore> {
  
  /** The keystore resource. */
  private Resource keyStoreResource; 
  
  /** The password needed to load the keystore. */
  private char[] keyStorePassword;
  
  /** The keystore type. */
  private String keyStoreType;

  /**
   * Constructor that takes a resource reference to a JKS-file and the password to unlock this file.
   * 
   * @param keyStoreResource
   *          JKS resource
   * @param storePassword
   *          the password for unlocking the keystore
   */
  public KeyStoreFactoryBean(final Resource keyStoreResource, final char[] keyStorePassword) {
    this(keyStoreResource, keyStorePassword, KeyStore.getDefaultType());
  }

  /**
   * Constructor that takes a resource reference to a keystore file, the password to unlock this file and the store type
   * ("JKS", "PKCS12", ...).
   * 
   * @param keyStoreResource
   *          JKS resource
   * @param storePassword
   *          the password for unlocking the keystore
   * @param storeType
   *          the type of keystore
   */
  public KeyStoreFactoryBean(final Resource keyStoreResource, final char[] keyStorePassword, final String keyStoreType) {
    Assert.notNull(keyStoreResource, "'keyStoreResource' must not be null");
    Assert.hasText(keyStoreType, "'keyStoreType' must not be null or empty");
    this.keyStoreResource = keyStoreResource;
    if (keyStorePassword != null) {
      this.keyStorePassword = new char[keyStorePassword.length];
      System.arraycopy(keyStorePassword, 0, this.keyStorePassword, 0, this.keyStorePassword.length);
    }
    else {
      // Assume empty password
      this.keyStorePassword = new char[0];
    }
    this.keyStoreType = keyStoreType;
  }

  /** {@inheritDoc} */
  public Class<? extends KeyStore> getObjectType() {
    return KeyStore.class;
  }
  
  /** {@inheritDoc} */
  @Override
  protected KeyStore createInstance() throws Exception {
    final KeyStore keyStore = KeyStore.getInstance(this.keyStoreType);
    keyStore.load(this.keyStoreResource.getInputStream(), this.keyStorePassword);
    return keyStore;
  }

}
