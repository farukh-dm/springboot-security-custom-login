package demo.springboot.security.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import demo.springboot.security.User.MyAuthorityDto;
import demo.springboot.security.User.MyUserDto;

@Service(value = "myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = null;
		MyUserDto myUserDto = null;
		
		// TODO: Do your validation. Make call to your DB to validate user
		// For now we're checking for USER OR ADMIN
		if(username.equals("user") || username.equals("admin")) {
			
			
			ArrayList<MyAuthorityDto> authorities = new ArrayList<MyAuthorityDto>(1);
			authorities.add(new MyAuthorityDto(username.equals("user") ? "USER" : "ADMIN"));
			
			// NOTE: Password should be set again correctly, so that it can be validated against 
			// Authentication object's input credentials. Here, send blank password = pass from UI to make it work.
			user = new User(username, "pass", authorities);
			
			myUserDto = new MyUserDto(user, "CusomtAttribute1Value");
			
		} else {
			
			throw new UsernameNotFoundException("Invalid username : " + username);
			
		}
		
		return myUserDto;
	
	}

}
