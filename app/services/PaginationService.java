package services;

import java.util.List;

import dto.PaginationDTO;

public interface PaginationService<M> extends BaseService<M>{
	public List<M> findAllInPage(PaginationDTO settings);
}
