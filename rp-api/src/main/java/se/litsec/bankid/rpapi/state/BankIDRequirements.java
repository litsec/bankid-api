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
package se.litsec.bankid.rpapi.state;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import se.litsec.bankid.rpapi.service.BankIDClient;
import se.litsec.bankid.rpapi.support.PersonalIdentityNumber;
import se.litsec.bankid.rpapi.types.Requirement;

/**
 * Requirements for an BankID operation. This class combines that relying party requirements that are passed to the
 * {@link BankIDClient#authenticate(String, String, se.litsec.bankid.rpapi.types.Requirement)} and
 * {@link BankIDClient#sign(String, String, se.litsec.bankid.rpapi.service.DataToSign, se.litsec.bankid.rpapi.types.Requirement)}
 * with other requirements such as relying party requirements for QR-code vs. personal identity number and already known
 * personal identity numbers (for example, if an authenticate-operation has been performed, the personal identity number
 * may be known before doing a sign-operation).
 *
 * <p>
 * Note that a system may have different requirements set up for different scenarios, for example one for authentication
 * and one for signatures.
 * </p>
 *
 * @author Martin Lindström (martin@litsec.se)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankIDRequirements {

  /** The static BankID requirements. */
  @JsonProperty(value = "requirementsConfig")
  private Requirement requirementsConfig;

  /** Configuration whether QR code or personal identity number should be used. */
  @JsonProperty(value = "personalIdOrQRCode")
  private PersonalIdOrQRCode personalIdOrQRCode;

  /**
   * When the user connects from a mobile telephone, should we really allow "other device"? The default is
   * {@code false}.
   */
  @JsonProperty(value = "allowOtherDeviceOnMobile")
  private boolean allowOtherDeviceOnMobile = false;

  /** Usage settings (from previous operation). May only be assigned via {@link #createFor(BankIDUsage)}. */
  @JsonProperty(value = "bankIDUsage")
  private BankIDUsage bankIDUsage;

  /**
   * Based on a static configuration the method creates a new requirements object containing also the supplied
   * {@code BankIDUsage} object.
   *
   * @param bankIDUsage
   *          the usage settings (from a previous operation?)
   * @return a {@code BankIDRequirements} object
   */
  public BankIDRequirements createFor(BankIDUsage bankIDUsage) {
    BankIDRequirements newReqs = new BankIDRequirements();
    newReqs.requirementsConfig = this.requirementsConfig;
    newReqs.personalIdOrQRCode = this.personalIdOrQRCode;
    newReqs.allowOtherDeviceOnMobile = this.allowOtherDeviceOnMobile;
    newReqs.bankIDUsage = bankIDUsage;
    return newReqs;
  }

  /**
   * Returns the static BankID requirements to be used.
   *
   * @return the static BankID requirements, or {@code null} if the default should apply
   */
  public Requirement getRequirementsConfig() {
    return this.requirementsConfig;
  }

  /**
   * Assigns the static BankID requirements to be used.
   *
   * @param requirementConfig
   *          static BankID requirements
   */
  public void setRequirementsConfig(Requirement requirementConfig) {
    this.requirementsConfig = requirementConfig;
  }

  /**
   * Returns whether personal identity number or a QR-code should be used when using BankID on "another device".
   * <p>
   * If {@code null} is returned it means that this information is not static and the BankID state engine will return a
   * {@link PersonalIdOrQRCodeState} where the application should decide.
   * </p>
   *
   * @return a {@code PersonalIdOrQRCode} enum, or {@code null} if this has not been configured
   */
  public PersonalIdOrQRCode getPersonalIdOrQRCode() {
    return this.personalIdOrQRCode;
  }

  /**
   * Assigns whether personal identity number or a QR-code should be used when using BankID on "another device".
   * <p>
   * If not assigned it means that this information is not static and the BankID state engine will return a
   * {@link PersonalIdOrQRCodeState} where the application should decide.
   * </p>
   *
   * @param personalIdOrQRCode
   *          a {@code PersonalIdOrQRCode} enum
   */
  public void setPersonalIdOrQRCode(PersonalIdOrQRCode personalIdOrQRCode) {
    this.personalIdOrQRCode = personalIdOrQRCode;
  }

  /**
   * When the user connects from a mobile telephone, should we really allow "other device"?
   *
   * @return if this is allowed {@code true} is returned, otherwise {@code false}
   */
  public boolean isAllowOtherDeviceOnMobile() {
    return this.allowOtherDeviceOnMobile;
  }

  /**
   * Assigns whether we should allow a user connecting from a mobile telephone to select "other device".
   * <p>
   * The default is {@code false}.
   * </p>
   *
   * @param allowOtherDeviceOnMobile
   *          {@code true} if this is allowed
   */
  public void setAllowOtherDeviceOnMobile(boolean allowOtherDeviceOnMobile) {
    this.allowOtherDeviceOnMobile = allowOtherDeviceOnMobile;
  }

  /**
   * The dynamic BankID usage (from a previous BankID-operation).
   *
   * @return the BankID usage object or {@code null}
   */
  public BankIDUsage getBankIDUsage() {
    return this.bankIDUsage;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "requirementsConfig=[" + this.requirementsConfig + "], personalIdOrQRCode=" + this.personalIdOrQRCode
        + ", allowOtherDeviceOnMobile=" + this.allowOtherDeviceOnMobile + ", bankIDUsage=[" + this.bankIDUsage + "]";
  }

  /**
   * The BankID usage is a class that may be used when creating a {@link BankIDRequirements} object using the
   * {@link BankIDRequirements#createFor(BankIDUsage)} method. It may be used to represent the information we have from
   * a previous BankID-operation or information obtained by other means.
   */
  public static class BankIDUsage {

    /** This or another device? */
    @JsonProperty(value = "thisOrAnotherDevice")
    private ThisOrAnotherDevice thisOrAnotherDevice;

    /** A known personal identity number. */
    @JsonProperty(value = "knownPersonalIdNumber")
    private PersonalIdentityNumber knownPersonalIdNumber;

    /**
     * Returns whether this device or another device should be used. Normally, this question is asked the user during a
     * BankID-flow, but if a BankID-operation already has been performed for the current session, the service probably
     * want to use the same method again.
     *
     * @return a {@link ThisOrAnotherDevice} object, or {@code null} if the user should be prompted
     */
    public ThisOrAnotherDevice getThisOrAnotherDevice() {
      return this.thisOrAnotherDevice;
    }

    /**
     * Assigns whether this device or another device should be used.
     *
     * @param thisOrAnotherDevice
     *          this device or another device
     */
    public void setThisOrAnotherDevice(ThisOrAnotherDevice thisOrAnotherDevice) {
      this.thisOrAnotherDevice = thisOrAnotherDevice;
    }

    /**
     * Returns the personal identity number to be used for a BankID-operation. The personal identity number may be known
     * from a previous BankID-operation or by other means.
     *
     * @return the personal identity number, or {@code null} if this is not known
     */
    public PersonalIdentityNumber getKnownPersonalIdNumber() {
      return this.knownPersonalIdNumber;
    }

    /**
     * Assigns a known personal identity number.
     *
     * @param knownPersonalIdNumber
     *          the personal identity number
     */
    public void setKnownPersonalIdNumber(PersonalIdentityNumber knownPersonalIdNumber) {
      this.knownPersonalIdNumber = knownPersonalIdNumber;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "thisOrAnotherDevice=" + this.thisOrAnotherDevice + ", knownPersonalIdNumber=" + this.knownPersonalIdNumber;
    }

    /**
     * A builder for {@link BankIDUsage}.
     */
    public static class BankIDUsageBuilder {

      /** The usage being built. */
      private BankIDUsage bankidUsage;

      /**
       * Constructor.
       */
      public BankIDUsageBuilder() {
        this.bankidUsage = new BankIDUsage();
      }

      /**
       * Builds the {@link BankIDUsage}.
       *
       * @return the {@link BankIDUsage}
       */
      public BankIDUsage build() {
        return this.bankidUsage;
      }

      /**
       * See {@link BankIDUsage#setThisOrAnotherDevice(ThisOrAnotherDevice)}.
       *
       * @param thisOrAnotherDevice
       *          this device or another device
       * @return the builder
       */
      public BankIDUsageBuilder thisOrAnotherDevice(ThisOrAnotherDevice thisOrAnotherDevice) {
        this.bankidUsage.setThisOrAnotherDevice(thisOrAnotherDevice);
        return this;
      }

      /**
       * See {@link BankIDUsage#setKnownPersonalIdNumber(String)}.
       *
       * @param knownPersonalIdNumber
       *          the personal identity number
       * @return the builder
       */
      public BankIDUsageBuilder knownPersonalIdNumber(PersonalIdentityNumber knownPersonalIdNumber) {
        this.bankidUsage.setKnownPersonalIdNumber(knownPersonalIdNumber);
        return this;
      }
    }

  }

}
