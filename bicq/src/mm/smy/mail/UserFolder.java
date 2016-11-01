package mm.smy.mail ;

/**
* ��ϵ���ļ��У������ϵ�˻������ļ��С�
* �����NetItem���л���������SerializableĬ�ϵ�writeObjectʵ�ֵģ���ʱû�ж����Լ������л���
* @author XF  <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/6-7
*/

import mm.smy.mail.channel.NetItem ;
import mm.smy.mail.channel.FormatException ;
import mm.smy.mail.event.mailuser.* ;

import java.util.Iterator ;
import java.util.LinkedList ;
import java.util.Vector ;
import java.util.Iterator ;

import java.io.ByteArrayInputStream ;
import java.io.ByteArrayOutputStream ;
import java.io.ObjectInputStream ;
import java.io.ObjectOutputStream ;
import java.io.IOException ;


import javax.swing.JLabel ;

public class UserFolder implements java.io.Serializable,NetItem,Cloneable{
/*	
	public static void main(String[] args){
		System.out.println(UserFolder.createNewFolder(null,"aaa").getRoot().getFolderID()) ;
	}
*/	
	public static UserFolder root = null ;
	
	public UserFolder getRoot(){ return root ; }
	
	static{
		root = new UserFolder(null,"��ϵ�˵�ַ��") ;
		root.folderID = 510275L ;
		root.setBubble(false) ;
	}
	
	protected UserFolder(UserFolder parent, String name){
		this.parent = parent ;
		this.foldername = name ;
		this.folderID = System.currentTimeMillis() ;
	}
	
	/**
	* �����������������UserFolder����
	* Ϊ�˹���ķ��㣬����Ҫ��ÿ��UserFolder��parent��Ϊ�գ��Ҷ���root֮�¡�
	* ����root��parent is null.
	* �ڽ����Ĺ����У��丸�ļ��н��ᷢ��UserFolderEvent 
	* 
	* @param parent Ҫ�����ļ��еĸ��ļ��У����Ϊnull������root���档 
	* @param foldername �ļ��е�����
	* @see UserFolderEvent
	*/
	public static UserFolder createNewFolder(UserFolder parent, String foldername) throws UserFolderException{
		UserFolder temp = parent ; //��ֹ����ĸĶ�Ӱ�쵽�����parent����
		if(temp == null)
			temp = root ;		
		UserFolder newfolder = new UserFolder(temp, foldername) ;
		temp.addChildFolder(newfolder) ;
		
		return temp ;
	}
	
	/**
	* �û��ļ��е�ID��Ψһ�ı�ʾһ���ļ��У�һ������Ӧ�ñ��ֲ��䡣
	*/
	public long getFolderID(){ return folderID ;}
	
	/**
	* �����û��ļ��е�ID���������root�ļ��н���������
	* @param id �û��ļ��е���ID
	* @return boolean ���Ҫ���������ļ���Ϊroot�����ǽ�Ҫ������id==root.getFolderID()������false�����򷵻�true.
	*/
	public boolean setFolderID(long id){
		if(this == root || id == root.getFolderID() )
			return false ;
		folderID = id ;
		return true ;
	}
	
	public String getFolderName(){ return foldername ; }
	
	public void setFolderName(String m_foldername){foldername = m_foldername ; }
	
	public UserFolder getParent(){return parent ;}
	
	public void setParent(UserFolder folder){ parent = folder ; }
	
	/**
	* ����ļ��е�ͼ�α�ʾ�������ʼʱû�ж���ͼ�ν��棬���� new JLabel(foldername) ;
	* ����ͼ�ν��棬����ͨ��setRender(JLabel panel)ʵ�֡�
	* 
	*/
	public JLabel getRender(){
		if(render == null)
			return new JLabel(foldername) ;
		else
			return render ;
	}
	
	/**
	* ������ļ��е�ͼ�ν��档
	* @param panel ��������ʾ��ʾthis�ļ��е�ͼ�ν��棬���Ϊnull����ȡ�����涨�壬����Ĭ�ϵķ�ʽ��ʾ��
	*/
	public void setRender(JLabel label){
		this.render = label ;
	}
	
/*------------------MailUser---------------------------------*/	
	public Iterator getMailUsers(){
		return mailusers.iterator() ;	
	}
	
	public void addMailUsers(MailUser[] user) throws UserFolderException{	
		if(user == null) return ;
		
		mailusers.add(user) ;
		this.notifyMailUserAddedListener(user) ;
	}
	
