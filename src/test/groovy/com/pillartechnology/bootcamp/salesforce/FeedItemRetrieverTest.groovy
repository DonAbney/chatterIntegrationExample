package com.pillartechnology.bootcamp.salesforce

import org.apache.http.client.methods.HttpUriRequest

class FeedItemRetrieverTest extends GroovyTestCase {

	private FeedItemRetriever feedItemRetriever;

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

	void testCreateFeedItemHttpRequestWithCorrectTopic() {
		FeedItemRetrieverImpl feedItemRetrieverImpl = new FeedItemRetrieverImpl()
		HttpUriRequest request = feedItemRetrieverImpl.createFeedItemHttpRequest("testUrl", "token", "topic")
		
		assertEquals("testUrl/services/data/v31.0/connect/topics?exactMatch=true&q=topic", request.getURI().toString());
	}
}
