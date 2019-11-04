/*
 * Copyright 2018-2019 Litsec AB
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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import se.litsec.bankid.rpapi.state.BankIDStateOutput;
import se.litsec.bankid.rpapi.support.BankIDMessage;

/**
 * Implementation of the {@code BankIDStateOutput}.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankIDStateOutputImpl implements BankIDStateOutput {

  /** BankID message. */
  private BankIDMessage.ShortName message;

  /** Should the UI display a waiting image? */
  private boolean displayWaitingImage = false;

  /** Should the UI prompt for the personal identity number? */
  private boolean promptForPersonalNumber = false;

  /** The URL that is used to auto start the BankID app. */
  private String launchUrl;

  /** The QR code image to display. */
  private String qrCodeImage;

  /**
   * Default constructor.
   */
  public BankIDStateOutputImpl() {
  }

  /**
   * Constructor accepting a message ID.
   * 
   * @param message
   *          the message ID
   */
  public BankIDStateOutputImpl(BankIDMessage.ShortName message) {
    this.message = message;
  }

  /** {@inheritDoc} */
  @JsonGetter("message")
  @Override
  public BankIDMessage.ShortName getMessage() {
    return this.message;
  }

  /**
   * Assigns the message ID to be displayed.
   * 
   * @param message
   *          the message ID
   */
  @JsonSetter("message")
  public void setMessage(BankIDMessage.ShortName message) {
    this.message = message;
  }

  /** {@inheritDoc} */
  @JsonGetter("displayWaitingImage")
  @Override
  public boolean displayWaitingImage() {
    return this.displayWaitingImage;
  }

  /**
   * Assigns whether a waiting image should be displayed.
   * 
   * @param displayWaitingImage
   *          whether to show a waiting image
   */
  @JsonSetter("displayWaitingImage")
  public void setDisplayWaitingImage(boolean displayWaitingImage) {
    this.displayWaitingImage = displayWaitingImage;
  }

  /** {@inheritDoc} */
  @JsonGetter("promptForPersonalNumber")
  @Override
  public boolean promptForPersonalNumber() {
    return this.promptForPersonalNumber;
  }

  /**
   * Assigns whether we should prompt the user for his or hers personal identity number
   * 
   * @param promptForPersonalNumber
   *          {@code true} to prompt, and {@code false} otherwise
   */
  @JsonSetter("promptForPersonalNumber")
  public void setPromptForPersonalNumber(boolean promptForPersonalNumber) {
    this.promptForPersonalNumber = promptForPersonalNumber;
  }

  /** {@inheritDoc} */
  @JsonGetter("launchUrl")
  @Override
  public String getLaunchUrl() {
    return this.launchUrl;
  }

  /**
   * Assigns the launch URL that is used to auto start the BankID app.
   * 
   * @param launchUrl
   *          the launch URL
   */
  @JsonSetter("launchUrl")
  public void setLaunchUrl(String launchUrl) {
    this.launchUrl = launchUrl;
  }

  /** {@inheritDoc} */
  @JsonGetter("qrCodeImage")
  @Override
  public String qrCodeImage() {
    return this.qrCodeImage;
  }

  /**
   * Assigns the Base64 encoded image representing a QR code to be displayed.
   * 
   * @param qrCodeImage
   *          image to be displayed
   */
  @JsonSetter("qrCodeImage")
  public void setQrCodeImage(String qrCodeImage) {
    this.qrCodeImage = qrCodeImage;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("BankID-state-output[message=");
    sb.append(this.message != null ? this.message : "'not-set'");
    sb.append(",display-waiting-image=").append(this.displayWaitingImage);
    sb.append(",prompt-for-personal-number=").append(this.promptForPersonalNumber);
    sb.append(",launch-url='").append(this.launchUrl != null ? this.launchUrl : "not-set").append("'");
    sb.append(",qr-code-image=").append(this.qrCodeImage != null ? "assigned" : "not-assigned");
    sb.append(")");
    return sb.toString();
  }

}
