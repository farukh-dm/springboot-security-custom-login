package demo.springboot.security.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.springboot.security.dto.AuthenticationRequestDto;
import demo.springboot.security.dto.AuthenticationResponseDto;

@Controller
@RequestMapping(value="/user")
public class LoginController {
	
	private static final Logger LOGGER = (Logger)LoggerFactory.getLogger(LoginController.class);
	
	// Provided by MySecurityConfiguration#authenticationManagerBean()
	@Autowired
	private AuthenticationManager authenticationManager; 
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(
		Model model, String error, String logout, 
		HttpServletRequest request, HttpServletResponse response) {
		
		// If user is logged-in. This will log user out.
		//-----------------------------------------
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		 
	    //---------------------------------------
		  
		if(error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if(logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "login";
    }
	
	@RequestMapping(value = "/custom_login", method = RequestMethod.GET)
    public String customLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		// If user is logged-in. This will log user out.
		//-----------------------------------------
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		 
	    //---------------------------------------
		  
		if(request.getParameter("error") != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if(request.getParameter("logout") != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "custom-login";
    }
	
	// Custom Authenticate method. Used for form login.
	@RequestMapping(value = "/custom_login_authenticate", method = RequestMethod.POST)
	public void authenticateFormRequest(
		@ModelAttribute("authenticationRequest") AuthenticationRequestDto requestDto,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		LOGGER.debug("in authenticateFormRequest()  ---->>>>");

		StringBuilder redirectTo = new StringBuilder();

		Authentication authentication = 
			new UsernamePasswordAuthenticationToken(
				requestDto.getUsername(),
				requestDto.getPassword());

		try {

			Authentication authenticated = authenticationManager.authenticate(authentication);

			// UserDetails userDetails =
			// userDetailService.loadUserByUsername(requestDto.getUsername());

			SecurityContextHolder.getContext().setAuthentication(authenticated);

		} catch (BadCredentialsException e) {

			redirectTo.append(request.getContextPath() + "/user/custom_login?error=1");

		}

		if (redirectTo.length() == 0) {
			redirectTo.append(request.getContextPath() + "/user/name");
		}

		response.sendRedirect(redirectTo.toString());

	}
	
	// Custom Authenticate method. Not used by login form served using above action.
	@RequestMapping(value = "/custom_login_authenticate", method = RequestMethod.POST, 
		consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AuthenticationResponseDto> authenticateJsonRequest(
			@RequestBody AuthenticationRequestDto requestDto) throws Exception {

		LOGGER.debug("in authenticateJsonRequest()  ---->>>>");

		AuthenticationResponseDto responseDto = null;

		Authentication authentication = new UsernamePasswordAuthenticationToken(requestDto.getUsername(),
				requestDto.getPassword());

		try {

			Authentication authenticated = authenticationManager.authenticate(authentication);

			SecurityContextHolder.getContext().setAuthentication(authenticated);

			// AppUserDetailsDto principal = (AppUserDetailsDto) authenticated.getPrincipal();

			responseDto = new AuthenticationResponseDto("success");

		} catch (BadCredentialsException e) {

			responseDto = new AuthenticationResponseDto("Incorrect username and/or password!");
			return new ResponseEntity<AuthenticationResponseDto>(responseDto, HttpStatus.BAD_REQUEST);

		}

		return new ResponseEntity<AuthenticationResponseDto>(responseDto, HttpStatus.OK);
	}
	
	// Common method to cater to logout request of type ajax & non ajax.
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logoutPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
		if("XMLHttpRequest".equals(ajaxHeader)) {

			response.setStatus(HttpStatus.OK.value());

		} else {

			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/user/custom_login?logout=1"));

		}

	}

	@RequestMapping(value = "/name", method = RequestMethod.GET)
	@ResponseBody
	public String name() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		return "Welcome " + currentPrincipalName;

	}

	@RequestMapping(value = "/name2", method = RequestMethod.GET)
	@ResponseBody
	public String name2(Authentication authentication) {

		return authentication.getName();

	}

	@RequestMapping(value = "/name3", method = RequestMethod.GET)
	@ResponseBody
	public String name3(HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();
		return principal.getName();

	}

	@RequestMapping(value = "/name4", method = RequestMethod.GET)
	@ResponseBody
	public String name4() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return userDetails.getUsername();

	}

}
