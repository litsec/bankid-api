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
package se.litsec.bankid.rpapi.state.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import se.litsec.bankid.rpapi.service.BankIDClient;
import se.litsec.bankid.rpapi.service.DataToSign;
import se.litsec.bankid.rpapi.state.BankIDRequirements;
import se.litsec.bankid.rpapi.state.PersonalIdOrQRCode;
import se.litsec.bankid.rpapi.state.ThisOrAnotherDevice;
import se.litsec.bankid.rpapi.state.UserAgent;
import se.litsec.bankid.rpapi.support.PersonalIdentityNumber;

/**
 * Represents the context during a BankID operation.
 *
 * @author Martin Lindström (martin@litsec.se)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankIDStateContext {
  
  /** The BankID client that is used to communicate with the BankID server. */ 
  @JsonIgnore
  private BankIDClient client;

  /** Are we running in production mode? */
  @JsonProperty(value = "prod")
  private boolean productionMode = true;

  /** User agent information. */
  @JsonProperty(value = "userAgent")
  private UserAgent userAgent;

  /** The type of BankID operation. */
  @JsonProperty(value = "operation")
  private BankIDOperation operation;

  /** This or another device? */
  @JsonProperty(value = "thisOrAnotherDevice")
  private ThisOrAnotherDevice thisOrAnotherDevice;
  
  /** Should personal ID or QR code be used for "other device"? */
  @JsonProperty(value = "personalIdOrQrCode")
  private PersonalIdOrQRCode personalIdOrQRCode;
  
  /** The personal identity number. */
  @JsonProperty(value = "personalIdentityNumber")
  private PersonalIdentityNumber personalIdentityNumber;

  /** The requirements for the operation. */
  @JsonProperty(value = "requirements")
  private BankIDRequirements requirements;

  /** The order reference string. */
  @JsonProperty(value = "orderRef")
  private String orderReference;

  /** The auto start token. */
  @JsonProperty(value = "autoStartToken")
  private String autoStartToken;
  
  @JsonProperty(value = "launchUrl")
  private String launchUrl;

  /** The data to sign. */
  @JsonProperty(value = "dataToSign")
  private DataToSign dataToSign;
  
  /** The QR code to display for the user. */
  @JsonProperty(value = "qrCode")
  private String qrCode;

  /**
   * Default constructor.
   */
  public BankIDStateContext() {
  }
  
  public BankIDStateContext(BankIDClient client) {
    this.client = client;
  }
  
  public BankIDClient getClient() {
    return this.client;
  }

  public void setClient(BankIDClient client) {
    this.client = client;
  }

  /**
   * Are we running in production mode?
   * 
   * @return {@code true} for production and {@code false} for test
   */
  public boolean isProductionMode() {
    return this.productionMode;
  }

  /**
   * Assigns whether we are running in production mode or not.
   * 
   * @param productionMode
   *          {@code true} for production and {@code false} for test
   */
  public void setProductionMode(boolean productionMode) {
    this.productionMode = productionMode;
  }

  /**
   * Returns the user agent information.
   * 
   * @return user agent information
   */
  public UserAgent getUserAgent() {
    return this.userAgent;
  }

  /**
   * Assigns the user agent information.
   * 
   * @param userAgent
   *          user agent information
   */
  public void setUserAgent(UserAgent userAgent) {
    this.userAgent = userAgent;
  }

  /**
   * Returns the type of BankID operation being processed.
   *
   * @return the type of BankID operation
   */
  public BankIDOperation getOperation() {
    return this.operation;
  }

  /**
   * Assigns the type of BankID operation being processed.
   *
   * @param operation
   *          the type of BankID operation
   */
  public void setOperation(BankIDOperation operation) {
    this.operation = operation;
  }

  /**
   * Returns whether this or another device should be used.
   * 
   * @return this or another device
   */
  public ThisOrAnotherDevice getThisOrAnotherDevice() {
    return this.thisOrAnotherDevice;
  }

  /**
   * Assigns whether this or another device should be used.
   * 
   * @param thisOrAnotherDevice
   *          this or another device
   */
  public void setThisOrAnotherDevice(ThisOrAnotherDevice thisOrAnotherDevice) {
    this.thisOrAnotherDevice = thisOrAnotherDevice;
  }

  public PersonalIdOrQRCode getPersonalIdOrQRCode() {
    return this.personalIdOrQRCode;
  }

  public void setPersonalIdOrQRCode(PersonalIdOrQRCode personalIdOrQRCode) {
    this.personalIdOrQRCode = personalIdOrQRCode;
  }

  public PersonalIdentityNumber getPersonalIdentityNumber() {
    return this.personalIdentityNumber;
  }

  public void setPersonalIdentityNumber(PersonalIdentityNumber personalIdentityNumber) {
    this.personalIdentityNumber = personalIdentityNumber;
  }

  /**
   * Returns the requirements for the current operation.
   *
   * @return the requirements for the current operation
   */
  public BankIDRequirements getRequirements() {
    return this.requirements;
  }

  /**
   * Assigns the requirements for the current operation.
   *
   * @param requirements
   *          the requirements for the current operation
   */
  public void setRequirements(BankIDRequirements requirements) {
    this.requirements = requirements;
  }

  /**
   * Returns the order reference string.
   *
   * @return the order reference
   */
  public String getOrderReference() {
    return this.orderReference;
  }

  /**
   * Assigns the order reference string.
   *
   * @param orderReference
   *          the order reference
   */
  public void setOrderReference(String orderReference) {
    this.orderReference = orderReference;
  }

  /**
   * Returns the auto start token.
   * 
   * @return the auto start token
   */
  public String getAutoStartToken() {
    return this.autoStartToken;
  }

  /**
   * Assigns the auto start token.
   * 
   * @param autoStartToken
   *          the auto start token
   */
  public void setAutoStartToken(String autoStartToken) {
    this.autoStartToken = autoStartToken;
  }

  /**
   * Returns the data to sign.
   *
   * @return data to sign
   */
  public DataToSign getDataToSign() {
    return this.dataToSign;
  }

  /**
   * Assigns the data to sign
   *
   * @param dataToSign
   *          data to sign
   */
  public void setDataToSign(DataToSign dataToSign) {
    this.dataToSign = dataToSign;
  }

  public String getQrCode() {
    return this.qrCode;
  }

  public void setQrCode(String qrCode) {
    this.qrCode = qrCode;
  }

  public String getLaunchUrl() {
    return this.launchUrl;
  }

  public void setLaunchUrl(String launchUrl) {
    this.launchUrl = launchUrl;
  }

  @Override
  public String toString() {
    return "operation=" + this.operation + ", requirements=[" + this.requirements + "], orderReference=" + this.orderReference
        + ", dataToSign=[" + this.dataToSign + "]";
  }

}
