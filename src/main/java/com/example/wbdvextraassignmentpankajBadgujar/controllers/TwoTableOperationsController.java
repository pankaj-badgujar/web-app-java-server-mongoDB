package com.example.wbdvextraassignmentpankajBadgujar.controllers;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wbdvextraassignmentpankajBadgujar.services.TwoTableOperationsService;
import com.mongodb.client.result.DeleteResult;

@RestController
public class TwoTableOperationsController {
	
	@Autowired
	TwoTableOperationsService ttoService;
	
	@PostMapping("/api/{table1}/{id1}/{table2}/{id2}")
	public Document createMappingTable(@PathVariable("table1") String table1, @PathVariable("id1") int id1,
			@PathVariable("table2") String table2, @PathVariable("id2") int id2) {
		return ttoService.createMappingTable(table1, table2, id1, id2);
	}
	
	@GetMapping("/api/{table1}/{id}/{table2}")
	public List<Document> getDocumentsFromTable2ByTable1Id(@PathVariable("table1") String table1, 
			@PathVariable("id") int id, @PathVariable("table2") String table2) {
		return ttoService.getDocumentsFromTable2ByTable1Id(table1,id,table2);
		
	}
	
	@DeleteMapping("/api/{table1}/{id1}/{table2}/{id2}")
	public DeleteResult deleteRecordFromMappingTableByBothIds(@PathVariable("table1") String table1,
			@PathVariable("table2") String table2, @PathVariable("id1")int id1, @PathVariable("id2")int id2) {
		return ttoService.deleteRecordFromMappingTableByBothIds(table1,table2,id1,id2);
	}
	
	@DeleteMapping("/api/{table1}/{id1}/{table2}")
	public DeleteResult deleteRecordFromMappingTableByOneId(@PathVariable("table1") String table1,
			@PathVariable("table2") String table2, @PathVariable("id1")int id1) {
		return ttoService.deleteRecordFromMappingTableByOneId(table1,table2, id1);
	}
	
}
