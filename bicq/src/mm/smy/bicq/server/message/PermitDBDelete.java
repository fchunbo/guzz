package mm.smy.bicq.server.message ;

/**
* PermitMessage��ɾ��
* 
* 
* @author XF
* @date 2003-11-21
* 
*/

import mm.smy.bicq.server.db.ReadWriteStatement ;

import java.sql.SQLException ;
import java.sql.PreparedStatement ;

import java.util.Vector ;

public class PermitDBDelete{
	
	private ReadWriteStatement ro = null ;
	private PreparedStatement pstm = null ;
	
	public PermitDBDelete(){
		
	}
	
	/**
	* �����û�number��ɾ������ ���͸� ���û���PermitMessage������Ϣ��
	* ���Զ�ε��øú�����ÿ�����²쿴���ݿ⡣ʹ��PreparedStatement����ÿ�ε��ú󲻹ر����ӡ�
	* �����ε��ÿ������Ч�ʣ�����ע���������ʱ����close()�����ͷ�jdbc��Դ��
	* @param to Ҫɾ���� ���͸� ���û���BICQ��
	* @return boolean pstm.execute()�ķ��ؽ����
	*/
	public boolean deleteByNumber(int to) throws SQLException{
		if(ro == null){
			ro = new ReadWriteStatement("permit") ;	
		}
		if(pstm == null)
			pstm = ro.getPreparedStatement("delete from permit where tonumber = ? ") ;
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