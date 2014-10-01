package com.pillartechnology.bootcamp.salesforce

import org.apache.http.HttpResponse
import org.apache.http.HttpVersion
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicHttpResponse
import org.apache.http.message.BasicStatusLine

class FeedItemRetrieverTest extends GroovyTestCase {

	private FeedItemRetrieverImpl feedItemRetriever;

	void initFeedItemRetriever() {
		feedItemRetriever = new FeedItemRetrieverImpl()
	}

	void testFindFeedItemsDoesNotFail() {
		initFeedItemRetriever()
		List<FeedItem> feedItems = feedItemRetriever.findFeedItems("url", "token", "topic")
		assertNotNull(feedItems)
	}

	void testFindFeedItemsFailsWithNullUrl() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems(null, "token", "topic")
		}
	}

	void testFindFeedItemsFailsWithNullToken() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems("url", null, "topic")
		}
	}

	void testFindFeedItemsFailsWithNullTopic() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems("url", "token", null)
		}
	}

	void testCreateTopicIdHttpRequestWithCorrectTopic() {
		initFeedItemRetriever()
		HttpUriRequest request = feedItemRetriever.createTopicIdHttpRequest("testUrl", "token", "topic")
		
		assertEquals("testUrl/services/data/v31.0/connect/topics?exactMatch=true&q=topic", request.getURI().toString());
	}
	
	void testParseTopicIdFromTopicIdResponse() {
		initFeedItemRetriever()
		StringEntity respBody = new StringEntity(
		"{ " +
			"\"currentPageUrl\": null," +
			"\"nextPageUrl\": null," +
			"\"topics\": [{ " +
		    "\"createdDate\": \"2014-08-21T18:29:53.000Z\"," +
	        "\"description\": null," +
            "\"id\": \"targetTopicId\"," +
			"\"name\": \"Holler\"," +
			"\"talkingAbout\": 1," +
			"\"url\": \"/services/data/v31.0/connect/topics/0TOo00000008QB7GAM\"" +
			"}]" +
		"}")
		
		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 201, "test"))
		response.setEntity(respBody)
		def topicId = feedItemRetriever.parseTopicIdFromResponse(response)
		assertEquals("targetTopicId",topicId)
	}

	void testCreateMessagesFromTopicIdHttpRequestWithCorrectTopicId() {
		initFeedItemRetriever()
		HttpUriRequest request = feedItemRetriever.createMessagesFromTopicIdHttpRequest("testUrl", "token", "topicid")
		assertEquals("testUrl/services/data/v31.0/chatter/feeds/topics/topicid/feed-items", request.getURI().toString());
	}
	
}
