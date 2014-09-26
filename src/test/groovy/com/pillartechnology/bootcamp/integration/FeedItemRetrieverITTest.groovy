package com.pillartechnology.bootcamp.integration

import com.pillartechnology.bootcamp.salesforce.FeedItemRetriever
import com.pillartechnology.bootcamp.salesforce.FeedItemRetrieverImpl

class FeedItemRetrieverITTest extends GroovyTestCase {

	void testFindFeedItems() {
		FeedItemRetriever feedItemRetriever = new FeedItemRetrieverImpl();
		def response = feedItemRetriever.makeChatterRequest()
	}
}
