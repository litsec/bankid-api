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
package se.litsec.bankid.rpapi.service.impl;

import java.io.IOException;
import java.util.Base64;

import org.springframework.util.Assert;

import se.litsec.bankid.rpapi.service.QRGenerator;

/**
 * Abstract base class for QR generation.
 *
 * @author Martin Lindström (martin@litsec.se)
 */
public abstract class AbstractQRGenerator implements QRGenerator {

  /** The default width (in pixels) to use for generated QR images. */
  public static final int DEFAULT_WIDTH = 300;

  /** The default height (in pixels) to use for generated QR images. */
  public static final int DEFAULT_HEIGHT = 300;

  /** The default image format to use for generated QR images. */
  public static final ImageFormat DEFAULT_IMAGE_FORMAT = ImageFormat.PNG;

  /** The configured default width (in pixels) to use for generated QR images. */
  private int defaultWidth = DEFAULT_WIDTH;

  /** The configured default height (in pixels) to use for generated QR images. */
  private int defaultHeight = DEFAULT_HEIGHT;

  /** The configured default image format to use for generated QR images. */
  private ImageFormat defaultImageFormat = DEFAULT_IMAGE_FORMAT;

  /**
   * Builds the URI that is used as input for the QR generation.
   *
   * @param autoStartToken
   *          the BankID autostart token
   * @return an URI string
   */
  protected String buildInput(final String autoStartToken) {
    return "bankid:///?autostarttoken=" + autoStartToken;
  }

  /** {@inheritDoc} */
  @Override
  public byte[] generateQRCodeImage(final String autoStartToken) throws IOException {
    return this.generateQRCodeImage(autoStartToken, this.defaultWidth, this.defaultHeight, this.defaultImageFormat);
  }

  /** {@inheritDoc} */
  @Override
  public String generateQRCodeBase64Image(final String autoStartToken, final int width, final int height, final ImageFormat format) 
      throws IOException {
    
    final byte[] imageBytes = this.generateQRCodeImage(autoStartToken, width, height, format);
    return String.format("data:image/%s;base64, %s", 
      format.getImageFormatName().toLowerCase(), Base64.getEncoder().encodeToString(imageBytes));
  }

  /** {@inheritDoc} */
  @Override
  public String generateQRCodeBase64Image(final String autoStartToken) throws IOException {
    return this.generateQRCodeBase64Image(autoStartToken, this.defaultWidth, this.defaultHeight, this.defaultImageFormat);
  }

  /**
   * Assigns the default width (in pixels) to use for generated QR images.
   * <p>
   * If not assigned, {@value #DEFAULT_WIDTH} will be used.
   * </p>
   *
   * @param defaultWidth
   *          default width
   */
  public void setDefaultWidth(final int defaultWidth) {
    this.defaultWidth = defaultWidth;
  }

  /**
   * Assigns the default height (in pixels) to use for generated QR images.
   * <p>
   * If not assigned, {@value #DEFAULT_HEIGHT} will be used.
   * </p>
   *
   * @param defaultHeight
   *          default height
   */
  public void setDefaultHeight(final int defaultHeight) {
    this.defaultHeight = defaultHeight;
  }

  /**
   * Assigns the configured default image format to use for generated QR images.
   * <p>
   * If not assigned, {@value #DEFAULT_IMAGE_FORMAT} will be used.
   * </p>
   *
   * @param defaultImageFormat
   *          the default format
   */
  public void setDefaultImageFormat(final ImageFormat defaultImageFormat) {
    Assert.notNull(defaultImageFormat, "defaultImageFormat must not be null");
    this.defaultImageFormat = defaultImageFormat;
  }

}
