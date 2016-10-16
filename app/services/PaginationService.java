package services;

import java.util.List;

import dto.PaginationDTO;

public interface PaginationService<M,N> extends BaseService<M,N>{
	List<M> findAllInPage(PaginationDTO settings);
	boolean hasPreviousPage(PaginationDTO settings);
}
