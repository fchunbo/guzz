package mm.smy.bicq.user.manager ;

/**
* ���������
* �û��ļ���������������ʱ��guests,guestgroups�ĳ�ʼ����
* �ṩ�˶Ժ������ϱ���Ŀ�ݷ�ʽ��
*
* ���ദ��User���ϵ�����������գ�stateChanged,Permit���⡣
*
* @author XF
* @also see mm.smy.bicq.user.manager.UserFileManager
* @date 2003-10-23
*
*/

import java.util.Hashtable ;
import java.util.Vector ;
import java.util.Enumeration ;
import java.util.Date ;

import mm.smy.bicq.user.* ;
import mm.smy.bicq.message.* ;
import mm.smy.bicq.MainManager ;
import mm.smy.bicq.message.UserInforMessageListener;
import mm.smy.bicq.message.MessageListener;
import mm.smy.util.*;
import mm.smy.bicq.search.TempUser ;

public class UserNetManager implements MessageListener{
	public static final int DEFAULT_INTERVAL = 50 ;
	public static final long DEFAULT_TOTAL_TIME = 20000 ; //default timeout 20s
	
	public static final int WAITING = 1 ;
	public static final int FINISHED = 2 ;
	public static final int NET_ERROR = 3 ;
	public static final int TIMEOUT = 4 ;
	public static final int UNDEFINE = 0 ;
	
	private Hashtable guestgroups = new Hashtable(6) ;
	private Hashtable guests = new Hashtable(40) ;
	private Host host = null ;
	private Host newhost = null ; //����Ҫ�����newhost�����ϣ������Ϊ�ݴ��á�
	
	private Hashtable isGuestTimeOut = new Hashtable(40) ; //���ǿ���Ҫ����º��ѵ����ϣ�������͸ø��¡����ұ�־��Ϣ�Ƿ��յ���
	private int isHostTimeOut  = this.UNDEFINE ;
	private int isLoadAllMyGuestsTimeOut = this.UNDEFINE ;

	private MainManager m = null ;
	
	public UserNetManager(MainManager m_mm){
		m = m_mm ;
		this.host = m.getHost() ;
		newhost = this.host ;
		m.addMessageListener(this) ;
	}
	
	/**
	* ����UserInforMessage��Ȼ���͸�����������ʾ���¸������ϵ���������
	*/
	public void writeHostNet(Host m_host){
		if(m_host == null ) return ;
		
		newhost = m_host ;
		
		
		UserInforMessage message = new UserInforMessage() ;
		message.setUser(m_host) ;
		message.setFrom(host) ;
		message.setTo(m.getServer()) ;
		message.setMinType(UserInforMessage.UPDATE_HOST_INFOR) ;
		
		System.out.println("write host to net:" + (Host) message.getUser()) ;
		m.sendOutMessage(message) ;
		this.isHostTimeOut = this.WAITING ;
		
	}

	//������������������к������ϵ���Ϣ
	public void readNet(){
		ICMPMessage icmp = new ICMPMessage() ;
		icmp.setMinType(ICMPMessage.LOAD_ALL_GUESTS) ;
		icmp.setFrom(host) ;
		icmp.setTo(m.getServer()) ;
		m.sendOutMessage(icmp) ;
		
		this.isLoadAllMyGuestsTimeOut = this.WAITING ;
	}
	public void readGuestNet(Guest u){
		if( u == null) return ;
		
		//if(isGuestTimeOut.containsKey(new Integer(u.getNumber()))) return ;
		
		isGuestTimeOut.put(new Integer(u.getNumber()),new Integer(this.WAITING)) ;
		
		ICMPMessage icmp = new ICMPMessage() ;
		icmp.setMinType(ICMPMessage.LOAD_SINGLE_GUEST_INFOR) ;
		icmp.setFrom(host) ;
		icmp.setTo(m.getServer()) ;
		icmp.setContent(new Integer(u.getNumber()).toString()) ;
		
		m.sendOutMessage(icmp) ;
	}

	public void readHostNet(){
		ICMPMessage icmp = new ICMPMessage() ;
		icmp.setMinType(ICMPMessage.LOAD_HOST_INFOR) ;
		icmp.setContent(new Integer(host.getNumber()).toString()) ;
		m.sendOutMessage(icmp) ;
		this.isHostTimeOut = this.WAITING ;
	}

	
	public int getHosResult(){ return isHostTimeOut ; }
	
	public int getGuestResult(int m_number){
		Object o = isGuestTimeOut.get(new Integer(m_number)) ;
		if(o == null) return this.UNDEFINE ;
		return ((Integer) o).intValue() ;
	}
	
