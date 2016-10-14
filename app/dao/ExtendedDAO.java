package dao;

import org.mongodb.morphia.dao.BasicDAO;

public abstract class ExtendedDAO<T, K> extends BasicDAO<T, K> {

	protected ExtendedDAO() {
		super(DataStoreFactory.get());
	}
}
