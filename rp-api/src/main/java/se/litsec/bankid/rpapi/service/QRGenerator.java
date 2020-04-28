/*
 * Copyright 2018-2020 Litsec AB
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
package se.litsec.bankid.rpapi.service;

import java.io.IOException;

/**
 * Interface for generating a QR code.
 * <p>
 * Section 4 of the BankID Relying Party Guidelines describes how to generate a QR code based on an auto start token and
 * how to present it for the user.
 * </p>
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public interface QRGenerator {

  /**
   * Generates a QR code image.
   * <p>
   * The auto start token will be used to build an URI according to section 4 of the BankID Relying Party Guidelines.
   * </p>
   * 
   * @param autoStartToken
   *          the BankID autostart token
   * @param width
   *          the width of the generated QR code (in pixels)
   * @param height
   *          the height of the generated QR code (in pixels)
   * @param format
   *          the format for the generated QR code
   * @return an byte array representing the generated QR code
   * @throws IOException
   *           for errors during generation
   * @see #generateQRCodeImage(String)
   */
  byte[] generateQRCodeImage(final String autoStartToken, final int width, final int height, final ImageFormat format) throws IOException;

  /**
   * Generates a QR code image using default settings for width, height and the image format.
   * 
   * @param autoStartToken
   *          the BankID autostart token
   * @return an byte array representing the generated QR code
   * @throws IOException
   *           for errors during generation
   * @see #generateQRCodeImage(String, int, int, ImageFormat)
   */
  byte[] generateQRCodeImage(final String autoStartToken) throws IOException;

  /**
   * Generates a QR code image as a Base64 encoded image.
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
   * @param autoStartToken
   *          the BankID autostart token
   * @param width
   *          the width of the generated QR code (in pixels)
   * @param height
   *          the height of the generated QR code (in pixels)
   * @param format
   *          the format for the generated QR code
   * @return the base64 encoded image
   * @throws IOException
   *           for errors during generation
   * @see #generateQRCodeBase64Image(String)
   */
  String generateQRCodeBase64Image(final String autoStartToken, final int width, final int height, final ImageFormat format) throws IOException;

  /**
   * Generates a QR code image as a Base64 encoded image using default settings for width, height and the image format.
   * 
   * @param autoStartToken
   *          the BankID autostart token
   * @return the base64 encoded image
   * @throws IOException
   *           for errors during generation
   * @see #generateQRCodeBase64Image(String, int, int, ImageFormat)
   */
  String generateQRCodeBase64Image(final String autoStartToken) throws IOException;

  /**
   * Enum representing an image format.
   */
  public enum ImageFormat {
    JPG("JPG"), PNG("PNG"), SVG("SVG");

    /**
     * Returns the image format in text format.
     * 
     * @return the image format
     */
    public String getImageFormatName() {
      return this.imageFormatName;
    }

    /**
     * Parses an image format string into an {@code ImageFormat} instance.
     * 
     * @param formatName
     *          the string to parse
     * @return an ImageFormat or null if there is no matching format
     */
    public static ImageFormat parse(String formatName) {
      for (ImageFormat i : ImageFormat.values()) {
        if (i.getImageFormatName().equals(formatName)) {
          return i;
        }
      }
      return null;
    }

    /**
     * Hidden constructor.
     * 
     * @param imageFormatName
     *          the image format name
     */
    private ImageFormat(String imageFormatName) {
      this.imageFormatName = imageFormatName;
    }

    /** The image format name. */
    private String imageFormatName;
  }

}
