package mm.smy.mail.event.mailuser ;

/**
* �ļ����е���ϵ�˵����Ϸ����仯ʱ�ļ�����
* ���������ļ��������ϵı仯����������Ŀ�ı仯��
* 
* @author XF <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/7
*/

import java.util.EventObject ;
import mm.smy.mail.MailUser ;

public class MailUserChangedEvent extends EventObject implements java.io.Serializable{
	
	protected MailUser olduser = null ;
	protected MailUser newuser = null ;
	
	/**
	* 
	* 
	* @param source the object where this event is created.
	* @param olduser the MailUser before modified
	* @param newuser the MailUser used to replace the old one. 
	*/
	public MailUserChangedEvent(Object source, MailUser olduser, MailUser newuser){
		super(source) ;
		this.olduser = olduser ; 
		this.newuser = newuser ;
	}
	
	public MailUser getOldUser(){ return olduser ; }
	
	public MailUser getNewUser(){ return newuser ; }
	
}
