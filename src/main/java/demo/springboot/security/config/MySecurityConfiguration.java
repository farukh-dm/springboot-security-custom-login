package demo.springboot.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import demo.springboot.security.authprovider.MyAuthenticationProvider;
import demo.springboot.security.service.MyUserDetailsService;

@EnableWebSecurity
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	MyUserDetailsService myUserDetailsService;
	
	@Autowired
	MyAuthenticationProvider myAuthenticationProvider;
	
	/*
	 * @Override public void configure(WebSecurity web) throws Exception {
	 * web.ignoring().antMatchers("/resources/**"); }
	 */
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//auth.userDetailsService(myUserDetailsService);
		
		// OR
		
		auth.authenticationProvider(myAuthenticationProvider);
		
		// OR
		
		/*
		 * auth.inMemoryAuthentication()
		 * .withUser("user").password("user").roles("USER") .and()
		 * .withUser("admin").password("admin").roles("USER", "ADMIN");
		 */
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();		

		http.authorizeRequests()
		.antMatchers("/user/custom_login").permitAll()
		.antMatchers("/user/custom_login_authenticate").permitAll()
		.antMatchers("/custom_logout").permitAll()
		.anyRequest().authenticated()
		.and().formLogin()
		.loginPage("/user/login").permitAll()
		.defaultSuccessUrl("/home");
		
		/*
		 * http.authorizeRequests() .antMatchers("/").permitAll()
		 * .antMatchers("/welcome").hasAnyRole("USER", "ADMIN")
		 * .anyRequest().authenticated()
		 * .and().formLogin().loginPage("/user/login").permitAll()
		 * .and().logout().permitAll();
		 */
		
	}
	
	// Something spring security expects, to decode password provided as input & then verify
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	
	
}
