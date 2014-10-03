package com.pillartechnology.bootcamp.salesforce

import groovy.json.JsonSlurper

import org.apache.http.HttpException
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder


class FeedItemRetrieverImpl implements FeedItemRetriever {

	private static String TOPIC_ID_URI = "/services/data/v31.0/connect/topics?exactMatch=true&q="
	private static String TOPIC_MESSAGES_URI_PREFIX = "/services/data/v31.0/chatter/feeds/topics/"
	private static String TOPIC_MESSAGES_URI_SUFFIX = "/feed-items"

	//add web url for topic page to third party page to link back to sales force using TOPIC_PAGE_URL
	private static String TOPIC_PAGE_URL = "_ui/core/chatter/topics/TopicPage?name="

	List<FeedItem> findFeedItems(url, token, topic, httpClient) {

		if (anyParamsNull(url, token, topic, httpClient)) {
			throw new IllegalArgumentException()
		}
		parseMessagesFromResponse(requestMessages(url,token,requestTopicId(url, token, topic, httpClient), httpClient))
	}

	boolean anyParamsNull(url, token, topic, httpClient) {
		url == null || token == null || topic == null
	}
	
	String requestTopicId(url, token, topic, httpClient) {
		def topicRequest = createTopicIdHttpRequest(url,token,topic)
		
		HttpResponse response = httpClient.execute(topicRequest)
		throwHttpExceptionOnBadStatusValue(response)
		parseTopicIdFromResponse(response)

	}
	
	HttpResponse requestMessages(url, token, topicId, httpClient) {
		def messageRequest = createMessagesFromTopicIdHttpRequest(url, token, topicId)

		HttpResponse response = httpClient.execute(messageRequest)
		throwHttpExceptionOnBadStatusValue(response)
		response		
	}
	
	void throwHttpExceptionOnBadStatusValue(response) {
		if (response.getStatusLine().getStatusCode() != 201) {
			throw new HttpException("Failed to post message to chatter: " + response.getStatusLine().toString())
		}
	}

	protected HttpUriRequest createTopicIdHttpRequest(url, token, topic) {
		RequestBuilder.get().setUri(url + TOPIC_ID_URI + topic).build()
	}

	protected String parseTopicIdFromResponse(HttpResponse response) {
		def topics = new JsonSlurper().parseText(response.entity.content.text).get("topics")
		if (!topics.isEmpty()) {
			topics[0].get("id")
		}
		else {
			null
		}
	}

	protected HttpUriRequest createMessagesFromTopicIdHttpRequest(url, token, topicId) {
		RequestBuilder.get().setUri(url + TOPIC_MESSAGES_URI_PREFIX + topicId + TOPIC_MESSAGES_URI_SUFFIX).build()
	}

	protected List<FeedItem> parseMessagesFromResponse(HttpResponse response) {
		List<FeedItem> feedItems = new ArrayList<FeedItem>()
		def topics = new JsonSlurper().parseText(response.entity.content.text).get("items").each({ item ->
			FeedItem feedItem = new FeedItem()
			feedItem.author = item.get("actor").get("name")
			feedItem.body = item.get("body").get("messageSegments")[0].get("text")
			feedItems.add(feedItem)
		})
		feedItems
	}
}
