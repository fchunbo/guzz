package mm.smy.bicq.user.manager ;

/**
* �ļ�����
* �û��ļ���������������ʱ��guests,guestgroups�ĳ�ʼ����
* �ṩ�˶Ժ������ϱ���Ŀ�ݷ�ʽ��
*
* @author XF
* @also see mm.smy.bicq.user.manager.UserNetManager
* @date 2003-10-23
*
*/
import java.util.Hashtable ;
import java.util.Vector ;
import java.util.Enumeration ;
import java.util.Date ;

import java.io.* ;

import mm.smy.bicq.user.* ;

public class UserFileManager{
	
	private Hashtable guestgroups = null ;
	private Hashtable guests = null ;
	
	private Host host = null ;
	private boolean isFileRead = false ;
	
	public UserFileManager(Host host){
		this.host = host ;	
	}
	
	public void writeHostFile(){
		HostFile hf = new HostFile(host.getNumber()) ;
		try{
			hf.save(host) ;
		}catch(Exception e){
			mm.smy.bicq.debug.BugWriter.log(e,"����������ϳ���UserFileManager.class�����쳣") ;	
		}
	}
	
	public void setIsFileRead(boolean m_read){
		isFileRead = m_read ;	
	}
	
	public boolean isDataExsit(){
		if(!isFileRead) readFile() ;
		
		return 	!(guestgroups == null) ;
	}
	
	/**
	* ���ļ��е�Guest, GuestGroup����ȫ���ָ������������ļ��Ļ���
	* ����ָ���guests, guestgroupsΪ�գ���������Ϊnull. 
	*/
	private void readFile(){
		Hashtable tempgroups = null ;
		GuestGroupFile ggf = new GuestGroupFile(host.getNumber()) ;
		try{
			tempgroups = ggf.getAll() ;
//			System.out.println("after reading immediately, guestgroups:" + guestgroups) ;	
		}catch(FileNotFoundException e){
			System.out.println("FileNotFoundException" + e.getMessage()) ;
		}catch(IOException e){
			System.out.println("IOException" + e.getMessage()) ;
			e.printStackTrace() ;
		}catch(ClassNotFoundException e){
			System.out.println("ClassNotFoundException" + e.getMessage()) ;
		}catch(Exception e){
			mm.smy.bicq.debug.BugWriter.log(e,"�ڶ����ѵ�ʱ������쳣��UserFileManager.class�ಶ���쳣��") ;	
		}
		
		if(tempgroups == null || tempgroups.size() == 0) return ;
		
		//if (guests == null) guests = new Hashtable(40) ;
		
		//guestgroups.clear() ;
		//guests.clear() ;
		
		Enumeration e = tempgroups.elements() ;
		while(e.hasMoreElements()){
			GuestGroup gg = (GuestGroup) e.nextElement() ;
			guestgroups.put(gg.getGroupname(),gg) ;
			Vector v = gg.getAllGuests() ;
			System.out.println("------------------------------------------------------------------") ;
			System.out.println("gg:" + gg.getGroupname()) ;
			System.out.println("members:" + v) ;
			System.out.println("------------------------------------------------------------------") ;
			Enumeration e2 = v.elements() ;
			while(e2.hasMoreElements()){
				Guest g = (Guest) e2.nextElement() ;
				if( g == null) continue ;
				guests.put(new Integer(g.getNumber()) , g) ;	
				//g.joinGuestGroup(gg) ;
			}
		}
		
//		System.out.println("++++++++++++++++++After read file, guestgroups:" + guestgroups) ;
//		System.out.println("guests:" + guests) ;
/* guestgroups and guests are created in the unm.class, useful through all the BCIQ programme, any part cannot make it
	//null, or NullPointException will be thrown.
			
		if(guestgroups != null){
			if(guestgroups.size() == 0){
				guestgroups = null ;			
				guests = null ;
			}
		}
*/
		isFileRead = true ;		
	}	
	
	public Hashtable getGuestGroups(){
		if(!isFileRead) readFile() ;
		
		return guestgroups ;
	}
	
	public Hashtable getGuests(){
		if(!isFileRead) readFile() ;
		
		return guests ;
	}
	
	public void setGuestGroups(Hashtable ggs){
		guestgroups = ggs ;	
	}
	public void setGuests(Hashtable gs){
		guests = gs ;
	}
	
	public void saveGuests(){
		GuestGroupFile file = new GuestGroupFile(host.getNumber()) ;
		try{
			file.save(guestgroups) ;	
		}catch(Exception e){
			System.out.println("save error:" + e.getMessage()) ;	
		}
		file.close() ;
	}

/*	
	public static void main(String[] args){
		UserFileManager file = new UserFileManager(new Host(2000)) ;
		file.test() ;	
	}

	public void test(){
		
		//initGuestGroup() ;
		System.out.println("data exsits:" + this.isDataExsit()) ;
		System.out.println("********************************************created:************") ;
		System.out.println("guestgroups:" + guestgroups) ;
		System.out.println("guests:" + guests ) ;
		//this.writeHostFile() ;
		//this.saveGuest() ;
		//guestgroups = null ;
		//guests = null ;
		this.readFile() ;
		System.out.println("*******************read file******************") ;
		System.out.println("guestgroups:" + guestgroups) ;
		System.out.println("guests:" + guests ) ;
		
			
	}
	
	private void initGuestGroup(){
		if(guestgroups == null) guestgroups = new Hashtable(5) ;
		
		GuestGroup g1 = new GuestGroup("�ҵĺ���") ;
		g1.setCreateTime(new Date(1992,5,5)) ;
		g1.setIsSystemic(true) ;
		GuestGroup g2 = new GuestGroup("İ����") ;
		g2.setCreateTime(new Date(1998,2,4)) ;
		g2.setIsSystemic(true) ;
		GuestGroup g3 = new GuestGroup("������") ;
		g3.setIsSystemic(true) ;
		g3.setCreateTime(new Date(1999,3,18)) ;
	
		g1.add(new Guest(1000)) ;
		g1.add(new Guest(2000)) ;
		
		g2.add(new Guest(3000)) ;
		g2.add(new Guest(5000,"С��")) ;
		
		guestgroups.put(g1.getGroupname(),g1) ;
		guestgroups.put(g2.getGroupname(),g2) ;		
		guestgroups.put(g3.getGroupname(),g3) ;	
		
	}	
*/	
}
