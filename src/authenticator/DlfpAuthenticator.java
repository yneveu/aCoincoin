package authenticator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class DlfpAuthenticator {

private static final String HTTP_LINUXFR_CONNEXION = "http://linuxfr.org/compte/connexion";
private static final String TAG = "DLFP - Authentication";


public static Cookie getAuthCookie(String login, String password, Boolean useProxy) throws ClientProtocolException, IOException, URISyntaxException {
    Log.v(TAG, "call getAuthCookie");
    DefaultHttpClient httpClient = new DefaultHttpClient();
    Cookie retObj = null;


    HttpPost httpPost = new HttpPost(loginUrl());
    httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");

    httpPost.setParams( createFormParams(login, password) );    

    HttpResponse response = httpClient.execute(httpPost);

    if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK ) {

        if (httpClient.getCookieStore().getCookies().size() > 0) {
            for(Cookie cookie : httpClient.getCookieStore().getCookies()) {
                Log.d(TAG,"cookie " + cookie.getName() + " " + cookie.getValue());
                if(cookie.getName().equals("linuxfr.org_session")) {
                    retObj = cookie;
                    Log.d(TAG, "SUCCEDED" );
                    Log.d(TAG,"response cookie " +  cookie.getName() + " " + cookie.getValue());
                }
            }
        }
    } else {
        Log.e(TAG, "Error response : " + response.getStatusLine().getStatusCode());
    }


    httpClient.getConnectionManager().shutdown();

// Log.v(TAG, "returns " + retObj.getValue() );

    return retObj;

}



private static HttpParams createFormParams(String login, String password) {
    HttpParams postParams = new BasicHttpParams();
    postParams.setParameter( "account[login]", login );
    postParams.setParameter( "account[password]", password );
    return postParams;
}





private static URI loginUrl() throws URISyntaxException {
    // TODO Auto-generated method stub
    return new URI(HTTP_LINUXFR_CONNEXION);
}
}