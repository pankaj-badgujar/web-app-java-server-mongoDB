package com.example.wbdvextraassignmentpankajBadgujar.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.print.Doc;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;

@Service
public class SingleTableOperationsService {

	private String connectionString = "mongodb://webdev19:webdev19su19@ds311128.mlab.com:11128";
	private String dbName = "heroku_xvnbt5xs";

	private MongoClient mongoClient;

	public Document insertDataInCollection(String tableName, String jsonDataString) {
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

		if (!collectionExists(tableName)) {
			db.createCollection(tableName);
		}

		MongoCollection<Document> collection = db.getCollection(tableName);
		Document doc = Document.parse(jsonDataString);

		if (doc.containsKey("id")) {
			Object id = doc.get("id");
			doc.remove("id");
			doc.append("_id", id);
		}
		collection.insertOne(doc);
		return doc;
	}

	public List<Document> getAllCollections() {
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);
			
		
		MongoIterable<Document> allCollections = db.listCollections();

		
		List<Document> collectionList = new ArrayList<Document>();
		String schema = "";
		for (Document collection : allCollections) {
			
			for(String key :  collection.keySet()) {
				schema.concat(key + " "+collection.get(key));
			}
			collectionList.add(collection);
		}

		return collectionList;
	}

	public List<Document> getAllDocsOfCollection(String collectionName) {
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

		if (!collectionExists(collectionName)) {
			return null;
		}

		MongoCollection<Document> collection = db.getCollection(collectionName);
		MongoIterable<Document> data = collection.find();
		List<Document> docList = new ArrayList<Document>();
		for (Document doc : data) {
			docList.add(doc);
		}
		return docList;
	}

	public Document getDocById(String collectionName, int id) {
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

		if (!collectionExists(collectionName)) {
			return null;
		}

		MongoCollection<Document> collection = db.getCollection(collectionName);

		BasicDBObject whereQuery = new BasicDBObject();
	    whereQuery.put("_id", id);
		
		MongoIterable<Document> data = collection.find(whereQuery);
		return data.first();
	}
	
	public Document updateDocById(String collectionName, int id, String jsonString) {
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

		if (!collectionExists(collectionName)) {
			return null;
		}
		
		BasicDBObject whereQuery = new BasicDBObject();
	    whereQuery.put("_id", id);
		

		MongoCollection<Document> collection = db.getCollection(collectionName);
		Document replacement = Document.parse(jsonString);
		collection.replaceOne(whereQuery, replacement);

		return replacement;
	}
	
	public DeleteResult deleteDocById(String collectionName, int id) {
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

		if (!collectionExists(collectionName)) {
			return null;
		}
		
		BasicDBObject whereQuery = new BasicDBObject();
	    whereQuery.put("_id", id);
	    
		MongoCollection<Document> collection = db.getCollection(collectionName);
		return collection.deleteOne(whereQuery);
	
	}
	
	public void truncate(String collectionName) {
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

	    
		MongoCollection<Document> collection = db.getCollection(collectionName);
		collection.drop();
		db.createCollection(collectionName);
	}

	
	
	
	private boolean collectionExists(String collectionName) {
		return mongoClient.getDatabase(dbName).listCollectionNames().into(new ArrayList<String>())
				.contains(collectionName);
	}
	
}
