package redcrawl.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
public class RawLink {
	private String link;
	private Integer id;
	private DateTime dateTime;
	
	public RawLink(String url){
		this.link = url;
		this.id = null;
		this.dateTime = new DateTime(0); //set new link unaccessed to the beginning of the epoch
	}
	
	public RawLink(){};
	private RawLink(String url, Integer id){
		this.link = url;
		this.id = id;
		this.dateTime = new DateTime(0);
	}
	private RawLink(String url, Integer id, DateTime dt){
		this.link = url;
		this.id = id;
		this.dateTime = dt;
	}
	
	private ArrayList<RawLink> convertToLinks(List<String> strings){
		RawLink l;
		ArrayList<RawLink> links = new ArrayList<RawLink>(strings.size());
		for(String s : strings){
			l = new RawLink(s);
			links.add(l);
		}
		return links;
	}
	public void addLink() throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		try{
			con.setAutoCommit(false);
			if(id == null){
				String query = "INSERT INTO rawlinks (LINK,lastAccess) VALUES (?,?);";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, link);
				ps.setTimestamp(2, new Timestamp(dateTime.getMillis()));
				ps.executeUpdate();
			}else{
				String query = "UPDATE rawlinks LINK=?,lastAccess=? where ID=?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(3,id);
				ps.setString(1, link);
				ps.setTimestamp(2,new Timestamp(dateTime.getMillis()));
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
	
	public void addAndMarkLink(DateTime d) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		try{
			con.setAutoCommit(false);
			if(id == null){
				String query = "INSERT INTO rawlinks VALUES (auto,?,?);";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1,link);
				ps.setObject(2,dateTime);
				ps.executeUpdate();
			}
		}catch(SQLException sql){
			con.rollback();
			throw sql;
		}finally{
			con.close();
		}
	}
	
	public void addList(ArrayList<RawLink> list) throws SQLException{
		for(RawLink link : list){
			link.addLink();
		}
	}
	public void addList(String[] list) throws SQLException{
		ArrayList<RawLink> convList = new ArrayList<RawLink>(list.length);
		RawLink rl;
		for(String s : list){
			rl = new RawLink(s);
			convList.add(rl);
		}
		addList(convList);
		
	}
	
	public void addList(List<String> list) throws SQLException{
		ArrayList<RawLink> convList = convertToLinks(list);
		addList(convList);
	}
	
	public ArrayList<RawLink> getLinksAbove(Integer id) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		ArrayList<RawLink> list = new ArrayList<RawLink>(0); 
		try{
				String query = "Select * FROM rawlinks WHERE ID >= ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				list =  new ArrayList<RawLink>(rs.getFetchSize());
				while(rs.next()){
					Timestamp t = rs.getTimestamp(3);
					RawLink rl;
					if(t != null)
						 rl = new RawLink(rs.getString(2),rs.getInt(1), new DateTime(rs.getTimestamp(3)));
					else
						rl = new RawLink(rs.getString(2),rs.getInt(1));
					list.add(rl);
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return list;
	}
	
	public RawLink getLinkById(Integer id) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		RawLink link = null; 
		try{
				String query = "Select * FROM rawlinks WHERE ID == ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					link = new RawLink(rs.getString(2),rs.getInt(1),new DateTime(rs.getTimestamp(3)));
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return link;
	}
	
	public ArrayList<RawLink> getLinkRange(Integer startID, Integer endID) throws SQLException{
		Connection con = ConnectionFactory.getConnection();
		ArrayList<RawLink> list = new ArrayList<RawLink>(0); 
		try{
				String query = "Select * FROM rawlinks WHERE ID >= ? AND ID <= ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, startID);
				ps.setInt(2, endID);
				ResultSet rs = ps.executeQuery();
				list =  new ArrayList<RawLink>(rs.getFetchSize());
				while(rs.next()){
					RawLink rl = new RawLink(rs.getString(2),rs.getInt(1), new DateTime(rs.getTimestamp(3)));
					list.add(rl);
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			con.close();
		}
		return list;
	}


	public void setLink(String link){
		this.link = link;
	}
	
	public String getLink(){
		return this.link;
	}
	
	public Integer getInt(){
		return this.id;
	}
	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
}