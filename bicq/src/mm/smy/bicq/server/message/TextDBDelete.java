package mm.smy.bicq.server.message ;

/**
* TextMessage�����ݿ��ȡ
* 
* 
* @author XF
* @date 2003-11-21
* 
*/

import mm.smy.bicq.server.db.ReadWriteStatement ;

import java.sql.SQLException ;
import java.sql.PreparedStatement ;

import java.util.Date ;
import java.util.Vector ;

public class TextDBDelete{
	
	private ReadWriteStatement ro = null ;
	private PreparedStatement pstm = null ;
	
	public TextDBDelete(){
		
	}
	
	/**
	* �����û�number��ɾ������ ���͸� ���û���TextMessage������Ϣ��
	* ���Զ�ε��øú�����ÿ�����²쿴���ݿ⡣ʹ��PreparedStatement����ÿ�ε��ú󲻹ر����ӡ�
	* �����ε��ÿ������Ч�ʣ�����ע���������ʱ����close()�����ͷ�jdbc��Դ��
	* @param to Ҫɾ���� ���͸� ���û���BICQ��
	* @return boolean pstm.execute()�ķ��ؽ����
	*/
	public boolean deleteByNumber(int to) throws SQLException{
		if(ro == null){
			ro = new ReadWriteStatement("text") ;	
		}
		if(pstm == null)
			pstm = ro.getPreparedStatement("delete from textmessage where tonumber = ? ") ;
		else
			pstm.clearParameters() ;
		pstm.setInt(1,to) ;
		
		return pstm.execute() ;
		
	}
	
	public void close(){
		if(ro != null)
			ro.close() ;
		if(pstm != null){
			try{
				pstm.close() ;
			}catch(Exception e){
			}
		}
		return ;
	}

		
	
	
	
}