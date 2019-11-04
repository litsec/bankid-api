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

import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.litsec.bankid.rpapi.types.BankIDException;
import se.litsec.bankid.rpapi.types.CollectResponse;
import se.litsec.bankid.rpapi.types.CollectResponseJson;
import se.litsec.bankid.rpapi.types.ErrorCode;
import se.litsec.bankid.rpapi.types.ErrorResponse;
import se.litsec.bankid.rpapi.types.OrderResponse;
import se.litsec.bankid.rpapi.types.Requirement;
import se.litsec.bankid.rpapi.types.UserCancelException;

/**
 * An implementation of the BankID Relying Party API methods.
 * 
 * @author Martin Lindström (martin.lindstrom@litsec.se)
 */
public class BankIDClientImpl implements BankIDClient {

  /** Class logger. */
  private final Logger log = LoggerFactory.getLogger(BankIDClientImpl.class);

  /** The REST template used to send requests to the BankID server. */
  private RestTemplate restTemplate;

  /** The QR code generator. */
  private QRGenerator qrGenerator;

  /** Object mapper for JSON. */
  private static ObjectMapper objectMapper = new ObjectMapper();

  /** The /auth endpoint. */
  private URI authUri;

  /** The /sign endpoint. */
  private URI signUri;

  /** The /cancel endpoint. */
  private URI cancelUri;

  /** The /collect endpoint. */
  private URI collectUri;

  /**
   * Constructor.
   * 
   * @param restTemplate
   *          the REST template used to send requests to the BankID server
   * @param serviceUrl
   *          the URL to the BankID web service
   * @param qrGenerator
   *          the QR code generator (may be {@code null} if QR codes are not used)
   */
  public BankIDClientImpl(RestTemplate restTemplate, String serviceUrl, QRGenerator qrGenerator) {
    Assert.notNull(restTemplate, "'restTemplate' must be not be null");
    this.restTemplate = restTemplate;
    Assert.hasText(serviceUrl, "'serviceUrl' must not be null or empty");
    this.qrGenerator = qrGenerator;
    
    UriBuilderFactory uriFactory = new DefaultUriBuilderFactory(serviceUrl);
    this.authUri = uriFactory.builder().path("/auth").build();
    this.signUri = uriFactory.builder().path("/sign").build();
    this.cancelUri = uriFactory.builder().path("/cancel").build();
    this.collectUri = uriFactory.builder().path("/collect").build();
  }

