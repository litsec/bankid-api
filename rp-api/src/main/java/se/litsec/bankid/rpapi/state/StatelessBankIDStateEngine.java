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

/**
 * A BankID operation may be implemented in a state-less fashion where the state is held by the client. This interface
 * extends the {@link BankIDStateEngine} with methods for resuming an operation based on a state delivered by the
 * client.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public interface StatelessBankIDStateEngine extends BankIDStateEngine {

  /**
   * In order to accomplish a state-less BankID operation the client must supply the server with the current state so
   * that the next step may be taken. This method decodes the supplied encoded state into a {@link BankIDState} object.
   * <p>
   * After a successful decoding, the method will invoke {@link #resume(BankIDState)} to set up a fully functional state
   * object.
   * </p>
   * 
   * @param encodedState
   *          the encoded state (for example JSON)
   * @return a {@link BankIDState} object
   */
  BankIDState<?> parse(String encodedState);

  /**
   * When the BankID operation is implemented in a state-less fashion, the client will pass the current state to the
   * server for each call. Depending on how the state implementation looks like, the server may have to do a parse
   * operation (see {@link #parseEncodedState(String)}) or it may get a {@link BankIDState} object directly, for example
   * by using Spring's Type Conversion (e.g., from JSON to Java object using Jackson). This method should be called in
   * the latter case to fully set up the Java object. Some of its properties may not be part of the encoding passed to
   * and from the client.
   * 
   * @param state
   *          the incoming state
   * @return a fully functional state holding any properties that are excluded from an encoding operation
   */
  BankIDState<?> resume(BankIDState<?> state);

}
