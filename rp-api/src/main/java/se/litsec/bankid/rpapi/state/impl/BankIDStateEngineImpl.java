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

import se.litsec.bankid.rpapi.service.BankIDClient;
import se.litsec.bankid.rpapi.service.DataToSign;
import se.litsec.bankid.rpapi.state.BankIDRequirements;
import se.litsec.bankid.rpapi.state.BankIDState;
import se.litsec.bankid.rpapi.state.BankIDStateEngine;
import se.litsec.bankid.rpapi.state.BankIDStateException;
import se.litsec.bankid.rpapi.state.UserAgent;

/**
 * Implementation of a BankID state engine.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class BankIDStateEngineImpl implements BankIDStateEngine {

  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(BankIDStateEngineImpl.class);

  /** The BankID client. */
  protected BankIDClient bankIDClient;

  /** Are we running in production mode? */
  private boolean productionMode = true;

  /**
   * Constructor.
   * 
   * @param bankIDClient
   *          the BankID client
   */
  public BankIDStateEngineImpl(BankIDClient bankIDClient) {
    this.bankIDClient = bankIDClient;
  }

  /** {@inheritDoc} */
  @Override
  public BankIDState<?> startAuthentication(UserAgent userAgent, BankIDRequirements requirements) throws BankIDStateException {
    Assert.notNull(userAgent, "userAgent must not be null");

    BankIDStateContext context = new BankIDStateContext(this.bankIDClient);
    context.setProductionMode(this.productionMode);
    context.setOperation(BankIDOperation.AUTHENTICATION);
    context.setUserAgent(userAgent);
    context.setRequirements(requirements != null ? requirements : new BankIDRequirements());

    return this.start(context);
  }

  /** {@inheritDoc} */
  @Override
  public BankIDState<?> startSignature(DataToSign dataToSign, UserAgent userAgent, BankIDRequirements requirements)
      throws BankIDStateException {

    Assert.notNull(dataToSign, "dataToSign must not be null");
    Assert.hasText(dataToSign.getUserVisibleData(), "dataToSign.userVisibleData must be assigned");
    Assert.notNull(userAgent, "userAgent must not be null");

    BankIDStateContext context = new BankIDStateContext(this.bankIDClient);
    context.setProductionMode(this.productionMode);
    context.setOperation(BankIDOperation.SIGNATURE);
    context.setDataToSign(dataToSign);
    context.setUserAgent(userAgent);
    context.setRequirements(requirements != null ? requirements : new BankIDRequirements());

    return this.start(context);
  }

  /**
   * Initiates the state machine and returns the first "non server state".
   * 
   * @param context
   *          the BankID state context
   * @return a BankID state
   * @throws BankIDStateException
   *           for state errors
   */
  protected BankIDState<?> start(BankIDStateContext context) throws BankIDStateException {
    BankIDState<?> state = new ThisOrAnotherDeviceStateImpl(context);
    while (state.isServerState()) {
      state = state.next();
    }
    return state;
  }

  /**
   * Assigns whether we are running in production mode or not.
   * 
   * @param productionMode
   *          {@code true} for production and {@code false} for test
   */
  public void setProductionMode(boolean productionMode) {
    this.productionMode = productionMode;
  }

}
