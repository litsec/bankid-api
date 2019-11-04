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
 * A state for determining whether to use QR-code or personal identity number when initiating an BankID operation on
 * another device. Normally, this is a RP configuration issue, but we make this a state to enable demo programs etc. to
 * ask the user.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public interface PersonalIdOrQRCodeState extends BankIDState<PersonalIdOrQRCode> {

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
    return "personal-id-or-qr-code-state";
  }

}