	public void addMailUser(MailUser user) throws UserFolderException{
		MailUser[] users = {user} ;
		addMailUsers(users) ;	
	}
	
	public void removeMailUsers(MailUser[] user) throws UserFolderException{ 
		if(user == null) return ;
		
		mailusers.remove(user) ;
		this.notifyMailUserRemovedListener(user) ;
	}
	public void removeMailUser(MailUser user)throws UserFolderException{
		MailUser[] users = {user} ;
		removeMailUsers(users) ;	
	}
	
	/**
	* ������ϵ�����ϡ�
	* @param olduser ��ǰ������
	* @param newuser �µ�����
	*/
	public void updateMailUser(MailUser olduser, MailUser newuser) throws UserFolderException{
		mailusers.remove(olduser) ;
		mailusers.add(newuser) ;
		this.notifyMailUserChangedListener(olduser, newuser) ;	
	}
/*------------------UserFolder---------------------------------*/	
	
	public Iterator getChildFolders(){
		return childfolders.iterator() ;	
	}
	
	public void addChildFolder(UserFolder folder) throws UserFolderException{
		if(folder == null) return ;
		
		childfolders.add(folder) ;
		this.notifyFolderAddedListener(folder) ;	
	}
	
	public void removeChildFolder(UserFolder folder) throws UserFolderException{
		if(folder == null) return ;
		
		childfolders.remove(folder) ;
		this.notifyFolderRemovedListener(folder) ;
	}
	
	/**
	* �÷���Ӧ��С��ʹ�ã��µ��ļ��н��᳹�׵ĸ��Ǿɵ��ļ��У����Ҳ������κμ�������
	* ����м�����Ҫ����ע�ᣬӦ���յ�UserFolderEvent[FOLDER_CHANGED]������µ�ע�ᡣ
	* �÷����ĵڶ���������ѡ���Ƿ񽫾��ļ��е����ļ�����ϵ����ӵ��µ��ļ����С�
	* �����ӣ������ļ����н������MailCountChangedEvent��UserFolderEvent[FOLDER_CREATED]��
	* ���������¼��ķ�������ھ��ļ��з�����UserFolderChangedEvent
	* �ɵ��ļ��У�����Ҫ��ȡ�����ļ��У�Ϊ��ǰ���ø÷������ļ���[this����]��
	* ����ɵ��ļ���Ϊroot[getParent() == null]���÷���ֱ�ӷ���null�������κβ�����
	* @param newfolder �µ��ļ��У�����ȡ���ɵ��ļ���
	* @param savechildren Ϊtrueʱ������ļ��е����ļ�������ϵ�˵��µ��ļ��С�
	* @return UserFolder �ɵ��ļ��С�
	* @see MailCountChangedEvent UserFolderEvent
	*/
	public UserFolder updateFolder(UserFolder newfolder, boolean savechildren) throws UserFolderException{
		if(getParent() == null) return null ;
		
		getParent().removeChildFolder(this) ;
		getParent().addChildFolder(newfolder) ;			
		this.notifyFolderChangedListener(this, newfolder) ;
		
		if(savechildren){ //save oldfolder's children to the newfolder
			Iterator i = this.getChildFolders() ;
			while(i != null && i.hasNext()){
				UserFolder temp = (UserFolder) i.next() ;	
				newfolder.addChildFolder(temp) ;
				temp.setParent(newfolder) ;
			}
			
			MailUser[] users = (MailUser[]) mailusers.toArray(new MailUser[mailusers.size()]) ;
			for(int j = 0 ; j < users.length ; j++){
				users[j].setFolder(newfolder) ;
			}
			newfolder.addMailUsers(users) ;
		}
		return this ;	
	}

	
	/**
	* ���ø��ļ��е��¼��Ƿ�����ϲ��ļ��д�����
	* Ĭ����������ϴ�����
	* ����FolderChangedEvent��MailCountChangedEvent
	* @param eventup �Ƿ���¼����ϴ�����
	* @see FolderChangedEvent MailCountChangedEvent 
	*/
	public void setBubble(boolean eventup){
		isbubble = eventup ;
	}
	
	public boolean isBubble(){ return isbubble ; }
	
