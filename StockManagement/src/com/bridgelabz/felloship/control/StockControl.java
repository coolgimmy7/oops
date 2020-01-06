package com.bridgelabz.felloship.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bridgelabz.felloship.model.StockUser;
import com.bridgelabz.felloship.model.TransactionLog;
import com.bridgelabz.felloship.model.stockmodel;
import com.bridgelabz.felloship.operation.StockOperations;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StockControl {
	static ObjectMapper mapper = new ObjectMapper();
	List<StockUser> user = new ArrayList<StockUser>();

	public static void main(String[] args) {

	}
	// transaction log read

	public static List<TransactionLog> readlog() throws JsonParseException, JsonMappingException, IOException {
		String userpath = "/home/admin1/Desktop/JavaProject/StockManagement/src/com/bridgelabz/felloship/log.json";
		List<TransactionLog> users = mapper.readValue(new File(userpath), new TypeReference<List<TransactionLog>>() {
		});
		return users;
	}

	// transactions log write
	public static void writelog(List<TransactionLog> logentry)
			throws JsonGenerationException, JsonMappingException, IOException {
		String userpath = "/home/admin1/Desktop/JavaProject/StockManagement/src/com/bridgelabz/felloship/log.json";
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(userpath), logentry);
	}

	// read users
	public static List<StockUser> readusers() throws JsonParseException, JsonMappingException, IOException {
		String userpath = "/home/admin1/Desktop/JavaProject/StockManagement/src/com/bridgelabz/felloship/users.json";
		List<StockUser> users = mapper.readValue(new File(userpath), new TypeReference<List<StockUser>>() {
		});
		return users;
	}

	// writes users
	public static void writeusers(List<StockUser> newentry)
			throws JsonGenerationException, JsonMappingException, IOException {
		String userpath = "/home/admin1/Desktop/JavaProject/StockManagement/src/com/bridgelabz/felloship/users.json";
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(userpath), newentry);
	}

	// red stock info
	public static List<stockmodel> readStock(String spath)
			throws JsonParseException, JsonMappingException, IOException {
		List<stockmodel> list = mapper.readValue(new File(spath), new TypeReference<List<stockmodel>>() {
		});
		return list;
	}

	// write stock info
	public static void writeStock(List<stockmodel> list)
			throws JsonGenerationException, JsonMappingException, IOException {
		String spath = "/home/admin1/Desktop/JavaProject/StockManagement/src/com/bridgelabz/felloship/stockinventory.json";
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(spath), list);
	}
}