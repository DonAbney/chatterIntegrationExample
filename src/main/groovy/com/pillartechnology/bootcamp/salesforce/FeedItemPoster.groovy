package com.pillartechnology.bootcamp.salesforce
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicHeader
import groovy.json.JsonBuilder

class FeedItemPoster {

	void postFeedItem(String url, String token, String feedback, String topic) {
		throw new IllegalArgumentException()
	}

	HttpUriRequest createFeedRequest(String url, String token, String feedback, String topic) {
		def output = new JsonBuilder()
		output {
			body {
				messageSegments([
					[
						type: "Text",
						text: "${feedback} #[${topic}]"
					]
				])
			}
			feedElementType("FeedItem")
			subjectId("Playbook Feedback")
		}

		def request = RequestBuilder.post()
				.addHeader(new BasicHeader("Authorization", "Bearer ${token}"))
				.addHeader("Content-Type", "application/json")
				.setUri("${url}/services/data/v31.0/chatter/feed-elements")
				.setEntity(new StringEntity(output.toPrettyString()))
				.build()
	}
}
