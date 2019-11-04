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
package se.litsec.bankid.rpapi.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import se.litsec.bankid.rpapi.types.OrderResponse;

/**
 * Testing the {@link BankIDClient}.
 * 
 * @author Martin Lindström (martin.lindstrom@litsec.se)
 */
public class BankIDClientTest {
  
  private RestTemplate restTemplate = new RestTemplate();
  
  private MockRestServiceServer mockServer;
  
  private BankIDClientImpl client;
  
  private static final String BANKID_URL = "https://appapi2.bankid.com/rp/v5";
  
  @Before
  public void setup() {
    RestGatewaySupport gateway = new RestGatewaySupport();
    gateway.setRestTemplate(this.restTemplate);
    this.mockServer = MockRestServiceServer.createServer(gateway);
    
    this.client = new BankIDClientImpl(this.restTemplate, BANKID_URL, null);
  }

  @Test
  public void testAuthenticate() throws Exception {
    
    String responseBytes = "{ \"orderRef\" : \"131daac9-16c6-4618-beb0-365768f37288\", \"autoStartToken\" : \"7c40b5c9-fa74-49cf-b98c-bfe651f9a7c6\" }";
    
    this.mockServer.expect(MockRestRequestMatchers.requestTo(BANKID_URL + "/auth"))
      .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
      .andExpect(MockRestRequestMatchers.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
      .andRespond(MockRestResponseCreators.withSuccess(responseBytes, MediaType.APPLICATION_JSON));
    
    OrderResponse response = this.client.authenticate("196911292032", "85.228.133.223", null);
    Assert.assertEquals("131daac9-16c6-4618-beb0-365768f37288", response.getOrderReference());
  }
}
