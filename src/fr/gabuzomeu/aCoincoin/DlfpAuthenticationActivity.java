package fr.gabuzomeu.aCoincoin;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import fr.gabuzomeu.aCoincoin.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import authenticator.DlfpAuthenticator;

public class DlfpAuthenticationActivity extends Activity {

	
	CoinCoinApp app;
	Context acontext;
	
	Button loginButton;
	EditText loginText;
	EditText passwordText;
	
	String login;
	String password;
	
	public void setApp(CoinCoinApp app) {
		this.app = app;
	}

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		acontext = this;
		app = (CoinCoinApp)getApplication();
		setContentView(R.layout.board_pref);
		
		Button loginButton = ( Button)findViewById( R.id.button1);
		loginText = ( EditText)findViewById( R.id.editText1);
		passwordText = ( EditText)findViewById( R.id.editText2);
		
		loginButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	
	        	
	        	login = loginText.getText().toString();
	        	password = passwordText.getText().toString();
	        	
	        	String resultString = "Failed to login";
	        	boolean result = false;	        	
	        	
	        	try {
					if( DlfpAuthenticator.getAuthCookie( app, login, password) ){
						resultString = "Logged!!!";
						app.getBoards();
						result = true;
						
					}
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
	        
	        	Toast toast = Toast.makeText( acontext , resultString, Toast.LENGTH_SHORT);
            	toast.show();
            	
            	if( result)
            		finish();
	        
	        }
		});
		
		
	}
	
	
	
	
	
	
}
