package dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class DataStoreFactory {
	private static volatile Datastore dataStore = null;
	private static volatile Morphia morphia = null;

	private DataStoreFactory() {
	}

	public static synchronized Datastore get() {

		if (dataStore == null) {
			morphia = new Morphia();
			new ValidationExtension(morphia);
			Config config = ConfigFactory.defaultApplication();
			MongoClient client = new MongoClient(new MongoClientURI(config.getString("morphia.db.uri")));;
			dataStore = morphia.createDatastore(client, config.getString("morphia.db.name"));
		}
		return dataStore;
	}

	public static Morphia getMorphia() {
		return morphia;
	}
}
