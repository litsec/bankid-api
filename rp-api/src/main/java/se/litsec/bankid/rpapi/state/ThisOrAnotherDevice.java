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
 * Represents "this device" or "other device".
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public enum ThisOrAnotherDevice implements BankIDStateInput {

  /** BankID operation on "this device". */
  THIS_DEVICE("thisDevice"),

  /** BankID operation on "other device". */
  OTHER_DEVICE("otherDevice");

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
   * @return a {@code ThisOrAnotherDevice} object
   */
  @JsonCreator
  public static ThisOrAnotherDevice forValue(String value) {
    for (ThisOrAnotherDevice e : ThisOrAnotherDevice.values()) {
      if (e.getValue().equalsIgnoreCase(value)) {
        return e;
      }
    }
    return null;
  }

  /**
   * Constructor assigning the enum string value.
   * 
   * @param value
   *          enum string value
   */
  private ThisOrAnotherDevice(String value) {
    this.value = value;
  }

  /** The enum string value. */
  private final String value;
}
