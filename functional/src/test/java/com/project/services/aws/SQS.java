package com.project.services.aws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.project.core.AppLib;
import com.project.core.ReporterPlus;

public class SQS {
	protected String environment = "";
	Regions clientRegion = Regions.US_WEST_2;
	private AmazonSQS sqs = null;

	public SQS(String env) {
		environment = env;
		connectToService();
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
			throw new RuntimeException(
					"Environment specified: " + environment + " does not have a profile associated with it");
		}

		sqs = AmazonSQSClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(clientRegion)
				.build();
	}

	public void getQueueList() {
		ListQueuesResult lq_result = sqs.listQueues();
		ReporterPlus.log("The list of SQS Queue URLs are: ");
		for (String url : lq_result.getQueueUrls()) {
			ReporterPlus.log(url);
		}
	}

	public String getUrlForQueue(String queueName) {
		String queue_url = sqs.getQueueUrl(queueName).getQueueUrl();
		return queue_url;
	}

	public void sendMessageRequest(String queueUrl, String messageBody, int delaySeconds) {
		Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
		messageAttributes.put("AttributeOne",
				new MessageAttributeValue().withStringValue("AutoTests for Tech Project").withDataType("String"));

		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl)
				.withMessageBody(messageBody).withDelaySeconds(delaySeconds).withMessageAttributes(messageAttributes);
		sqs.sendMessage(sendMessageRequest);
	}

	public List<Message> getMessage(String queueUrl) {
		List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
		return messages;

	}

}
