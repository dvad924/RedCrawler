package redcrawl.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostComment {
	private static final String db_name="PostComments";
	private Integer id;
	private String link;
	private String content;
	private Integer titleId;
	private String redditID;
	private String parentID;
	
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
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
	public Integer getTitleId() {
		return titleId;
	}
	
	public void addComment() throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		try{
			con.setAutoCommit(false);
			if(id == null){
				String query = "INSERT INTO "+db_name+" VALUES (auto,?,?,?,?);";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, this.link);
				ps.setString(2, this.content);
				ps.setString(3, this.redditID);
				ps.setString(4, this.parentID);
				ps.executeUpdate();
			}else{
				String query = "UPDATE "+db_name+" LINK=?, content=?, redditID=? parentID=?, where ID=?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, this.link);
				ps.setString(2,this.content);
				ps.setString(3,this.redditID);
				ps.setString(4, parentID);
				ps.setInt(5, id);
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
	
	public void addList(ArrayList<PostComment> list) throws SQLException{
		for(PostComment comm : list){
			comm.addComment();
		}
	}
	
	public PostComment getTitleByRedditID(String id) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		PostComment comm = null; 
		try{
				String query = "Select * FROM "+db_name+" WHERE redditID == ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, id);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					comm = new PostComment();
					comm.setLink(rs.getString(2));
					comm.setContent(rs.getString(3));
					comm.setRedditID(rs.getString(4));
					comm.setParentID(rs.getString(5));
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return comm;
	
	}
	
	public PostComment getTitleById(Integer id) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		PostComment comm = null; 
		try{
				String query = "Select * FROM "+db_name+" WHERE ID == ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					comm = new PostComment();
					comm.setLink(rs.getString(2));
					comm.setContent(rs.getString(3));
					comm.setRedditID(rs.getString(4));
					comm.setParentID(rs.getString(5));
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return comm;
	}
	
	public ArrayList<PostComment> getTitleRange(Integer startID, Integer endID) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		ArrayList<PostComment> list = new ArrayList<PostComment>(0); 
		try{
				String query = "Select * FROM "+db_name+" WHERE ID >= ? AND ID <= ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, startID);
				ps.setInt(2, endID);
				ResultSet rs = ps.executeQuery();
				list =  new ArrayList<PostComment>(rs.getFetchSize());
				while(rs.next()){
					PostComment comm = new PostComment();
					comm.setLink(rs.getString(2));
					comm.setContent(rs.getString(3));
					comm.setRedditID(rs.getString(4));
					comm.setParentID(rs.getString(5));
					list.add(comm);
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return list;
	}
}