	public int getGuestResult(Guest g){
		return getGuestResult(g.getNumber()) ;	
	}
	
	//����״̬��������timeout��������û��Լ��жϡ�
	public int getLoadAllState(){
		return this.isLoadAllMyGuestsTimeOut ;	
	}
	
	public Hashtable getGuestGroups(){
		return guestgroups ;
	}
	
	public Hashtable getGuests(){
		return guests ;
	}
	
	//If the guestgroups is inited using the local data, we need to set this
	public void setGuestGroups(Hashtable h){
		guestgroups = h ;	
	}
	
	public void setGuests(Hashtable h){
		guests = h ;	
	}
	
	//implements methods
	
	public void messageAction(ReceivedMessage rm) {
		System.out.println("user net mangage receive a message.....  " + rm.getType() ) ;
		try{
			switch(rm.getType()){
				case MessageType.USER_INFOR_MESSAGE :
					UserInforMessage message = new UserInforMessage() ;
					message.setByteContent(rm.getContent()) ;
					message.setFrom(m.getGuest(rm.getFrom())) ;
					//message
					changeUserInforResult(message) ;
					break ;
				case MessageType.ICMP_MESSAGE :
					ICMPMessage icmp = new ICMPMessage() ;
					icmp.setByteContent(rm.getContent()) ;
					icmp.setFrom(m.getGuest(rm.getFrom())) ;
					icmp.setTo(m.getGuest(rm.getTo())) ;
					icmpMessageResult(icmp) ;
					break ;
				case MessageType.LOAD_GUEST_RESULT_MESSAGE :
					LoadGuestResultMessage lgrm = new LoadGuestResultMessage() ;
					lgrm.setByteContent(rm.getContent()) ;
					lgrm.setFrom(m.getGuest(rm.getFrom())) ;
					lgrm.setTo(m.getGuest(rm.getTo())) ;
					loadGuestResult(lgrm) ;
					break ;
				default:
					break ;
			}
		}catch(Exception e){
			mm.smy.bicq.debug.BugWriter.log("usernetmanager",e,"messageAction()��������������ת�����ִ���") ;	
		}
		return ;
	}
	
	private void icmpMessageResult(ICMPMessage message){
		if(message == null)	return ;
		if(message.getMinType() == ICMPMessage.UPDATE_HOST_INFOR_RESULT){
			try{
				int i = new Integer(message.getContent().trim()).intValue() ;
				if(i == 1){ //�������ϳɹ����£���������д��Ӳ�̡�
					this.isHostTimeOut = this.FINISHED ;
					m.setHost(newhost) ;
					System.out.println("update the host infor successful by UserNetManager.") ;
					System.out.println("current host:" + m.getHost()) ;
				}
				else if(i == 2) 
					this.isHostTimeOut = this.NET_ERROR ;
			}catch(Exception e){
				mm.smy.bicq.debug.BugWriter.log("usernetmanager",e,"�޷����õ���ICMPMessage.content���ת����intֵ���õ���ֵ�ǣ�" + message.getContent()) ;	
			}
		}
		return ;
	}
	
	/**
	* ��User Infor update ��Ϣ�Ĵ���
	* ͬʱ���ñ��Ϊ ��ɸ���
	*/
	private void changeUserInforResult(UserInforMessage message){
		System.out.println("run to unt:cuir()") ;
		System.out.println("guests:" + guests) ;
		System.out.println("message.getUser():" + message.getUser()) ;
		if (message == null) return ;
		if(guests == null) return ;
		if(message.getUser() == null) return ;
		System.out.println("UserNetManager changeUserInforResult got a message, user:" + message.getUser()) ;
		
		if(message.getMinType() == UserInforMessage.UPDATE_HOST_INFOR){
			System.out.println("unm got a message to update the host's information.") ;
			Host h = (Host) message.getUser() ;
			//host = null ;
			newhost = h ;
			System.out.println("unm reports:\n\n\n\n") ;
			System.out.println("host email:" + h.getMail()) ;
			isHostTimeOut = this.FINISHED ;
			m.setHost(h) ;
			
			return ;
		}

/*		
		//����server�ķ�����������ĸĽ�������suggestion.txt
		if(message.getMinType() == UserInforMessage.UPDATE_GUEST_INFOR){
			Guest g = (Guest) message.getUser() ;
			if(g.equals(m.getServer())){ //������
				m.setServer(g) ;
			}
		}
*/		
		//System.out.println("") ;
		Enumeration e = guests.elements() ;
		while(e.hasMoreElements()){
			User u = (User) e.nextElement() ;
			if(u.equals(message.getUser())){
				if(message.getMinType() == UserInforMessage.UPDATE_GUEST_INFOR){
					System.out.println("unm got a message to update the guest's information") ;
					Guest g = (Guest) message.getUser() ;
					//ע���ַ�ĸı䣬���ǲ��ܼ򵥵ĸ���һ��Guest������ΪGuestGroup����ָ���Guest�����ǲ����ġ�
					//�����ְ취��һ�����µ�Guest�� ���� ����ԭ�������ݡ���һ����ͬ���ĸı�Guest����GuestGroup��
					//������������������������Guest����Ļ�����ô�õڶ��ְ취�����ܸı�ԭ�����õ�ֵ������������
					//��һ�ַ�����ֱ���޸�ԭ�����õ����ݡ�
					//guests.put(new Integer(g.getNumber()) , g) ;
					Guest oldg = m.getGuest(g.getNumber()) ;
					
					oldg = oldg.copyInfor(g) ;
					
					isGuestTimeOut.put(new Integer(u.getNumber()),new Integer(this.FINISHED)) ;
					m.getUserManager().saveGuests() ;
				}
			}
		}
		return ;
	}
	
