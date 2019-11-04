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
package se.litsec.bankid.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Main controller.
 * 
 * @author Martin Lindström (martin.lindstrom@idsec.se)
 */
@Controller
@RequestMapping("/")
public class BankIDController extends BaseController {

  /**
   * Controller method for the home endpoint.
   * 
   * @param device
   *          the type of device
   * @param debug
   *          debug flag
   * 
   * @return a model and view object
   */
  @GetMapping
  public ModelAndView home(Device device, @RequestParam(value = "debug", required = false, defaultValue = "false") Boolean debug) {
    ModelAndView mav = new ModelAndView("home");
    mav.addObject("debug", debug);

    return mav;
  }

  @RequestMapping("/auth")
  public ModelAndView auth(HttpServletRequest request, 
      @RequestParam(value = "action", required = false) String action,
      @RequestParam(value = "otherAction", required = false) String otherAction,
      @RequestParam(value = "personalIdNumber", required = false) String personalIdNumber) {
    
    // Protect against reloads of the page
    if (action == null) {
      return new ModelAndView("other");
    }
    
    if ("otherDevice".equals(action)) {
      if (otherAction == null) {
        // User hasn't selected personal ID number or QR code yet ...
        return new ModelAndView("other");
      }
      else if ("qrCode".equals(otherAction)) {
        // User selected to login using QR code. Initiate the authentication, generate a QR code
        // and display it.
        return new ModelAndView("qr-code");
      }
      else if ("personalId".equals(otherAction)) {
        // User selected to login using personal ID number. Unless the user has turned off
        // Javascript, we should find the personal number in the personalIdNumber parameter.
        // But since we want to handle also the dogmatists that refuse Javascript we handle
        // that case also.
        //
        if (personalIdNumber == null) {
          ModelAndView mav = new ModelAndView("other");
          mav.addObject("enterPersonalNumber", Boolean.TRUE);
          return mav;
        }
        else {
          // Initiate the authentication and start the flow.
          //
          
        }
      }
      return new ModelAndView("other");
    }
    else if ("thisDevice".equals(action)) {
      // Initiate and autostart
    }
    else {
      
    }
    
    return new ModelAndView("home");
  }

  @PostMapping("/startOther")
  public ModelAndView startOther(@RequestParam("action") String action,
      @RequestParam(value = "personalIdNumber", required = false) String personalIdNumber) {

    if ("personalId".equals(action)) {
      if (personalIdNumber == null) {
        ModelAndView mav = new ModelAndView("other");
        mav.addObject("enterPersonalNumber", Boolean.TRUE);
        return mav;
      }
      return new ModelAndView("home");
    }
    else {
      return new ModelAndView("home");
    }
  }

  /**
   * Displays the result of an authentication.
   * 
   * @param request
   *          the HTTP request
   * @param response
   *          the HTTP response
   * @return a model and view object
   * @throws ApplicationException
   *           for
   */
  // @GetMapping("/result")
  // public ModelAndView displayResult(HttpServletRequest request, HttpServletResponse response) {
  //
  // HttpSession session = request.getSession();
  // ModelAndView mav = (ModelAndView) session.getAttribute("sp-result");
  // if (mav == null) {
  // log.warn("No session for user, directing to start page [client-ip-address='{}']", request.getRemoteAddr());
  // return new ModelAndView("redirect:/");
  // }
  //
  // return mav;
  // }

}
