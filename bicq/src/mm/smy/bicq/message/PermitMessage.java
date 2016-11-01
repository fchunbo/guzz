package mm.smy.bicq.message ;

import mm.smy.bicq.user.*;
import java.io.* ;

/**
* ��Ϣ��֤��������Ϊ��������/��Ϊ���Ѿܾ��ȵ�
*
*
*
*
*@author XF
*@author e-mail:myreligion@163.com
*@copyright Copyright 2003 XF All Rights Reserved.
*/

public class PermitMessage implements Message,Serializable {
	public static final int PERMIT_REQUEST = 601 ; //��Ҫ�����֤
	public static final int PERMIT_SEND    = 602 ; //���������֤
	public static final int PERMIT_ALLOWED = 603 ; //�����Ϊ����
	public static final int PERMIT_REFUSED = 604 ; //�ܾ�����
	
	private User from = null ;
	private User to   = null ;
	private int mintype  = MessageType.UNKNOWN_TYPE ;
	private String content = "" ; //������
	
	public PermitMessage(){
		
	}
//////////////////////////////////////////////////////////////////////////////////////////////////	
	public void setMintype(int m_type){
		mintype = m_type ;
	}
	
	public void setContent(String s){
		content = s ;
	}
	
	public String getContent(){ return content ; }
	
/////////////////////////////////////////////////////////////////////////////////////////////////	
	public int getType() {	return MessageType.PERMIT_MESSAGE ;	}

	public int getMinType() { return mintype ; }

	public User getFrom() {	return from ; }

	public User getTo() { return to ; }
	
	public void setFrom(User u){ from = u ; }
	
	public void setTo(User u){ to = u ; }
	
	public byte[] getByteContent() {
		byte[] back = null ;
		ByteArrayOutputStream bout = new ByteArrayOutputStream() ;
		DataOutputStream dos = new DataOutputStream(bout) ;
		try{
			dos.writeInt(mintype) ;
			dos.writeUTF(content == null?"":content) ;
			back = bout.toByteArray() ;
		}catch(Exception e){
			System.out.println("PermitMessage throws an Exception while convert to byte[]==>" + e.getMessage() ) ;
		}finally{
			try{
				dos.close() ;
				bout.close() ;
			}catch(Exception e1){}		
			
		}
		return back ;
	}

	public void setByteContent(byte[] b) {
		if (b == null) return ;
		
		ByteArrayInputStream bin = new ByteArrayInputStream(b) ;
		DataInputStream dis = new DataInputStream(bin) ;
		try{
			mintype = dis.readInt() ;
			content = dis.readUTF() ;
		}catch(Exception e){
			System.out.println("PermitMessage throws an Exception while convert byte[] back ==>" + e.getMessage() ) ;
		}finally{
			try{
				dis.close() ;
				bin.close() ;
			}catch(Exception e1){
			
			}		
		}
		return ;
	}
	
}
