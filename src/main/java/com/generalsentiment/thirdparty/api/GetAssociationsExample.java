/*
 * To try this example, get the repo at https://github.com/jdevjram/gsapi-scribe-examples
 *
 * Run "mvn clean package" on the downloaded code.
 * This will assemble a jar-file with the examples and the scribe dependency.
 *
 * To run the example :
 *
 * /path/to/java -cp /path/to/gsapi-scribe-examples-1.0.0-jar-with-dependencies.jar \
 *  -Doauth.consumer.key=YOUR_API_KEY \
 *  -Doauth.consumer.secret=YOUR_API_SECRET \
 *  com.generalsentiment.thirdparty.api.GetAssociationsExample
 *
 */


package com.generalsentiment.thirdparty.api;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class GetAssociationsExample {

    public static class GSAPI extends DefaultApi10a {


        @Override
        public String getRequestTokenEndpoint() {
            return "http://api.generalsentiment.com/oauth/request_token";
        }

        @Override
        public String getAccessTokenEndpoint() {
            return "http://api.generalsentiment.com/oauth/access_token";
        }

        @Override
        public String getAuthorizationUrl(Token requestToken) {
            return "http://api.generalsentiment.com/oauth/authorize?oauth_token=" + requestToken.getToken();
        }
    }

    private static final String NETWORK_NAME = "General Sentiment";
    private static final String PROTECTED_RESOURCE_URL = "http://api.generalsentiment.com/api/getAssociations";

    public static void main(String[] args)
    {
        // Pass in your consumer key and secret from the command-line.
        String apiKey = System.getProperty("oauth.consumer.key");
        String apiSecret = System.getProperty("oauth.consumer.secret");
        OAuthService service = new ServiceBuilder().provider(GSAPI.class).apiKey(apiKey).apiSecret(apiSecret).build();

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        Token requestToken = service.getRequestToken();
        System.out.println("Got the Request Token! : " + requestToken.toString());
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println("Got the Authorization URL! : " + authorizationUrl);

        // Authorize the request token by getting a token verifier.
        System.out.println("Authorizing the token here : " + authorizationUrl);
        Request request1 = new Request(Verb.GET, authorizationUrl);
        Verifier verifier = new Verifier(request1.send().getBody());
        System.out.println("Request token verifier : " + verifier.getValue());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Getting an access token for the request token and verifier ...");
        Token accessToken = service.getAccessToken(requestToken, verifier);
        System.out.println("Access token : " + accessToken);
        System.out.println();

        /*
        * You can cache access-tokens once you receive one.
        * Access tokens expire in 2 hours , so if you get back
        * a 400 response with "invalid token" in it, get a new
        * access token.
        */

        // Query the General Sentiment API.
        System.out.println("POST /api/getAssociations : ");
        OAuthRequest request2 = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);

        request2.addBodyParameter("synset", "Google\tgoogle\tGOOG");
        request2.addBodyParameter("start_date", "201303");
        request2.addBodyParameter("end_date", "201305");
        request2.addBodyParameter("time_unit", "month");
        request2.addBodyParameter("depository", "twitter");
        request2.addBodyParameter("top_count", "50");
        request2.addBodyParameter("include_association_scores", "true");
        request2.addBodyParameter("sort_by_association_scores", "true");
        request2.addBodyParameter("include_associated_entity_count", "true");
        request2.addBodyParameter("include_association_time_series", "true");

        service.signRequest(accessToken, request2);
        Response response = request2.send();
        System.out.println("Query result : ");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
    }
}
