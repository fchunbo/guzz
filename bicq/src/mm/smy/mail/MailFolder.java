package mm.smy.mail ;

/**
* ���䣬����ʼ�ʹ�ã����������ʼ��ȵ����磬�ļ��Ȳ�����
* ���в��������ڴ��н��С�
*
* @author XF <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/7
*/

import mm.smy.mail.channel.NetItem ;
import mm.smy.mail.channel.FormatException ;
import mm.smy.security.HashAuthenticator ;
import mm.smy.security.AuthenticatorException ;


import mm.smy.mail.event.mailfolder.* ;

import java.io.Serializable ;
import java.util.Vector ;
import java.util.Iterator ;
import java.util.Enumeration ;

import java.security.NoSuchAlgorithmException ;

public class MailFolder implements NetItem, Serializable{
	
	public static MailFolder localroot = null ;
	public static MailFolder INBOX, SENTBOX, DRAFTBOX, TRASHBOX, SERVERBOX ;
	public static int HOLDS_FOLDERS = 456 ;
	public static int HOLDS_MAILS   = 457 ;
	public static int HOLDS_ANY     = 458 ;
	
	static{
		
		
	}
	
	public static UserFolder getFolder(String foldername){ return null ;}
	
	public static UserFolder getFolder(long folderID){ return null ; }
	
	public static MailFolder createNewFolder(MailFolder parent, String name, int type) throws MailFolderException, AuthenticatorException{
		if(type != HOLDS_ANY && type != HOLDS_FOLDERS && type != HOLDS_MAILS)
			throw new MailFolderException("Mail folder type unexpected.") ;
		MailFolder folder = new MailFolder(name, type, false)  ;
		if(parent == null)
			localroot.addChildFolder(folder) ;
		else
			parent.addChildFolder(folder) ;
		return folder ;
	}
	
	/**
	* @param name �ļ�����
	* @param type �ļ�������
	* @param issystemic �Ƿ�Ϊϵͳ�ļ���
	*/
	protected MailFolder(String name, int type, boolean issystemic){
		foldername = name ;
		this.type = type ;
		this.systemic = issystemic ;
	}
	public void setMailContext(MailContext mc){
		this.mc = mc ;
	}
	
	
	public MailFolder getRoot(){ return localroot ; }
	
	public MailFolder getParent(){ return parent ; }
	
	/**
	* @see UserFolder.isBubble()
	*/
	public boolean isBubble(){ return isbubble ; }
	
	/**
	* @see UserFolder.setBubble(boolean isbubble)
	*/
	public void setBubble(boolean m_bubble){ isbubble = m_bubble ; }
	
	public void addChildFolder(MailFolder folder) throws MailFolderException, AuthenticatorException{
		if(needAuthenticator(ADDFOLDER)) throw new AuthenticatorException("No rights to Add new subfoder") ;
		
		if(acceptType(this.HOLDS_FOLDERS)){
			childfolders.add(folder) ;
			this.notifySubfolderAddedListener(folder) ;
		}else{
			throw new MailFolderException(MailFolderException.NO_HOLD_FOLDERS) ;
		}
	}
	
	public void removeChildFolder(MailFolder folder) throws MailFolderException, AuthenticatorException{
		if(needAuthenticator(REMOVEFOLDER)) throw new AuthenticatorException("No rights to Remove subfolder") ;
				
		if(childfolders != null){
			if(!folder.isSystemic()){
				childfolders.remove(folder) ;
				this.notifySubfolderRemovedListener(folder) ;
			}else{
				throw new MailFolderException("ϵͳ�ļ��У�����ɾ����") ;
			}
		}
	}
	
	
	public void addMail(Mail m) throws MailFolderException, AuthenticatorException{
		Mail[] mails = {m} ;
		addMails(mails) ;
	}
	
	public void addMails(Mail[] m) throws MailFolderException, AuthenticatorException{
		if(!acceptType(this.HOLDS_MAILS)) throw new MailFolderException(MailFolderException.NO_HOLD_MAILS) ;
		if(needAuthenticator(WRITE)) throw new AuthenticatorException("Append mail to folder denied, Authenticator needed.") ;
		for(int i = 0 ; i < m.length ; i++){
			mails.add(m[i]) ;
		}
		this.notifyMailAddedListener(m) ;
	}
	
	public String getFolderName(){ return foldername ; }	
	public void setFolderName(String m_name){ foldername = m_name ; }
	
