package fr.gabuzomeu.aCoincoin;

import android.util.Log;

public class CoinCoinMessage implements Comparable {

	private int boardId;
	private String board;
	private long time;
	private int id;
	private String info;
	private String message;
	private String login;
	private String backgroundColor;
	
	
	
	
	
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public String getBoard() {
		return board;
	}
	public void setBoard(String board) {
		this.board = board;
	}
	
	
	public long getTime() {
		return time;
	}
	public void setTime(long l) {
		this.time = l;
	}
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	@Override
	public String toString() {
		/*return "CoinCoinMessage [\nboard=" + board + "\nboardId=" + boardId
				+ "\nid=" + id + "\ninfo=" + info + "\nlogin=" + login
			+ "\nmessage=" + message + "\ntime=" + time + "]";*/
		//return "CoinCoinMessage [board=" + board + "boardId=" + boardId + " id=" + id + " info=" + info + " login=" + login + " message=" + message + " time=" + time + "]";
		return "CoinCoinMessage [ boardId=" + boardId + " id=" + id + " time=" + time + " message= " + message + "]";
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public int compareTo(Object another) {
		//Log.i(getClass().getSimpleName(), "SORT");
		if ( ((CoinCoinMessage)another).getTime() > this.getTime() )
			return 1;
		else if (((CoinCoinMessage)another).getTime() < this.getTime())
			return -1;
		else
			return 0;
		
	}
	
	
	
	
	
}
