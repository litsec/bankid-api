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

import se.litsec.bankid.rpapi.support.PersonalIdentityNumber;
import se.litsec.bankid.rpapi.support.PersonalIdentityNumberException;

/**
 * Class for supplying the personal identity number to a BankID state.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class PersonalIdentityNumberInput extends PersonalIdentityNumber implements BankIDStateInput {

  /** For serializing. */
  private static final long serialVersionUID = -944903133644293107L;

  /**
   * Constructor.
   * 
   * @param number
   *          the personal identity number as a string
   * @throws PersonalIdentityNumberException
   *           if the supplied number is invalid
   */
  public PersonalIdentityNumberInput(String number) throws PersonalIdentityNumberException {
    super(number);
  }

}