	/**
	* ��½��ʱ�����ǿ��ܻ�Ҫ���������е��û���
	* ����ϢΪ������Ļش���Ϣ�Ľ��ͳ���
	* 
	* 
	*/
	private void loadGuestResult(LoadGuestResultMessage message){
		if(message == null) return ;
		
		this.isLoadAllMyGuestsTimeOut = this.FINISHED ;	

			
	//	if(message.getTempUserNumbers() == 0 ) return ;
	//  �������ܴ������⣬����û����һ�����Ѷ�û��
	//  ��ô���Ǿͻ���������أ�������ȥ��ͼ����Ĭ�ϵ��顣��Ĭ����Ľ���ʹ�����ɵġ�
	//  ���������û�û���飬Ȼ��UserManager��setupGuests()�����ỳ�����糬ʱ��
	//  ��Ϊ�Ķ��������û��ʲô���������û��ǳ�ʱ������������ˡ�
		
		
		if(guests == null) guests = new Hashtable(40) ;
		else guests.clear() ;
		
		//init guestgroups, put the guests in the �ҵĺ��� guestgroup
		if(guestgroups ==null) guestgroups = new Hashtable(5) ;
		
		if(guestgroups.size() == 0 ){
			GuestGroup gg1 = new GuestGroup("�ҵĺ���") ;
			gg1.setCreateTime(new Date(82,11,23)) ;
			gg1.setIsSystemic(true) ;
			GuestGroup gg2 = new GuestGroup("İ����") ;
			gg2.setCreateTime(new Date(83,1,4)) ;
			gg2.setIsSystemic(true) ;
			GuestGroup gg3 = new GuestGroup("������") ;
			gg3.setCreateTime(new Date(84,0,30)) ;
			gg3.setIsSystemic(true) ;
			
			guestgroups.put(gg1.getGroupname(),gg1) ;
			guestgroups.put(gg2.getGroupname(),gg2) ;
			guestgroups.put(gg3.getGroupname(),gg3) ;
		}
		
		GuestGroup myfriend = (GuestGroup) guestgroups.get("�ҵĺ���") ;
		if(myfriend == null){
			myfriend = new GuestGroup("�ҵĺ���") ;
			myfriend.setCreateTime(new Date(1983,2,4)) ;
			myfriend.setIsSystemic(true) ;
			guestgroups.put(myfriend.getGroupname(),myfriend) ;
		}
		
		Vector v = message.getTempUsers() ;
		Enumeration e = v.elements() ;
		while(e.hasMoreElements()){
			
			TempUser t = (TempUser) e.nextElement() ;
			if( t == null) continue ;
			
			Guest g = new Guest() ;
			g.setNumber(t.getNumber()) ;
			g.setNickname(t.getNickname()) ;
			g.setState(t.getState()) ;
			g.setIP(t.getIP()) ;
			g.setGender(t.getGender()) ;
			g.setPortrait(t.getPortrait()) ;
			g.setProvince(t.getFrom()) ;
			g.joinGuestGroup(myfriend) ;
			guests.put(new Integer(t.getNumber()),g) ;			
		}
		
		m.getUserManager().saveGuests() ;
		
	System.out.println("unm got the read net reply. setup guestgroups OK:guestgroups:" + guestgroups) ;
	System.out.println("guests��" + guests) ;			
	}
	
}
