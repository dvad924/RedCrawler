package redcrawl.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;


import org.joda.time.DateTime;

import redcrawl.constants.Constants;
public class RawLink {
	private String link;
	private Integer id;
	private DateTime dateTime;
	/**
	 * Create unseen RawLink 
	 * @param url
	 */
	public RawLink(String url){		
		this.link = url;
		this.id = null;
		this.dateTime =  new DateTime(1970, 1, 1, 0, 0, 0, 0); //set new link unaccessed to the beginning of the epoch
	}
	
	public RawLink(){};
	/**
	 * Create RawLink object from database call
	 * @param url
	 * @param id
	 */
	private RawLink(String url, Integer id){
		this.link = url;
		this.id = id;
		this.dateTime = new DateTime(0);
	}
	
	/**
	 * Create RawLink object from database call with time
	 * @param url
	 * @param id
	 * @param dt
	 */
	private RawLink(String url, Integer id, DateTime dt){
		this.link = url;
		this.id = id;
		this.dateTime = dt;
	}
	
//	private ArrayList<RawLink> convertToLinks(List<String> strings){
//		RawLink l;
//		ArrayList<RawLink> links = new ArrayList<RawLink>(strings.size());
//		for(String s : strings){
//			l = new RawLink(s);
//			links.add(l);
//		}
//		return links;
//	}
	
