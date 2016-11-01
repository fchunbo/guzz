package mm.smy.bicq.message ;

import java.io.Serializable ;
import mm.smy.bicq.user.* ;

/**
* ����ϳ��������⣬Ϊ��ʵ�ָ��õĿ���չ�ԣ����Ǽ���OtherMessage��ʵ�֡�
* ���ܿ�����ͨ������Message��Ȼ������Լ���Message����������Щ�˷�ʱ��
* 
* ����ͨ��OtherMessageΪ�Ƕ������Ϣ��������ڡ�
* ���еĵڶ��ο����õ���Ϣ��Ҫ�̳и��࣬��ʵ�ֶ�OtherMessage�Ŀ��ơ�
* ����Ļ������ܻ���� �� ��ƥ��ȴ���ʹ�������
* ���Ǹ����޷�ʵ��
*
*
*/

public class OtherMessage implements Message,Serializable{
	protected byte[] b = null ;
	protected User from = null ;
	protected User to  =  null ;
	protected int mintype = MessageType.UNKNOWN_TYPE ;
	
	public byte[] getByteContent() {
		return b ;
	}

	public int getType() {
		return MessageType.UNKNOWN_TYPE ;
	}

	public int getMinType() {
		return mintype ;
	}

	public User getFrom() {
		return from ;
	}

	public User getTo() {
		return to ;
	}

	public void setFrom(User u) {
		from = u ;
	}

	public void setTo(User u) {
		to = u ;
	}

	public void setByteContent(byte[] b) {
		this.b = b ;
	}
	
}