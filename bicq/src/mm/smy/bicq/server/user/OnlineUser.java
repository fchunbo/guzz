package mm.smy.bicq.server.user ;

/**
* �����û����ڴ��б���Ĳ������ݡ�
* �ò�����Ϊ���Ż� ���������û� �� �쿴�������ϵ����ݿ���ҡ�
* ��������û���IP,port�����������Ǵ�TempUser�̳����ġ�
* 
* @also see mm.smy.bicq.search.TempUser ;
*/

import mm.smy.bicq.search.TempUser ;
import java.io.Serializable ;

public class OnlineUser extends TempUser implements Serializable{
	
	private long ID = -1 ; //���û������ݿ��user������ռ��¼��ID��š�
	
	public OnlineUser(){	
	
	}
	
	public long getRecordID(){ return ID ; }
	
	public void setRecordID(long m_ID) { ID = m_ID ; }
	
}
