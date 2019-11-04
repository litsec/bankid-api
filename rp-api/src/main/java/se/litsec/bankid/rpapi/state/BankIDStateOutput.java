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

import se.litsec.bankid.rpapi.support.BankIDMessage;

/**
 * Interface for output from the BankID state. This can be a message or image to be displayed for the user.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public interface BankIDStateOutput {

  /**
   * If there is a message to be displayed for the user, this method returns the message ID for that message.
   * 
   * @return the message, or {@code null} if no message should be displayed.
   */
  BankIDMessage.ShortName getMessage();

  /**
   * If a "waiting image" should be displayed this method returns {@code true}.
   * 
   * @return {@code true} if some sort of waiting image is to be displayed and {@code false} otherwise
   */
  boolean displayWaitingImage();

  /**
   * If the user interface should prompt for the user's personal identity number this method returns {@code true}.
   * 
   * @return {@code true} if the UI should prompt for the personal number, and {@code false} otherwise
   */
  boolean promptForPersonalNumber();

  /**
   * Returns the URL that is used to autostart the BankID app or program.
   * 
   * @return the launch URL, or {@code null}
   */
  String getLaunchUrl();

  /**
   * If the user should scan a QR code this method will return the Base64 encoded image.
   * 
   * <p>
   * For example:
   * 
   * <pre>
   * {@code data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUA
   * AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO
   * 9TXL0Y4OHwAAAABJRU5ErkJggg==
   * }
   * </pre>
   * </p>
   * <p>
   * The image may then be directly inserted in HTML code as:
   * 
   * <pre>
   * {@code <img src="data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUA
   * AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO
   * 9TXL0Y4OHwAAAABJRU5ErkJggg==" scale="0"> 
   * }
   * </pre>
   * </p>
   * 
   * @return the QR code image or {@code null}
   */
  String qrCodeImage();

}
