package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.query.Query;
import org.springframework.util.CollectionUtils;

import dto.PaginationQueryDTO;

public abstract class PaginationDAO<T, K> extends ExtendedDAO<T, K> {
	
	public List<T> findAllInPage(PaginationQueryDTO<T> paginationQuery) {
		if (paginationQuery == null)
			return new ArrayList<>();

		int offset = paginationQuery.getLimit() * paginationQuery.getPage();
		String orderDirection = paginationQuery.isSortAsc() ? "" : "-";
		Query<T> pagination = filterPagination(paginationQuery.getFilters(),
				getDatastore().createQuery(paginationQuery.getModelType()));

		return pagination.limit(paginationQuery.getLimit()).offset(offset)
				.order(orderDirection + paginationQuery.getSortBy()).asList();
	}

	public boolean hasPreviousPage(PaginationQueryDTO<T> paginationQuery){
		if (paginationQuery == null)
			return false;
		
		int offset = paginationQuery.getLimit() * (paginationQuery.getPage() + 1);
		Query<T> pagination = filterPagination(paginationQuery.getFilters(),
				getDatastore().createQuery(paginationQuery.getModelType()));

		return !CollectionUtils.isEmpty(pagination.limit(paginationQuery.getLimit()).offset(offset).asList());
	}
	
	private Query<T> filterPagination(Map<String, Object> filters, Query<T> pagination) {
		if (filters != null) {
			for (String key : filters.keySet()) {
				pagination = pagination.filter(key, filters.get(key));
			}
		}
		return pagination;
	}
}
