package mm.smy.mail.event.mailuser ;

/**
* һ���ļ����е���ϵ�˸����仯�����������ļ��е���Ŀ�仯��
* 
* @author XF <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/7
*/

import mm.smy.mail.MailUser ;

public class MailUserCountChangedEvent extends java.util.EventObject implements java.io.Serializable{
	
	public static final int USER_ADDED = 89 ;
	public static final int USER_REMOVED = 90 ;
	
	protected MailUser users[] = null ;
	protected int type ;
	
	/**
	* @param source this is used for EventObject; the objet where this event is created
	* @param user   the user added/removed
	* @param type   Type of the event, must be USER_ADD or USER_REMOVED.
	*/
	public MailUserCountChangedEvent(Object source, MailUser[] users, int type){
		super(source) ;	
		this.users = users ;
		this.type = type ;
	}
	
	public int getType(){ return type ; }
	
	public MailUser[] getMailUsers(){ return users ; }
}
