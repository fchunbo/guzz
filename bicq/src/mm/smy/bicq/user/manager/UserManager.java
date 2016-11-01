package mm.smy.bicq.user.manager ;

/**
* ��Guest�Ĵ���
*
* @author XF
*
*
*
*/
import java.io.* ;
import java.util.* ;

import mm.smy.bicq.* ;
import mm.smy.bicq.message.* ;
import mm.smy.bicq.login.LoginException ;

import mm.smy.bicq.user.* ;
import mm.smy.util.* ;

public class UserManager{
	
	private MainManager m = null ;
	private Host host = null ;
	
	private Hashtable guestgroups = null ;
	private Hashtable guests = null ;
	
	private UserFileManager filemanager = null ;
	private UserNetManager netmanager = null ;
	
	private boolean istimeout = false ;
	private int loadGuestState = UserNetManager.UNDEFINE ;
	
	public UserManager(MainManager m_mm){
		m = m_mm ;
		host = m.getHost() ;
		filemanager = new UserFileManager(host) ;
		netmanager = m.getUserNetManager() ;
		
		//��unm��guestgroups,guests��Ϊ���еĺ��Ѷ����ַ
		
		guestgroups = netmanager.getGuestGroups() ;
		guests = netmanager.getGuests() ;
		
		filemanager.setGuestGroups(guestgroups) ;
		filemanager.setGuests(guests) ;
	}
	
	//first put
	
	//��Ŀǰ��hostд��Ӳ��.
	public void writeHostToLocal(){
		filemanager.writeHostFile() ;
	}
	
	/**
	* ����Guest�����ϣ����ȶ���loacal data����������ڵĻ��������硣
	* 
	* �÷���Ϊ�жϷ���������һֱ�ȴ���ֱ��������Ϣ�����ǳ�ʱ����ʱ�ñ��Ϊistimeout = true ;
	* m��Ӧ�ò鿴�Ƿ�ʱ���Ѿ�����һ��Ӧ����ô����
	*/
	public void setupGuest() throws LoginException{
		filemanager.setIsFileRead(false) ;
		this.istimeout = false ;
		
		if(isLocalDataExsit()){
			initLocalData() ;
			//�����ݸ�unm
			//netmanager.setGuestGroups(guestgroups) ;
			//netmanager.setGuests(guests) ;
		}
		
		if(guestgroups == null || guestgroups.size() == 0){//local data not exsits, read from net...
//					System.out.println(" whiling setupGuest in um, UserNetManager is:" + netmanager) ;
			SmyTimer timer = new SmyTimer() ;
			timer.setTimerListener(new WaitInitGuest(timer)) ;
			timer.setInterval(UserNetManager.DEFAULT_INTERVAL) ;
			timer.setTotalTime(UserNetManager.DEFAULT_TOTAL_TIME) ;
			timer.startTimer() ;
			
			this.loadGuestState = netmanager.WAITING ;
			
			netmanager.readNet() ;//��ȡ���к������ϡ�
//			System.out.println("read from net start:" + netmanager.getLoadAllState() ) ;
			
			
			while(loadGuestState == netmanager.WAITING){
				try{
					synchronized(this){
						wait(500) ;	
					}
				}catch(Exception e){
					System.out.println("Exception in setupGuest() in um") ;						
				}

//				System.out.println("do nothing but loop " + loadGuestState) ;
			}
//			System.out.println("Waiting is:" + netmanager.WAITING ) ;
			//System.out.println("loop should go on:" + (loadGuestState)) ;
//			System.out.println("read from net end:" + netmanager.getLoadAllState() ) ;
			
			//��������������ǲ���Ҫ��,��Ϊ�ڹ��캯���������Ѿ������еĵ�ַָ����unm��
			guestgroups = netmanager.getGuestGroups() ;
			guests      = netmanager.getGuests() ;
			
//			System.out.println("read from netmanager: guestgroups:" + guestgroups) ;
//			System.out.println("guests:" + guests) ;
			
			if(guestgroups == null || guestgroups.size() == 0 ){ //wang luo cao shi.
				LoginException e = new LoginException("���糬ʱ��������磬��쿴��־") ;
				mm.smy.bicq.debug.BugWriter.log("UserManager.java", e, "��ȡguestgroupsʱ������󣬱�־��" + netmanager.getLoadAllState() ) ;
				throw e  ;
				//�������ӹ������糬ʱ�Ĵ���������ʾ�û����˳�����ȵȡ�
			}
			//��������������ǲ���Ҫ��,��Ϊ�ڹ��캯���������Ѿ������еĵ�ַָ����unm��
			//filemanager.setGuestGroups(guestgroups) ;
			//filemanager.setGuests(guests) ;
		}
		
		if (guests == null){			
			//guests = new Hashtable(40) ;
			throw new LoginException("guests��um��Ϊ�գ����ǲ�Ӧ�ó��ֵģ�˵�������ʼ������ӦΪ������ƴ���") ;
		}
		//��������������ǲ���Ҫ��,��Ϊ��mm�Ĺ��캯���������Ѿ��õ�ַָ����unm��
		//m.setGuestGoups(guestgroups) ;
		//m.setGuests(guests) ;
		
		return ;		
	}
	
