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
	private static String TOPIC_PAGE_URL = "_ui/core/chatter/topics/TopicPage?name="

	List<FeedItem> findFeedItems(url, token, topic, httpClient) {

		if (url == null || token == null || topic == null) {
			throw new IllegalArgumentException()
		}
		def request = createTopicIdHttpRequest(url,token,topic)
		HttpResponse response = httpClient.execute(request)
		if (response.getStatusLine().getStatusCode() != 201) {
			throw new HttpException("Failed to post message to chatter: " + response.getStatusLine().toString())
		}
		new ArrayList<FeedItem>()
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

	//add web url for topic page
}
