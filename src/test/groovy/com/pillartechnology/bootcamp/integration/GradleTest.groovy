package com.pillartechnology.bootcamp.integration
import com.pillartechnology.bootcamp.salesforce.SalesforceAuthProvider;

class GradleTest extends GroovyTestCase {

	void testCanGetURLandTokenFromSalesforce() {
		final clientId = "3MVG9xOCXq4ID1uGkfqs8olvegeBTR34Tlez7JOI2BgmGRUQ_ajQlWhLAF3I0HRzRNzJrnR6SjAiFkZBSXhZY"
		final clientSecret = "7554976980304999635"
		final username = "ksmith2@pillartechnology.com"
		final password = "agile123"
		def (url, token) = SalesforceAuthProvider.auth(clientId, clientSecret, username, password)
		assertNotNull(url)
		assertNotNull(token)
	}
}
