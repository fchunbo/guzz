package mm.smy.bicq.search ;

import mm.smy.bicq.* ;
import mm.smy.bicq.user.* ;
import mm.smy.bicq.message.* ;

import mm.smy.util.* ;

import java.util.* ;
/**
* �ṩ��Seach Guest��ͳһ�������ཫ���������һ��ʵ����Ȼ�����е��½���Ϣ���ᷢ�͸���ʵ����
* �ɸ����Լ�������ô��������
*
* Ŀǰ�������� ֻ��һ��ʵ�����У�
* ά��SearchGuestMessage �� SearchGuestResultMessage �����У�
*
*/
import javax.swing.* ;

public class SearchGuestManager implements SearchGuestResultMessageListener{
	public static final String STEP_CLOSE = "close" ;
	public static final String STEP_PREVIOUS   = "previous" ;
	public static final String STEP_NEXT  = "next" ;
	public static final String STEP_FINISH = "finish" ;
	
	private SearchStep1 ss1 = null ;
	private SearchStep2 ss2 = null ;
	private SearchStep3 ss3 = null ;
	private SearchByNumber s_number = null ;
	private SearchByNickname s_nickname = null ;
	private SearchByGFA s_gfa = null ;
	
	private boolean isworking = false ; //��ǰ�Ĳ����Ƿ��������У��Ա�����ʵ��ͬʱ���ڣ�
	
	private JFrame currentframe = null ; //��ǰ����
	
	private SearchGuestManager out = this ; //��ǰ��ָ�룬�����ڲ�����á�
	
	private SearchGuestMessage sgm = new SearchGuestMessage() ;
	private SearchGuestResultMessage sgrm = null ; //���淵�ص�message����
	
	private MainManager m = null ;
	
	private int timeout = 20000 ; //�ȴ���ʱ 2��
	
	
	public 	SearchGuestManager(MainManager m_mm){
		m = m_mm ;
		init() ;
	}
	/*
	public SearchGuestManager(){
		init() ;		
	}
	public static final void main(String[] args){
		SearchGuestManager sgm = new SearchGuestManager() ;
		
	}
	*/
	private void init(){
		if(m != null){
//			m.addSearchGuestMessageListener(this) ;
			m.addSearchGuestResultMessageListener(this) ;
		}
		ss1 = new SearchStep1(this) ;
		ss1.show() ;
		isworking = true ;
		return ;
	}
	
	/**
	* ��finish()�����Ժ����ǿ��ܻ�Ҫ����������
	* �÷��������¹���sgm��
	*
	*
	*
	*
	*/
	public void rework(){
		ss1 = null ;
		ss1 = new SearchStep1(this) ;	
		ss1.show() ;
		isworking = true ;	
	}
	
