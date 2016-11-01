package mm.smy.mail.event ;

/**
* ������FolderChanged��һЩ�����������޲�����
* 
* @author XF <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/7
* @see MailFolderChangedEvent UserFolderChangedEvent
*/


public interface FolderEventConstances extends  java.io.Serializable{
	/**
	* �ļ��н����¼�����ʵ���Ǹ��ļ���������ļ��е��¼���
	* ���¼��ɸ��ļ��з������¼��а����ĵ��ļ���ָ���½����ļ��С� 
	*/
	public static final int FOLDER_CREATED = 56 ;
	
	/**
	* �ļ���ɾ���¼����ɱ�ɾ�ļ��еĸ��ļ��з���
	* �¼���ָ��ɾ�����ļ��� 
	*/
	public static final int FOLDER_REMOVED = 57 ;
	
	/**
	* �ļ��и������ã��ɱ����ĵ��ļ��з����¼���
	* ������ο��������ʵ���ࡣ
	*/
	public static final int FOLDER_CHANGED = 58 ;	
}