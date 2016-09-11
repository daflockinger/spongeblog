package dto;

public class OperationResult<T> {
	private T entity;
	private int status;
	
	public OperationResult(T entity, int status){
		this.entity = entity;
		this.status = status;
	}
	
	public OperationResult(int status){
		this.status = status;
		this.entity = null;
	}
	
	public T getEntity() {
		return entity;
	}
	public void setEntity(T entity) {
		this.entity = entity;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
