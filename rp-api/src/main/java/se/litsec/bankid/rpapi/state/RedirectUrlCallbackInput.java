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
package se.litsec.bankid.rpapi.state;

/**
 * A callback interface for providing input to the state that calculates the launch URL to be used to auto start the
 * BankID app. Note that this callback will only be used when the "redirect URL" (or return URL) is needed (i.e., when
 * the platform is iOS).
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public interface RedirectUrlCallbackInput extends BankIDStateInput {

  /**
   * Returns the <b>URL-encoded</b> URL that should be supplied to the BankID app that will take the user back to the
   * web application after completing the BankID authentication or signature.
   * <p>
   * Note that the entire URL needs to be URL-encoded.
   * </p>
   * <p>
   * The current BankID order reference may be needed for building the redirect URL. It is therefore supplied in the
   * call.
   * </p>
   * 
   * @param orderReference
   *          the BankID order reference
   * @return the URL-encoded redirect URL
   */
  String getRedirectUrl(String orderReference);

}
