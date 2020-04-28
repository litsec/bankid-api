/*
 * Copyright 2018-2020 Litsec AB
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

import se.litsec.bankid.rpapi.types.BankIDException;
import se.litsec.bankid.rpapi.types.CollectResponse;
import se.litsec.bankid.rpapi.types.OrderResponse;
import se.litsec.bankid.rpapi.types.Requirement;
import se.litsec.bankid.rpapi.types.UserCancelException;

/**
 * An interface that declares the methods for a BankID Relying Party (client).
 * 
 * @author Martin Lindström (martin.lindstrom@litsec.se)
 */
public interface BankIDClient {

  /**
   * Request an authentication order. The {@link #collect(String)} method is used to query the status of the order.
   * 
   * @param personalIdentityNumber
   *          the ID number of the user trying to be authenticated (optional). If the ID number is omitted the user must
   *          use the same device and the client must be started with the autoStartToken returned in orderResponse
   * @param endUserIp
   *          the user IP address as seen by the relying party
   * @param requirement
   *          used by the relying party to set requirements how the authentication or sign operation must be performed.
   *          Default rules are applied if omitted
   * @return an order response
   * @throws BankIDException
   *           for errors
   */
  OrderResponse authenticate(final String personalIdentityNumber, final String endUserIp, final Requirement requirement) throws BankIDException;

  /**
   * Request a signing order. The {@link #collect(String)} method is used to query the status of the order.
   * 
   * @param personalIdentityNumber
   *          the ID number of the user trying to be authenticated (optional). If the ID number is omitted the user must
   *          use the same device and the client must be started with the autoStartToken returned in orderResponse
   * @param endUserIp
   *          the user IP address as seen by the relying party
   * @param dataToSign
   *          the data to sign
   * @param requirement
   *          used by the relying party to set requirements how the authentication or sign operation must be performed.
   *          Default rules are applied if omitted
   * @return an order response
   * @throws BankIDException
   *           for errors
   */
  OrderResponse sign(final String personalIdentityNumber, final String endUserIp,
      final DataToSign dataToSign, final Requirement requirement) throws BankIDException;

  /**
   * Cancels an ongoing order.
   * 
   * @param orderReference
   *          the order reference
   * @throws BankIDException
   *           for errors
   */
  void cancel(final String orderReference) throws BankIDException;

  /**
   * Collects the result from {@link #authenticate(String, String, Requirement)} or
   * {@link #sign(String, String, DataToSign, Requirement)}.
   * 
   * @param orderReference
   *          the unique order reference
   * @return a collect response object
   * @throws UserCancelException
   *           if the user cancels the operation
   * @throws BankIDException
   *           for errors
   */
  CollectResponse collect(final String orderReference) throws UserCancelException, BankIDException;

  /**
   * Returns the QR generator that should be used to generate QR codes.
   * 
   * @return a QRGenerator or null if no QR code generator has been configured
   */
  QRGenerator getQRGenerator();

}
