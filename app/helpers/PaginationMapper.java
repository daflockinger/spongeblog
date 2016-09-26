package helpers;

import static org.apache.commons.lang3.BooleanUtils.toBooleanDefaultIfNull;
import static org.apache.commons.lang3.BooleanUtils.toBooleanObject;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Map;
import java.util.stream.Collectors;

import dto.PaginationDTO;

public class PaginationMapper {
	private final static String PARAM_PAGE = "page";
	private final static String PARAM_LIMIT = "limit";
	private final static String PARAM_SORTBY = "sort";
	private final static String PARAM_SORTASC = "orderasc";
	
	public PaginationDTO mapParamsToPagination(Map<String, String[]> params) {
		PaginationDTO pagination = null;
		
		if (!isEmpty(params)) {
			Map<String,String> simpleParams = params.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, e -> firstNonNull(e.getValue())) );
			
			pagination = new PaginationDTO();
			pagination.setLimit(toInt(simpleParams.remove(PARAM_LIMIT)));
			pagination.setPage(toInt(simpleParams.remove(PARAM_PAGE)));
			pagination.setSortBy(simpleParams.remove(PARAM_SORTBY));
			pagination.setSortAsc(
					toBooleanDefaultIfNull(toBooleanObject(simpleParams.remove(PARAM_SORTASC)), true));
			pagination.setFilters(simpleParams);
		}
		return pagination;
	}
}
