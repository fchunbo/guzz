package mm.smy.bicq ;

import java.util.* ;
import java.io.IOException ;
import mm.smy.bicq.message.* ;
import mm.smy.bicq.user.* ;

/**
* ���촰�ڹ����ṩ���������촰�ڵĹ���������ɾ�������أ�etc...
* ������MainManger�����ã�����Ϣ���б�Ҫ�Ĵ���Ȼ����MainManger����
* ChatWindow�Ӹ�ʹ�ø�������÷����������Ϣ��
*
* ����ʹ��addTextMessageListener(..), addStateChangedMessageListener(....)
*
* ��ChatWindow��Ҫ������û����ϵ���Ϣʱ�����ø���ķ�����
*
*
* ����ΪMainManger��ֱ�ӹ������֮һ������ʵ������������ʾ�����ͣ����������¼�ȵȡ�
*
* �ڱ��������¼��ʱ�򣬲��û��巽�������Ч�ʡ�
* ע�⣺Guest,Host���������ִ��棬��ȡʱҪ�ָ���
* 
*/

public class ChatWindowManager implements TextMessageListener,StateChangedMessageListener{
	private MainManager mm = null ;
	private Hashtable chatwindows = new Hashtable(5) ; //number VS chatwindow
	private TextMessage[] textmessages = new TextMessage[10] ; //ÿ10�µ���Ϣ�����浽����һ�Ρ���Ϊ����
	private boolean isCacheEnable = true ; //��Ϊ���ʱ���ڱ���ʱʹ�û��壻����Ļ������˳�����ʱ�����������е���Ϣ��
	private int i = 0 ; //��¼��ǰ�������е���Ϣ������


//constructors
	public ChatWindowManager(MainManager m_mm){
		mm = m_mm ;
		//��Ӽ�����
		mm.addTextMessageListener(this) ;
		mm.addStateChangedMessageListener(this) ;
	}
//user deal....
	public Host getHost(){
		return mm.getHost() ;
		//return new Host(5000,"host_nickname") ;
	}
	
	public MainManager getMainManager(){
		return mm ;	
	}
	
//chatwindow manager.....
	/**
	*ͨ�������� User���󣬷���ChatWindow����
	*�����ChatWindow�����ڣ������µġ�
	*/
	private ChatWindow getChatWindow(User u){
//		System.out.println("getChatWindow() starts..") ;
//		System.out.println("User is null:" + (u == null) ) ;
		Integer n = new Integer(u.getNumber()) ;
		ChatWindow back = (ChatWindow) chatwindows.get(n) ;
		if(back == null){ //not exsits
//			System.out.println("chatwindow is null") ;
			back = new ChatWindow(this,u) ;
			chatwindows.put(n,back) ;
		}
//		System.out.println("getChatWindow() finished...") ;
		return back ;
	}
	
	//��ȡ��ʷ�����¼,����TextMessage�Ķ���
	public Vector getTextMessages(User u){
		if (u == null) return null ;
		
		Vector v = null ;
		//////////////////////////////////////////////////Maybe we should deal with the host here....... Later.
		TextMessageFile readf = new TextMessageFile(u.getNumber()) ;
		try{
			v = readf.read(u) ;
			readf.close() ;
		}catch(IOException e){
			mm.sendException("��ȡ�����¼ʱ����IOException",e, MainManager.ERROR) ;
		}catch(ClassNotFoundException e1){
			mm.sendException("��ȡ�����¼ʱ����ClassNotFoundException",e1,MainManager.DEBUG) ;
		}
		return v ;
	}
	
	public void closeSingeWindow(Integer i){
		if(i == null) return ;
		ChatWindow window = (ChatWindow) chatwindows.remove(i) ;
		if(window == null) return ;
		window.dispose() ;
		window = null ;
	}
	
	public Vector getTextMessages(User u,int count){
		if (u == null) return null ;
		
		Vector v = null ;
		//////////////////////////////////////////////////Maybe we should deal with the host here....... Later.
		TextMessageFile readf = new TextMessageFile(u.getNumber()) ;
		readf.setMainManager(mm) ;
		try{
			v = readf.read(u,count) ;
			readf.close() ;
		}catch(IOException e){
			mm.sendException("��ȡ�����¼ʱ����IOException",e, MainManager.ERROR) ;
		}catch(ClassNotFoundException e1){
			mm.sendException("��ȡ�����¼ʱ����ClassNotFoundException",e1,MainManager.DEBUG) ;
		}
		return v ;
	}
		
	private void saveTextMessage(TextMessage tm){
		//���������¼��ע�⻺�塣�ڱ���ʱ��File�ļ���������Զ����Ե�Ϊnull��TextMessage.
		textmessages[i] = tm ;
		i++ ;
		if(i >= textmessages.length || isCacheEnable == false){ //save
//System.out.println("saveTextMessage funtion works.") ;
			TextMessageFile savef = new TextMessageFile(this.getHost().getNumber()) ;
			try{
				savef.save(textmessages) ;
				savef.close() ;
			}catch(java.io.IOException e){
				mm.sendException("���������¼ʱ����IOException",e,MainManager.ERROR) ;	
			//	System.out.println("IOException while saving tm==>" + e.getMessage() ) ;
			}
			//��ջ�����
			for(int i = 0 ; i < textmessages.length ; i++){
				textmessages[i] = null ;
			}
			i = 0 ;
		}
	}
	
