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

import mm.smy.bicq.server.db.ReadWriteStatement ;

import java.sql.SQLException ;
import java.sql.PreparedStatement ;

public class MyFriendsDBDelete{
	
	private ReadWriteStatement rw = null ;
	private PreparedStatement delete = null ;
	static String sql = "delete from friend where belongnumber = ? and friendnumber = ? " ;
	
	public MyFriendsDBDelete(){
		
	}
	
	/**
	* ɾ������
	* @param belongnumber ���������û�����
	* @param friendnumber ���ѵĺ���
	* @return ִ�к�Ӱ��ļ�¼����
	* @throws SQLException
	*/
	public int deleteFriend(int belongnumber, int friendnumber) throws SQLException{
		if(rw == null)
			rw = new ReadWriteStatement("friend") ;
		if(delete == null)
			delete = rw.getPreparedStatement(sql) ;
		else
			delete.clearParameters() ;
			
		delete.setInt(1, belongnumber) ;
		delete.setInt(2, friendnumber) ;
		
		return delete.executeUpdate() ;
	}
	
	
	public void close(){
		if(rw != null)
			rw.close() ;
		if(delete != null){
			try{
				delete.close() ;
			}catch(Exception e){
			}
		}
	}

}
