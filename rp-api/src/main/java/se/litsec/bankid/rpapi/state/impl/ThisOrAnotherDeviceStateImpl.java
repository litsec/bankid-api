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
import se.litsec.bankid.rpapi.state.ThisOrAnotherDevice;
import se.litsec.bankid.rpapi.state.ThisOrAnotherDeviceState;
import se.litsec.bankid.rpapi.state.UserDeviceType;
import se.litsec.bankid.rpapi.support.BankIDMessage;
import se.litsec.bankid.rpapi.types.Requirement;
import se.litsec.bankid.rpapi.types.Requirement.RequirementBuilder;

/**
 * Implementation of {@code ThisOrAnotherDeviceState}.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class ThisOrAnotherDeviceStateImpl extends AbstractBankIDState<ThisOrAnotherDevice> implements ThisOrAnotherDeviceState {

  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(ThisOrAnotherDeviceStateImpl.class);

  /**
   * Constructor.
   * 
   * @param context
   *          the BankID context
   */
  protected ThisOrAnotherDeviceStateImpl(BankIDStateContext context) {
    super(context);

    // The requirements may tell us directly whether this or another device should be used.
    // Let's check that ...
    //

    // From a previous operation?
    if (this.getContext().getRequirements().getBankIDUsage() != null
        && this.getContext().getRequirements().getBankIDUsage().getThisOrAnotherDevice() != null) {

      log.debug("{}: {} was set in BankID usage state - will not have to ask user",
        this.getStateName(), this.getContext().getRequirements().getBankIDUsage().getThisOrAnotherDevice());
      this.getContext().setThisOrAnotherDevice(this.getContext().getRequirements().getBankIDUsage().getThisOrAnotherDevice());
    }
    // User on a mobile phone? And config that states "this device" for those cases?
    else if (this.getContext().getUserAgent().getUserDeviceType() == UserDeviceType.MOBILE
        && !this.getContext().getRequirements().isAllowOtherDeviceOnMobile()) {

      log.debug("{}: Configuration states that 'other device' should not be presented when user is on mobile phone", this.getStateName());
      this.getContext().setThisOrAnotherDevice(ThisOrAnotherDevice.THIS_DEVICE);
    }
    // If certificate policies are set, we may deduce this or another device ...
    else if (this.getContext().getRequirements().getRequirementsConfig() != null
        && this.getContext().getRequirements().getRequirementsConfig().getCertificatePolicies() != null
        && !this.getContext().getRequirements().getRequirementsConfig().getCertificatePolicies().isEmpty()) {

      if (this.getContext().getUserAgent().getUserDeviceType() == UserDeviceType.DESKTOP) {
        // If the user is on a desktop and the requirements does not include mobile BankID, we use this device.
        if (!this.getContext().getRequirements().getRequirementsConfig().getCertificatePolicies().contains(
            RequirementBuilder.MOBILE_BANKID(this.getContext().isProductionMode()))) {

          log.debug("{}: User is on desktop and config says 'no mobile bankid' - will use same device", this.getStateName());
          this.getContext().setThisOrAnotherDevice(ThisOrAnotherDevice.THIS_DEVICE);
        }
        // If the only policy set is Mobile BankID and we are on a desktop it must mean "other device".
        else if (this.getContext().getRequirements().getRequirementsConfig().getCertificatePolicies().size() == 1
            && this.getContext().getRequirements().getRequirementsConfig().getCertificatePolicies()
              .contains(RequirementBuilder.MOBILE_BANKID(this.getContext().isProductionMode()))) {

          log.debug("{}: User is on desktop and config says 'only mobile bankid' - will use other device", this.getStateName());
          this.getContext().setThisOrAnotherDevice(ThisOrAnotherDevice.OTHER_DEVICE);
        }
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public BankIDStateOutput getOutput() {
    if (this.getContext().getThisOrAnotherDevice() != null) {
      log.debug("{}: State already knows that {} is to be used, no user interaction needed",
        this.getStateName(), this.getContext().getThisOrAnotherDevice());
      return null;
    }

    if (this.getContext().getUserAgent().getUserDeviceType() == UserDeviceType.DESKTOP) {
      return this.getContext().getOperation() == BankIDOperation.AUTHENTICATION
          ? new BankIDStateOutputImpl(BankIDMessage.ShortName.RFA19_AUTH)
          : new BankIDStateOutputImpl(BankIDMessage.ShortName.RFA19_SIGN);
    }
    else { // MOBILE OR TABLET
      return this.getContext().getOperation() == BankIDOperation.AUTHENTICATION
          ? new BankIDStateOutputImpl(BankIDMessage.ShortName.RFA20_AUTH)
          : new BankIDStateOutputImpl(BankIDMessage.ShortName.RFA20_SIGN);
    }
  }

  /**
   * If the BankID context holds information whether to to use "this device" or "another device" we do not require
   * input.
   */
  @Override
  public boolean isInputRequired() {
    if (this.getContext().getThisOrAnotherDevice() != null) {
      return false;
    }
    return true;
  }

  /** {@inheritDoc} */
  @Override
  protected BankIDState<?> nextInternal(ThisOrAnotherDevice input) throws BankIDStateException {
    if (input != null) {
      log.debug("{}: {} was selected by the user", this.getStateName(), input);
      this.getContext().setThisOrAnotherDevice(input);
    }
    
    if (this.getContext().getThisOrAnotherDevice() == ThisOrAnotherDevice.OTHER_DEVICE) {
      // Restrict to Mobile BankID usage ...
      Requirement newReq = new Requirement.RequirementBuilder(this.getContext().getRequirements().getRequirementsConfig())
          .mobile(true).onFile(false).onSmartCard(false).nordea(false).build();
      this.getContext().getRequirements().setRequirementsConfig(newReq);
    }    

    return new InitiateOperationStateImpl(this.getContext());
  }

}
