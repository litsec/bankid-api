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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import se.litsec.bankid.rpapi.state.BankIDState;
import se.litsec.bankid.rpapi.state.BankIDStateException;
import se.litsec.bankid.rpapi.state.BankIDStateInput;

/**
 * Abstract base class for a BankID state.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractBankIDState<I extends BankIDStateInput> implements BankIDState<I> {
  
  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(AbstractBankIDState.class);

  /** The BankID state context. */
  private BankIDStateContext context;

  /** Has the next() method already been invoked? */
  @JsonIgnore
  private boolean stale = false;

  /**
   * Constructor.
   * 
   * @param context
   *          the BankID context.
   */
  protected AbstractBankIDState(BankIDStateContext context) {
    Assert.notNull(context, "context must not be null");
    this.context = context;
  }

  /**
   * Returns the context for the BankID operation.
   * 
   * @return the context
   */
  protected BankIDStateContext getContext() {
    return this.context;
  }

  /** {@inheritDoc} */
  @Override
  public synchronized final BankIDState<?> next() throws BankIDStateException {
    return this.next(null);
  }

  /** {@inheritDoc} */
  @Override
  public synchronized final BankIDState<?> next(I input) throws BankIDStateException {
    if (this.isStaleState()) {
      throw new BankIDStateException("Illegal call to next - State is stale (next has already been called)");
    }
    if (input == null && this.isInputRequired()) {
      throw new BankIDStateException("Illegal call to next() - Input is required");
    }
    BankIDState<?> nextState = this.nextInternal(input);
    log.debug("{} - Going to state: {}", this.getStateName(), nextState.getStateName());
    
    this.stale = true;
    return nextState;
  }

  /**
   * Implements the BankID logic for the current state and returns a new state.
   * 
   * @param input
   *          the BankID state input (or {@code null} if the state does not require input)
   * @return the next state
   * @throws BankIDStateException
   *           for state errors
   */
  protected abstract BankIDState<?> nextInternal(I input) throws BankIDStateException;

  /** {@inheritDoc} */
  @Override
  public final boolean isServerState() {
    return !this.isInputRequired() && this.getOutput() == null && !this.isEndState();
  }

  /** {@inheritDoc} */
  @Override
  public final boolean isStaleState() {
    return this.stale;
  }

}
