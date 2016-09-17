package dto;

import java.util.Map;

public class PaginationDTO {
	private int page;
	private int limit;
	private String sortBy;
	private boolean sortAsc;
	private Map<String, String> filters;
	
	public Map<String, String> getFilters() {
		return filters;
	}
	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public boolean isSortAsc() {
		return sortAsc;
	}
	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