	//�Ƿ񻺳���Ϣ
	public void setCacheEnabled(boolean m_c){
		isCacheEnable = m_c ;
	}
	
	/**
	* �رա�
	* ���滺�����е��������ݡ�
	* �����е�ChatWindowΪnull, let gc works.
	*/
	public void close(){
		setCacheEnabled(false) ;
		saveTextMessage(null) ;
			
		if (chatwindows != null){
			Enumeration e = chatwindows.elements() ;
			while(e.hasMoreElements()){
				Object o = e.nextElement() ;
				o = null ;
			}
		}
	}

//�ܹ����ָ���û����ڴ��ڵ�״̬��
public static final int CHATWINDOW_QUICK_MODE = 2005 ;
public static final int CHATWINDOW_FULL_MODE = 2006 ;
public static final int CHATWINDOW_HIDDEN = 2000 ;
public static final int CHATWINDOW_ICONED = 2001 ;
public static final int CHATWINDOW_MINIMIZED = 2002 ;
public static final int CHATWINDOW_MAXIMUMED = 2003 ;
public static final int CHATWINDOW_NOT_EXSIT = 2004 ;
	
	/**
	* ����ChatWindow������private .. getChatWindow(User u)��ȣ������cw������
	* �ͷ���null����getChatWindow(User u)���Զ������µĴ��ڡ�
	*
	*/
	public ChatWindow getOutChatWindow(User u){
		if(u == null) return null ;
		
		Object temp_cw = chatwindows.get( new Integer(u.getNumber()) ) ;
		if(temp_cw == null){
			return null ;
		}
		return (ChatWindow) temp_cw ;
	}
	
//implements actions
	public void textMessageAction(TextMessage tm){ //mm will send text message here.
	
//	System.out.println("cwm has received a TextMessage:" + tm.getContent() ) ;
//	System.out.println("from:" + tm.getFrom().getNumber()) ;
//	System.out.println("to:" + tm.getTo().getNumber()) ;

		ChatWindow cw = getOutChatWindow(tm.getFrom()) ;
		if (cw != null){
			cw.sendTextMessage(tm) ;
			if (cw.isShowing() == false){
				//TODO:add some code here to tell the user this window has got a new message.
				//cw.show() ;
				mm.getMainFrame().fix() ;
			}
		}else{
			System.out.println("cwm adds an unread textmessage") ;
			this.addUnreadTextMessage(tm) ;
			tm.getFrom().incUnreadMessages() ;
			
			try{ //����п��ܳ���NullPointException��ԭ������
				mm.getMainFrame().fix() ;
			}catch(Exception e){
				//do nothing
			}
		}
		saveTextMessage(tm) ;
		return ;
	}
	public void stateChangedMessageAction(StateChangedMessage scm){
		ChatWindow cw = getOutChatWindow(scm.getFrom()) ;
		if(cw != null){
			cw.sendStateChangedMessage(scm) ;
		}
		return ;
	}
	
	/**
	* �û�����Ҫ�����ĳ���û������촰�ڡ�
	* �ô��ڿ��ܲ����ڣ�Ҳ�����Ѿ���
	* ���ฺ��ѡ�񣬲��ҰѴ�����ʾ������
	* �����û�������ĳ������������
	*
	* ��������ý��������mm�У���ķ�������ֱ�ӵ��ø÷�����
	*
	*/
	public void showChatWindow(User u){

		ChatWindow temp_cw = getChatWindow(u) ;
		if(temp_cw != null){
			if(!temp_cw.isActive()){
				temp_cw.show() ;
			}
		}
		return ;
	}
	
/**
* For ChatWindow sends TextMessage out to the MainManager.
* 
* 
* 
*/
	public void sendOutTextMessage(TextMessage tm){
		tm.setFrom(mm.getHost()) ;
		
		System.out.println("cwm sendOutTextMessage() send out an message.") ;
		this.saveTextMessage(tm) ;
		mm.sendOutMessage(tm) ;
		return ;
	}
/*	
	public ChatWindowManager(){}
	
	public static void main(String[] args){
		User u = new User(3000,"����") ;
		ChatWindowManager cwm = new ChatWindowManager() ;
		
		ChatWindow cw = cwm.getChatWindow(u) ;
		cw.setSize(400,400) ;
		cw.show() ;
		
	}
	
	

	
	public void test(User u){
		System.out.println("==================get guest:" + u.getNumber()) ;
		ChatWindow cw = this.getChatWindow(u) ;
		cw.setSize(300,300) ;
		cw.show() ;		
	}
	
*/	
/***********************************************************************************************************/
/**
* ����δ����Ϣ��
*
*
*
*/
	private LinkedList unreadmessages = new LinkedList() ;
	
	private void addUnreadTextMessage(TextMessage tm){
		if(tm == null) return ;
		
		unreadmessages.add(tm) ;
	}
	
	public TextMessage getUnreadTextMessage(User u){
		if(u == null) return null ;
		
		TextMessage temp_tm = null ;
		Iterator iter = unreadmessages.iterator() ;
		
		while(iter.hasNext()){
			temp_tm = (TextMessage) iter.next() ;
			if(temp_tm.getFrom().equals(u)){
				iter.remove() ;
				return temp_tm ;
			}			
		}
		
		return null ;
	}
/**********************************************************************************************************/

	
}


