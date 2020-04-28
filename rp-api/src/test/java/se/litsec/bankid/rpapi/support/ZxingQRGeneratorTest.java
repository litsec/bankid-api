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
package se.litsec.bankid.rpapi.support;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import se.litsec.bankid.rpapi.service.QRGenerator;
import se.litsec.bankid.rpapi.service.QRGenerator.ImageFormat;
import se.litsec.bankid.rpapi.service.impl.ZxingQRGenerator;

/**
 * Test cases for the {@code ZxingQRGenerator} class.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class ZxingQRGeneratorTest {

  @Test
  public void testGenerate() throws Exception {
    QRGenerator generator = new ZxingQRGenerator();

    final String autoStartToken = "46f6aa68-a520-49d8-9be7-f0726d038c26";

    byte[] bytes = generator.generateQRCodeImage(autoStartToken, 300, 300, ImageFormat.PNG);
    String textInQR = decodeQRBytes(bytes);
    Assert.assertTrue(textInQR.endsWith(autoStartToken));

    bytes = generator.generateQRCodeImage(autoStartToken, 100, 100, ImageFormat.JPG);
    textInQR = decodeQRBytes(bytes);
    Assert.assertTrue(textInQR.endsWith(autoStartToken));
    
    try {
      generator.generateQRCodeImage(autoStartToken, 100, 100, ImageFormat.SVG);
      Assert.fail("SVG should not be accepted");
    }
    catch (IOException e) {      
    }
  }
  
  @Test
  public void testGenerateEmbedded() throws Exception {
    QRGenerator generator = new ZxingQRGenerator();

    final String autoStartToken = "46f6aa68-a520-49d8-9be7-f0726d038c26";

    String image = generator.generateQRCodeBase64Image(autoStartToken, 300, 300, ImageFormat.PNG);
    Assert.assertTrue(image.startsWith("data:image/png;base64, "));
    
    String base64 = image.substring("data:image/png;base64, ".length());
    byte[] bytes = Base64.getDecoder().decode(base64);
    
    String textInQR = decodeQRBytes(bytes);
    Assert.assertTrue(textInQR.endsWith(autoStartToken));    
  }

  /**
   * Decodes the QR code bytes into a string.
   * 
   * @param bytes
   *          the bytes to decode
   * @return the encoded string
   * @throws Exception
   *           for errors
   */
  private static String decodeQRBytes(byte[] bytes) throws Exception {
    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
    LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

    return new MultiFormatReader().decode(bitmap).getText();
  }

}
