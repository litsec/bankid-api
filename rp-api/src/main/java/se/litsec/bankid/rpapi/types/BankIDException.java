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

/**
 * Exception class for error concerning the communication between the RP and the backend service.
 * 
 * @author Martin Lindström (martin.lindstrom@litsec.se)
 */
public class BankIDException extends Exception {

  /** For serializing. */
  private static final long serialVersionUID = 713980887665580664L;

  /** The error code. */
  private ErrorCode errorCode;

  /** Error details. */
  private String details;

  /**
   * Constructor creating an instance based on an error response.
   * 
   * @param errorResponse
   *          the response
   */
  public BankIDException(ErrorResponse errorResponse) {
    super();
    this.errorCode = errorResponse.getErrorCode();
    this.details = errorResponse.getDetails();
  }

  /**
   * Constructor creating an instance based on an error response and an error message.
   * 
   * @param errorResponse
   *          the error response
   * @param message
   *          the error message
   */
  public BankIDException(ErrorResponse errorResponse, String message) {
    super(message);
    this.errorCode = errorResponse.getErrorCode();
    this.details = errorResponse.getDetails();
  }

  /**
   * Constructor creating an instance based on an error response and the cause of the error.
   * 
   * @param errorResponse
   *          the error response
   * @param cause
   *          the cause of the error
   */
  public BankIDException(ErrorResponse errorResponse, Throwable cause) {
    super(cause);
    this.errorCode = errorResponse.getErrorCode();
    this.details = errorResponse.getDetails();
  }

  /**
   * Constructor creating an instance based on an error response, the error message and the cause of the errors.
   * 
   * @param errorResponse
   *          the error response
   * @param message
   *          the error message
   * @param cause
   *          the cause of the error
   */
  public BankIDException(ErrorResponse errorResponse, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorResponse.getErrorCode();
    this.details = errorResponse.getDetails();
  }

  /**
   * Constructor assigning the error code.
   * 
   * @param errorCode
   *          the error code
   */
  public BankIDException(ErrorCode errorCode) {
    super();
    this.errorCode = errorCode;
  }

  /**
   * Constructor assigning the error code and the error message.
   * 
   * @param errorCode
   *          the error code
   * @param message
   *          the error message
   */
  public BankIDException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  /**
   * Constructor assigning the error code and the cause of the error.
   * 
   * @param errorCode
   *          the error code
   * @param cause
   *          the cause of the error
   */
  public BankIDException(ErrorCode errorCode, Throwable cause) {
    super(cause);
    this.errorCode = errorCode;
  }

  /**
   * Constructor assigning the error code, the error message and the cause of the errors.
   * 
   * @param errorCode
   *          the error code
   * @param message
   *          the error message
   * @param cause
   *          the cause of the error
   */
  public BankIDException(ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  /**
   * Returns the error code.
   * 
   * @return the error code
   */
  public ErrorCode getErrorCode() {
    return this.errorCode;
  }

  /**
   * Returns the error details.
   * 
   * @return the error details
   */
  public String getDetails() {
    return this.details;
  }

  /**
   * Assigns the error details.
   * 
   * @param details
   *          the error details
   */
  public void setDetails(String details) {
    this.details = details;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("error-code='%s', details=%s", (this.errorCode != null ? this.errorCode.getValue() : "unknown"), 
      this.details != null ? this.details : "<not-set>"); 
  }

}