	public long getFolderID(){ return folderID ; }
	
	/**
	* ����ļ�����ϵͳ���ļ��У�������ò���������false��
	* ���򷵻�true��
	* ϵͳ�ļ���Ĭ�ϰ�����INBOX, SENTBOX, DRAFTBOX, TRASHBOX, SERVERBOX
	*/
	public boolean setFolderID(long m_folderID){
		if(isSystemic()) return false ;
	
		folderID = m_folderID ;
		return true ;
	}
	
	/**
	* �Ƿ�Ϊϵͳ�ļ���
	*/
	public boolean isSystemic(){ return systemic ; }	
	protected void setSystemic(boolean issystemic){ systemic = issystemic ; }
	
	/**
	* ���ļ����Ƿ�Ϊָ�����͵��ļ��С�
	* ���磺this.type = HOLDS_ANY, ����HOLDS_FOLDERS��HOLDS_MAILS����ʱ�������档
	* @param m_type �������ͣ�������HOLDS_ANY��HOLDS_FOLDERS��HOLDS_MAILS�е�һ�������򷵻�false��
	*/
	public boolean acceptType(int m_type){
		if(type == HOLDS_ANY) return true ;
		return m_type == type ;
	}
	
	public int getType(){ return type ; }
	
	/**
	* �Ը��ļ��н����������ҵ����з�����������Ŀ���ҷ��ء�
	* �����Ƕ�Mail���в��ң���javax.mail.search.SerchTerm���ǽ���Message���ң����߲��ڼ��ݡ�
	* �����ǵĳ����н�ʹ���Զ����mm.smy.mail.search.SearchTerm��ʹ�÷�����javax���е����ơ�
	* @param term ��������������������null�����������ʼ���
	* @return Vector ����Mail�����Vector
	* @see mm.smy.mail.search.SearchTerm
	*/
//	public Vector search(mm.smy.mail.search.SearchTerm term){
//		return null ;
//	}
	
	/**
	* �����µ��ļ�����֤�������õĹ��������ǲ��ı�ԭ��֤��ĵ�ַ���µ���֤ʵ����
	* �������ǰ�����֤HashAuthenticator�����ϸ��Ƹ�ԭ���ġ�By value ��ֵ��
	* 
	* @param newauth �µ�HashAuthenticator���ϴ�ŵ�ַ�����Ϊnull��ȡ�����ļ��е�������֤��
	* @param oldpassword �����룬�ṩ�޸�ǰ��֤��
	* @param newpassword �޸ĺ�������룬���Ϊnull������ȡ������[��һ��ȡ����֤]��
	* @exception AuthenticatorException ����ṩ�ľ�����������׳���
	*/
	public void setAuthenticator(HashAuthenticator newauth, char[] oldpassword, char[] newpassword) throws AuthenticatorException{
		if(auth == null){
			auth = newauth ;
			return ;
		}
		if(auth.isPasswordOK(oldpassword)){
			if(newauth != null){
				try{
				auth.setPassword(newpassword) ;
				}catch(NoSuchAlgorithmException e){
					throw new AuthenticatorException("ϵͳ����HashAuthenticator�����㷨��ʧ") ;
				}catch(java.io.UnsupportedEncodingException e2){
					throw new AuthenticatorException("ϵͳȱ��ISO-8859-1���룬��ȷ�ϲ���װ���������ϵͳ�޷����С�") ;
				}
				auth.setEchoOn(newauth.isEchoOn()) ;
				auth.setPromt(newauth.getPromt()) ;
			}
			return ;
		}
		throw new AuthenticatorException("���������ǰ������Ϸ��ַ�") ;
	}
	
	public HashAuthenticator getAuthenticator(){ return auth ; }
	
	/**
	* �������´ε����ʱ��Ҫ����֤�����û�������֤������Ϊ�գ��÷���Ч����
	* @param action �������ƣ��ο�needAuthenticator(String)������"all"�ֶΡ�
	* @return boolean true:�ɹ����������´��������롣false:����ʧ�ܡ�
	*/
	public boolean nextNeedPassword(String action){
		password = null ;
		return needAuthenticator(action) ;
	}
	
