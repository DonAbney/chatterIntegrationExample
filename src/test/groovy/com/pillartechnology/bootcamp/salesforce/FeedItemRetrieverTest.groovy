package com.pillartechnology.bootcamp.salesforce

import org.apache.http.HttpException
import org.apache.http.HttpResponse
import org.apache.http.HttpVersion
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.InputStreamEntity
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicHttpResponse
import org.apache.http.message.BasicStatusLine
import org.mockito.Mockito
import static org.mockito.Mockito.*

class FeedItemRetrieverTest extends GroovyTestCase {

	private FeedItemRetrieverImpl feedItemRetriever;

	void initFeedItemRetriever() {
		feedItemRetriever = new FeedItemRetrieverImpl()
	}

	void testFindFeedItemsFailsWithNullUrl() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems(null, "token", "topic", null)
		}
	}

	void testFindFeedItemsFailsWithNullToken() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems("url", null, "topic", null)
		}
	}

	void testFindFeedItemsFailsWithNullTopic() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems("url", "token", null, null)
		}
	}
	
	void testFindFeedItemsThrowsHttpExceptionOnRequestFailure() {
		initFeedItemRetriever()
		def defaultHttpClient = Mockito.mock(HttpClient.class)
		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 404, "test"))
		when(defaultHttpClient.execute(any())).thenReturn(response)
		shouldFail(HttpException) {
			feedItemRetriever.findFeedItems("test", "test", "test", defaultHttpClient)
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
	
	void testParseTopicIdFromTopicIdResponseWithEmptyTopics() {
		initFeedItemRetriever()
		StringEntity respBody = new StringEntity(
		"{ " +
			"\"currentPageUrl\": null," +
			"\"nextPageUrl\": null," +
			"\"topics\": []" +
		"}")
		
		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 201, "test"))
		response.setEntity(respBody)
		def topicId = feedItemRetriever.parseTopicIdFromResponse(response)
		assertNull(topicId)
	}

	void testCreateMessagesFromTopicIdHttpRequestWithCorrectTopicId() {
		initFeedItemRetriever()
		HttpUriRequest request = feedItemRetriever.createMessagesFromTopicIdHttpRequest("testUrl", "token", "topicid")
		assertEquals("testUrl/services/data/v31.0/chatter/feeds/topics/topicid/feed-items", request.getURI().toString());
	}
	
	void testParseMessagesFromTopicResponse() {
		initFeedItemRetriever()
		InputStreamEntity respBody = new InputStreamEntity(
			Thread.currentThread().getContextClassLoader().getResourceAsStream("messagesResponse.json"))
		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 201, "test"))
		response.setEntity(respBody)
		def messages = feedItemRetriever.parseMessagesFromResponse(response)
		assertTrue(!messages.isEmpty())
		
		assertEquals(2, messages.size())
		assertEquals("kristen smith", messages[0].author)
		assertEquals("Something cool! ", messages[0].body)
	}
}
