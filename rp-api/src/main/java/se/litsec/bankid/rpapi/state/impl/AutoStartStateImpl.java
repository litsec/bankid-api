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
package se.litsec.bankid.rpapi.state.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.litsec.bankid.rpapi.state.AutoStartState;
import se.litsec.bankid.rpapi.state.BankIDState;
import se.litsec.bankid.rpapi.state.BankIDStateException;
import se.litsec.bankid.rpapi.state.BankIDStateOutput;
import se.litsec.bankid.rpapi.state.VoidInput;

/**
 * Implementation of the autostart state.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class AutoStartStateImpl extends AbstractBankIDState<VoidInput> implements AutoStartState {

  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(AutoStartStateImpl.class);

  /** Set if auto start is not possible. */
  @JsonProperty(value = "manualStartRequired")
  private boolean manualStartRequired = false;

  /** Set if BankID app is unable to return to our application. */
  @JsonProperty(value = "informAboutReturn")
  private boolean informAboutReturn = false;

  /**
   * Constructor.
   * 
   * @param context
   *          the BankID context
   */
  protected AutoStartStateImpl(BankIDStateContext context) {
    super(context);
  }

  /**
   * Returns the launch URL for starting the BankID app along with a message informing the user
   */
  @Override
  public BankIDStateOutput getOutput() {
    BankIDStateOutputImpl output = new BankIDStateOutputImpl();
    if (this.manualStartRequired) {
      // Test about manual start and return
    }
    else {
      output.setLaunchUrl(this.getContext().getLaunchUrl());
      if (this.informAboutReturn) {
        // Don't autostart, instead show text about manual return and display "start" button
      }
      else {
        // autostart
      }
    }
    return null;
  }

  @Override
  protected BankIDState<?> nextInternal(VoidInput input) throws BankIDStateException {
    return null;
  }

  /**
   * Predicate that tells if manual start is required, i.e., autostart won't work.
   * 
   * @return {@code true} if manual start of the BankID app is required, and {@code false} otherwise
   */
  public boolean isManualStartRequired() {
    return this.manualStartRequired;
  }

  public void setManualStartRequired(boolean manualStartRequired) {
    this.manualStartRequired = manualStartRequired;
  }

  public boolean isInformAboutReturn() {
    return this.informAboutReturn;
  }

  public void setInformAboutReturn(boolean informAboutReturn) {
    this.informAboutReturn = informAboutReturn;
  }

}