	public boolean isWorking(){
		return isworking ;
	}

//���step��ͨѶ���Ե��ظ������Эͬ������
/**
* @param m_step �ڼ�����������1��ʼ����1����SearchStep1   2����SearchByNumber 3����SearchByNickname 5����SearchByGFA[Gender/From/Age]
* @param m_actioncommand �û���������Ӧ����Ӧ��ť��actionCommand��
*/

	
	public void report(int m_step,String m_actioncommand){
		System.out.println("Step" + m_step + " reports:" + m_actioncommand ) ;
		if(m_actioncommand == null) return ;
		
		switch(m_step){
			case 1 : //step 1
				if (ss1 == null){
					ss1 = new SearchStep1(this) ;
				 	ss1.show() ;
				 	currentframe = ss1 ;
				 	return ;
				 }
				

				 if(m_actioncommand.equals(this.STEP_NEXT)){
					String temp_ss1 = ss1.getSelectedItem() ;
					if(temp_ss1 == null) return ; //�û�ʲôҲû��ѡ������ֱ�ӷ��أ�ʲôҲ��ȥ����
					
					ss1.hide() ;
					
				 	if(temp_ss1.equalsIgnoreCase("byonline")){
				 		sgm.setMinType(SearchGuestMessage.SEARCH_ONLINE) ;
				 		currentframe = ss1 ;
				 		send() ;
				 		return ;
				 	}else if(temp_ss1.equalsIgnoreCase("bynumber")){
				 		if(s_number == null) s_number = new SearchByNumber(this) ;
				 		currentframe = s_number ;
				 		s_number.show() ;
				 	}else if(temp_ss1.equalsIgnoreCase("bynickname")){
				 		if(s_nickname == null) s_nickname = new SearchByNickname(this) ;
				 		currentframe = s_nickname ;
				 		s_nickname.show() ;
				 	}else if(temp_ss1.equalsIgnoreCase("byGFA")){
				 		if(s_gfa == null) s_gfa = new SearchByGFA(this) ;
				 		currentframe = s_gfa ;
				 		s_gfa.show() ;
				 	}
				 	return ;
				}
				ss1.hide() ;
				finish() ; //quit the search.
				break ;
			case 2 : //search by number.
				if(m_actioncommand.equals(this.STEP_NEXT)){
					int temp_number = s_number.getNumber() ;
					if(temp_number <= 0) return ; //�д�����
					
					sgm.setMinType(SearchGuestMessage.SEARCH_BY_NUMBER) ;
					sgm.setNumber(temp_number) ;
					s_number.hide() ;
					send() ;
					return ;
				}
				if(m_actioncommand.equals(this.STEP_PREVIOUS)){
					if(s_number != null ) s_number.hide() ;
					ss1.show() ;
					return ;
				}
				
				finish() ;
				break ;
				
			case 3 : //search by nickname
				if(m_actioncommand.equals(this.STEP_NEXT)){	
					String temp_nickname = s_nickname.getNickname() ;
					if(temp_nickname == null ) return ; //�д�����
					temp_nickname = temp_nickname.trim() ;
					if (temp_nickname.length() == 0 ) return ;
					
					sgm.setMinType(SearchGuestMessage.SEARCH_BY_NICKNAME) ;
					sgm.setNickname(temp_nickname) ;
					s_nickname.hide() ;
					send() ;
					return ;
				}
				if(m_actioncommand.equals(this.STEP_PREVIOUS)){
					if(s_nickname != null ) s_nickname.hide() ;
					ss1.show() ;
					return ;
				}
				
				finish() ;
				break ;	
			case 4 : //search by gfa
				if(m_actioncommand.equals(this.STEP_NEXT)){
					sgm.setMinType(SearchGuestMessage.SEARCH_BY_GFA) ;
					sgm.setAge(s_gfa.getAgeFrom(),s_gfa.getAgeTo()) ;
					sgm.setProvince(s_gfa.getFrom()) ;
					sgm.setGender(s_gfa.getGender()) ; //0 girl; 1 boy ; -1 anyone
					s_gfa.hide() ;
					send() ;
					return ;
				}
				if(m_actioncommand.equals(this.STEP_PREVIOUS)){
					s_gfa.hide() ;
					ss1.show() ;
					return ;
				}
				
				finish() ;
				break ;
			case 5 : //connect and commuicate with the server....
				//nothing will send here now....
				//Maybe later, we will use this.
			case 6 : //net error/timeout...
				if(m_actioncommand.equals(this.STEP_PREVIOUS)){
					System.out.println("ss2 is null:" + (ss2==null)) ;
					System.out.println("currentframe is null :" + (currentframe == null)) ;
					ss2.hide() ;
					//ss2.getPreFrame().show() ; 
					currentframe.show() ;
					return ;
				}
				finish() ;
				break ;
			case 7 : //searchstep3, search OK, show result......
				if(m_actioncommand.equals(this.STEP_PREVIOUS)){
					ss3.hide() ;
					currentframe.show() ;
					return ;
				}
				finish() ;
				break ; 
			default:
				return ;
		}
		return ;
	}
	
	//send the message out. check its valid...
	private void send(){
		
		ss2 = new SearchStep2(this,currentframe) ;
		ss2.show() ;
		
		//send out the message
		sgm.setFrom(m.getHost()) ;
		sgm.setTo(m.getServer()) ;
		m.sendOutMessage(sgm) ;
		
		System.out.println("message sends out....MessageType:" + sgm.getMinType()) ;
		//wait.. until timeout
		
		SmyTimer timer = new SmyTimer() ;
		timer.setTimerListener(new WaitReply(timer)) ;
		timer.setTotalTime(timeout) ;
		timer.setInterval(timeout/100) ;
		timer.startTimer() ;

	}
	
	
	//�ȴ��յ���Ϣ���������޷��յ��Ļ����ͱ��泬ʱ��
	private class WaitReply implements TimerListener{
		private SmyTimer timer = null ;
		
		public WaitReply(SmyTimer m_timer){
			timer = m_timer ;	
		}
		
		public void timeElapsed(){
			if(sgrm != null){ //�յ���������������Ϣ��ss2��������ʾss3.
				timer.stopTimer() ;
				
				ss2.hide() ;
				ss3 = new SearchStep3(m, out, sgrm) ;
				ss3.show() ;
			}
		}
		
		public void timeOut(){
			timer.stopTimer() ;
			
			ss2.setCurrent(6) ;
			ss2.repaint() ;
		}
		
	}
	
	/**
	* �û�Ҫ���˳� ����
	* ����������
	*/
	public void finish(){
		System.out.println("finish() is invoked!!!!!!!") ;
		if(s_number != null){
			s_number.hide() ;
			s_number = null ;
		}
		if(s_nickname != null){
			s_nickname.hide() ;
			s_nickname = null ;
		}
		if(s_gfa != null){
			s_gfa.hide() ;
			s_gfa = null ;
		}
		if(ss1 != null){
			ss1.hide() ; 
			ss1 = null ;
		}
		if(ss2 != null){
			ss2.hide() ;
			ss2 = null ;
		}
		if(ss3 != null){
			ss3.hide() ;
			ss3 = null ;
		}
		
		isworking = false ;
		return ;
	}
	
	
//implements methods
//	public void searchGuestMessageAction(SearchGuestMessage sgm){
//		System.out.println("sgm has received a searchguestmessage....") ;
//		
//		return ;
//	}
	public void searchGuestResultMessageAction(SearchGuestResultMessage sgrm){
		this.sgrm = sgrm ;		
		System.out.println("sgm has received a seachguestresultmessage...") ;
		
		return ;
	}
}

