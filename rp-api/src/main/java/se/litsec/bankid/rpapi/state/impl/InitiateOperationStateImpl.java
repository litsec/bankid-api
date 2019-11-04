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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.litsec.bankid.rpapi.service.BankIDClient;
import se.litsec.bankid.rpapi.state.BankIDState;
import se.litsec.bankid.rpapi.state.BankIDStateException;
import se.litsec.bankid.rpapi.state.InitiateOperationState;
import se.litsec.bankid.rpapi.state.PersonalIdOrQRCode;
import se.litsec.bankid.rpapi.state.ThisOrAnotherDevice;
import se.litsec.bankid.rpapi.state.VoidInput;
import se.litsec.bankid.rpapi.support.PersonalIdentityNumber.Format;
import se.litsec.bankid.rpapi.types.BankIDException;
import se.litsec.bankid.rpapi.types.OrderResponse;
import se.litsec.bankid.rpapi.types.Requirement;

/**
 * Implements the initiate state.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class InitiateOperationStateImpl extends AbstractBankIDState<VoidInput> implements InitiateOperationState {

  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(InitiateOperationStateImpl.class);

  /**
   * Constructor.
   * 
   * @param context
   *          the BankID context
   */
  protected InitiateOperationStateImpl(BankIDStateContext context) {
    super(context);
  }

  /** {@inheritDoc} */
  @Override
  protected BankIDState<?> nextInternal(VoidInput input) throws BankIDStateException {

    try {
      if (this.getContext().getThisOrAnotherDevice() == ThisOrAnotherDevice.OTHER_DEVICE) {

        // If BankID should be used on another device, we need to figure out whether to use QR code or prompt
        // for the personal id number.
        //
        if (this.getContext().getPersonalIdOrQRCode() == null) {
          log.debug("{}: Mobile BankID on other device is selected, need to know whether to use QR-code ...", this.getStateName());
          return new PersonalIdOrQRCodeStateImpl(this.getContext());
        }
        // Else, if this is already known, take action ...
        //
        else if (this.getContext().getPersonalIdOrQRCode() == PersonalIdOrQRCode.PERSONAL_ID_NUMBER) {
          // Check if the personal ID number is known, otherwise prompt for it ...
          //
          if (this.getContext().getPersonalIdentityNumber() != null) {
            // OK, time to initiate the operation ...
            this.initiate();
            
            // Then go to the start the app manually-state ...
            return new ManualStartStateImpl(this.getContext());
          }
          else {
            // Prompt for personal identity number ...
            //
            log.debug("{}: Mobile BankID on other device is selected, personal identity number is required ...", this.getStateName());
            return new PersonalIdNumberPromptStateImpl(this.getContext());
          }
        }
        else { // QR-CODE
          // In order to generate a QR code we need an autostart token, so we make sure that we get one ...
          //
          Requirement newReq = new Requirement.RequirementBuilder(this.getContext().getRequirements().getRequirementsConfig())
              .autoStartTokenRequired().build();
          this.getContext().getRequirements().setRequirementsConfig(newReq);
          
          // Initiate the operation.
          this.initiate();
          
          // Generate a QR-code ...
          try {
            String qrCode = this.getContext().getClient().getQRGenerator().generateQRCodeBase64Image(this.getContext().getAutoStartToken());
            this.getContext().setQrCode(qrCode);
          }
          catch (IOException e) {
            log.error("{}: Failed to generate QR code - {}", this.getStateName(), e.getMessage(), e);
            
            // Cancel the initiated operation.
            this.getContext().getClient().cancel(this.getContext().getOrderReference());
            
            // TODO: Go to error state
          }
          
          // Then go to the start the app manually-state ...
          return new ManualStartStateImpl(this.getContext());
        }
      }
      else { // THIS_DEVICE
        // When "this device" is being used, we will try to autostart the app. This should work in all
        // cases except when an embedded browser is used in an app on iOS. In those cases, we have to
        // ask the user to manually start the app AND prompt for the personal identity number.
        //
        if (this.getContext().getPersonalIdentityNumber() == null
            && this.getContext().getUserAgent().is_iOS() && this.getContext().getUserAgent().isEmbeddedBrowser()) {
          log.debug("{}: User is using embedded browser on iOS, autostart is not possible - will need personal identity number",
            this.getStateName());
          
          PersonalIdNumberPromptStateImpl nextState = new PersonalIdNumberPromptStateImpl(this.getContext());
          nextState.setAutostartNotPossible(false);
          return nextState;
        }
        else {
          this.initiate();        
          return new InitiateAutoStartStateImpl(this.getContext());
        }
      }
    }
    catch (BankIDException e) {
      log.warn("{}: Failed to initiate BankID operation - {}", this.getStateName(), e);
      
      // TODO: go to error state
    }
    return null;
  }

  protected void initiate() throws BankIDException {

    final BankIDClient client = this.getContext().getClient();
    final String personalIdentityNumber = this.getContext().getPersonalIdentityNumber() != null
        ? this.getContext().getPersonalIdentityNumber().getNumber(Format.TWELVE_DIGITS_NO_DELIMITER)
        : null;
    final String ipAddress = this.getContext().getUserAgent().getUserIpAddress();

    OrderResponse response = this.getContext().getOperation() == BankIDOperation.AUTHENTICATION
        ? client.authenticate(personalIdentityNumber, ipAddress, this.getContext().getRequirements().getRequirementsConfig())
        : client.sign(personalIdentityNumber, ipAddress, this.getContext().getDataToSign(),
          this.getContext().getRequirements().getRequirementsConfig());

    this.getContext().setOrderReference(response.getOrderReference());
    this.getContext().setAutoStartToken(response.getAutoStartToken());
  }

}
