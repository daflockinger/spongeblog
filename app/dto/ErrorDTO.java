package dto;

public class ErrorDTO {
	private String errorMessage;
	private String sentMessage;
	
	public ErrorDTO(String errorMessage, String sentMessage){
		this.errorMessage = errorMessage;
		this.sentMessage = sentMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getSentMessage() {
		return sentMessage;
	}
	public void setSentMessage(String sentMessage) {
		this.sentMessage = sentMessage;
	}
	
	
}
