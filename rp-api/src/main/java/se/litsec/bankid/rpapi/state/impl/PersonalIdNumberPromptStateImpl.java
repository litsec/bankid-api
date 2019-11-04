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
package se.litsec.bankid.rpapi.state.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.litsec.bankid.rpapi.state.BankIDState;
import se.litsec.bankid.rpapi.state.BankIDStateException;
import se.litsec.bankid.rpapi.state.BankIDStateOutput;
import se.litsec.bankid.rpapi.state.PersonalIdNumberPromptState;
import se.litsec.bankid.rpapi.state.PersonalIdentityNumberInput;
import se.litsec.bankid.rpapi.support.BankIDMessage;

/**
 * Implementation of the prompt for personal identity number state.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class PersonalIdNumberPromptStateImpl extends AbstractBankIDState<PersonalIdentityNumberInput> implements
    PersonalIdNumberPromptState {

  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(PersonalIdNumberPromptStateImpl.class);

  /**
   * Flag that is set to indicate that the personal number is prompted for because auto start is not possible. This
   * happens when the user is using an embedded app browser on iOS.
   */
  @JsonProperty(value = "autostartNotPossible")
  private boolean autostartNotPossible = false;

  /**
   * Constructor.
   * 
   * @param context
   *          the BankID context
   */
  protected PersonalIdNumberPromptStateImpl(BankIDStateContext context) {
    super(context);

    // Find out if the personal ID number is known from before ...
    //
    if (this.getContext().getRequirements() != null && this.getContext().getRequirements().getBankIDUsage() != null) {
      if (this.getContext().getRequirements().getBankIDUsage().getKnownPersonalIdNumber() != null) {
        this.getContext().setPersonalIdentityNumber(this.getContext().getRequirements().getBankIDUsage().getKnownPersonalIdNumber());
        log.debug("{}: Personal identity number is known - {}", this.getStateName(), this.getContext().getPersonalIdentityNumber());
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInputRequired() {
    if (this.getContext().getPersonalIdentityNumber() != null) {
      return false;
    }
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public BankIDStateOutput getOutput() {
    if (this.isInputRequired()) {
      BankIDStateOutputImpl output = new BankIDStateOutputImpl();
      output.setPromptForPersonalNumber(true);
      if (this.autostartNotPossible) {
        output.setMessage(BankIDMessage.ShortName.EXT4);
      }
      return output;
    }

    return null;
  }

  /** {@inheritDoc} */
  @Override
  protected BankIDState<?> nextInternal(PersonalIdentityNumberInput input) throws BankIDStateException {
    if (input != null) {
      log.debug("{}: {} was supplied by user", this.getStateName(), input);
      this.getContext().setPersonalIdentityNumber(input);
    }
    Assert.notNull(this.getContext().getPersonalIdentityNumber(), "Fatal error - personal identity number not present");
    return new InitiateOperationStateImpl(this.getContext());
  }

  /**
   * Predicate that indicates if the state has been called to prompt for the personal identity number because auto start
   * is not possible.
   * 
   * @return {@code true} if auto start is not possible, and {@code false} otherwise
   */
  public boolean isAutostartNotPossible() {
    return this.autostartNotPossible;
  }

  /**
   * Assigns whether this state has been called to prompt for the personal identity number because auto start is not
   * possible.
   * 
   * @param autostartNotPossible
   *          {@code true} if auto start is not possible, and {@code false} otherwise
   */
  public void setAutostartNotPossible(boolean autostartNotPossible) {
    this.autostartNotPossible = autostartNotPossible;
  }

}
