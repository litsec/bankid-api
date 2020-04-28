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
package se.litsec.bankid.rpapi.support.useragent;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link UserAgent} interface. This implementation requires spring-mobile on the
 * classpath.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
public class DefaultUserAgent implements UserAgent {

  /** The Spring mobile device resolver helping us to determine the type of device. */
  private static final LiteDeviceResolver deviceResolver = new LiteDeviceResolver();

  /** The HTTP request. */
  private final HttpServletRequest request;

  /** The user agent header. */
  private String userAgentHeader;

  /**
   * Constructor.
   * 
   * @param request
   *          the HTTP servlet request for the user
   */
  public DefaultUserAgent(final HttpServletRequest request) {
    Assert.notNull(request, "request must not be null");
    this.request = request;
    final String header = this.request.getHeader("User-Agent");
    this.userAgentHeader = header != null ? header : "";
  }

  /** {@inheritDoc} */
  @Override
  public UserDeviceType getUserDeviceType() {
    final Device device = deviceResolver.resolveDevice(this.request);
    if (device.isMobile()) {
      return UserDeviceType.MOBILE;
    }
    else if (device.isTablet()) {
      return UserDeviceType.TABLET;
    }
    else {
      return UserDeviceType.DESKTOP;
    }
  }

  /** {@inheritDoc} */
  @Override
  public String getUserAgentHeader() {
    return this.userAgentHeader;
  }

  /** {@inheritDoc} */
  @Override
  public String getUserIpAddress() {
    return request.getRemoteAddr();
  }

  /** {@inheritDoc} */
  @Override
  public boolean is_iOS() {
    final String userAgent = this.getUserAgentHeader();
    return (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad"));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isNonEmbeddedMobileSafari() {
    final String header = this.getUserAgentHeader();
    if (header.contains("AppleWebKit") 
        && !(header.contains("CriOS") || header.contains("Chrome")) /* Chrome */
        && !header.contains("FxiOS") /* Firefox */) 
    {
      // Make sure that this is not an embedded Safari. So filter out the most common apps
      // that we know do not have a whitelisting for the BankID app.
      //
      if (this.isEmbeddedBrowser()) {
        return false;
      }
      return true;
    }
    else {
      return false;
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEmbeddedBrowser() {
    final String header = this.getUserAgentHeader();
    if (header.contains("GSA")) {
      // Google Search App
      return true;
    }
    else if (header.contains("FBAN")) {
      // Facebook app
      return true;
    }
    else if (header.contains("Twitter")) {
      // Twitter app
      return true;
    }
    return false;
  }

}