	public boolean isTimeOut(){
		return istimeout ;	
	}
	
	private class WaitInitGuest implements TimerListener{
		private SmyTimer timer = null ;
		
		public WaitInitGuest(SmyTimer m_timer){
			timer = m_timer ;
			//timer.startTimer() ;
		}
		
		public void timeElapsed(){
			if(netmanager.getLoadAllState() == netmanager.FINISHED){
				loadGuestState = netmanager.FINISHED ;
				istimeout = false ;
				timer.stopTimer() ;	
			}
		}
		
		public void timeOut(){
			loadGuestState = netmanager.TIMEOUT ;
			istimeout = true ;
			timer.stopTimer() ;	
		}		
	}

/*********************************************************************************************/
	public void initLocalData(){
		//��������ڣ��õ���ȫ��null
		System.out.println("init local data................................................"); 
		//���ֻ��ǿ��filemanagerִ��private ������ȡ �ļ���¼��
		filemanager.getGuestGroups() ;
		filemanager.getGuests() ;
	}
	
	public boolean isLocalDataExsit(){
		return filemanager.isDataExsit() ;
	}

	
/*********************************************************************************************/
	
	public void showUserInfor(Guest g){
		if(g == null) return ;
	//	InforWindow window = new InforWindow(g,m) ;
		GuestInforWindow window = new GuestInforWindow(g, m) ;
		window.show() ;
		return ;
	}
	public void showUserInfor(Host h){
		if(h == null) return ;
//		InforWindow window = new InforWindow(h,m) ;
	System.out.println("trying to show the host's information:" + h) ;
		HostInforWindow window = new HostInforWindow(h,m) ;
		window.show() ;
		return ;
	}
	
	public Hashtable getGuestGroups(){
		return guestgroups ;
	}
	
	public Hashtable getGuests(){
		return guests ;
	}
	
//guest methods.....

	public void updateGuest(Guest g){
		if(g == null) return ;
		guests.put(new Integer(g.getNumber()),g) ;
	}
	
	public Guest addGuest(Guest g, GuestGroup gg){
		if(g == null) return null;
		if(gg == null) gg = getGuestGroup("�ҵĺ���") ;
		
		Guest newguest = (Guest) guests.get(new Integer(g.getNumber())) ;
		if ( newguest == null ){
			newguest = g ;
			newguest.joinGuestGroup(gg) ;
			//put the newguest in the memory
			guests.put(new Integer(g.getNumber()), newguest) ;
		}else{
			newguest.joinGuestGroup(gg) ;
			return newguest ; 
		}
		//Here we write all the GuestGroup information into the 'number/guestgroup.bicq' file.
		saveGuests() ;
		return newguest;			
	}
	
	public Guest addGuest(int m_number,String m_groupname){
		if(m_groupname == null) m_groupname = "�ҵĺ���" ;
		
		return addGuest(m_number, getGuestGroup(m_groupname)) ;
	}
	public Guest addGuest(int m_number,GuestGroup m_gg){
		if(m_gg == null) return null ;
		
		Guest newguest = (Guest) guests.get(new Integer(m_number)) ;
		if ( newguest == null ){
			newguest = new Guest(m_number) ;
			newguest.joinGuestGroup(m_gg) ;
			//put the newguest in the memory
			guests.put(new Integer(m_number),newguest) ;
		}else{
			return newguest ; 
		}
		//Here we write all the GuestGroup information into the 'number/guestgroup.bicq' file.
		saveGuests() ;
		return newguest;
	}
	
	public Guest moveGuest(Guest g, GuestGroup gg){ //�ƶ����ѵ��µ�С��
		if ( !guestgroups.containsValue(gg) ){ //����鲻���ڣ���ֱ�ӷ���
			return g ;
		}
		
		if(gg.getGroupname().equals("İ����") || gg.getGroupname().equals("������")){
			//�û�����Ҫ��ɾ���û���
			GuestGroup oldgroup = g.getGuestGroup() ;
			if(oldgroup != null){
				if(!"İ����".equals(oldgroup.getGroupname()) && !"������".equals(oldgroup.getGroupname())){
					//ɾ���û�
					removeGuest(g.getNumber()) ;
				}				
			}
		}
		
		g.joinGuestGroup(gg) ;
		return g ;
	}
	
