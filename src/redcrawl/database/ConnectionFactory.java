package redcrawl.database;
import java.sql.*;

public class ConnectionFactory {
	private static final String pass = "redcrawler1234";
	private static final String user = "redcrawler";
	private static final String url = "jdbc:mysql://localhost:3306/redcrawler_?user="+user+"&password="+pass;
	
	public static Connection getConnection() throws SQLException{
		
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
