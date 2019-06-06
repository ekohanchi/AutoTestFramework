package com.project.services.api;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiMethodCalls {
	private static void checkApiResponseIsOK(HttpResponse response) {
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode() + " - "
					+ response.getStatusLine().getReasonPhrase());
		}
	}

	public static JsonElement postCall(String endpoint, JsonObject reqBody, Header[] headers) {
		JsonElement jsonResponse = null;

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost request = new HttpPost(endpoint);
			StringEntity requestBody = new StringEntity(reqBody.toString());
			request.setHeaders(headers);
			//request.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			request.setEntity(requestBody);
			HttpResponse response = httpClient.execute(request);
			checkApiResponseIsOK(response);
			JsonParser parser = new JsonParser();
			jsonResponse = parser.parse(EntityUtils.toString(response.getEntity()));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jsonResponse;
	}

	public static JsonElement getCall(String endpoint) {
		JsonElement jsonResponse = null;

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpGet request = new HttpGet(endpoint);
			HttpResponse response = httpClient.execute(request);
			checkApiResponseIsOK(response);
			JsonParser parser = new JsonParser();
			jsonResponse = parser.parse(EntityUtils.toString(response.getEntity()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}

	public static JsonElement getCall(String endpoint, Header[] headers) {
		JsonElement jsonResponse = null;

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpGet request = new HttpGet(endpoint);
			request.setHeaders(headers);
			HttpResponse response = httpClient.execute(request);
			checkApiResponseIsOK(response);
			JsonParser parser = new JsonParser();
			jsonResponse = parser.parse(EntityUtils.toString(response.getEntity()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}

	public static JsonElement putCall(String endpoint, JsonObject reqBody, Header[] headers) {
		JsonElement jsonResponse = null;

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPut request = new HttpPut(endpoint);
			StringEntity requestBody = new StringEntity(reqBody.toString());
			request.setHeaders(headers);
			request.setEntity(requestBody);
			HttpResponse response = httpClient.execute(request);
			checkApiResponseIsOK(response);
			JsonParser parser = new JsonParser();
			jsonResponse = parser.parse(EntityUtils.toString(response.getEntity()));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jsonResponse;
	}

}
