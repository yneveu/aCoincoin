package fr.gabuzomeu.aCoincoin;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class CoinCoinParser extends DefaultHandler {

	
	private boolean in_time = false; 
    private boolean in_id = false; 
    private boolean in_mytag = false; 
    private boolean in_info = false;
    private boolean in_message = false;
    private boolean in_login = false;
    private boolean in_post = false;
    private boolean in_board = false;
    private boolean in_site = false;
    private boolean in_timezone = false;
    
    
    private CoinCoinMessage message;
    
    ArrayList<CoinCoinMessage> messages;
    
	public ArrayList<CoinCoinMessage> getMessages() {
		//Log.i( CoinCoinApp.LOG_TAG, "PLOPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
		return messages;
	}

	

	@Override 
    public void startDocument() throws SAXException { 
         //this.myParsedExampleDataSet = new ParsedExampleDataSet(); 
		Log.i( CoinCoinApp.LOG_TAG, "Parsing started");
		messages=new ArrayList();
		
		
    } 

    @Override 
    public void endDocument() throws SAXException { 
    	Log.i( CoinCoinApp.LOG_TAG, "Parsing ended " + messages.size() +  " messages parsed" );
    } 

	
    public void startElement(String namespaceURI, String localName, 
              String qName, Attributes atts) throws SAXException { 
    	//Log.i( CoinCoinApp.LOG_TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Start element: " + localName );
    	
    	if( localName.equals("time")){
    		
    		//this.in_time=true;
    	}else if ( localName.equals("id")){
    		this.in_id=true;
    	}else if ( localName.equals("info")){
    		this.in_info=true;
    	}else if ( localName.equals("message")){
    		this.in_message=true;
    	}else if ( localName.equals("login")){
    		this.in_login=true;
        }else if ( localName.equals("post")){
        	message=new CoinCoinMessage();
    		message.setId( Integer.parseInt( atts.getValue("id")));
        	message.setTime( Long.parseLong( atts.getValue("time")));
        	
        	//Log.i( CoinCoinApp.LOG_TAG, "DEBUT D'UN POST: " + localName + " ID: " + message.getId() );
		this.in_post=true;	
        }else if ( localName.equals("board")){
    		this.in_board=true;
        }else if ( localName.equals("timezone")){
    		this.in_timezone=true;
        }
    	
    }
    
    
    
    
    @Override 
    public void endElement(String namespaceURI, String localName, String qName) 
              throws SAXException { 
    	//Log.i( CoinCoinApp.LOG_TAG, "End element: " + localName);
    	if( localName.equals("time")){
    		this.in_time=false;
    	}else if ( localName.equals("id")){
    		this.in_id=false;
    	}else if ( localName.equals("info")){
    		this.in_info=false;
    	}else if ( localName.equals("message")){
    		this.in_message=false;
    	}else if ( localName.equals("login")){
    		this.in_login=false;
        }else if ( localName.equals("post")){
        	messages.add( message );
        //	Log.i( CoinCoinApp.LOG_TAG, "AJOUT ELEMENT POST: " + localName);
        //	
        	//Log.i( CoinCoinApp.LOG_TAG, "MESSAGE " + message) ;
        //	Log.i( CoinCoinApp.LOG_TAG, "************************************************") ;
        	
        }else if ( localName.equals("board")){
    		this.in_board=false;
        }else if ( localName.equals("timezone")){
    		this.in_timezone=false;
        }
    	
    	
    	
    } 
     
    /** Gets be called on the following structure: 
     * <tag>characters</tag> */ 
    @Override 
   public void characters(char ch[], int start, int length) { 
    	String cdata = new String(ch, start, length);
       // cdata = cdata.trim();
    	  
    	if( this.in_time){
    		message.setTime( Integer.parseInt( cdata ));
    		this.in_time=false;
    	}else if( this.in_id ){
    		message.setId( Integer.parseInt( cdata ));
    		Log.i( CoinCoinApp.LOG_TAG, "ID-------------->: " + cdata);
    		this.in_id=false;
    	}else if( this.in_info ){
    		message.setInfo( cdata );
    		this.in_info=false;
    	}else if( this.in_message ){
    		if( message.getMessage() != null ){
    			message.setMessage( message.getMessage() + cdata );
    			//Log.i( CoinCoinApp.LOG_TAG, "*** MESSAGE XML-------------->: " + cdata);
    		}
    		else 
    			message.setMessage( cdata);
    		//this.in_message=false;
    	}else if( this.in_login ){
    		message.setLogin( cdata );
    	}else if( this.in_board ){
    		//message.setBoard("TEST");
    		//message.setBoard( new String( ch, start, length) );
    		this.in_board=false;
    	}
    	
   } 
	
	
	
	
	
}
