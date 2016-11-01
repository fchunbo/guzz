package mm.smy.bicq.server.manager ;

/**
* ��������Ϣ����֮һ������UserInforMessage
* �����û������Լ������ϡ��û�ֻ�ܸ����Լ������ϣ����Ҫ���ĵ��û������ߣ����������������ϣ���
* ��ô�ò����Ƿ������ش�����Ϣ��
* 
* @author XF
* @date 2003-11-24
* @copyright Copyright 2003 XF All Rights Reserved
*/

import mm.smy.bicq.server.StartServer ;

import mm.smy.bicq.server.db.BugWriter ;

import mm.smy.bicq.server.user.UpdateUserDB ;
import mm.smy.bicq.server.user.OnlineManager ;
import mm.smy.bicq.server.user.OnlineUser ;
import mm.smy.bicq.server.user.ServerGuest ;

import mm.smy.bicq.message.MessageType ;
import mm.smy.bicq.message.ReceivedMessage ;
import mm.smy.bicq.message.UserInforMessage ;
import mm.smy.bicq.message.ICMPMessage ;

import mm.smy.bicq.user.Host ;

import java.sql.SQLException ;

public class UserInforManager{
	
	private StartServer ss = null ;
	private OnlineManager manager = null ;
	
	
	public UserInforManager(StartServer m_ss){
		ss = m_ss ;
		manager = ss.getOnlineManager() ;
	}
	
	public void messageAction(ReceivedMessage rm){
		if(rm == null) return ;	
		if(rm.getType() != MessageType.USER_INFOR_MESSAGE) return ;
		
		OnlineUser user = manager.getOnlineUser(rm.getFrom()) ;
		if(user == null){ //user not log online
			//�û�û�е�½�����ǲ�֪�����ĵ�ַ~~~~��just return.
			System.out.println("UserInforManager Reports: �յ�һ�����¸���������Ϣ�����û����������ߣ�������Ϣ���û�Ϊ��" + rm.getFrom() ) ;
			return ;
		}
		
		ICMPMessage icmp = new ICMPMessage() ;
		icmp.setMinType(ICMPMessage.UPDATE_HOST_INFOR_RESULT) ;
		
		UserInforMessage message = new UserInforMessage() ;
		message.setByteContent(rm.getContent()) ;
		
		Host host = null ;
		try{
			host = (Host) message.getUser() ;
		}catch(Exception e){
			System.out.println("UserInforManager Reports: ת����Hostʱ���������û���" + rm.getFrom() ) ;

			icmp.setContent(new Integer(2).toString()) ;
			ss.sendMessage(icmp.getByteContent(), icmp.getType(), 1000, rm.getFrom(), user.getIP(), user.getPort() ) ;
			return ;
		}
		
		System.out.println("host:" + host) ;
		System.out.println("user in message:" + message.getUser() ) ;
		
		if(host == null) return ;
		
		ServerGuest guest = new ServerGuest() ;
		guest.setUser(message.getUser()) ;
		//���������host�����е����ԡ�
		guest.setAuth(host.getAuth()) ;
		
		guest.setNumber(rm.getFrom()) ; //�������¶�������û��޸��������ϣ���
		
		boolean success = false ;
		UpdateUserDB update = new UpdateUserDB(guest) ;
		try{
			update.update() ;
			success = true ;
		}catch(SQLException e){
			BugWriter.log(this, e, "�����û�����ʱ�����û���" + guest.getNumber() ) ;
		}finally{
			update.close() ;
		}
		
		if(success){
			icmp.setContent(new Integer(1).toString()) ;
			//�޸��û����ڴ��е����ϡ�
			user.setAuth(host.getAuth()) ;
			user.setFrom(host.getProvince()) ;
			user.setGender(host.getGender()) ;
			user.setNickname(host.getNickname()) ;
			user.setPortrait(host.getPortrait()) ;
		}else{
			icmp.setContent(new Integer(4).toString()) ;
		}
		
		ss.sendMessage(icmp.getByteContent(), icmp.getType(), 1000, rm.getFrom(), user.getIP(), user.getPort() ) ;
		return ;
	}
	
	
	
	
	
	
	
	
	
}
