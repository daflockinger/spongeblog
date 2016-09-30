package services;

import java.util.List;

import dto.PaginationDTO;

public interface PaginationService<M> extends BaseService<M>{
	List<M> findAllInPage(PaginationDTO settings);
	boolean hasPreviousPage(PaginationDTO settings);
}
