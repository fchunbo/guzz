package mm.smy.mail.event.mailfolder ;

/**
* Mail folder events listener
*
* @author XF <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/8
*/

public interface MailChangedListener extends java.io.Serializable{
	public void mailChangedAction(MailChangedEvent event) ;

}
