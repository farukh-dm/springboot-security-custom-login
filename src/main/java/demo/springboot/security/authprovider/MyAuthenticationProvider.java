package demo.springboot.security.authprovider;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import demo.springboot.security.User.MyAuthorityDto;
import demo.springboot.security.User.MyUserDto;

@Component(value = "myAuthenticationProvider")
public class MyAuthenticationProvider implements AuthenticationProvider   {

	@Override
	public Authentication authenticate(Authentication authentication) {
		
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		User user = null;
		MyUserDto myUserDto = null;
		ArrayList<MyAuthorityDto> authorities = new ArrayList<MyAuthorityDto>(1);
		
		if((username.equals("user") && password.equals("pass")) 
			|| (username.equals("admin") && password.equals("pass"))) {
			
			authorities.add(new MyAuthorityDto(username.equals("user") ? "USER" : "ADMIN"));
			
			user = new User(username, password, authorities);
			
			myUserDto = new MyUserDto(user, "CusomtAttribute1Value");
			
		} else {
			throw new BadCredentialsException("Invalid username and/or password.");
		}
		
		return new UsernamePasswordAuthenticationToken(myUserDto, password, authorities);
	
	}

	@Override
	public boolean supports(Class<?> authentication) {
		
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
