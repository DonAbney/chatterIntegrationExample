package com.pillartechnology.bootcamp.salesforce

import org.apache.http.client.HttpClient;


interface FeedItemPoster {
	void postFeedItem(String url, String token, String feedback, String topic,	HttpClient httpClient, String thirdPartyUrl)
}
