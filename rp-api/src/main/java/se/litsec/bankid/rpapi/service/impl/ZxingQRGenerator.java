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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * A QR generator implementation based on the ZXing open source library.
 * <p>
 * <b>Note:</b> This implementation does not support the SVG image format.
 * </p>
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class ZxingQRGenerator extends AbstractQRGenerator {
  
  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(ZxingQRGenerator.class);

  /** {@inheritDoc} */
  @Override
  public byte[] generateQRCodeImage(final String autoStartToken, final int width, final int height, final ImageFormat format) throws IOException {
    
    if (ImageFormat.SVG.equals(format)) {
      throw new IOException("Image format SVG is not supported by " + this.getClass().getSimpleName());
    }    
    try {
      final String input = this.buildInput(autoStartToken);
      log.debug("Generating QR code in {} format based on {}", format, input);
      final QRCodeWriter writer = new QRCodeWriter();
      final BitMatrix bytes = writer.encode(input, BarcodeFormat.QR_CODE, width, height);
      final ByteArrayOutputStream stream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bytes, format.getImageFormatName(), stream);
      return stream.toByteArray();
    }
    catch (WriterException e) {
      throw new IOException("Failed to generate QR code: " + e.getMessage(), e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setDefaultImageFormat(final ImageFormat defaultImageFormat) {
    Assert.isTrue(ImageFormat.SVG.equals(defaultImageFormat), 
      "Image format SVG is not supported by " + this.getClass().getSimpleName());
    super.setDefaultImageFormat(defaultImageFormat);
  }

}