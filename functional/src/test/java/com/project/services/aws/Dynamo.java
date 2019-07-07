package com.project.services.aws;

import org.testng.Reporter;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.project.core.AppLib;

public class Dynamo {
	protected String environment = ""; 
	private Regions clientRegion = Regions.US_WEST_2;
	private AmazonDynamoDB dynamoDBClient = null;
	private DynamoDB dynamoDB = null;
	private String tableName = "sandbox.demo";
	private static String PRIMARYKEY = "ID";
	
	public Dynamo(String env) {
		environment = env;
		if (!AppLib.allowableEnvs().contains(environment)) {
			throw new RuntimeException(
					"Environment specified [ " + environment + "] not supported as part of allowable environments");
		}
		connectToService();
		/* If table doesn't exist then create it, generally just the first time. */
		if (!doesTableExist(tableName)) {
			Reporter.log("Table: " + tableName + " does not exist");
			throw new RuntimeException("Table: " + tableName + " does not exist");
		}
	}
	
	private void connectToService() {
		AWSCredentialsProvider awsCredentialsProvider;
		
		if (environment.equals(AppLib.ENV_PROD)) {
			awsCredentialsProvider = new ProfileCredentialsProvider("prodprofile");
		} else if (environment.equals(AppLib.ENV_DEV)) {
			awsCredentialsProvider = new ProfileCredentialsProvider("devprofile");
		} else if (environment.equals(AppLib.ENV_QA)) {
			awsCredentialsProvider = new ProfileCredentialsProvider("qaprofile");
		} else if (environment.equals(AppLib.ENV_LOCAL)) {
			awsCredentialsProvider = new ProfileCredentialsProvider("localprofile");
		} else {
			awsCredentialsProvider = null;
			throw new RuntimeException("Environment specified: " + environment + " does not have a profile associated with it");
		}
		
		dynamoDBClient = AmazonDynamoDBClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(clientRegion).build();
		dynamoDB = new DynamoDB(dynamoDBClient);
		dynamoDB.getTable(tableName);
	}
	
	
	private boolean doesTableExist(String tableName) {
		boolean isTableFound = false;
		TableCollection<ListTablesResult> tableList = dynamoDB.listTables();
		for (Table table : tableList) {
			if (table.getTableName().equals(tableName)) {
				try {
					table.waitForActive();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				isTableFound = true;
				return isTableFound;
			}
		}

		return isTableFound;
	}
	
	public boolean doesIdExistInDB(String id) {
		boolean idExists = false;

		if (doesTableExist(tableName)) {
			Table table = dynamoDB.getTable(tableName);
			Item item = new Item().withPrimaryKey(PRIMARYKEY, id);

			item = table.getItem(PRIMARYKEY, id);
			if (item != null) {
				idExists = true;
			}
		} else {
			Reporter.log("ERROR: Can't find table name specified - " + tableName);
		}
		
		return idExists;
	}
}