	/**
	* �����ļ��иĶ����ã����Ǹ��ļ��е����ļ��н�����ɾ��ʱ�����¼�
	* �����ע�����Щ�¼��ļ�����
	*
	* @param listener Ҫ�������� 
	* @see UserFolderChangedListener
	*/
	public void addFolderListener(UserFolderListener listener){
		folderchangedlisteners.add(listener) ;
	}
	
	public void removeFolderListener(UserFolderListener listener){
		folderchangedlisteners.remove(listener) ;	
	}

	/**
	* fire MailFolder changed listeners
	* @param oldfolder ���Ķ���MailFolder
	* @param newfolder �ú��MailFolder
	*/
	protected void notifyFolderChangedListener(UserFolder oldfolder, UserFolder newfolder){
		UserFolderEvent event = new UserFolderEvent(this,oldfolder,newfolder,UserFolderEvent.FOLDER_CHANGED) ;
		dispatchFolderChangedEvent(event) ;	
	}
	protected void notifyFolderAddedListener(UserFolder source){
		UserFolderEvent event = new UserFolderEvent(this,source, UserFolderEvent.FOLDER_CREATED) ;
		dispatchFolderAddedEvent(event) ;
	}
	protected void notifyFolderRemovedListener(UserFolder source){
		UserFolderEvent event = new UserFolderEvent(this,source, UserFolderEvent.FOLDER_REMOVED) ;
		dispatchFolderRemovedEvent(event) ;	
	}
	
	public void dispatchFolderChangedEvent(UserFolderEvent event){
		Iterator i = this.getFolderChangedListeners() ;
		UserFolderListener listener ;
		while(i != null && i.hasNext()){
			listener = (UserFolderListener) i.next() ;
			listener.userFolderChanged(event) ;
		}
		
		if(isBubble() && getParent() != null){
			getParent().dispatchFolderChangedEvent(event) ;
		}		
	}
	
	public void dispatchFolderAddedEvent(UserFolderEvent event){
		Iterator i = this.getFolderChangedListeners() ;
		UserFolderListener listener ;
		while(i != null && i.hasNext()){
			listener = (UserFolderListener) i.next() ;
			listener.userFolderCreated(event) ;
		}
		
		if(isBubble() && getParent() != null){
			getParent().dispatchFolderAddedEvent(event) ;
		}
	}
	
	public void dispatchFolderRemovedEvent(UserFolderEvent event){
		Iterator i = this.getFolderChangedListeners() ;
		UserFolderListener listener ;
		while(i != null && i.hasNext()){
			listener = (UserFolderListener) i.next() ;
			listener.userFolderRemoved(event) ;
		}
		
		if(isBubble() && getParent() != null){
			getParent().dispatchFolderRemovedEvent(event) ;
		}	
	}
	
	/**
	* �����ļ����е���ϵ��[���������ļ���]��Ŀ�����ı�ʱ����
	* register such event's listener here. 
	* @see MailUserCountChangedListener
	*/
	public void addMailUserCountChangedListener(MailUserCountChangedListener listener){
		usercountlisteners.add(listener) ;
	}
	
	public void removeMailUserCountChangedListener(MailUserCountChangedListener listener){
		usercountlisteners.remove(listener) ;
	}

	public void addMailUserChangedListener(MailUserChangedListener listener){
		userchangedlisteners.add(listener) ;
	}
	
	public void removeMailUserChangedListener(MailUserChangedListener listener){
		userchangedlisteners.remove(listener) ;	
	}	
	
	/**
	* fire mailuser changed listeners
	* @param olduser ���Ķ���MailUser
	* @param newuser �ú��MailUser
	*/
	protected void notifyMailUserChangedListener(MailUser olduser, MailUser newuser){
		MailUserChangedEvent event = new MailUserChangedEvent(this,olduser, newuser) ;	
		dispatchMailUserChangedEvent(event) ;
	}

	protected void notifyMailUserAddedListener(MailUser[] source){
		MailUserCountChangedEvent event = new MailUserCountChangedEvent(this, source, MailUserCountChangedEvent.USER_ADDED) ;
		dispatchMailUserAddedEvent(event) ;	
	}
	
	protected void notifyMailUserRemovedListener(MailUser[] source){
		MailUserCountChangedEvent event = new MailUserCountChangedEvent(this, source, MailUserCountChangedEvent.USER_REMOVED) ;
		dispatchMailUserRemovedEvent(event) ;
	}
	
