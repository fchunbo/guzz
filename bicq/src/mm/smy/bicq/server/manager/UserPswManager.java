package mm.smy.bicq.server.manager ;

/**
* ��UserPswMessage�Ĵ���
* 
* 
* @author XF
* @date 2003-11-22
* 
* 
*/

import mm.smy.bicq.message.MessageType ;
import mm.smy.bicq.message.ReceivedMessage ;
import mm.smy.bicq.message.UserPswMessage ;

import mm.smy.bicq.server.StartServer ;

import mm.smy.bicq.server.user.OnlineManager ;
import mm.smy.bicq.server.user.OnlineUser ;
import mm.smy.bicq.server.user.SelectUserDB ;
import mm.smy.bicq.server.user.UpdateUserDB ;
import mm.smy.bicq.server.user.ServerGuest ;

import mm.smy.bicq.server.db.BugWriter ;

import mm.smy.bicq.message.StateChangedMessage ;

import java.util.Vector ;
import java.util.Enumeration ;

import java.sql.SQLException ;


public class UserPswManager{
	private StartServer ss = null ;
	private OnlineManager manager = null ;
	
	public UserPswManager(StartServer m_ss){
		ss = m_ss ;
		manager = ss.getOnlineManager() ;
	}
	
	
	public void messageAction(ReceivedMessage rm){
		if(rm == null) return ;
		if(rm.getType() != MessageType.USER_PSW_MESSAGE) return ;
		
		UserPswMessage message = new UserPswMessage() ;	
		message.setByteContent(rm.getContent()) ;
		
		if(message.getMinType() == UserPswMessage.LOGIN_REQUEST){
			doLogin(rm, message) ;
			return ;	
		}else if(message.getMinType() == UserPswMessage.MODIFY_PSW_REQUEST){
			doModify(rm, message) ; //�޸����롣	
			return ;
		}else{
			System.out.println("UserPswManager: �յ�һ���޷��������Ϣ���ͣ�" + message.getMinType()) ;
			return ;
		}
		
	}
	
	/**
	* �����½����
	* @param rm ԭ��Ϣ
	* @param message ԭ��Ϣ����������UserPswMessage��Ϣ
	* ���ǰ���������������Ϊ�˽�ʡһ����messageAction(����)���Ѿ��������Ϣת����
	*
	*
	*****/
	private void doLogin(ReceivedMessage rm, UserPswMessage message){
			SelectUserDB select = new SelectUserDB() ;
			ServerGuest guest = null ;
			try{
				guest = select.selectByNumber(new Integer(message.getExplain()).intValue()) ;
			}catch(SQLException e){
				BugWriter.log(this, e, "�ڵ�½ʱ�������û���" + rm.getFrom() + " ���ִ���") ;
			}finally{
				select.close() ;	
			}
			
			UserPswMessage upm = new UserPswMessage() ;	
			//�û������ڡ�
			if(guest == null){
				upm.setMinType(UserPswMessage.LOGIN_FAILED) ;
				upm.setExplain("�û������� ���� ���������ݿ�ϵͳ����") ;
				ss.sendMessage(upm.getByteContent(),MessageType.USER_PSW_MESSAGE, 1000, rm.getFrom(), rm.getIP(), message.getPort() ) ;
				return ;
			}
			
			if(message.getPassword() == null){
				message.setPassword("") ;
			}
			
			if(!message.getPassword().equals(guest.getPassword())){
				upm.setMinType(UserPswMessage.LOGIN_FAILED) ;
				upm.setExplain("���������ע���Сд��") ;
				ss.sendMessage(upm.getByteContent(),MessageType.USER_PSW_MESSAGE, 1000, rm.getFrom(), rm.getIP(), message.getPort() ) ;
				return ;
			}
			
			//�쿴�ú��Ƿ��Ѿ���½��
			OnlineUser user = manager.getOnlineUser(rm.getFrom()) ;	
			
			//ע�⣺�������if��仮���Ժ�Ϳ���ǿ�Ƶĵ�½�ˡ�
/*								
			if(user != null){ //�Ѿ���½�ˣ�һ�����������ε�½~~~�����Ǿܾ��ڶ��εĵ�½��
				upm.setMinType(UserPswMessage.LOGIN_FAILED) ;
				upm.setExplain("ָ���˺��Ѿ���½����½IP��" + user.getIP()) ;
				ss.sendMessage(upm.getByteContent(),MessageType.USER_PSW_MESSAGE, 1000, rm.getFrom(), rm.getIP(), message.getPort() ) ;
				return ;
			}
*/			
			//��½�ɹ���д��online��
			if(user == null)
				user = new OnlineUser() ;
			user.setAuth(guest.getAuth()) ;
			user.setFrom(guest.getProvince()) ;
			user.setGender(guest.getGender()) ;
			user.setIP(rm.getIP()) ;
			user.setPort(message.getPort()) ;
			user.setPortrait(guest.getPortrait()) ;
			user.setRecordID(guest.getRecordID()) ;
			user.setState(mm.smy.bicq.user.User.ONLINE) ;
			user.setNickname(guest.getNickname()) ;
			user.setNumber(guest.getNumber()) ;
			manager.addOnlineUser(user) ;
			
			//�����û���½�ɹ�
			upm.setMinType(UserPswMessage.LOGIN_SUCCESS) ;
			ss.sendMessage(upm.getByteContent(),MessageType.USER_PSW_MESSAGE, 1000, rm.getFrom(), user.getIP(), user.getPort() ) ;
			System.out.println("��½�ɹ����ɹ���Ϣ�ѷ���") ;
			
			//��������Ϊ���ѵ��˷���StateChangedMessage
			StateChangedMessage scm = new StateChangedMessage() ;
			scm.setIP(user.getIP()) ;
			scm.setPort(user.getPort()) ;
			scm.setIsNotify(true) ;
			scm.setMinType(mm.smy.bicq.user.User.ONLINE) ;
			
			ss.getStateChangedManager().sendStateChangedMessage(scm, user.getNumber(), false) ;
			return ;
	}
	
