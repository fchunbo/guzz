package mm.smy.mail ;

/**
* �ʼ��ͻ��Ļ����������е��������MainManager�ࡣ��������и���Account�ĳ�ʼ����
* ��Ȼ���������ʼ��������ܹ�������BICQ�����У��������ڻ��ﲻ��������������Ҫ����Ϊ
* �ܶණ��Ҫ��BICQ�������ṩ֧�֣�Ҳ��ҪBICQ�������
* ���ǰ���BICQ��ϵ���ܵĵط����������˸����С�
*
* @author XF <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/8
* @see mm.smy.bicq.MainManager
*/

import java.util.Properties ;

public class MailContext implements java.io.Serializable{
	
	/**
	* �����ⲿ��ʼ����ϣ��������ʼ��ͻ��ˡ�
	* 
	*/
	public void startService(){
		
	}
	
	/**
	* ���MailContext���󡣲���PropertiesӦ�ð���������Ŀ��
	* <b>initfile</b> :���ļ�ָ���˷�������ʼ�����ļ���ַ��
	* <pre>���ļ��а������mail:�ʼ������ļ���ַ��Ĭ��Ϊusername.mail
	*                        account:�˺ű����ַ��Ĭ��Ϊusername.account
	*                        contact:JavaMail��ַ�����ò�����JavaMail��Account�Լ�ָ�������ֻ��Ϊ�˷��㡣Ĭ��Ϊusername.contact
	* </pre>
	* ���Ƕ�����������ļ��ṩ����֧�֡�
	* <b>username</b> :�û�����Ҫ��ΪString������Bicq����bicq�ţ������������ļ��С�
	* <b>password</b> :���룬������Ϊ�������룡�������ṩ��Bicq��ʹ���ļ���ͳһ���ܡ�
	* password�����Ǽ򵥵�String���󣬶���mm.smy.security.HashAuthenticator����
	* <b>userfolder</b>Ĭ�ϵ�ʹ���ļ��У����initfileʹ��ʧ��[��Ϊnull�����м���Ŀ��ȱ�����]
	* ���ǽ���ʹ�ø��ļ��С����ļ��л��������ݸ������ϣ����Ҫ��Ϊ�ա�
	* �����ָ�����ļ������Ѿ�������Ҫ��Ŀ����initfileû��ָ��������ʹ����Щ��Ŀ��
	* �����Щ��Ŀ���Ϸ������ᴴ�����������ļ���Ȼ���޸�Properties����
	* ��֮�������÷����󣬴���ĺ������ᱻ�޸ĳɰ����������ݵĸ�ʽ��
	* �������Ĳ����а����в��Ϸ��Ҳ����Զ������ĳɷ֣�����null.
	* ���಻��singleģʽ��ÿ�η����µ�ʵ������null.
	* <b>mm</b>MainManager��ʵ�������ֻ���չ��Ե�Apaterģʽ�������Ժ�ı䡣
	* ������BICQ������ͨ�š�
	* @param Properties p :��ʼ������
	* @return MailContext :��initfile����Ϊnull�������κ���Ϊ�ն�������null��
	*/
	public static MailContext getMailContext(Properties p){		
		return null ;
	}
	
	
	
	
	
	
}
