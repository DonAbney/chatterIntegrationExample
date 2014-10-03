package com.pillartechnology.bootcamp.salesforce
import groovy.json.JsonBuilder

import org.apache.http.HttpException
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicHeader


class FeedItemPosterImpl implements FeedItemPoster {

	static String CHATTER_GROUP = "Playbook Feedback"
	static final String CHATTER_ELEMENT_TYPE = "FeedItem"
	static final String CHATTER_POST_FEED_ITEM_URL = "/services/data/v31.0/chatter/feed-elements"


	void postFeedItem(String url, String token, String feedback, List<String> topics, HttpClient httpClient, String thirdPartyUrl) {
		if (anyParamsNull(url, token, feedback, topics, httpClient, thirdPartyUrl)) {
			throw new IllegalArgumentException()
		}
		def request = createFeedRequest(url, token, feedback, topics, thirdPartyUrl)
		HttpResponse response = httpClient.execute(request)
		if (response.getStatusLine().getStatusCode() != 201) {
			throw new HttpException("Failed to post message to chatter: " + response.getStatusLine().toString())
		}
	}
	
	boolean anyParamsNull(String url, String token, String feedback, List<String> topics, HttpClient httpClient, String thirdPartyUrl) {
		url == null || token == null || feedback == null || topics == null || httpClient == null || thirdPartyUrl == null
	}

	HttpUriRequest createFeedRequest(String url, String token, String feedback, List<String> topics, String thirdPartyUrl) {
		String topicList = buildTopicListAsString(topics)
		def output = buildMessage(feedback, topicList, thirdPartyUrl)
		buildRequest(url, token, output)
	}

	String buildTopicListAsString(topicArray) {
		String topicList = ""
		topicArray.each() { topic -> topicList <<= "#[${topic}] " }
		topicList
	}

	JsonBuilder buildMessage(String feedback, String topicList, String thirdPartyUrl) {
		def output = new JsonBuilder()
		output {
			body {
				messageSegments([
					[type: "Text", text: "${feedback} ${topicList}"]
				])
			}
			attachment([attachmentType: "Link", url: "${thirdPartyUrl}", urlName: "${thirdPartyUrl}"])
			feedElementType(CHATTER_ELEMENT_TYPE)
			subjectId(CHATTER_GROUP)
		}
	}

	HttpUriRequest buildRequest(String url, String token, JsonBuilder output) {
		def request = RequestBuilder.post()
				.addHeader(new BasicHeader("Authorization", "Bearer ${token}"))
				.addHeader("Content-Type", "application/json")
				.setUri("${url}${CHATTER_POST_FEED_ITEM_URL}")
				.setEntity(new StringEntity(output.toPrettyString()))
				.build()
	}
}
