package mm.smy.bicq.server.db ;

/**
* ֻ������������. ���ڴ���Statement
* ��Ϊ���ǿ��ܻ�Ѳ�ͬ�����ݷ��ڲ�ͬ�����ݿ��У����������ʵ�����ݿ�ĵ�ַ��
* �û�ֻҪָ����Ŀ(name)�������Զ�����Ԥ�ȹ涨�õ�ӳ���ϵ������һ����Ӧ�ñ�Ŀɶ�д��Statement����
* @author XF myreligion@163.com
* @date 2003-11-19
* @copyright Copyright 2003 XF All Rights Reserved
* @also see ReadonlyStatement 
*/

import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.PreparedStatement ;
import java.sql.SQLException ;
import java.sql.ResultSet ;


public class ReadWriteStatement{
	private String name = "" ; //���ǽ������������ȷ��ʹ�õ����ݿ⡣
	private int scroll = ResultSet.TYPE_SCROLL_INSENSITIVE ;
	
	private PreparedStatement psmt = null ;	
	private Statement stmt = null ;
	private DBConnection dbconn = null ;
	private Connection conn = null ;
		
	public ReadWriteStatement(String name){
		this.name = name ;	
	}
	
	/**
	* @param name Ҫ������Ŀ������,�������Ѿ���ǰ�������. ӳ���ϵ��ο� reflect.txt
	* @param type �α����͡���ResultSet.TYPE_SCROLL_INSENSITIVE��ResultSet.TYPE_SCROLL_SENSITIVE��ѡ��Ĭ��Ϊinsensitive
	* 
	*/
	public ReadWriteStatement(String name, int type){
		this.name = name ;
		scroll = type ;
	}
	
	public void setScrollType(int type){
		scroll = type ;	
	}
	
	public int getScrollType(){
		return scroll ;	
	}
	
	/**
	* ��������name����һ�� ֻ�� ��Statement
	* ���һ�����������ε��ø÷���������õ�������ͬ��Statementʵ����ͬʱǰһ��statement���ᱻ�Զ��رա�
	* ����������ȫ���ͷ�. ��close().
	*/
	public Statement getStatement() throws SQLException{
		dbconn = DBConnection.getInstance() ;
		if(conn == null){
			conn = dbconn.getConnection() ;
		}
		if(stmt != null){
			stmt.close() ;	
		}
		stmt = conn.createStatement(scroll, ResultSet.CONCUR_UPDATABLE) ;
		return stmt ;
	}

	//see above
	public PreparedStatement getPreparedStatement(String m_sql) throws SQLException{
		if(dbconn == null)
			dbconn = DBConnection.getInstance() ;
		if(conn == null){
			conn = dbconn.getConnection() ;	
		}
		if(psmt != null){
			psmt.close() ;	
		}
		psmt = conn.prepareStatement(m_sql) ;
		
		return psmt ;
		
	}
	
	public void close(){
		if (stmt != null ){
			try{
				stmt.close();
			}catch(SQLException e){
				BugWriter.log("mm.smy.vote.db.ReadWriteStatement:close():e", e , null) ;
			}
		}
		
		if (psmt != null ){
			try{
				psmt.close();
			}catch(SQLException e){
				BugWriter.log("mm.smy.vote.db.ReadonlyStatement:close():e", e , null) ;
			}
		}
		
		if (conn != null ){
			dbconn.freeConnection(conn) ;
		}
	}
	
}