package redcrawl.database;
import java.sql.*;

public class ConnectionFactory {
	
	//private static Connection con = null;
	
	
	public static MyConnection getConnection() throws SQLException{
		return MyConnection.getConnection();
	}

	
}
