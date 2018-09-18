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
package se.litsec.bankid.rpapi.service;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Class the represents input for a signature operation.
 * 
 * @author Martin Lindström (martin.lindstrom@litsec.se)
 */
public class DataToSign {

  /** The text to be displayed and signed. */
  private String userVisibleData;

  /** Data not displayed to the user (optional). */
  private String userNonVisibleData;

  /**
   * Default constructor.
   */
  public DataToSign() {
  }

  /**
   * In the BankID sign call, two data sets can be supplied; the {@code userVisibleData} and the
   * {@code userNonVisibleData}. The first text will displayed to the user and the second is not displayed. Both are
   * Base64-encoded. 
   * <p>
   * By using this method, the caller can assign the text that will be displayed to the user. The method will take care of Base64-encoding.
   * </p>
   * 
   * @param displayText the (non-encoded) display text
   */
  public void setSignatureDisplayText(String displayText) {
    this.userVisibleData = Base64.getEncoder().encodeToString(displayText.getBytes(Charset.forName("UTF-8")));
  }
  
  /**
   * Assigns the text to be displayed and signed.
   * <p>
   * Must be UTF-8 encoded. The value must be base 64-encoded. 1-40 000 characters (after base 64-encoding). The text
   * can be formatted using CR = new line, LF = new line and CRLF = new line.
   * </p>
   * <p>
   * See also {@link DataToSign#setSignatureDisplayText(String)}.
   * </p>
   * 
   * @param userVisibleData
   *          base64-encoded data to be displayed and signed
   */
  public void setUserVisibleData(String userVisibleData) {
    this.userVisibleData = userVisibleData;
  }
  
  /**
   * Returns the text to be displayed and signed. The returned string is Base64 encoded.
   * 
   * 
   * @return text to be displayed and signed (base64-encoded)
   */
  public String getUserVisibleData() {
    return this.userVisibleData;
  }

  /**
   * Assigns the data that is part of the signature process but should not be displayed to the user. This supplied data is the raw bytes and the method will Base64 encode it.
   * 
   * <p>
   * See also {@link DataToSign#setUserNonVisibleData(String)}.
   * </p>
   * 
   * @param bytes
   *          the data that is part of the signature process but should not be displayed to the user (raw data)
   */
  public void setUserNonVisibleDataRaw(byte[] bytes) {
    this.userNonVisibleData = Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * Assigns the data that is part of the signature process but should not be displayed to the user.
   * <p>
   * The value must be base 64-encoded. 0 - 200 000 characters (after base 64-encoding).
   * </p>
   * <p>
   * See also {@link DataToSign#setUserNonVisibleDataRaw(byte[])}.
   * </p>
   * 
   * @param userNonVisibleData
   *          the data that is part of the signature process but should not be displayed to the user (base64-encoded)
   */
  public void setUserNonVisibleData(String userNonVisibleData) {
    this.userNonVisibleData = userNonVisibleData;
  }
  
  /**
   * Returns the data that is part of the signature process but should not be displayed to the user.
   * 
   * @return data to be signed, but not displayed to the user
   */
  public String getUserNonVisibleData() {
    return this.userNonVisibleData;
  }
  
  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("%s (non-visible: %s)", this.userVisibleData,
      this.userNonVisibleData != null ? this.userNonVisibleData : "<not set>");
  }

}
