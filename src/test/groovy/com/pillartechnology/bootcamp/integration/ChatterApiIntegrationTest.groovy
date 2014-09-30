package com.pillartechnology.bootcamp.integration

import org.apache.http.impl.client.HttpClients

import com.pillartechnology.bootcamp.salesforce.FeedItem
import com.pillartechnology.bootcamp.salesforce.FeedItemPoster
import com.pillartechnology.bootcamp.salesforce.FeedItemPosterImpl
import com.pillartechnology.bootcamp.salesforce.FeedItemRetriever
import com.pillartechnology.bootcamp.salesforce.FeedItemRetrieverImpl
import com.pillartechnology.bootcamp.salesforce.SalesforceAuthProvider

class ChatterApiIntegrationTest extends GroovyTestCase {

	static final String INTEGRATION_TEST_TOPIC = "bootcamp_it_topic"
	static final String INTEGRATION_TEST_MESSAGE = "test message"
	static final String BOOTCAMP_TEST_GROUP_ID = "0F9o0000000LOanCAG"
	
	public void testDoEndToEndChatterApiTest() {
		String[] urlAndToken = getURLandTokenFromSalesforce()
		assertNotNull(urlAndToken)
		String url = urlAndToken[0]
		String token = urlAndToken[1]
		postFeedItems(url, token)
		List<FeedItem> feedItems = findFeedItems(url, token)
		assertTrue(!feedItems.isEmpty())
	}
	
	String[] getURLandTokenFromSalesforce() {
		final clientId = "3MVG9xOCXq4ID1uGkfqs8olvegeBTR34Tlez7JOI2BgmGRUQ_ajQlWhLAF3I0HRzRNzJrnR6SjAiFkZBSXhZY"
		final clientSecret = "7554976980304999635"
		final username = "ksmith2@pillartechnology.com"
		final password = "agile123"
		def urlAndToken = SalesforceAuthProvider.auth(clientId, clientSecret, username, password)
		assertNotNull(urlAndToken[0])
		assertNotNull(urlAndToken[1])
		
		urlAndToken
	}
	
	void postFeedItems(String url, String token) {
		FeedItemPoster feedItemPoster = new FeedItemPosterImpl()
		feedItemPoster.CHATTER_GROUP = BOOTCAMP_TEST_GROUP_ID
		feedItemPoster.postFeedItem(url, token, INTEGRATION_TEST_MESSAGE, INTEGRATION_TEST_TOPIC, HttpClients.createDefault())
	}
	
	List<FeedItem> findFeedItems(String url, String token) {
		FeedItemRetriever feedItemRetriever = new FeedItemRetrieverImpl()
		feedItemRetriever.findFeedItems(url, token, INTEGRATION_TEST_TOPIC)
	}

}
