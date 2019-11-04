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
 * State that requires input telling whether "this device" or "another device" should be used.
 * <p>
 * Note that this information may be available (for example from a previous operation in the same session), and in that
 * case the {@link #isInputRequired()} will return false and {@link #next()} may be invoked. Otherwise
 * {@link #next(ThisOrAnotherDevice)} needs to be invoked.
 * </p>
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public interface ThisOrAnotherDeviceState extends BankIDState<ThisOrAnotherDevice> {

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
    return "this-or-another-device";
  }
    
}
