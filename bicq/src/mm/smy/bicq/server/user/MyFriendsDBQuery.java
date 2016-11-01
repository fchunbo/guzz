package mm.smy.bicq.server.user ;

/**
* �����ҵĺ��ѡ�����û���½ʱ�����Լ�����
* �Լ���StateChangedMessage���͸��������ġ�
* 
* 
* @author XF
* @date 2003-11-22
* 
* 
* 
* 
*/

import mm.smy.bicq.server.db.* ;

import java.sql.SQLException ;
import java.sql.PreparedStatement ;
import java.sql.ResultSet ;


import java.util.Date ;
import java.util.Vector ;

public class MyFriendsDBQuery{
	private PreparedStatement onlinesearch = null ;
	private PreparedStatement statesearch = null ;
	
	private ReadonlyStatement ro = null ;
	private ResultSet rs = null ;
	
	public MyFriendsDBQuery(){
		
	}
	
	/**
	* �û�״̬�ı�ʱ֪ͨ����ѡ�
	* �ú�����ð�����Ϊ���ѵ��û���number���ϡ�
	* @param number �ı�״̬���û���number
	* @return Vector���ϡ���number��Ϊ���ѵ��û���number[new Integer(number)]���ϡ�
	* 
	* 
	*/
	public Vector selectInState(int number) throws SQLException{
		if(ro == null)
			ro = new ReadonlyStatement("friend") ;
		if(statesearch == null)
			statesearch = ro.getPreparedStatement("select belongnumber from friend where friendnumber = ? ") ;
		else
			statesearch.clearParameters() ;
		statesearch.setInt(1,number) ;
		
		
		rs = statesearch.executeQuery() ;
		if(rs == null) return null ;
		
		Vector v = new Vector() ;
		
		while(rs.next()){
				v.add(new Integer(rs.getInt(1))) ;	
		}
		rs.close() ;
		return v ; 
	}
	
	/**
	* �û���½ʱ�����û��ĺ��ѡ�
	* @param number �û���number
	* @return Vector �û��ĺ��ѵ�m_number���ɵ�new Integer(m_number)���ϡ�
	*/
	public Vector selectInOnline(int number) throws SQLException{
		if(ro == null)
			ro = new ReadonlyStatement("friend") ;
		if(onlinesearch == null)
			onlinesearch = ro.getPreparedStatement("select friendnumber from friend where belongnumber = ? ") ;
		else
			onlinesearch.clearParameters() ;
		onlinesearch.setInt(1,number) ;
		
		rs = onlinesearch.executeQuery() ;
		if(rs == null) return null ;
				
		Vector v = new Vector() ;
		while(rs.next()){
			v.add(new Integer(rs.getInt(1))) ;	
		}
		
		rs.close() ;
		
		return v ;
	}
	
	public void close(){
		if(ro != null)
			ro.close() ;
		if(onlinesearch != null){
			try{
				onlinesearch.close() ;
			}catch(Exception e){
			}
		}
		if(statesearch != null){
			try{
				statesearch.close() ;	
			}catch(Exception e){
			}
		}
		return ;
	}


		
}
