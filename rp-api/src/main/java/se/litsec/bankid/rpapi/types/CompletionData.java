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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the completion data for completed orders.
 * 
 * @author Martin Lindström (martin.lindstrom@litsec.se)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompletionData {

  /** Information related to the user. */
  private User user;

  /** Information related to the device used during the BankID operation. */
  private Device device;

  /** Information about the user's BankID certificate. */
  private Cert cert;

  /** The Base64-encoded BankID signature. */
  private String signature;

  /** The Base64-encoded OCSP-response. */
  private String ocspResponse;

  /**
   * Returns the user information (such as personal identity number).
   * 
   * @return user information
   */
  public User getUser() {
    return this.user;
  }

  /**
   * Assigns the user information.
   * 
   * @param user
   *          user information
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns the information related to the device used during the BankID operation.
   * 
   * @return the device information
   */
  public Device getDevice() {
    return this.device;
  }

  /**
   * Assigns the information related to the device used during the BankID operation.
   * 
   * @param device
   *          the device information
   */
  public void setDevice(Device device) {
    this.device = device;
  }

  /**
   * Returns information about the user's BankID certificate.
   * 
   * @return the certificate information
   */
  public Cert getCert() {
    return this.cert;
  }

  /**
   * Assigns information about the user's BankID certificate.
   * 
   * @param cert
   *          the certificate information
   */
  public void setCert(Cert cert) {
    this.cert = cert;
  }

  /**
   * Returns the Base64-encoded BankID signature.
   * 
   * @return the Base64-encoded BankID signature
   */
  public String getSignature() {
    return this.signature;
  }

  /**
   * Assigns the Base64-encoded BankID signature.
   * 
   * @param signature
   *          the Base64-encoded BankID signature
   */
  public void setSignature(String signature) {
    this.signature = signature;
  }

  /**
   * Returns the Base64-encoded OCSP-response.
   * 
   * @return the Base64-encoded OCSP-response
   */
  public String getOcspResponse() {
    return this.ocspResponse;
  }

  /**
   * Assigns the Base64-encoded OCSP-response.
   * 
   * @param ocspResponse
   *          the Base64-encoded OCSP-response
   */
  public void setOcspResponse(String ocspResponse) {
    this.ocspResponse = ocspResponse;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("user=[%s], device=[%s], cert=[%s], signature='%s', ocspResponse='%s'",
      this.user, this.device, this.cert, this.signature, this.ocspResponse);
  }

  /**
   * Represents the user field of the completion data.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class User {

    /** The personal identity number. */
    private String personalNumber;

    /** The user full name (given name and surname). */
    private String name;

    /** The user given name. */
    private String givenName;

    /** The user surname. */
    private String surname;

    /**
     * Returns the personal identity number.
     * 
     * @return the personal identity number
     */
    public String getPersonalNumber() {
      return this.personalNumber;
    }

    /**
     * Assigns the personal identity number.
     * 
     * @param personalNumber
     *          the personal identity number
     */
    public void setPersonalNumber(String personalNumber) {
      this.personalNumber = personalNumber;
    }

    /**
     * Returns the user full name.
     * 
     * @return the user full name
     */
    public String getName() {
      return this.name;
    }

    /**
     * Assigns the user full name.
     * 
     * @param name
     *          the user full name
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * Returns the user given name.
     * 
     * @return the user given name
     */
    public String getGivenName() {
      return this.givenName;
    }

    /**
     * Assigns the user given name.
     * 
     * @param givenName
     *          the user given name
     */
    public void setGivenName(String givenName) {
      this.givenName = givenName;
    }

    /**
     * Returns the user surname.
     * 
     * @return the user surname
     */
    public String getSurname() {
      return this.surname;
    }

    /**
     * Returns the user surname.
     * 
     * @param surname
     *          the user surname
     */
    public void setSurname(String surname) {
      this.surname = surname;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return String.format("personalNumber='%s', name='%s', givenName='%s', surname='%s'",
        this.personalNumber, this.name, this.givenName, this.surname);
    }
  }

  /**
   * Represents the device field of the completion data.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Device {
    /** The device IP address. */
    private String ipAddress;

    /**
     * Returns the device IP address.
     * 
     * @return the device IP address
     */
    public String getIpAddress() {
      return this.ipAddress;
    }

    /**
     * Assigns the device IP address.
     * 
     * @param ipAddress
     *          the device IP address
     */
    public void setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return String.format("ipAddress='%s'", this.ipAddress);
    }

  }

  /**
   * Represents the cert field of the completion data.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Cert {

    /** The valid-from time for the user BankID certificate (given in millis since epoch). */
    private long notBefore;

    /** The valid-to time for the user BankID certificate (given in millis since epoch). */
    private long notAfter;

    /**
     * Returns the valid-from time for the user BankID certificate.
     * 
     * @return the valid-from time (in millis since epoch)
     */
    public long getNotBefore() {
      return this.notBefore;
    }

    /**
     * Assigns the valid-from time for the user BankID certificate.
     * 
     * @param notBefore
     *          the valid-from time (in millis since epoch)
     */
    public void setNotBefore(long notBefore) {
      this.notBefore = notBefore;
    }

    /**
     * Returns the valid-to time for the user BankID certificate.
     * 
     * @return the valid-to time (in millis since epoch)
     */
    public long getNotAfter() {
      return this.notAfter;
    }

    /**
     * Assigns the valid-to time for the user BankID certificate.
     * 
     * @param notAfter
     *          the valid-to time (in millis since epoch)
     */
    public void setNotAfter(long notAfter) {
      this.notAfter = notAfter;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return String.format("notBefore=%d, notAfter=%d", this.notBefore, this.notAfter);
    }

  }

}
