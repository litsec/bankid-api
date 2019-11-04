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

import se.litsec.bankid.rpapi.state.BankIDState;
import se.litsec.bankid.rpapi.state.BankIDStateException;
import se.litsec.bankid.rpapi.state.BankIDStateOutput;
import se.litsec.bankid.rpapi.state.InitiateAutoStartState;
import se.litsec.bankid.rpapi.state.RedirectUrlCallbackInput;
import se.litsec.bankid.rpapi.support.BankIDMessage;

/**
 * State that is responsible of finding out how the BankID app should be auto started by checking the User-agent header.
 * The rules described in chapter 3 of the BankID Relying Party Guidelines are followed.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class InitiateAutoStartStateImpl extends AbstractBankIDState<RedirectUrlCallbackInput> implements InitiateAutoStartState {

  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(InitiateAutoStartStateImpl.class);

  /** URL used in launch URL. */
  private String redirectUrl = null;

  /** Set if auto start is not possible. */
  private boolean manualStartRequired = false;

  /** Set if BankID app is unable to return to our application. */
  private boolean informAboutReturn = false;

  /**
   * Constructor.
   * 
   * @param context
   *          the BankID context
   */
  protected InitiateAutoStartStateImpl(BankIDStateContext context) {
    super(context);

    log.debug("{}: Determining autostart URL based on User-Agent header: {}",
      this.getStateName(), this.getContext().getUserAgent().getUserAgentHeader());

    // The only platform that needs a redirect URL is iOS, and in all other cases we
    // assign the "null" value.
    //
    if (this.getContext().getUserAgent().is_iOS()) {
      if (this.getContext().getUserAgent().isNonEmbeddedMobileSafari()) {
        log.debug("{}: Browser is Safari on iOS - redirectUrl is required", this.getStateName());
      }
      else if (this.getContext().getUserAgent().isEmbeddedBrowser()) {
        // If an embedded browser is used on iOS, we will not be able to autostart the app.
        // Instead we have to ask the user to manually start the BankID-app and later switch back.
        //
        this.manualStartRequired = true;

        log.info("{}: User is on iOS and is using an embedded browser. We will have to ask the user to manually start the app",
          this.getStateName());
      }
      else {
        log.info("{}: Platform is iOS, but Safari is not used - "
            + "BankID app will not be able to return to our app - We'll inform the user about that", this.getStateName());

        this.informAboutReturn = true;
        this.redirectUrl = "null";
      }
    }
    else {
      log.debug("{}: Platform is not iOS, no need to set redirectUrl in autostart URL", this.getStateName());
      this.redirectUrl = "null";
    }

  }

  /**
   * If the user is on iOS and is using Safari (default browser), we need the redirect URL to be used in the autostart
   * URL as input.
   */
  @Override
  public boolean isInputRequired() {
    return this.redirectUrl == null && !this.manualStartRequired;
  }

  /** {@inheritDoc} */
  @Override
  public BankIDStateOutput getOutput() {
    return null;
//    BankIDStateOutputImpl output = new BankIDStateOutputImpl();
//
//    // TODO: Re-write
//
//    // Ask the application to display some sort of "waiting" image.
//    output.setDisplayWaitingImage(true);
//
//    if (this.issueManualWarning) {
//      // Inform the user that he or she has to manually start the app.
//      output.setMessage(BankIDMessage.ShortName.EXT3);
//    }
//    else {
//      // Inform the user that we are trying to start the app.
//      output.setMessage(BankIDMessage.ShortName.RFA13);
//    }
//    return output;
  }

  /** {@inheritDoc} */
  @Override
  protected BankIDState<?> nextInternal(RedirectUrlCallbackInput input) throws BankIDStateException {

    if (this.redirectUrl == null) {
      this.redirectUrl = input.getRedirectUrl(this.getContext().getOrderReference());
      if (this.redirectUrl == null) {
        log.warn("{}: No BankID return URL provided by RedirectUrlCallbackInput", this.getStateName());
      }
    }
    
    if (!this.manualStartRequired) {    
      // Build autostart launch URL ...
      String launchUrl = String.format("bankid:///?autostarttoken=%s&redirect=%s",
        this.getContext().getAutoStartToken(), this.redirectUrl);

      log.debug("{}: Launch URL for autostart: {}", launchUrl);
      this.getContext().setLaunchUrl(launchUrl);
      
      AutoStartStateImpl nextState = new AutoStartStateImpl(this.getContext());
      
      if (this.informAboutReturn) {
        nextState.setInformAboutReturn(true);
      }
      return nextState;                
    }
    else {
      // Manual start is required, so we don't need a launch URL
      AutoStartStateImpl nextState = new AutoStartStateImpl(this.getContext());
      nextState.setManualStartRequired(true);
      return nextState;
    }
  }

}