	/**
	 * Add a single link to the database
	 * @throws SQLException
	 */
	public void addLink() throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		try{
			con.setAutoCommit(false);                   	//turn off autocommit
			if(id == null){									//if this doesn't have an id
				String query = "INSERT INTO rawlinks (LINK,lastAccess) VALUES (?,?);"; //create new link
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, link);
				ps.setTimestamp(2, new Timestamp(dateTime.getMillis()));
				ps.executeUpdate();
			}else{
				String query = "UPDATE rawlinks LINK=?,lastAccess=? where ID=?;"; //otherwise edit the existing database entry
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(3,id);
				ps.setString(1, link);
				ps.setTimestamp(2,new Timestamp(dateTime.getMillis()));
				ps.executeUpdate();
			}
			con.commit();
		}catch(SQLException sql){
			con.rollback();                                  //if failure retain old database state
			throw sql;
		}finally{
			//con.close();
		}
	}
	
	/**
	 * Marks link entry as seen
	 * @param d
	 * @throws SQLException
	 */
	public void addAndMarkLink(DateTime d) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		try{
			con.setAutoCommit(false);
			if(id == null){												//if new link
				String query = "INSERT INTO rawlinks (LINK,lastaccess) VALUES (?,?);";//insert it into database
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1,link);
				ps.setTimestamp(2,new Timestamp(d.getMillis()));
				ps.executeUpdate();
			}else{
				String query = "UPDATE rawlinks SET lastAccess=? WHERE ID=?"; //otherwise update
				PreparedStatement ps =  con.prepareStatement(query);
				ps.setTimestamp(1,new Timestamp(DateTime.now().getMillis()) );
				ps.setInt(2,id);
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
	
	public ArrayList<RawLink> checkList(Collection <? extends RawLink> list) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		ArrayList<RawLink> arr= new ArrayList<RawLink>();
		con.setAutoCommit(false);
		String query = "Select LINK from rawlinks WHERE";
		int size = list.size();
		int i=1;
		for(RawLink rl : list){
	
			if(i++ < size)
				query += " LINK='"+ rl.getLink()+"' OR ";
			else
				query += " LINK='"+rl.getLink()+"';";
		}
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			RawLink rawl = new RawLink(rs.getString(1));
			arr.add(rawl);
		}
		return arr;
	}
	
	/**
	 * Adds an Arraylist of RawLinks to the database in batch 
	 * to reduce traffic and the number of slow database calls
	 * @param list
	 * @throws SQLException
	 */
	public int[] addList(Collection <? extends RawLink> list) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		int[] returns = new int[list.size()];
		try{
			con.setAutoCommit(false);
																							
			String query = "INSERT INTO rawlinks (LINK,lastAccess) VALUES (?,?);";	//add new entry
			PreparedStatement ps = con.prepareStatement(query);
			for(RawLink rlink : list){												//for every item in list
				ps.setString(1, rlink.link);
				ps.setTimestamp(2, new Timestamp(rlink.dateTime.getMillis()));
				ps.addBatch();													//add sql to batch
			}
			if(list.size() > 0){
				returns = ps.executeBatch();			//execute the batch of statements
				con.commit();
			}
		}catch(SQLException sql){
			con.rollback();
			throw sql;
		}finally{
			//con.close();
		}
		return returns;
	}
	/**
	 * Add array of strings containing urls to database
	 * @param list
	 * @throws SQLException
	 */
	public void addList(String[] list) throws SQLException{
		ArrayList<RawLink> convList = new ArrayList<RawLink>(list.length);
		RawLink rl;
		for(String s : list){
			rl = new RawLink(s);
			convList.add(rl);
		}
		addList(convList);
		
	}
	
	
	
	/**
	 * Returns a list of links above a certain ID from the database
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<RawLink> getLinksAbove(Integer id) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		ArrayList<RawLink> list = new ArrayList<RawLink>(0); 
		try{
				String query = "Select * FROM rawlinks WHERE ID >= ?;"; //get all links above passed id
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
		//	con.close();
		}
		return list;
	}
	
	/**
	 * Returns a Link with the passed ID
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public RawLink getLinkById(Integer id) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		RawLink link = null; 
		try{
				String query = "Select * FROM rawlinks WHERE ID == ?;"; //get the specific link
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					link = new RawLink(rs.getString(2),rs.getInt(1),new DateTime(rs.getTimestamp(3))); //create it
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			//con.close();
		}
		return link;  //return it
	}
	
	/**
	 * Get all links from database from within the specified range INCLUSIVE
	 * @param startID
	 * @param endID
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<RawLink> getLinkRange(Integer startID, Integer endID) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		ArrayList<RawLink> list = new ArrayList<RawLink>(0); 
		try{
				String query = "Select * FROM rawlinks WHERE ID >= ? AND ID <= ?;"; //get all links between the two IDs
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, startID);
				ps.setInt(2, endID);
				ResultSet rs = ps.executeQuery();
				list =  new ArrayList<RawLink>(rs.getFetchSize());
				while(rs.next()){
					RawLink rl = new RawLink(rs.getString(2),rs.getInt(1), new DateTime(rs.getTimestamp(3))); //create them and build list
					list.add(rl);
				}
		}catch(SQLException sql){
			throw sql;
		}finally{
			//con.close();
		}
		return list;
	}

	
	/**
	 * Check for existence of a link in the database
	 * @param link
	 * @return
	 * @throws SQLException
	 */
	public boolean linkExists(String link) throws SQLException{
		Connection con = null; 
		boolean bool = false;
		MyConnection mc;
		
			mc = ConnectionFactory.getConnection();
			con = mc.getCon();
			String query = "Select count(*) FROM rawlinks WHERE LINK==?"; //get links with the passed url
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, link);
			ResultSet rs =  ps.executeQuery();
			bool = rs.next();                  //if there are any return true, else return false
		
		return bool;
	}
	public boolean linkExists() throws SQLException{
		MyConnection mycon = null; 
		boolean bool = false;
		
			mycon = ConnectionFactory.getConnection();
			Connection con = mycon.getCon();
			String query = "Select count(*) FROM rawlinks WHERE LINK=?"; //get links with the passed url
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, this.link);
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

	
	/**
	 * This function will try to fetch the first n unseen links in the database
	 * if n is larger than the queue, it will try return enough to fill the queue
	 * if it is less than 1 it will default to 1
	 * @param n
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<RawLink> getUnseen(int n) throws SQLException{
		MyConnection mcon = ConnectionFactory.getConnection();
		Connection con = mcon.getCon();
		if(n > Constants.MemoryQueueLength){
			n = Constants.MemoryQueueLength;
		} else if (n < 1){
			n = 1;
		}
		ArrayList<RawLink> list = new ArrayList<RawLink>(n);
		String query = "Select * FROM rawlinks WHERE lastAccess=? LIMIT ?";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setTimestamp(1,new Timestamp(new DateTime(1970,1,1,0,0,0,0).getMillis()));
		ps.setInt(2, n);
		ResultSet rs  = ps.executeQuery();
		RawLink rl;
		while(rs.next()){
			rl = new RawLink(rs.getString(2),rs.getInt(1), new DateTime(rs.getTimestamp(3)));
			list.add(rl);
		}
		return list;
		
	}
	public void setLink(String link){
		this.link = link;
	}
	
	public String getLink(){
		return this.link;
	}
	
	public Integer getId(){
		return this.id;
	}
	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
	
	public boolean equals(Object rl){
		if(rl == null) return false;
		if(!(rl instanceof RawLink)) return false;
		return this.link.equals(((RawLink)rl).getLink());
		
	}
	
	public int hashCode(){
		return this.link.hashCode();
	}

	public void checkID() throws SQLException {
		MyConnection mycon = null; 
		boolean bool = false;
		
			mycon = ConnectionFactory.getConnection();
			Connection con = mycon.getCon();
			String query = "Select * FROM rawlinks WHERE LINK=?"; //get links with the passed url
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, this.link);
			ResultSet rs =  ps.executeQuery();
			if( rs.next()){                  //if there are any return true, else return false
				int id = rs.getInt(1);
				this.id = id;
			}
	}
	
}