	/**
	* ���¼����͸����ļ����ߡ�ͬʱ��� ���� �����ϸ������¼����͸����ĸ���
	* ������ϴ��ݡ�
	* @param event Ҫ�������¼�
	* @see MailUserChangedEvent
	*/
	public void dispatchMailUserChangedEvent(MailUserChangedEvent event){
		Iterator i = this.getMailUserChangedListeners() ;
		while(i != null && i.hasNext()){
			MailUserChangedListener listener = (MailUserChangedListener) i.next() ;
			listener.mailUserChangedAction(event) ;
		}
		if(isBubble() && getParent() != null){
			getParent().dispatchMailUserChangedEvent(event) ;	
		}
	}
	
	public void dispatchMailUserAddedEvent(MailUserCountChangedEvent event){
		Iterator i = this.getMailUserCountListeners() ;
		while(i != null && i.hasNext()){
			MailUserCountChangedListener listener = (MailUserCountChangedListener) i.next() ;
			listener.mailUserCountAdded(event) ;	
		}
		if(isBubble() && getParent() != null){
			getParent().dispatchMailUserAddedEvent(event) ;	
		}
	}
	
	public void dispatchMailUserRemovedEvent(MailUserCountChangedEvent event){
		Iterator i = this.getMailUserCountListeners() ;
		while(i != null && i.hasNext()){
			MailUserCountChangedListener listener = (MailUserCountChangedListener) i.next() ;
			listener.mailUserCountRemoved(event) ;	
		}
		if(isBubble() && getParent() != null){
			getParent().dispatchMailUserRemovedEvent(event) ;	
		}
	}
	
	public Iterator getMailUserCountListeners(){
		return usercountlisteners.iterator() ;	
	}
	
	public Iterator getMailUserChangedListeners(){
		return userchangedlisteners.iterator() ;	
	}
	
	public Iterator getFolderChangedListeners(){
		return folderchangedlisteners.iterator() ;	
	}

/*-**********************implements NetItem********************************************************/
	public byte[] toBytes() throws FormatException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream(128) ;
		byte[] back = null ;
		ObjectOutputStream out = null ;
		try{
			out = new ObjectOutputStream(bout) ;
			out.writeObject(this) ;
			back = bout.toByteArray() ;
		}catch(Exception e){
			throw new FormatException(e) ;
		}finally{
			try{
				out.close() ;
				bout.close() ;
			}catch(Exception e2){}
		}		 
		return back ;
	}
	
	public Object toObject(byte[] b) throws FormatException {
		if(b == null) throw new FormatException("the given byte[] is null") ;
		
		ByteArrayInputStream bin = new ByteArrayInputStream(b) ;
		Object back = null ;
		ObjectInputStream in = null ;
		try{
			in = new ObjectInputStream(bin) ;
			back = in.readObject() ;
		}catch(Exception e){
			throw new FormatException(e) ;	
		}finally{
			try{
				in.close() ;
				bin.close() ;
			}catch(Exception e2){}
		}
		return back ;	
	}
/*-*****************************override methods***********************************************/
	/**
	* ��¡����������Object.clone()���п�¡��Ȼ��folderID��ΪSystem.currentTimeMillis().
	* @return Object ��ʵ��UserFolder����
	* @exception CloneNotSupportedException ��ʵCloneable�Ѿ�ʵ���ˣ�Ӧ�ò����׳�^_^. 
	*/
	public Object clone() throws CloneNotSupportedException{
		UserFolder folder = (UserFolder) super.clone() ;
		folder.setFolderID(System.currentTimeMillis()) ;
		return folder ;
	}
	
	/**
	* ����folderID�ıȽϣ���ͬ�Ļ�����ture, else return false.
	* if(folder == null) return false ;
	*/
	public boolean equals(UserFolder folder){
		if(folder == this) return true ;
		if(folder == null) return false ;
		return folder.getFolderID() == this.getFolderID() ;	
	}
	
	protected LinkedList usercountlisteners = new LinkedList() ;
	protected LinkedList userchangedlisteners = new LinkedList() ;
	protected LinkedList folderchangedlisteners = new LinkedList() ;
	
	private long folderID = -1 ; //Ψһ�ı�ʾһ���ļ��У����ø���
	private UserFolder parent = root ; //Ĭ��Ϊroot
	private String foldername = null ;
	private boolean isbubble = true ; //�Ƿ��¼����ϲ㴫����
	private JLabel render = null ;
	
	protected LinkedList mailusers = new LinkedList() ;
	protected Vector childfolders = new Vector() ;	
	
}