import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader

class SalesforceAuthProvider {

	static GRANT_TYPE = "password"
	static SALESFORCE_AUTH_URL = "https://login.salesforce.com/services/oauth2/token"
	static auth = { clientId, clientSecret, username, password ->
        
	def entity = MultipartEntityBuilder.create()
            .addTextBody("client_id", clientId)
            .addTextBody("client_secret", clientSecret)
            .addTextBody("grant_type", GRANT_TYPE)
            .addTextBody("username", username)
            .addTextBody("password", password)
            .build()

        def request = RequestBuilder.post()
            .setEntity(entity)
            .setUri(SALESFORCE_AUTH_URL)
            .build()

        def response = HttpClients.createDefault().execute(request)

        def data =  new JsonSlurper().parseText(response.entity.content.text)

       [data.instance_url, data.access_token]
       }
}
