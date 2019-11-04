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

import se.litsec.bankid.rpapi.state.BankIDState;
import se.litsec.bankid.rpapi.state.BankIDStateException;
import se.litsec.bankid.rpapi.state.BankIDStateOutput;
import se.litsec.bankid.rpapi.state.ManualStartState;
import se.litsec.bankid.rpapi.state.PersonalIdOrQRCode;
import se.litsec.bankid.rpapi.state.VoidInput;
import se.litsec.bankid.rpapi.support.BankIDMessage;

/**
 * Implementation of the manual start state.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class ManualStartStateImpl extends AbstractBankIDState<VoidInput> implements ManualStartState {
  
  /** Class logger. */
//  private final Logger log = LoggerFactory.getLogger(ManualStartStateImpl.class);

  /**
   * Constructor.
   * 
   * @param context
   *          the BankID context
   */
  protected ManualStartStateImpl(BankIDStateContext context) {
    super(context);
  }

  /** {@inheritDoc} */
  @Override
  public BankIDStateOutput getOutput() {
    if (this.getContext().getPersonalIdOrQRCode() == PersonalIdOrQRCode.QR_CODE) {
      BankIDStateOutputImpl output = new BankIDStateOutputImpl(BankIDMessage.ShortName.EXT2);
      output.setQrCodeImage(this.getContext().getQrCode());
      return output;
    }
    else {
      BankIDStateOutputImpl output = new BankIDStateOutputImpl(BankIDMessage.ShortName.RFA1);
      output.setDisplayWaitingImage(true);
      return output;
    }
  }
  
  /** {@inheritDoc} */
  @Override
  protected BankIDState<?> nextInternal(VoidInput input) throws BankIDStateException {
    // TODO: Go to collect state
    return null;
  }

}
