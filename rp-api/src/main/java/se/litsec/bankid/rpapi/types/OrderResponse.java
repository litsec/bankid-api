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
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an order response, i.e., the response message received from an auth or sign request.
 * 
 * @author Martin Lindström (martin.lindstrom@litsec.se)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {

  /** The order reference string. */
  @JsonProperty(value = "orderRef", required = true)
  private String orderReference;

  /** The auto start token. */
  @JsonProperty(value = "autoStartToken")
  private String autoStartToken;

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

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("orderRef='%s', autoStartToken='%s'", this.orderReference, this.autoStartToken);
  }

}
