package mm.smy.bicq.server ;

/**
* server parts
* mainmanager
* 
* 
* 
* 
* 
*/

import java.io.* ;
import java.util.* ;
import java.net.InetAddress ;

import mm.smy.bicq.Monitor;
import mm.smy.bicq.SendMessage;
import mm.smy.bicq.user.* ;
import mm.smy.bicq.message.* ;
import mm.smy.bicq.search.* ;
import mm.smy.bicq.user.manager.* ;

import mm.smy.bicq.login.LoginException ;

public class MainManager implements Serializable{
	
	private Monitor monitor = null ;
	private SendMessage sm  = null ;
	public static final int SERVER_NUMBER =  1000 ;	
	private Guest server ;
	
	public Guest getServer(){ return server ; }
	
	public void setServer(Guest m_server) { server = m_server ; }
	
	/**
	* �ɹ���½ǰ��׼��������Ϣ��������Ϣ�����̡߳�
	*/
	public void openSocket(int m_sendport) throws IOException, SecurityException{
		sm = new SendMessage(m_sendport) ;
		sm.setHost(host) ;
		sm.setServer(this.getServer().getIP(),this.getServer().getPort()) ;
		monitor = new Monitor(this) ;
		monitor.setIsInited(true) ;
		monitor.setTimeOut(1) ;
		Thread t = new Thread(monitor) ;
		t.start() ;	
	}
	

	public void close(){
		if(sm != null) sm.close() ;
		sm = null ;
		if(monitor != null) monitor.close() ;
		monitor = null ;
	}
	
/**************************************************************************************************/
//usernetmanager methods

	

//��Ϣ����  ��ڡ��м䴦��

private Vector ml    = new Vector(10) ;  //Message Listener.
	
	//message
	public boolean addMessageListener(MessageListener m_ml){
		return ml.add(m_ml) ;
	}
	public boolean removeMessageListener(MessageListener m_ml){
		return ml.remove(m_ml) ;
	}		
	
//the Monitor send received message.
	public void sendReceivedMessage(ReceivedMessage rm){
		System.out.println("Receive Message gets a message.") ;
		//TODO:Analyse the ReceivedMessage,dispatch it to diffent parts.
		Enumeration e = null ;
		int m_type = rm.getType() ;
		
		Guest m_from = this.getGuest(rm.getFrom()) ;
		
		//��Ϊ���������ǵ���ģ������ھ������⣬�������Ƕ�����һ��Enumeration e �Ϳ�������
		
				if (ml != null && ml.size() > 0 ){
					e = ml.elements() ;
					while(e.hasMoreElements()){
						((MessageListener) e.nextElement()).messageAction(tm) ;
					}
				}
						
	

		return ;
	}
	
//Exception sent from the monitor at runtime.
	public void sendMonitorException(Exception e){
		//System.out.println("Minitor has got an Exception==>" + e.getMessage() ) ;
		
	}
	
	
	public static final int ERROR   = 10 ;
	public static final int WARNING = 11 ;
	public static final int HINT    = 12 ;
	public static final int DEBUG   = 13 ;
	/**
	* Exception sent from other parts of BICQ in runntime.
	* @param message �û�ָ������Ϣ����
	* @param e       �쳣����
	* @param type    �쳣����������
	*/
	public void sendException(String message, Exception e , int type){
		System.out.println("Received an Exception:" + message + "||type:" + type + "||Exception trace:" + e.getMessage()) ; ;	
		
	}
//send out messages.... A lot of message types...
	public void sendOutMessage(Message message){
		try{
			sm.send(message) ;
		}catch(Exception e){
			System.out.println("Error to send out message to net") ;
		}	
	}


}



