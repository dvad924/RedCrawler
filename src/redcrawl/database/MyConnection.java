package redcrawl.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
	private Connection con;
	private static MyConnection c = null;
	
	public Connection getCon(){
		return this.con;
	}
	private MyConnection() throws SQLException{
		this.con = new ConnectionBuilder().getConnection();
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				try {
					if(con.isClosed())
						return;
					else
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
	}
	
	public static MyConnection getConnection() throws SQLException{
		if(c == null){
			c = new MyConnection();
		}
		if(c.con.isClosed())
			c.con = c.new ConnectionBuilder().getConnection();
		return c;
	}

	
	private class ConnectionBuilder{
		private static final String pass = "redcrawler1234";
		private static final String user = "redcrawler";
		private static final String url = "jdbc:mysql://localhost:3306/redcrawler_?user="+user+"&password="+pass;
		
		private Connection getConnection() throws SQLException{
			
			Connection con = null;	
				try{
					Class.forName("com.mysql.jdbc.Driver"); //load DB Driver into JVM
				}catch(ClassNotFoundException e){
					System.out.println("Driver does not exist!!");
					e.printStackTrace();
				}
					con = DriverManager.getConnection(url);
					if(con == null)
						System.out.println("Error retrieving connection, check url");
				return con;
			}
	}
	
}
