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
package se.litsec.bankid.rpapi.types;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the structure in which a relying party can define how an authentication or signature process should be
 * performed.
 * 
 * <p>
 * <b>Note:</b> It is recommended to use the {@link RequirementBuilder} to construct a {@code Requirement}.
 * </p>
 * 
 * @author Martin Lindström (martin.lindstrom@litsec.se)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Requirement {

  /** Certificate policy Object Identifier - BankID on file (for production) should be used. */
  public static final String CP_BANKID_ON_FILE = "1.2.752.78.1.1";

  /** Certificate policy Object Identifier - BankID on smartcard (for production) should be used. */
  public static final String CP_BANKID_ON_SMARTCARD = "1.2.752.78.1.2";

  /** Certificate policy Object Identifier - Mobile BankID (for production) should be used. */
  public static final String CP_MOBILE_BANKID = "1.2.752.78.1.5";

  /** Certificate policy Object Identifier - Nordea eID on file and smartcard (for production) should be used. */
  public static final String CP_NORDEA_EID = "1.2.752.71.1.3";

  /** Certificate policy Object Identifier - BankID on file (for test) should be used. */
  public static final String CP_BANKID_ON_FILE_TEST = "1.2.3.4.5";

  /** Certificate policy Object Identifier - BankID on smartcard (for test) should be used. */
  public static final String CP_BANKID_ON_SMARTCARD_TEST = "1.2.3.4.10";

  /** Certificate policy Object Identifier - Mobile BankID (for test) should be used. */
  public static final String CP_MOBILE_BANKID_TEST = "1.2.3.4.25";

  /** Certificate policy Object Identifier - Nordea eID on file and smartcard (for test) should be used. */
  public static final String CP_NORDEA_EID_TEST = "1.2.752.71.1.3";

  /** Certificate policy Object Identifier - Test BankID for some BankID Banks. */
  public static final String CP_TEST_BANKID = "1.2.752.60.1.6";

  /** eID on smart card issued by Nordea CA (for production). */
  public static final String NORDEA_CA_SMARTCARD = "Nordea CA for Smartcard users 12";

  /** eID on smart card issued by Nordea CA (for test). */
  public static final String NORDEA_CA_SMARTCARD_TEST = "Nordea Test CA for Smartcard users 12";

  /** eID on file issued by Nordea CA (for test). */
  public static final String NORDEA_CA_FILE = "Nordea CA for Softcert users 13";

  /** eID on file issued by Nordea CA (for test). */
  public static final String NORDEA_CA_FILE_TEST = "Nordea Test CA for Softcert users 13";

  /** Requirement for which type of smart card reader that is required. */
  private CardReaderRequirement cardReader;

  /** Object identifiers for which policies that should be used. */
  private List<String> certificatePolicies;

  /** Nordea CA issuer common names. Controls which Nordea BankID types that are supported. */
  private List<String> issuerCn;

  /**
   * Tells whether the client must have been started using the autoStartToken. A little bit of bad design is that its
   * value can only be {@code TRUE} or not set at all.
   */
  private Boolean autoStartTokenRequired;

  /** Tells whether finger print use should be allowed. */
  private Boolean allowFingerprint;

  /**
   * Creates a {@code RequirementBuilder}.
   * 
   * @return a builder
   */
  public static RequirementBuilder builder() {
    return new RequirementBuilder();
  }

  /**
   * Predicate that tells whether this object is "empty", meaning that no properties have been assigned.
   * 
   * @return {@code true} if not properties have been assigned, and {@code false} otherwise
   */
  public boolean isEmpty() {
    return this.cardReader == null && this.autoStartTokenRequired == null
        && (this.certificatePolicies == null || this.certificatePolicies.isEmpty())
        && this.issuerCn == null && this.allowFingerprint == null;
  }

  /**
   * Returns the requirement for which type of smart card reader that is required.
   * 
   * @return the card reader requirement, or {@code null}
   */
  public CardReaderRequirement getCardReader() {
    return this.cardReader;
  }

  /**
   * Assigns the requirement for which type of smart card reader that is required.
   * 
   * <p>
   * This condition should be combined with a {@code certificatePolicies} for a smart card to avoid undefined behavior.
   * </p>
   * 
   * @param cardReader
   *          the card reader requirement
   */
  public void setCardReader(CardReaderRequirement cardReader) {
    this.cardReader = cardReader;
  }

  /**
   * Returns the certificate policies telling which types of BankID:s that should be supported.
   * 
   * @return a list of certificate policy object identifiers
   */
  public List<String> getCertificatePolicies() {
    return this.certificatePolicies;
  }

  /**
   * Assigns the certificate policies telling which types of BankID:s that should be supported.
   * <p>
   * It is recommended to use the {@link RequirementBuilder} to set up certificate policies.
   * </p>
   * 
   * @param certificatePolicies
   *          a list of certificate policy object identifiers
   */
  public void setCertificatePolicies(List<String> certificatePolicies) {
    this.certificatePolicies = certificatePolicies;
  }

  /**
   * Returns the Nordea CA issuer common names. Controls which Nordea BankID types that are supported.
   * 
   * @return a list of CN strings
   */
  public List<String> getIssuerCn() {
    return this.issuerCn;
  }

  /**
   * Assigns the Nordea CA issuer common names. Controls which Nordea BankID types that are supported.
   * <p>
   * It is recommended to use the {@link RequirementBuilder} to set up certificate policies and requirements for Nordea
   * BankID usage.
   * </p>
   * 
   * @param issuerCn
   *          a list of CN strings
   */
  public void setIssuerCn(List<String> issuerCn) {
    this.issuerCn = issuerCn;
  }

  /**
   * Returns the {@code autoStartTokenRequired} flag that is used to determine whether the client must have been started
   * using the autoStartToken.
   * 
   * @return {@code Boolean.TRUE} or {@code null} (never {@code FALSE})
   */
  public Boolean getAutoStartTokenRequired() {
    return this.autoStartTokenRequired;
  }

  /**
   * Assigns the {@code autoStartTokenRequired} flag that is used to determine whether the client must have been started
   * using the autoStartToken.
   * <p>
   * A little bit of bad design is that its value can only be {@code TRUE} or not set at all. Therefore, if the supplied
   * parameter is {@code FALSE}, the value will be un-set.
   * </p>
   * 
   * @param autoStartTokenRequired
   *          the auto start flag
   */
  public void setAutoStartTokenRequired(Boolean autoStartTokenRequired) {
    if (autoStartTokenRequired != null && !autoStartTokenRequired.booleanValue()) {
      this.autoStartTokenRequired = null;
    }
    else {
      this.autoStartTokenRequired = autoStartTokenRequired;
    }
  }

  /**
   * Returns the {@code allowFingerprint} flag telling whether finger print use should be allowed.
   * <p>
   * For default setup {@code null} is returned. This means fingerprint is allowed for authentication and disallowed for
   * signature.
   * </p>
   * 
   * @return the {@code allowFingerprint} flag or {@code null} for default behaviuor
   */
  public Boolean getAllowFingerprint() {
    return this.allowFingerprint;
  }

  /**
   * Assigns the {@code allowFingerprint} flag telling whether finger print use should be allowed.
   * <p>
   * For default behaviour, this method need to be invoked.
   * </p>
   * 
   * @param allowFingerprint
   *          the {@code allowFingerprint} flag
   */
  public void setAllowFingerprint(Boolean allowFingerprint) {
    this.allowFingerprint = allowFingerprint;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("cardReader=%s, certificatePolicies=%s, issuerCn=%s, autoStartTokenRequired=%s, allowFingerprint=%s",
      this.cardReader != null ? this.cardReader : "<not set>",
      this.certificatePolicies != null ? this.certificatePolicies : "<not set - defaults apply>",
      this.issuerCn != null ? this.issuerCn : "<not set - defaults apply>",
      this.autoStartTokenRequired != null ? this.autoStartTokenRequired : "<not set>",
      this.allowFingerprint != null ? this.allowFingerprint : "<not set>");
  }

  /**
   * A class implementing a builder pattern for constructing {@link Requirement} objects.
   */
  public static class RequirementBuilder {

    /** The requirement being built. */
    private Requirement requirement;

    /** Flag telling whether we are setting up a requirement for a production system. */
    private boolean productionSetup = true;

    /** Flag for enabling mobile BankID. */
    private boolean enableMobile = true;

    /** Flag for enabling BankID on file. */
    private boolean enableOnFile = true;

    /** Flag for enabling BankID on smart card. */
    private boolean enableOnSmartCard = true;

    /** Flag for enabling Nordea BankID. */
    private boolean enableNordea = true;

    /**
     * Default constructor.
     */
    public RequirementBuilder() {
      this.requirement = new Requirement();
    }

    /**
     * Returns the built {@code Requirement} object
     * 
     * @return a {@code Requirement} object
     */
    public Requirement build() {

      // If no configuration for certificate policies has been performed, we don't assign anything for
      // certificate policies and let the default kick in.
      //
      if (this.enableMobile && this.enableOnFile && this.enableOnSmartCard && this.enableNordea) {
        return this.requirement;
      }

      List<String> oids = Arrays.asList(BANKID_ON_FILE(this.productionSetup), BANKID_ON_SMARTCARD(this.productionSetup),
        MOBILE_BANKID(this.productionSetup), NORDEA_EID(this.productionSetup));

      List<String> issuerCns = Arrays.asList(NORDEA_CA_FILE(this.productionSetup), NORDEA_CA_SMARTCARD(this.productionSetup));

      if (!this.enableMobile) {
        oids.removeIf(item -> item.equals(MOBILE_BANKID(this.productionSetup)));
      }
      if (!this.enableOnFile) {
        oids.removeIf(item -> item.equals(BANKID_ON_FILE(this.productionSetup)));
        issuerCns.removeIf(item -> item.equals(NORDEA_CA_FILE(this.productionSetup)));
      }
      if (!this.enableOnSmartCard) {
        oids.removeIf(item -> item.equals(BANKID_ON_SMARTCARD(this.productionSetup)));
        issuerCns.removeIf(item -> item.equals(NORDEA_CA_SMARTCARD(this.productionSetup)));

        // Also unset the card reader requirements since we are not using smart cards.
        this.requirement.setCardReader(null);

        // If both BankID on file and BankID on smart card were disabled, we also turn off Nordea.
        if (!this.enableOnFile) {
          oids.removeIf(item -> item.equals(NORDEA_EID(this.productionSetup)));
          this.requirement.setIssuerCn(null);
        }
      }
      if (!this.enableNordea) {
        oids.removeIf(item -> item.equals(NORDEA_EID(this.productionSetup)));
        this.requirement.setIssuerCn(null);
      }

      this.requirement.setCertificatePolicies(oids);
      if (issuerCns.size() != 2) {
        this.requirement.setIssuerCn(issuerCns);
      }

      return this.requirement;
    }

    /**
     * Tells whether we are setting up the requirement for a production system.
     * 
     * @param production
     *          {@code true} for production and {@code false} for test
     * @return the builder
     */
    public RequirementBuilder productionSetup(boolean production) {
      this.productionSetup = production;
      return this;
    }

    /**
     * Assigns the requirement for which type of smart card reader that is required.
     * 
     * <p>
     * See {@link Requirement#setCardReader(CardReaderRequirement)}.
     * </p>
     * 
     * @param cardReaderRequirement
     *          the card reader requirement
     * @return the builder
     */
    public RequirementBuilder cardReader(CardReaderRequirement cardReaderRequirement) {
      this.requirement.setCardReader(cardReaderRequirement);
      return this;
    }

    /**
     * Tells that the client must have been started using the autoStartToken.
     * 
     * <p>
     * See {@link Requirement#setAutoStartTokenRequired(Boolean)}.
     * </p>
     * 
     * @return the builder
     */
    public RequirementBuilder autoStartTokenRequired() {
      this.requirement.setAutoStartTokenRequired(Boolean.TRUE);
      return this;
    }

    /**
     * Assigns whether fingerprint is allowed.
     * 
     * <p>
     * See {@link Requirement#setAllowFingerprint(Boolean)}.
     * </p>
     * 
     * @param allowFingerprint
     *          the allowFingerprint flag
     * @return the builder
     */
    public RequirementBuilder allowFingerprint(boolean allowFingerprint) {
      this.requirement.setAllowFingerprint(allowFingerprint);
      return this;
    }

    /**
     * Enables/disables use of Mobile BankID.
     * 
     * <p>
     * By default, Mobile BankID is enabled.
     * </p>
     * 
     * @param enable
     *          should Mobile BankID be enabled or disabled?
     * @return the builder
     */
    public RequirementBuilder mobile(boolean enable) {
      this.enableMobile = enable;
      return this;
    }

    /**
     * Enables/disables use of BankID on file.
     * 
     * <p>
     * By default, BankID on file is enabled.
     * </p>
     * 
     * @param enable
     *          should BankID on file be enabled or disabled?
     * @return the builder
     */
    public RequirementBuilder onFile(boolean enable) {
      this.enableOnFile = enable;
      return this;
    }

    /**
     * Enables/disables use of BankID on smart card.
     * 
     * <p>
     * By default, BankID on smart card is enabled.
     * </p>
     * 
     * @param enable
     *          should BankID on smart card be enabled or disabled?
     * @return the builder
     */
    public RequirementBuilder onSmartCard(boolean enable) {
      this.enableOnSmartCard = enable;
      return this;
    }

    /**
     * Enables/disables use of Nordea BankID:s.
     * 
     * <p>
     * By default, Nordea BankID:s are enabled.
     * </p>
     * <p>
     * Note: Nordea BankID:s will automatically be disabled if BanID on file and BankID on smart card are disabled.
     * </p>
     * 
     * @param enable
     *          should Nordea BankID:s be enabled or disabled?
     * @return the builder
     */
    public RequirementBuilder nordea(boolean enable) {
      this.enableNordea = enable;
      return this;
    }

    //
    // Helpers
    //
    private static String BANKID_ON_FILE(boolean production) {
      return production ? CP_BANKID_ON_FILE : CP_BANKID_ON_FILE_TEST;
    }

    private static String BANKID_ON_SMARTCARD(boolean production) {
      return production ? CP_BANKID_ON_SMARTCARD : CP_BANKID_ON_SMARTCARD_TEST;
    }

    private static String MOBILE_BANKID(boolean production) {
      return production ? CP_MOBILE_BANKID : CP_MOBILE_BANKID_TEST;
    }

    private static String NORDEA_EID(boolean production) {
      return production ? CP_NORDEA_EID : CP_NORDEA_EID_TEST;
    }

    private static String NORDEA_CA_SMARTCARD(boolean production) {
      return production ? NORDEA_CA_SMARTCARD : NORDEA_CA_SMARTCARD_TEST;
    }

    private static String NORDEA_CA_FILE(boolean production) {
      return production ? NORDEA_CA_FILE : NORDEA_CA_FILE_TEST;
    }
  }

  /**
   * Represents a requirement for which type of smart card reader that is required.
   */
  public static enum CardReaderRequirement {

    /**
     * The transaction must be performed using a smart card reader where the PIN code is entered on the computer's
     * keyboard, or a card reader of a higher security class.
     */
    CLASS1("class1"),

    /**
     * The transaction must be performed using a smart card reader where the PIN code is entered on the reader itself,
     * or a card reader of a higher security class.
     */
    CLASS2("class2");

    /** The string value of the enum. */
    private String value;

    /**
     * Constructor.
     * 
     * @param value
     *          the string value of the enum
     */
    private CardReaderRequirement(String value) {
      this.value = value;
    }

    /**
     * Given a string representation its enum object is returned.
     * 
     * @param value
     *          the string representation
     * @return a {@code CardReaderRequirement} or {@code null} if not match is found
     */
    @JsonCreator
    public static CardReaderRequirement forValue(String value) {
      for (CardReaderRequirement cr : CardReaderRequirement.values()) {
        if (cr.getValue().equals(value)) {
          return cr;
        }
      }
      return null;
    }

    /**
     * Returns the string representation of the enum.
     * 
     * @return the string representation
     */
    @JsonValue
    public String getValue() {
      return this.value;
    }
  }

}
