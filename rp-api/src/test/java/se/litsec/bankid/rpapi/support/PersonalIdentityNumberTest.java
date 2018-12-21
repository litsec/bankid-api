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

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.litsec.bankid.rpapi.support.PersonalIdentityNumber.Format;

/**
 * Test cases for {@code PersonalIdentityNumber}.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class PersonalIdentityNumberTest {

  @Test
  public void testValid() throws Exception {
    String[] numbers = { "196904146856", "19690414-6856", "6904146856", "690414-6856" };
    for (String number : numbers) {
      PersonalIdentityNumber p = new PersonalIdentityNumber(number);
      Assert.assertEquals(String.format("Expected \"196904146856\" but was \"%s\"", p.getNumber(Format.TWELVE_DIGITS_NO_DELIMITER)),
        "196904146856", p.getNumber(Format.TWELVE_DIGITS_NO_DELIMITER));
    }

    // Test old people's numbers ...
    String[] numbers2 = { "190212240469", "19021224-0469", "19021224+0469", "021224+0469" };
    for (String number : numbers2) {
      PersonalIdentityNumber p = new PersonalIdentityNumber(number);
      Assert.assertEquals(String.format("Expected \"19021224+0469\" but was \"%s\"", p.getNumber(Format.TWELVE_DIGITS_DELIMITER)),
        "19021224+0469", p.getNumber(Format.TWELVE_DIGITS_DELIMITER));
    }
  }

  @Test
  public void testSamordningsnummer() throws Exception {
    String[] numbers = { "197010632391", "19701063-2391", "7010632391", "7010632391" };
    for (String number : numbers) {
      PersonalIdentityNumber p = new PersonalIdentityNumber(number);
      Assert.assertEquals(String.format("Expected \"197010632391\" but was \"%s\"", p.getNumber(Format.TWELVE_DIGITS_NO_DELIMITER)),
        "197010632391", p.getNumber(Format.TWELVE_DIGITS_NO_DELIMITER));
      Assert.assertTrue("Expected samordningsnummer", p.isSamordningsnummer());
    }
  }

  @Test
  public void testBadLuhn() throws Exception {
    String[] numbers = { "196904146850", "196904146851", "196904146852", "196904146853",
        "196904146854", "196904146855", "196904146857", "196904146858", "196904146859",
        "19690414-6850", "19690414-6851", "19690414-6852", "19690414-6853",
        "19690414-6854", "19690414-6855", "19690414-6857", "19690414-6858", "19690414-6859",
        "6904146850", "6904146851", "6904146852", "6904146853",
        "6904146854", "6904146855", "6904146857", "6904146858", "6904146859",
        "690414-6850", "690414-6851", "690414-6852", "690414-6853",
        "690414-6854", "690414-6855", "690414-6857", "690414-6858", "690414-6859" };

    for (String number : numbers) {
      try {
        new PersonalIdentityNumber(number);
        Assert.fail(String.format("Expected '%s' to fail due to bad luhn", number));
      }
      catch (PersonalIdentityNumberException e) {
      }
    }
  }

  @Test
  public void testBadFormat() throws Exception {
    String[] numbers = { "", "1923132", "19691129NNNN", "192313229252", "192300229252", "2313229252",
        "2300229252", "19739922-4737", "19730800-4737", "197308324737", "7308324737", "19730860-4737",
        "19730892-4737", "1969041468569", "197202311412"
    };

    for (String number : numbers) {
      try {
        new PersonalIdentityNumber(number);
        Assert.fail(String.format("Expected '%s' to fail", number));
      }
      catch (PersonalIdentityNumberException e) {
      }
    }
  }

  @Test
  public void testFormat() throws Exception {
    PersonalIdentityNumber p = new PersonalIdentityNumber("196904146856");
    Assert.assertEquals("196904146856", p.getNumber());
    Assert.assertEquals("196904146856", p.getNumber(Format.TWELVE_DIGITS_NO_DELIMITER));
    Assert.assertEquals("19690414-6856", p.getNumber(Format.TWELVE_DIGITS_DELIMITER));
    Assert.assertEquals("6904146856", p.getNumber(Format.TEN_DIGITS_NO_DELIMITER));
    Assert.assertEquals("690414-6856", p.getNumber(Format.TEN_DIGITS_DELIMITER));
    Assert.assertEquals("19690414-6856", p.toString());

    p = new PersonalIdentityNumber("190212240469");
    Assert.assertEquals("190212240469", p.getNumber());
    Assert.assertEquals("190212240469", p.getNumber(Format.TWELVE_DIGITS_NO_DELIMITER));
    Assert.assertEquals("19021224+0469", p.getNumber(Format.TWELVE_DIGITS_DELIMITER));
    Assert.assertEquals("0212240469", p.getNumber(Format.TEN_DIGITS_NO_DELIMITER));
    Assert.assertEquals("021224+0469", p.getNumber(Format.TEN_DIGITS_DELIMITER));
    Assert.assertEquals("19021224+0469", p.toString());
  }

  @Test
  public void testEquals() throws Exception {
    PersonalIdentityNumber[] p = {
        new PersonalIdentityNumber("196904146856"),
        new PersonalIdentityNumber("19690414-6856"),
        new PersonalIdentityNumber("6904146856"),
        new PersonalIdentityNumber("690414-6856")
    };
    PersonalIdentityNumber p2 = new PersonalIdentityNumber("190212240469");

    for (int i = 0; i < p.length; i++) {
      for (int j = 0; j < p.length; j++) {
        Assert.assertTrue(p[i].equals(p[j]));
      }
      Assert.assertFalse(p[i].equals(p2));
      Assert.assertFalse(p2.equals(p[i]));
    }
  }
  
  @Test
  public void testJson() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    
    PersonalIdentityNumber p = new PersonalIdentityNumber("196904146856");

    // Encode
    Assert.assertEquals("\"196904146856\"", mapper.writeValueAsString(p));

    // Decode
    PersonalIdentityNumber p2 = mapper.readValue("\"196904146856\"", PersonalIdentityNumber.class);
    Assert.assertEquals(p, p2);
  }
}