	private void doModify(ReceivedMessage rm, UserPswMessage message){
			OnlineUser user = manager.getOnlineUser(rm.getFrom()) ;
			
			if(user == null){ //not login in
				//upm.setMinType(UserPswMessage.LOGIN_FAILED) ;
				//upm.setExplain("�޷��޸����룬�û���" + rm.getFrom() + " ��δ��½��") ;
				//û��port���޷����͡����Ǻ��Ե��û�������~~~~��
				System.out.println("UserPswManager::�޷��޸����룬�û���" + rm.getFrom() + " ��δ��½��") ;
				return ;
			}
			
			//�����û������ݿ��е�����
			SelectUserDB select = new SelectUserDB() ;
			ServerGuest guest = null ;
			try{
				guest = select.selectByNumber(rm.getFrom()) ;
			}catch(SQLException e){
				BugWriter.log(this, e, "�ڵ�½ʱ�������û���" + rm.getFrom() + " ���ִ���") ;
			}finally{
				select.close() ;	
			}
			
//			System.out.println("oldpassword:" + message.getPassword()) ;
//			System.out.println("newpassword:" + message.getNewPassword()) ;
//			System.out.println("database:" + guest.getPassword()) ;
				
			UserPswMessage upm = new UserPswMessage() ;	
			//�û������ڡ�
			if(guest == null){
				upm.setMinType(UserPswMessage.MODIFY_PSW_FAILED) ;
				upm.setExplain("���������ݿ�ϵͳ���������ݿ����Ҳ����û���" + rm.getFrom()) ;
				BugWriter.log(this, new Exception("û�м�¼"), "�����Ѿ���OnlineManager���ҵ����û������������ݿ���û�����ļ�¼���û�Ϊ��" + rm.getFrom() ) ; 
				
				ss.sendMessage(upm.getByteContent(),MessageType.USER_PSW_MESSAGE, 1000, rm.getFrom(), user.getIP(), user.getPort() ) ;
				return ;
			}
			if(message.getNewPassword() == null || message.getNewPassword().length() == 0){
				upm.setMinType(UserPswMessage.MODIFY_PSW_FAILED) ;
				upm.setExplain("�޸�����ʧ�ܣ����벻��Ϊ�գ�") ;
				ss.sendMessage(upm.getByteContent(),MessageType.USER_PSW_MESSAGE, 1000, rm.getFrom(), user.getIP(), user.getPort() ) ;
				return ;
				
			}
			if(message.getPassword() == null || !message.getPassword().equals(guest.getPassword())){
				upm.setMinType(UserPswMessage.MODIFY_PSW_FAILED) ;
				upm.setExplain("�޸�ʧ�ܣ����������ע���Сд��") ;
				ss.sendMessage(upm.getByteContent(),MessageType.USER_PSW_MESSAGE, 1000, rm.getFrom(), user.getIP(), user.getPort() ) ;
				return ;
			}
			
			//���������ǰ��µ�����д�����ݡ�
			UpdateUserDB update = new UpdateUserDB() ;
			update.setNumber(rm.getFrom()) ;
			update.setPassword(message.getNewPassword()) ;
			try{
				update.update() ;
				update.close() ;
				upm.setMinType(UserPswMessage.MODIFY_PSW_OK) ;
			}catch(SQLException e){
				update.close() ;
				BugWriter.log(this, e, "�����û���" + user.getFrom() + " ����") ;
				upm.setMinType(UserPswMessage.MODIFY_PSW_FAILED) ;
				upm.setExplain("�޸�ʧ�ܣ����ݿ�������������뺬�зǷ��ַ�����������⣬��͹���Ա��ϵ��") ;
			}
			
			ss.sendMessage(upm.getByteContent(),MessageType.USER_PSW_MESSAGE, 1000, rm.getFrom(), user.getIP(), user.getPort() ) ;
			return ;
	}

	
	
	
	
	
	
	
}

