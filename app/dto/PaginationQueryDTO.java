package dto;

public class PaginationQueryDTO<T> extends PaginationDTO{
	private Class<T> modelType;

	public PaginationQueryDTO(PaginationDTO paginationDto, Class<T> modelType){
		setFilters(paginationDto.getFilters());
		setLimit(paginationDto.getLimit());
		setPage(paginationDto.getPage());
		setSortAsc(paginationDto.isSortAsc());
		setSortBy(paginationDto.getSortBy());
		this.modelType = modelType;
	}
	
	public Class<T> getModelType() {
		return modelType;
	}
	public void setModelType(Class<T> modelType) {
		this.modelType = modelType;
	}
}
