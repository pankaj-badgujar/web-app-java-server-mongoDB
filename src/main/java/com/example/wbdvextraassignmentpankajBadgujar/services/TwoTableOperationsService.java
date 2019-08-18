package com.example.wbdvextraassignmentpankajBadgujar.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

@Service
public class TwoTableOperationsService {

	private String connectionString = "mongodb://webdev19:webdev19@ds311128.mlab.com:11128";
	private String dbName = "heroku_xvnbt5xs";

	private MongoClient mongoClient;

	@Autowired
	SingleTableOperationsService stoService;

	public Document createMappingTable(String table1, String table2, int id1, int id2) {

		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

		if (!collectionExists(table1) || !collectionExists(table2)) {
			return null;
		}
		MongoCollection<Document> collection;
		if (collectionExists(table1 + "_" + table2)) {
			collection = db.getCollection(table1 + "_" + table2);
		} else if (collectionExists(table2 + "_" + table1)) {
			collection = db.getCollection(table2 + "_" + table1);
		} else {
			db.createCollection(table1 + "_" + table2);
			collection = db.getCollection(table1 + "_" + table2);
		}
		Document doc1 = stoService.getDocById(table1, id1);
		Document doc2 = stoService.getDocById(table2, id2);
		Document document = new Document(table1, doc1.get("_id"));
		document.append(table2, doc2.get("_id"));
		collection.insertOne(document);
		return document;
	}

	private boolean collectionExists(String collectionName) {
		return mongoClient.getDatabase(dbName).listCollectionNames().into(new ArrayList<String>())
				.contains(collectionName);
	}

	public List<Document> getDocumentsFromTable2ByTable1Id(String table1, int id, String table2) {
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

		if (!collectionExists(table1) || !collectionExists(table2)) {
			return null;
		}
		MongoCollection<Document> collection;
		if (collectionExists(table1 + "_" + table2)) {
			collection = db.getCollection(table1 + "_" + table2);
		} else if (collectionExists(table2 + "_" + table1)) {
			collection = db.getCollection(table2 + "_" + table1);
		} else {
			return null;
		}

		MongoCollection<Document> targetCollection = db.getCollection(table2);
		MongoIterable<Document> records = collection.find(Filters.eq(table1, id));
		List<Document> targetDocs = new ArrayList<Document>();
		for (Document eachRecord : records) {			
			targetDocs.add(targetCollection.find(Filters.eq("_id", eachRecord.get(table2))).first());
		}

		return targetDocs;

	}

	public DeleteResult deleteRecordFromMappingTableByBothIds(String table1, String table2, int id1, int id2) {
		
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

		if (!collectionExists(table1) || !collectionExists(table2)) {
			return null;
		}
		MongoCollection<Document> collection;
		if (collectionExists(table1 + "_" + table2)) {
			collection = db.getCollection(table1 + "_" + table2);
		} else if (collectionExists(table2 + "_" + table1)) {
			collection = db.getCollection(table2 + "_" + table1);
		} else {
			return null;
		}
		return collection.deleteOne(Filters.and(Filters.eq(table1, id1),Filters.eq(table2, id2)));
	
	}

	public DeleteResult deleteRecordFromMappingTableByOneId(String table1, String table2, int id1) {
		mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = mongoClient.getDatabase(dbName);

		if (!collectionExists(table1) || !collectionExists(table2)) {
			return null;
		}
		MongoCollection<Document> collection;
		if (collectionExists(table1 + "_" + table2)) {
			collection = db.getCollection(table1 + "_" + table2);
		} else if (collectionExists(table2 + "_" + table1)) {
			collection = db.getCollection(table2 + "_" + table1);
		} else {
			return null;
		}
		return collection.deleteMany(Filters.eq(table1, id1));		
	}

}
