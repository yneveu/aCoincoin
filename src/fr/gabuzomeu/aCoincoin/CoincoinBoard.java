package fr.gabuzomeu.aCoincoin;




public class CoincoinBoard {
	
	private int id;	
	private String name;
	private String backend_url;
	private String post_url;
	
	private boolean enabled;
	private String background_color;
	private String extraPostParams;
	private String cookies;
	private String host;
	private String postUrl;
	private String postReferer;
	private String userAgent;
	private String encoding;
	private int last_update;
	private String login;
	private int lastMessageId;
	
	private int newMessages = 0;
	private int nbMessages=0;
	
	


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBackend_url() {
		return backend_url;
	}
	public void setBackend_url(String backendUrl) {
		backend_url = backendUrl;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getBackground_color() {
		return background_color;
	}
	public void setBackground_color(String backgroundColor) {
		background_color = backgroundColor;
	}
	public void setExtraPostParams(String extraPostParams) {
		this.extraPostParams = extraPostParams;
	}
	public String getExtraPostParams() {
		return extraPostParams;
	}
	
	public String toString(){
		return getName();
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	public String getCookies() {
		return cookies;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHost() {
		return host;
	}
	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}
	public String getPostUrl() {
		return postUrl;
	}

	public String getPost_url() {
		return post_url;
	}
	public void setPost_url(String postUrl) {
		post_url = postUrl;
	}
	public String getPostReferer() {
		return postReferer;
	}
	public void setPostReferer(String postReferer) {
		this.postReferer = postReferer;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public int getLast_update() {
		return last_update;
	}
	public void setLast_update(int lastUpdate) {
		last_update = lastUpdate;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getLogin() {
		return login;
	}
	public void setNewMessages(int newMessages) {
		this.newMessages = newMessages;
	}
	public int getNewMessages() {
		return newMessages;
	}
	public void setNbMessages(int nbMessages) {
		this.nbMessages = nbMessages;
	}
	public int getNbMessages() {
		return nbMessages;
	}
	public void setLastMessageId(int lastMessageId) {
		this.lastMessageId = lastMessageId;
	}
	public int getLastMessageId() {
		return lastMessageId;
	}
	
}
