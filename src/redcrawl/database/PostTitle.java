package redcrawl.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostTitle {
	public static final String db_name = "PostTitles";
	private Integer id;
	private String link;
	private String content;
	private String redditID;
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRedditID() {
		return redditID;
	}
	public void setRedditID(String redditID) {
		this.redditID = redditID;
	}
	public Integer getId() {
		return id;
	}
	
	
	public void addTitle() throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		try{
			con.setAutoCommit(false);
			if(id == null){
				String query = "INSERT INTO "+db_name+" VALUES (auto,?,?,?);";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, this.link);
				ps.setString(2, this.content);
				ps.setString(3, this.redditID);
				ps.executeUpdate();
			}else{
				String query = "UPDATE "+db_name+" LINK=?, content=?, redditID=?, where ID=?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, this.link);
				ps.setString(2,this.content);
				ps.setString(3,this.redditID);
				ps.setInt(4, id);
				ps.executeUpdate();
			}
			con.commit();
		}catch(SQLException sql){
			con.rollback();
			throw sql;
		}finally{
			con.close();
		}
		
	}
	
	public static void addList(ArrayList<PostTitle> list) throws SQLException{
		for(PostTitle title : list){
			title.addTitle();
		}
	}
	
	public static PostTitle getTitleById(Integer id) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		PostTitle title = null; 
		try{
				String query = "Select * FROM "+db_name+" WHERE ID == ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					title = new PostTitle();
					title.setLink(rs.getString(2));
					title.setContent(rs.getString(3));
					title.setRedditID(rs.getString(4));
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return title;
	}
	
	public static PostTitle getTitleByRedditId(Integer id) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		PostTitle title = null; 
		try{
				String query = "Select * FROM "+db_name+" WHERE ID == ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					title = new PostTitle();
					title.setLink(rs.getString(2));
					title.setContent(rs.getString(3));
					title.setRedditID(rs.getString(4));
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return title;
	}
	
	public ArrayList<PostComment> getComments() throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		ArrayList<PostComment> comments = new ArrayList<PostComment>(0);
		try{
			String query = "Select * FROM PostComments where parentID==?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, this.redditID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				PostComment comm = new PostComment();
				comm = new PostComment();
				comm.setLink(rs.getString(2));
				comm.setContent(rs.getString(3));
				comm.setRedditID(rs.getString(4));
				comm.setParentID(rs.getString(5));
				comments.add(comm);
			}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return comments;
	}
	
	public ArrayList<PostTitle> getTitleRange(Integer startID, Integer endID) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		ArrayList<PostTitle> list = new ArrayList<PostTitle>(0); 
		try{
				String query = "Select * FROM "+db_name+" WHERE ID >= ? AND ID <= ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, startID);
				ps.setInt(2, endID);
				ResultSet rs = ps.executeQuery();
				list =  new ArrayList<PostTitle>(rs.getFetchSize());
				while(rs.next()){
					PostTitle title = new PostTitle();
					title.setLink(rs.getString(2));
					title.setContent(rs.getString(3));
					title.setRedditID(rs.getString(4));
					list.add(title);
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return list;
	}
}
