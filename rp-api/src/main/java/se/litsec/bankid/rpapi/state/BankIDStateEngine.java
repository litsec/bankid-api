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

import se.litsec.bankid.rpapi.service.DataToSign;

/**
 * An engine for setting up a BankID state that drives authentication and signature.
 * <p>
 * Note: Not all states returned should be sent directly to the client. Obviously the success state holding the
 * authentication/signature info should be kept at the server, but also cases where for example the state says that a
 * personal identity number is required and we already know which number to use (without asking the user) we may jump to
 * the next state.
 * </p>
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public interface BankIDStateEngine {

  /**
   * Starts a BankID authentication operation by setting up the first state in the process.
   * 
   * @param userAgent
   *          the user agent inforsmation
   * @param requirement
   *          requirements for how the operation should be performed (may be {@code null}
   * @return a BankID state
   * @throws BankIDStateException
   *           for state related errors
   */
  BankIDState<?> startAuthentication(UserAgent userAgent, BankIDRequirements requirements) throws BankIDStateException;

  /**
   * Starts a BankID signature operation by setting up the first state in the process.
   * 
   * @param dataToSign
   *          the data to be signed
   * @param userAgent
   *          the user agent information
   * @param requirements
   *          requirements for how the operation should be performed (may be {@code null}
   * @return a BankID state
   * @throws BankIDStateException
   *           for state related errors
   */
  BankIDState<?> startSignature(DataToSign dataToSign, UserAgent userAgent, BankIDRequirements requirements)
      throws BankIDStateException;

}