	/**
	* �Ƿ���Ҫ��֤���жϡ�
	* Ŀǰ���ǵı�׼������������²�Ҫ��֤��1.���ļ���û�ж�����֤ 2.������֤������Ϊ�� 3.�û�ѡ��������ʱ��֤һ�ζ���һ���Ѿ��ɹ���ͨ������֤��
	* @return boolean �Ƿ���Ҫ��֤
	* @param action ����������"listmail","write","delete","rename","addfolder","removefolder","listfolder"
	* ���������壺
	* listmail: ��ȡ�ļ��е��ļ��б�ͬʱ�����˶��ļ���δ�����ʼ��Ķ�ȡȨ����
	* write: �����ļ�����д���µ��ʼ�
	* delete:ɾ���ļ��е��ʼ�
	* rename:�����ļ������ã��ù��̲����Զ��ذѾ��ļ��е�AuthenticatorӦ���ھ��ļ����ϡ�
	* addfolder:�������ļ���
	* removefolder:ɾ�����ļ���
	* listfolder:�г����ļ���Ŀ¼
	*/
	
	public static final String LISTMAIL = "listmail" ;
	public static final String WRITE = "write" ;
	public static final String DELETE = "delete" ;
	public static final String RENAME = "rename" ;
	public static final String ADDFOLDER = "makefolder" ;
	public static final String REMOVEFOLDER = "removefolder" ;
	public static final String LISTFOLDER = "listfolder" ;
	
	public boolean needAuthenticator(String action){
		if(!"listmail".equalsIgnoreCase(action)) return false ; //�������"read"����������ֱ�ӷ���false��Ŀǰ���ṩ֧�֡�
		
		if(auth == null) return false ;
		if(auth.isPasswordOK(null)) return false ;
		if(password != null) return false ;
		return true ;
	}
	
	//�ر��ļ���
	public void close(){
		//close connections like tcp, jdbc...
	}
	
	public Iterator getMails() throws AuthenticatorException {
		if(needAuthenticator(LISTMAIL)) throw new AuthenticatorException("you have no rights to list mails in this folder") ;
		return mails.iterator() ;
	}
	public Iterator getChildFolders()throws AuthenticatorException {
		if(needAuthenticator(LISTFOLDER)) throw new AuthenticatorException("Access subfolders denied.") ;
		return childfolders.iterator() ;		
	}
	
/*---------------------listeners manager---------------------------------------*/
	//�ļ����¼�
	public void addFolderListener(MailFolderListener listener){
		folderlisteners.add(listener) ;
	}
	public void removeFolderListener(MailFolderListener listener){
		folderlisteners.remove(listener) ;
	}
	//�ʼ��ı�
	public void addMailChangedListener(MailChangedListener listener){
		mailchangelisteners.add(listener) ;
	}
	public void removeMailChangedListener(MailChangedListener listener){
		mailchangelisteners.remove(listener) ;
	}
	//�ʼ���Ŀ�仯, ���/ɾ���ʼ�
	public void addMailCountListener(MailCountListener listener){
		mailcountlisteners.add(listener) ;
	}
	public void removeMailCountListener(MailCountListener listener){
		mailcountlisteners.remove(listener) ;
	}
	
	/**
	* �ѱ��ļ������ļ��е�����¼����ݳ�ȥ��
	* @param source ����ӵ��ļ��С�
	*/
	protected void notifySubfolderAddedListener(MailFolder source){
		MailFolderEvent event = new MailFolderEvent(this, source, MailFolderEvent.FOLDER_CREATED ) ;
		dispatchFolderAddedEvent(event) ;
	}	
	public void dispatchFolderAddedEvent(MailFolderEvent event){
		Enumeration e = folderlisteners.elements() ;
		while(e.hasMoreElements()){
			MailFolderListener listener = (MailFolderListener) e.nextElement() ;
			listener.mailFolderCreated(event) ;		
		}
		//dispatch to the parent
		if(getParent() != null || isBubble()){
			getParent().dispatchFolderAddedEvent(event) ;			
		}
	}
	
	/**
	* �ѱ��ļ������ļ��е�ɾ���¼����ݳ�ȥ��
	* @param source ��ɾ�����ļ��С�
	*/
	protected void notifySubfolderRemovedListener(MailFolder source){
		MailFolderEvent event = new MailFolderEvent(this, source, MailFolderEvent.FOLDER_REMOVED ) ;
		dispatchFolderRemovedEvent(event) ;
	}
	public void dispatchFolderRemovedEvent(MailFolderEvent event){
		Enumeration e = folderlisteners.elements() ;
		while(e.hasMoreElements()){
			MailFolderListener listener = (MailFolderListener) e.nextElement() ;
			listener.mailFolderRemoved(event) ;		
		}
		//dispatch to the parent
		if(getParent() != null || isBubble()){
			getParent().dispatchFolderRemovedEvent(event) ;			
		}
	}
	