  /** {@inheritDoc} */
  @Override
  public OrderResponse authenticate(String personalIdentityNumber, String endUserIp, Requirement requirement)
      throws BankIDException {

    Assert.hasText(endUserIp, "'endUserIp' must not be null or empty");

    // Set up the request data.
    //
    AuthnRequest request = new AuthnRequest(personalIdentityNumber, endUserIp, requirement);
    log.debug("authenticate. request: [{}] [uri: {}]", request, this.authUri);

    try {
      OrderResponse response = this.restTemplate.postForObject(this.authUri, request, OrderResponse.class);
      log.info("authenticate. response: [{}]", response);
      return response;
    }
    catch (HttpStatusCodeException e) {
      log.info("authenticate. Error during auth-call - {} - {} - {}", e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
      throw new BankIDException(this.getErrorResponse(e), "Auth-call failed", e);
    }
    catch (Exception e) {
      log.error("authenticate. Error during auth-call - {}", e.getMessage(), e);
      throw new BankIDException(ErrorCode.UNKNOWN_ERROR, "Unknown error during auth", e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public OrderResponse sign(String personalIdentityNumber, String endUserIp, DataToSign dataToSign, Requirement requirement)
      throws BankIDException {

    Assert.hasText(endUserIp, "'endUserIp' must not be null or empty");
    Assert.notNull(dataToSign, "'dataToSign' must not be null");
    Assert.hasText(dataToSign.getUserVisibleData(), "'dataToSign.userVisibleData' must not be null");

    SignRequest request = new SignRequest(personalIdentityNumber, endUserIp, requirement, dataToSign);
    log.debug("sign. request: [{}] [uri: {}]", request, this.authUri);

    try {
      OrderResponse response = this.restTemplate.postForObject(this.signUri, request, OrderResponse.class);
      log.info("sign. response: [{}]", response);
      return response;
    }
    catch (HttpStatusCodeException e) {
      log.info("sign. Error during sign-call - {} - {} - {}", e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
      throw new BankIDException(this.getErrorResponse(e), "Sign-call failed", e);
    }
    catch (Exception e) {
      log.error("sign. Error during sign-call - {}", e.getMessage(), e);
      throw new BankIDException(ErrorCode.UNKNOWN_ERROR, "Unknown error during sign", e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void cancel(String orderReference) throws BankIDException {
    Assert.hasText(orderReference, "'orderReference' must not be null or empty");

    log.debug("cancel: Request for cancelling order {}", orderReference);

    OrderRefRequest request = new OrderRefRequest(orderReference);

    try {
      this.restTemplate.postForObject(this.cancelUri, request, Void.class);
      log.info("cancel. Order {} successfully cancelled", orderReference);
    }
    catch (HttpStatusCodeException e) {
      log.info("cancel. Error during cancel-call - {} - {} - {}", e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
      throw new BankIDException(this.getErrorResponse(e), "Cancel-call failed", e);
    }
    catch (Exception e) {
      log.error("cancel. Error during cancel-call - {}", e.getMessage(), e);
      throw new BankIDException(ErrorCode.UNKNOWN_ERROR, "Unknown error during cancel", e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public CollectResponse collect(String orderReference) throws UserCancelException, BankIDException {
    Assert.hasText(orderReference, "'orderReference' must not be null or empty");

    log.debug("collect: Request for collecting order {}", orderReference);

    OrderRefRequest request = new OrderRefRequest(orderReference);
    try {
      CollectResponseJson response = this.restTemplate.postForObject(this.collectUri, request, CollectResponseJson.class);
      log.info("collect. response: [{}]", response);

      if (CollectResponseJson.Status.FAILED.equals(response.getStatus())) {
        throw new BankIDException(response.getErrorCode(), String.format("Order '%s' failed with code '%s'", orderReference, response
          .getErrorCode()
          .getValue()));
      }
      return response;
    }
    catch (HttpStatusCodeException e) {
      log.info("cancel. Error during cancel-call - {} - {} - {}", e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
      throw new BankIDException(this.getErrorResponse(e), "Cancel-call failed", e);
    }
    catch (Exception e) {
      log.error("cancel. Error during cancel-call - {}", e.getMessage(), e);
      throw new BankIDException(ErrorCode.UNKNOWN_ERROR, "Unknown error during cancel", e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public QRGenerator getQRGenerator() {
    return this.qrGenerator;
  }

  /**
   * Given an HTTP status error the method returns its contents as an {@link ErrorResponse}.
   * 
   * @param exception
   *          the exception
   * @return an {@code ErrorResponse}
   */
  private ErrorResponse getErrorResponse(HttpStatusCodeException exception) {
    byte[] body = exception.getResponseBodyAsByteArray();
    if (body == null) {
      return new ErrorResponse(ErrorCode.UNKNOWN_ERROR, null);
    }
    try {
      return objectMapper.readValue(body, ErrorResponse.class);
    }
    catch (IOException e) {
      log.error("Failed to deserialize error response {} into ErrorResponse structure", exception.getResponseBodyAsString(), e);
      return new ErrorResponse(ErrorCode.UNKNOWN_ERROR, null);
    }
  }

  /**
   * Represents the data sent in a /auth call.
   */
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @JsonInclude(Include.NON_NULL)
  private static class AuthnRequest {

    private String personalNumber;
    private String endUserIp;
    private Requirement requirement;

    public AuthnRequest(String personalNumber, String endUserIp, Requirement requirement) {
      this.personalNumber = personalNumber;
      this.endUserIp = endUserIp;
      this.requirement = requirement;
    }

    @Override
    public String toString() {
      return String.format("personalNumber='%s', endUserIp='%s', requirement=[%s]", this.personalNumber, this.endUserIp, this.requirement);
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @JsonInclude(Include.NON_NULL)
  private static class SignRequest extends AuthnRequest {

    private String userVisibleData;
    private String userNonVisibleData;

    public SignRequest(String personalNumber, String endUserIp, Requirement requirement, DataToSign dataToSign) {
      super(personalNumber, endUserIp, requirement);
      this.userVisibleData = dataToSign.getUserVisibleData();
      this.userNonVisibleData = dataToSign.getUserNonVisibleData();
    }

    @Override
    public String toString() {
      return String.format("%s, userVisibleData='%s', userNonVisibleData='%s'",
        super.toString(), this.userVisibleData, this.userNonVisibleData != null ? this.userNonVisibleData : "<not set>");
    }

  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  private static class OrderRefRequest {
    @SuppressWarnings("unused")
    private String orderRef;

    public OrderRefRequest(String orderRef) {
      this.orderRef = orderRef;
    }

  }

}
