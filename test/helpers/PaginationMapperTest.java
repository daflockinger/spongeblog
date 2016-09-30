package helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import dto.PaginationDTO;

public class PaginationMapperTest {
	
	private PaginationMapper mapper;
	
	@Before
	public void setup(){
		mapper = new PaginationMapper();
	}
	
	
	@Test
	public void testMapParamsToPagination_withCorrectParams_shouldWork(){
		Map<String,String[]> testParams = new HashMap<>();
		
		testParams.put("limit", new String[]{"5"});
		testParams.put("page", new String[]{"1"});
		
		testParams.put("sort", new String[]{"name"});
		testParams.put("blub", new String[]{"bli"});
		testParams.put("oba", new String[]{"false"});
		testParams.put("numa", new String[]{"234"});
		testParams.put("goed", new String[]{"12.9"});
				
		PaginationDTO pagination = mapper.mapParamsToPagination(testParams);
		
		assertNotNull(pagination);
		assertTrue(pagination.getLimit() == 5);
		assertTrue(pagination.getPage() == 1);
		assertTrue(pagination.isSortAsc());
		assertEquals(pagination.getSortBy(),"name");
		
		Map<String,Object> filters = pagination.getFilters();
		
		assertNotNull(filters);
		assertTrue(filters.size() == 4);
		assertEquals(filters.get("blub"),"bli");
		assertEquals(filters.get("oba"),false);
		assertEquals(filters.get("numa"),234);
		assertEquals(filters.get("goed"),12.9f);
	}
	
	@Test
	public void testMapParamsToPagination_withOrderAscFalseParams_shouldReturnCorrect() throws Exception{
		Map<String,String[]> testParams = new HashMap<>();
		
		testParams.put("orderasc", new String[]{"false"});
		PaginationDTO pagination = mapper.mapParamsToPagination(testParams);
		
		assertNotNull(pagination);
		assertFalse(pagination.isSortAsc());
	}
	
	@Test
	public void testMapParamsToPagination_withWrongParams_shouldReturnCorrect() throws Exception{
		Map<String,String[]> testParams = new HashMap<>();
		
		testParams.put("limit", new String[]{"false"});
		testParams.put("page", new String[]{"s8df"});
		PaginationDTO pagination = mapper.mapParamsToPagination(testParams);
		
		assertNotNull(pagination);
		assertTrue(pagination.getLimit() == 0);
		assertTrue(pagination.getPage() == 0);
	}
	
	@Test
	public void testMapParamsToPagination_withNotExistingParams_shouldReturnCorrect() throws Exception{
		Map<String,String[]> testParams = new HashMap<>();
		
		testParams.put("lamit", new String[]{"false"});
		testParams.put("seitn", new String[]{"s8df"});
		PaginationDTO pagination = mapper.mapParamsToPagination(testParams);
		
		assertNotNull(pagination);
	}
	
	@Test
	public void testMapParamsToPagination_withNullParams_shouldReturnNull() throws Exception{
		Map<String,String[]> testParams = new HashMap<>();
		PaginationDTO pagination = mapper.mapParamsToPagination(testParams);
		
		assertNull(pagination);
	}
}
