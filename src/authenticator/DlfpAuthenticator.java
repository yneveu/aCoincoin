package authenticator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.ContentValues;
import android.util.Log;
import fr.gabuzomeu.aCoincoin.CoinCoinApp;

public class DlfpAuthenticator {

private static final String HTTP_LINUXFR_CONNEXION = "http://linuxfr.org/compte/connexion";
private static final String TAG = "DLFP - Authentication";

static CoinCoinApp app;

public DlfpAuthenticator( CoinCoinApp _app){
	app = _app;
}


public static boolean getAuthCookie( CoinCoinApp app, String login, String password) throws ClientProtocolException, IOException, URISyntaxException {
    Log.v(TAG, "call getAuthCookie");
    DefaultHttpClient httpClient = new DefaultHttpClient();
    Cookie retObj = null;

    boolean returnValue = false;
    
    HttpPost httpPost = new HttpPost(loginUrl());
    httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
    httpPost.setHeader("Referer","http://linuxfr.org");
    httpPost.setHeader("Origin","http://linuxfr.org");

    List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
    nvps.add( new BasicNameValuePair( "account[login]", login));
    nvps.add( new BasicNameValuePair( "account[password]", password));
    nvps.add( new BasicNameValuePair( "account[remember_me]", "1"));
    httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); 
    
    HttpResponse response = httpClient.execute(httpPost);

   // final String responseText =  EntityUtils.toString(response.getEntity());
    
       
    if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK ) {

    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
    response.getEntity().writeTo(outstream);
    byte [] responseBody = outstream.toByteArray();
    String result = new String( responseBody);
    
    Log.d(TAG,"RESPONSE " + new String(  responseBody) );
    	
    	
        if (httpClient.getCookieStore().getCookies().size() > 0) {
            for(Cookie cookie : httpClient.getCookieStore().getCookies()) {
                Log.d(TAG,"cookie " + cookie.getName() + " " + cookie.getValue());
                if(cookie.getName().equals("linuxfr.org_session")) {
                    retObj = cookie;
                    
                    Log.d(TAG,"response cookie " +  cookie.getName() + " " + cookie.getValue());
                    ContentValues cookieValues= new ContentValues();
					cookieValues.put( "cookie", cookie.getName() + "=" + cookie.getValue() );
                    app.getDb().update( "boards", cookieValues, "id=?", new String[]{String.valueOf(2)} );
                    httpClient.getConnectionManager().shutdown();
                    if ( result.contains("logged visitor")){
                    	Log.d(TAG, "SUCCEDED" );
                    	returnValue = true;	
                    }
                    	
                    
                }
            }
        }
    } else {
        Log.e(TAG, "Error response : " + response.getStatusLine().getStatusCode());
        httpClient.getConnectionManager().shutdown();
        
    }
    Log.d(TAG, "RETURN: " +returnValue );
    return returnValue;
    
}


private static URI loginUrl() throws URISyntaxException {
    // TODO Auto-generated method stub
    return new URI(HTTP_LINUXFR_CONNEXION);
}
}