	/**
	* �ѱ��ļ��еı䶯������ȥ
	* @param newfolder ���ļ��м������ĳɵ�����
	*/
	protected void notifyFolderChangedListener(MailFolder newfolder){
		MailFolderEvent event = new MailFolderEvent(this, this , newfolder, MailFolderEvent.FOLDER_CHANGED) ;
		dispatchFolderChangedEvent(event) ;
	}
	public void dispatchFolderChangedEvent(MailFolderEvent event){
		Enumeration e = folderlisteners.elements() ;
		while(e.hasMoreElements()){
			MailFolderListener listener = (MailFolderListener) e.nextElement() ;
			listener.mailFolderChanged(event) ;		
		}
		//dispatch to the parent
		if(getParent() != null || isBubble()){
			getParent().dispatchFolderChangedEvent(event) ;			
		}		
	}
	/////////////////mail part listeners//////////////////////////////////
	
	/**
	* �ʼ��ı��¼���
	* @param mail �Ķ��Ժ���ʼ�
	* @param type �Ķ�����
	* @see MailChangedEvent
	*/
	protected void notifyMailChangedListener(Mail mail, int type){
		MailChangedEvent event = new MailChangedEvent(this, mail, type) ;
		dispatchMailChangedEvent(event) ;
	}	
	public void dispatchMailChangedEvent(MailChangedEvent event){
		Enumeration e = mailchangelisteners.elements() ;
		while(e.hasMoreElements()){
			MailChangedListener listener = (MailChangedListener) e.nextElement() ;
			listener.mailChangedAction(event) ;
		}
		//bubble to the parent
		if(getParent() != null && isBubble()){
			getParent().dispatchMailChangedEvent(event) ;
		}
	}
	
	protected void notifyMailAddedListener(Mail[] mails){
		MailCountEvent event = new MailCountEvent(this, mails, MailCountEvent.MAIL_ADDED) ;
		dispatchMailAddedEvent(event) ;
	}	
	public void dispatchMailAddedEvent(MailCountEvent event){
		Enumeration e = mailcountlisteners.elements() ;
		while(e.hasMoreElements()){
			MailCountListener listener = (MailCountListener) e.nextElement() ;
			listener.mailAdded(event) ;
		}
		//bubble to the parent
		if(getParent() != null && isBubble()){
			getParent().dispatchMailAddedEvent(event) ;
		}
	}
	
	protected void notifyMailRemovedListener(Mail[] mails){
		MailCountEvent event = new MailCountEvent(this, mails, MailCountEvent.MAIL_REMOVED) ;
		dispatchMailRemovedEvent(event) ;
	}
	public void dispatchMailRemovedEvent(MailCountEvent event){
		Enumeration e = mailcountlisteners.elements() ;
		while(e.hasMoreElements()){
			MailCountListener listener = (MailCountListener) e.nextElement() ;
			listener.mailRemoved(event) ;
		}
		//bubble to the parent
		if(getParent() != null && isBubble()){
			getParent().dispatchMailRemovedEvent(event) ;
		}
	}
	
	/**
	* �ļ����¼����м�����
	*/
	protected Vector folderlisteners = new Vector() ;
	protected Vector mailchangelisteners = new Vector() ;
	protected Vector mailcountlisteners = new Vector() ;


/*---------------------implements NetItem--------------------------------------*/
	public byte[] toBytes() throws FormatException{
		return null ;
	}
	
	public Object toObject(byte[] b) throws FormatException{
		return null ;
	}
	
	private Vector mails = new Vector() ;
	private Vector childfolders = new Vector() ;
	
	private String foldername = "new folder" ; //folder name
	private long folderID = -1 ;
	private boolean systemic = false ; //whether this folder is systemic
	protected int type ;
	protected MailContext mc = null ;
	private HashAuthenticator auth = null ;//�����֤��֧�ֵ����ļ��еļ���
	private char[] password = null ; //���ļ��е����룬���������Ϊ����������
	private boolean password_everyclick = true ; //ÿ����ʾ���ļ��е����ݶ�Ҫ�����롣
	
	private boolean isbubble = true ; 
	private MailFolder parent = null ;
	
}
