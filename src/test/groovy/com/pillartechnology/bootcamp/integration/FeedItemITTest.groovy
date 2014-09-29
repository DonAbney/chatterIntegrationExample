package com.pillartechnology.bootcamp.integration

import com.pillartechnology.bootcamp.salesforce.FeedItemPoster
import com.pillartechnology.bootcamp.salesforce.SalesforceAuthProvider
import com.pillartechnology.bootcamp.salesforce.FeedItemRetriever
import com.pillartechnology.bootcamp.salesforce.FeedItemRetrieverImpl

class FeedItemITTest extends GroovyTestCase {

	void testFindFeedItems() {
		FeedItemRetriever feedItemRetriever = new FeedItemRetrieverImpl()
		def response = feedItemRetriever.makeChatterRequest()
	}

	void testPostFeedItems() {
		FeedItemPoster feedItemPoster = new FeedItemPoster()
		def urlAndToken = SalesforceAuthProvider.auth("3MVG9xOCXq4ID1uGkfqs8olvegeBTR34Tlez7JOI2BgmGRUQ_ajQlWhLAF3I0HRzRNzJrnR6SjAiFkZBSXhZY", "7554976980304999635", "ksmith2@pillartechnology.com", "agile123")
		shouldFail(IllegalArgumentException) {
			def request = feedItemPoster.postFeedItem(urlAndToken[0], urlAndToken[1], "This item has a topic", "testtopic")
		}
	}
}
