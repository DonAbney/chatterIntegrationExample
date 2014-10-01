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
	
	
	void postFeedItem(String url, String token, String feedback, List<String> topics,	HttpClient httpClient, String thirdPartyUrl) {
		if (url == null || token == null || feedback == null || topics == null || httpClient == null || thirdPartyUrl == null) {
			throw new IllegalArgumentException()
		}
		def request = createFeedRequest(url, token, feedback, topics, thirdPartyUrl)
		HttpResponse response = httpClient.execute(request) 
		if (response.getStatusLine().getStatusCode() != 201) {
			throw new HttpException("Failed to post message to chatter: " + response.getStatusLine().toString())
		}
	}

	HttpUriRequest createFeedRequest(String url, String token, String feedback, List<String> topics, String thirdPartyUrl) {
		String topicList = ""
		topics.each() { topic -> topicList <<= "#${topic} " }
		def output = new JsonBuilder()
		output {
			body {
				messageSegments([[type: "Text", text: "${feedback} ${topicList}"]])
			}
			attachment([attachmentType: "Link", url: "${thirdPartyUrl}", urlName: "${thirdPartyUrl}"])
			feedElementType(CHATTER_ELEMENT_TYPE)
			subjectId(CHATTER_GROUP)
		}

		def request = RequestBuilder.post()
				.addHeader(new BasicHeader("Authorization", "Bearer ${token}"))
				.addHeader("Content-Type", "application/json")
				.setUri("${url}/services/data/v31.0/chatter/feed-elements")
				.setEntity(new StringEntity(output.toPrettyString()))
				.build()
	}
}
