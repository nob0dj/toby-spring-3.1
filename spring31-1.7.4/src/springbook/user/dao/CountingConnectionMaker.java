package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {

	private int count;
	private ConnectionMaker realConnectionMaker;
	

	
	public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
		this.realConnectionMaker = realConnectionMaker;
	}


	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		count++;
		return realConnectionMaker.makeConnection();
	}


	public int getCount() {
		return count;
	}


}
