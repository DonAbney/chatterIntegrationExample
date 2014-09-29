package com.pillartechnology.bootcamp.salesforce

import org.apache.http.client.methods.HttpUriRequest

class FeedItemRetrieverTest extends GroovyTestCase {

	private FeedItemRetriever feedItemRetriever;

	void initFeedItemRetriever() {
		feedItemRetriever = new FeedItemRetrieverImpl()
	}

	void testFindFeedItemsDoesNotFail() {
		initFeedItemRetriever()
		List<FeedItem> feedItems = feedItemRetriever.findFeedItems("url", "token", "group", "topic")
		assertNotNull(feedItems)
	}

	void testFindFeedItemsReturnsExceptionWithNullInput() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems(null, null, null, null)
		}
	}

	void testFindFeedItemsFailsWithNullUrl() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems(null, "token", "group", "topic")
		}
	}

	void testFindFeedItemsFailsWithNullToken() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems("url", null, "group", "topic")
		}
	}

	void testFindFeedItemsFailsWithNullGroup() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems("url", "token", null, "topic")
		}
	}

	void testFindFeedItemsFailsWithNullTopic() {
		initFeedItemRetriever()
		shouldFail(IllegalArgumentException) {
			feedItemRetriever.findFeedItems("url", "token", "group", null)
		}
	}

	void testCreateFeedItemHttpRequestWithCorrectGroupAndTopic() {
		FeedItemRetrieverImpl feedItemRetrieverImpl = new FeedItemRetrieverImpl()
		HttpUriRequest request = feedItemRetrieverImpl.createFeedItemHttpRequest("testUrl", "token", "testGroup", "topic")
		
		assertEquals("testUrl/services/data/v32.0/chatter/feeds/record/testGroup/feed-elements", request.getURI().toString());
	}
}
