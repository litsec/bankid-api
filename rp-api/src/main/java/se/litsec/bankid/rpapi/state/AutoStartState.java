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
 * BankID state handling the autostarting of the BankID app.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public interface AutoStartState extends BankIDState<VoidInput> {

  /**
   * Returns {@code false}. This state does not require any input.
   */
  @Override
  default boolean isInputRequired() {
    return false;
  }

  /**
   * Returns {@code false}. This is not an end state.
   */
  @Override
  default boolean isEndState() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  default String getStateName() {
    return "auto-start-state";
  }

}
