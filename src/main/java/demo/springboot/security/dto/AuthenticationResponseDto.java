package demo.springboot.security.dto;

public class AuthenticationResponseDto {
	
	private String message;
	
	public AuthenticationResponseDto() {
		super();
	}

	public AuthenticationResponseDto(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
