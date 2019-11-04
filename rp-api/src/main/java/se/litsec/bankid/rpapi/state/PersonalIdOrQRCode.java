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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum that is used to represent the choice whether the service should prompt for personal identity number
 * and generate and display a QR-code when using BankID on "other device".
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public enum PersonalIdOrQRCode implements BankIDStateInput {

  /** The service should prompty for personal identity number when using BankID on "other device". */
  PERSONAL_ID_NUMBER("personal_id_number"),

  /** The service should generate a QR-code and display it when using BankID on "other device". */
  QR_CODE("qr_code");

  /**
   * Returns the string representation of the enum.
   * 
   * @return the string representation
   */
  @JsonValue
  public String getValue() {
    return this.value;
  }

  /**
   * Given a string representation its enum object is returned.
   * 
   * @param value
   *          the string representation
   * @return a {@code PersonalIdOrQRCode} object
   */
  @JsonCreator
  public static PersonalIdOrQRCode forValue(String value) {
    for (PersonalIdOrQRCode e : PersonalIdOrQRCode.values()) {
      if (e.getValue().equalsIgnoreCase(value)) {
        return e;
      }
    }
    return null;
  }

  /**
   * Constructor setting the value.
   * 
   * @param value
   *          the string value
   */
  private PersonalIdOrQRCode(String value) {
    this.value = value;
  }

  /** The string value. */
  private String value;
}