	public void removeGuest(int m_number){
		//Guest g = (Guest) guests.get(new Integer(m_number)) ;
		//if(g == null) return ;
		Guest g = (Guest) guests.remove(new Integer(m_number)) ;
		
		if(g != null){
			g.getGuestGroup().delete(g) ;
			g = null ; //ɾ��
			saveGuests() ;
			//���߷�����ɾ�����ѡ�
			ICMPMessage deleteicmp = new ICMPMessage() ;
			deleteicmp.setMinType(ICMPMessage.DELETE_FRIEND) ;
			deleteicmp.setContent(m.getHost().getNumber() + ":" + m_number ) ;
			m.sendOutMessage(deleteicmp) ;
			System.out.println("ɾ��������Ϣ���ͳɹ���") ;
		}
		return ;
	}
	/**
	*������û��Լ�(host)������null.
	*����û������ڣ������ú��ѣ����Ҽ��뵽 İ���� �
	*/
	public Guest getGuest(int m_number){
		System.out.println("==========um is requested to solve the guest:" + m_number) ;
		if(m_number <= 0) {
			return m.getServer() ; //����Server�ĵ�ַ��
		}

		if(m_number == m.getServer().getNumber()) return m.getServer() ;
		if(m_number == host.getNumber()) return m.getHost2() ; //�����߾��ǽ����ߣ����ֻ��Ϊ�˲��ԣ��������ĳ�����Ӧ�����ؿ��ǣ� 
		
		System.out.println("guests is:" + guests) ;
		if(guests == null) return null ; //�ڸ�����ʱ������Ϊ�գ�û���������Ϣ������Ѱ��Guestʱ�������
		
		Guest g = (Guest) guests.get(new Integer(m_number)) ;
		if (g == null) { //����ż�ĺ��ѣ��ŵ�İ�������档
			//Guest newguest = new Guest(m_number) ;
			//newguest.joinGuesgGroup(getGuestGroup("İ����")) ; //put this in the addGuest method.
			System.out.println("^^^^^^^^^^^^^^add " + m_number + " to stanger guestgroup.") ;
			g = addGuest(m_number, getGuestGroup("İ����")) ;
		}
		return g ;
	}

//guestgroup methods......
	/**
	*@param groupname ����ָ�����顣������������ڣ��½������ظ��顣
	*                 �Դ�Сд��������ǰ�����޿ո����С�
	*
	*/
	public GuestGroup getGuestGroup(String groupname){
		return (GuestGroup) guestgroups.get(groupname) ;
	}
	public GuestGroup addGuestGroup(String groupname){
		if (guestgroups.containsKey(groupname.trim().toLowerCase())){
			return getGuestGroup(groupname) ;
		}
		GuestGroup gg = new GuestGroup(groupname) ;
		gg.setIsSystemic(false) ;
		
		this.saveGuests() ;

		return gg ;
	}
	//remove
	public GuestGroup removeGuestGroup(GuestGroup m_group){
		m_group = (GuestGroup) guestgroups.remove(m_group.getGroupname()) ;
		
		saveGuests() ;
		
		return m_group ;
	}
	
	
	/**
	* �رշ��������µ�guests, guestgroups ��������Ӳ��
	* �Ժ���ܻ����� ������
	* 
	* �ͷ�����guestռ�õ���Դ���رպ��ѹ����̡߳�
	*/
	
	public void close(){
		try{
			GuestGroupFile ggf = new GuestGroupFile(host.getNumber()) ;
			ggf.save(guestgroups) ;
		}catch(FileNotFoundException e){
			//This should not happen....
			System.out.println("ϵͳ����guestgroup.bicq�޷��ҵ����ڱ�����ʱ-->"+ e.getMessage()) ;
		}catch(IOException e){
			//�ļ���������Ӧ���������򣬻�����û���
			System.out.println("ϵͳ�����ڱ���guestgroupʱ����IOException==>"+ e.getMessage() ) ;	
		}
		
		this.writeHostToLocal() ;
		
		if(guests !=null ){
//			System.out.println("guests is not null when we close the bicq:" + guests) ;
			guests.clear() ;
		}
		if(guestgroups != null){
//			System.out.println("guestgroups is not null when we close the bicq:" + guestgroups) ;	
			guestgroups.clear() ;
		}
		
		guests = null ;
		guestgroups = null ;
		
		return ;		
	}
	
	public void saveGuests(){
		try{
			GuestGroupFile ggf = new GuestGroupFile(host.getNumber()) ;
		//	System.out.println("\n\n\num saveGuests() reports:") ;
		//	System.out.println("guests.50282717") ;
		//	Guest g = (Guest) guests.get(new Integer(50282717)) ;
		//	System.out.println("nickname:" + g.getNickname()) ;
		//	System.out.println("nickname:" + g.getMail()) ;
		//	System.out.println("nickname:" + g.getExplain()) ;
		//	System.out.println("nickname:" + g.getCountry()) ;
		//	System.out.println("nickname:" + g.getProvince()) ;
			
			ggf.save(guestgroups) ;
		}catch(FileNotFoundException e){
			//This should not happen....
			System.out.println("ϵͳ����guestgroup.bicq�޷��ҵ����ڱ�����ʱ-->"+ e.getMessage()) ;
		}catch(IOException e){
			//�ļ���������Ӧ���������򣬻�����û���
			System.out.println("ϵͳ�����ڱ���guestgroupʱ����IOException==>"+ e.getMessage() ) ;	
		}
	}
	
	
	
}
