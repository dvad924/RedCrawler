package redcrawl.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class PostComment {
	private static final String db_name="PostComments";
	private Integer id;
	private String link;
	private String content;
	private Integer titleId;
	private String redditID;
	private String parentID;
	
	public int hashCode(){
		return redditID.hashCode();
	}
	
	public boolean equals(Object o){
		if(o == null) return false;
		if(!(o instanceof PostComment)) return false;
		return this.redditID.equals(((PostComment)o).redditID);
	}
	
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
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
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
			//con.close();
		}
		
	}
	public ArrayList<PostComment> listCheck(Collection <? extends PostComment> list) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		ArrayList<PostComment> rlist = new ArrayList<PostComment>();
		String query = "SELECT redditID FROM PostComments WHERE";
		int size = list.size();
		int i = 1;
		if(size == 0)
			return rlist; //return empty list if the size of the comments list is zero
		for(PostComment pc : list){
			if(i++ < size)
				query += " redditID='"+pc.getRedditID()+"' OR";
			else
				query += " redditID='"+pc.getRedditID()+"';";
		}
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			PostComment pc = new PostComment();
			pc.setRedditID(rs.getString(1));
			rlist.add(pc);
		}
		return rlist;
		
	}
	
	public int[] sendList(Collection <? extends PostComment> list, int Title_id) throws SQLException{
		ArrayList<PostComment> delList = listCheck(list);
		for(PostComment pc : delList){
			list.remove(pc);
		}
		return addList(list,Title_id);
		
	}
	public int[] addList(Collection <? extends PostComment> list,int Title_id) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		int[] returns = new int[list.size()];
		try{
			con.setAutoCommit(false);
																							
			String query = "INSERT INTO "+db_name+" (link,content,redditID,parentID) VALUES (?,?,?,?);";	//add new entry
			PreparedStatement ps = con.prepareStatement(query);
			for(PostComment pc : list){												//for every item in list
				ps.setString(1, pc.link);
				ps.setString(2,pc.content);
				ps.setString(3, pc.redditID);
				ps.setInt(4,Title_id);
				ps.addBatch();													//add sql to batch
			}
			returns = ps.executeBatch();			//execute the batch of statements
			con.commit();
		}catch(SQLException sql){
			con.rollback();
			throw sql;
		}finally{
			//con.close();
		}
		return returns;
	}
	
	public PostComment getCommentByRedditID(String id) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon(); 
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
		//	con.close();
		}
		return comm;
	
	}
	
	public PostComment getCommentById(Integer id) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
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
			//con.close();
		}
		return comm;
	}
	
	public ArrayList<PostComment> getCommentRange(Integer startID, Integer endID) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
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
			//con.close();
		}
		return list;
	}
	public boolean commentExists() throws SQLException{
		MyConnection mycon = null; 
		boolean bool = false;
		
			mycon = ConnectionFactory.getConnection();
			Connection con = mycon.getCon();
			String query = "Select count(*) FROM "+db_name+" WHERE redditID=?"; //get links with the passed url
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, this.redditID);
			ResultSet rs =  ps.executeQuery();
			if( rs.next()){                  //if there are any return true, else return false
				int count = rs.getInt(1);
				if(count > 0)
					bool = true;
				else
					bool = false;
			}
		return bool;
	}
}
