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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration representing the type of BankID operation.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public enum BankIDOperation {

  /** BankID authentication. */
  AUTHENTICATION("auth"),

  /** BankID signature operation. */
  SIGNATURE("sign");
  
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
   * @return a {@code BankIDOperation} object
   */
  @JsonCreator
  public static BankIDOperation forValue(String value) {
    for (BankIDOperation e : BankIDOperation.values()) {
      if (e.getValue().equalsIgnoreCase(value)) {
        return e;
      }
    }
    return null;
  }  

  /**
   * Constructor.
   * 
   * @param value
   *          the enum string value
   */
  private BankIDOperation(String value) {
    this.value = value;
  }

  /** The enum string value. */
  private final String value;

}
