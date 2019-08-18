package com.example.wbdvextraassignmentpankajBadgujar.controllers;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.wbdvextraassignmentpankajBadgujar.services.SingleTableOperationsService;
import com.mongodb.client.result.DeleteResult;

@RestController
public class SingleTableOperationsController {

	@Autowired
	private SingleTableOperationsService stoService;

	@PostMapping("/api/{table}")
	public Document insertDataInCollection(@PathVariable("table") String tableName,
			@RequestBody String jsonDataString) {

		return stoService.insertDataInCollection(tableName, jsonDataString);
	}

	@GetMapping("/api")
	public List<Document> getAllCollections() {
		return stoService.getAllCollections();
	}

	@GetMapping("/api/{table}")
	public List<Document> getAllDocsOfCollection(@PathVariable("table") String tableName) {
		return stoService.getAllDocsOfCollection(tableName);
	}

	@GetMapping("/api/{table}/{id}")
	public Document getDocById(@PathVariable("table") String tableName, @PathVariable("id") int id) {
		return stoService.getDocById(tableName, id);
	}

	@PutMapping("/api/{table}/{id}")
	public Document updateDocById(@PathVariable("table") String tableName, @PathVariable("id") int id,
			@RequestBody String jsonString) {
		return stoService.updateDocById(tableName, id, jsonString);
	}

	@DeleteMapping("/api/{table}/{id}")
	public DeleteResult deleteDocById(@PathVariable("table") String tableName, @PathVariable int id) {
		return stoService.deleteDocById(tableName, id);
	}

	@DeleteMapping("/api/{table}")
	public String truncateTable(@PathVariable("table") String tableName) {
		stoService.truncate(tableName);
		return tableName + " truncated";
	}

}
