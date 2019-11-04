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

import se.litsec.bankid.rpapi.state.BankIDState;
import se.litsec.bankid.rpapi.state.BankIDStateException;
import se.litsec.bankid.rpapi.state.BankIDStateOutput;
import se.litsec.bankid.rpapi.state.PersonalIdOrQRCode;
import se.litsec.bankid.rpapi.state.PersonalIdOrQRCodeState;
import se.litsec.bankid.rpapi.support.BankIDMessage;

/**
 * Implementation of the {@code PersonalIdOrQRCodeState}.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class PersonalIdOrQRCodeStateImpl extends AbstractBankIDState<PersonalIdOrQRCode> implements PersonalIdOrQRCodeState {
  
  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(PersonalIdOrQRCodeStateImpl.class);

  /**
   * Constructor.
   * 
   * @param context
   *          the BankID context
   */  
  protected PersonalIdOrQRCodeStateImpl(BankIDStateContext context) {
    super(context);
    
    // The configuration should tell us whether to use QR code or personal number ...
    //
    if (this.getContext().getRequirements() != null) {
      if (this.getContext().getRequirements().getPersonalIdOrQRCode() != null) {
        this.getContext().setPersonalIdOrQRCode(this.getContext().getRequirements().getPersonalIdOrQRCode());
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInputRequired() {
    if (this.getContext().getPersonalIdOrQRCode() != null) {
      return false;
    }
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public BankIDStateOutput getOutput() {
    if (this.getContext().getPersonalIdOrQRCode() != null) {
      log.debug("{}: State already knows that {} is to be used, no user interaction needed", 
        this.getStateName(), this.getContext().getPersonalIdOrQRCode());
      return null;
    }
    
    return new BankIDStateOutputImpl(BankIDMessage.ShortName.EXT1); 
  }

  /** {@inheritDoc} */
  @Override
  protected BankIDState<?> nextInternal(PersonalIdOrQRCode input) throws BankIDStateException {
    if (input == null && this.isInputRequired()) {
      throw new BankIDStateException("Illegal call to next() - Input is required");
    }
    
    if (input == null) {
      input = this.getContext().getPersonalIdOrQRCode();
    }
    else {
      log.debug("{}: {} was selected", this.getStateName(), input);
      this.getContext().setPersonalIdOrQRCode(input);
    }
    return new InitiateOperationStateImpl(this.getContext());
  }

}
