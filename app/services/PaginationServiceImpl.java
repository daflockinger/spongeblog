package services;

import static dto.RestError.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.google.common.collect.ImmutableList;

import dao.ExtendedDAO;
import dao.PaginationDAO;
import dto.BaseDTO;
import dto.PaginationDTO;
import dto.PaginationQueryDTO;
import model.BaseModel;

public abstract class PaginationServiceImpl<M extends BaseDTO,N extends BaseModel, T extends PaginationDAO<N, ObjectId>>
		extends BaseServiceImpl<N,M, T> implements PaginationService<M,N>{
	public List<M> findAllInPage(PaginationDTO settings) {
		List<N> models = new ArrayList<>();
		models = dao().findAllInPage(new PaginationQueryDTO<N>(settings, getModelClass()));

		if (models == null) {
			return ImmutableList.of(errorModel(NOT_FOUND));
		}
		return models.stream().map(model -> mapper().map(model, getDTOClass())).collect(Collectors.toList());
	}
	
	public boolean hasPreviousPage(PaginationDTO settings){
		return dao().hasPreviousPage(new PaginationQueryDTO<N>(settings, getModelClass()));
	}
}
