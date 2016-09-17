package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import dto.PaginationQueryDTO;

public abstract class ExtendedDAO<T, K> extends BasicDAO<T, K> {

	protected ExtendedDAO() {
		super(DataStoreFactory.get());

		// TODO do i need/want that?!
		// ValidationExtension validation = new
		// ValidationExtension(DataStoreFactory.getMorphia());
	}

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

	private Query<T> filterPagination(Map<String, String> filters, Query<T> pagination) {
		if (filters != null) {
			for (String key : filters.keySet()) {
				pagination = pagination.filter(key, filters.get(key));
			}
		}
		return pagination;
	}
}
