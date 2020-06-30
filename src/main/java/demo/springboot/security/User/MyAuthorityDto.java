package demo.springboot.security.User;

import org.springframework.security.core.GrantedAuthority;

public class MyAuthorityDto implements GrantedAuthority {
	
	private static final long serialVersionUID = -3275888734587677127L;
	
	private String authority;
	
	public MyAuthorityDto() {
		super();
	}
	
	public MyAuthorityDto(String authority) {
		super();
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
}
