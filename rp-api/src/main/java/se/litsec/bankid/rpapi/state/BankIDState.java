/*
 * Copyright 2018-2019 Litsec AB
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
 * BankID authentication and signature operations follow a flow that can be realized using a state machine, where each
 * state takes care of its business and takes us to the next step of the flow. Otherwise writing the BankID Relying
 * Party backend may end up in a clutter of if-else statements and home-crafted state handling.
 * <p>
 * A BankID state is created by invoking {@link BankIDStateEngine#startAuthentication(UserAgent, BankIDRequirements)} or
 * {@link BankIDStateEngine#startSignature(se.litsec.bankid.rpapi.service.DataToSign, UserAgent, BankIDRequirements)}.
 * When the backend has a BankID state it can start communicating with the user via the user agent. There are a number
 * of different states:
 * <ul>
 * <li>Server side states - A state that does not want to display any output for the user and does not require any
 * input. The backend simply invokes {@link BankIDState#next()} to go to the next state.</li>
 * <li>States wanting to display output - A state that want the backend to display some information for the user via the
 * UI, for example "Start the BankID application".</li>
 * <li>States requiring input - A state may require input before it can go to the next state. The input may be a
 * personal identity number that the user enters, but also input that can be provided by the backend such as "Should
 * QR-code or personal identity number be used for a BankID-operation on another device".</li>
 * </ul>
 * Note that a state may both want to output information and require input.
 * </p>
 * 
 * @author Martin Lindström (martin@litsec.se)
 * @see BankIDStateEngine
 */
public interface BankIDState<I extends BankIDStateInput> {

  /**
   * Executes the state operation and returns the next state of the flow. This method is invoked when the current state
   * does not require any input.
   * 
   * @return the next state
   * @throws BankIDStateException
   *           for state errors (required input, called in an end-state, called for a stale state, ...)
   */
  BankIDState<?> next() throws BankIDStateException;

  /**
   * Executes the state operation and returns the next state of the flow. This method is invoked when the current state
   * requires input to proceed.
   * 
   * @param input
   *          state input
   * @throws BankIDStateException
   *           if the supplied input is not valid
   */
  BankIDState<?> next(I input) throws BankIDStateException;

  /**
   * Predicate that tells whether this state expects any input before it can proceed to the next state.
   * 
   * @return {@code true} if input is required and {@code false} otherwise
   */
  boolean isInputRequired();

  /**
   * Returns the state output, i.e., the data to display to the user.
   * 
   * @return the state output, or {@code null} if the state has no output
   */
  BankIDStateOutput getOutput();

  /**
   * If the state does not require any input, or wants to display any output, the state is a "server state" which means
   * that the {@link #next()} method should be called directly on the server without involving the client.
   * 
   * @return {@code true} if the current state is a "server state" and {@code false} otherwise
   */
  boolean isServerState();

  /**
   * Predicate that tells if this state is an end-state (success or error).
   * 
   * @return {@code true} if this is an end-state and {@code false} otherwise
   */
  boolean isEndState();

  /**
   * Predicate that tells if this state is "stale". A state is stale if its {@link #next()} method has been invoked and
   * successfully executed.
   * 
   * @return {@code true} if the {@link #next()} method already has been called, and {@code false} otherwise
   */
  boolean isStaleState();

  /**
   * Returns the name for this name (for logging purposes).
   * 
   * @return state name
   */
  String getStateName();

}
