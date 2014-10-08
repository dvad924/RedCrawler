package redcrawl.database;
import java.sql.*;

public class ConnectionFactory {
	private static final String pass = "";
	private static final String user = "";
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
	
	public static void main(String[] args) throws SQLException{
		Connection con = null;
		try{
			con = ConnectionFactory.getConnection();
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement("INSERT INTO rawlinks (LINK) VALUES (?)");
			ps.setString(1, "http://www.reddit.com/r/programming/comments/2ik7gl/this_made_me_laugh_a_command_line_application/");
			ps.executeUpdate();
			con.commit();
		}catch (SQLException s){
			s.printStackTrace();
		}finally{
			con.close();
		}
	}
	
}
