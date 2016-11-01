package mm.smy.bicq.message ;

import java.io.* ;
import java.util.Vector ;
import java.util.Hashtable ;

import mm.smy.bicq.MainManager ;
import mm.smy.bicq.user.* ;
import mm.smy.bicq.user.manager.* ;
import mm.smy.bicq.message.* ;

/**
*�û����棬��ȡ�����¼��
*д�룬��ȡʱ���µ����ʹ���TextMessage���Լ�Сд����־�Ĵ�С��
*
*д�뵽�ļ���number/chatlog.log
*��һ����д����־����İ汾���Ա��Ժ�Ľ��㷨ʱת�������¼��
*��ǰ��Ϣ����汾��b0.01 2003-9-29
*/


public class TextMessageFile{
	public static final String LOG_VERSION = "mm-smy-b0.01 2003-9-29" ;
	private File logfile = null ;
	private int number = 0 ;
	private String logversion = null ;
	
	private MainManager m = null ;
	private Hashtable guests = null ;
	private Host filehost = null ; //��û��mm����ʱ�������ļ�host����
		
	public TextMessageFile(int m_number){
		number = m_number ; //Host number.
	}
	
	private void init() throws IOException{
		if (logfile == null)
			logfile = new File(number + File.separator + "chatlog.log") ;
		logfile = logfile.getAbsoluteFile() ;
		if (!logfile.exists()){
			logfile.getParentFile().mkdirs() ;
			logfile.createNewFile() ;
		}
	}
	
	public String getLogVersion() throws IOException{
		if(logversion != null && logversion.length() > 0 )
			return logversion ;

		if(logfile == null) init() ;
		
		if(logfile.length() <= 0 ) return this.LOG_VERSION ;
		
		FileInputStream fin = new FileInputStream(logfile) ;
		DataInputStream in = new DataInputStream(fin) ;
		
		logversion =  in.readUTF() ;		
		in.close() ;
		
		return logversion ;		
	}
	
	public void setMainManager(MainManager m_mm){
		m = m_mm ;	
	}
	

	//�����host�򷵻�host�����򷵻�Guest
	private User getUser(int m_number) throws FileNotFoundException, IOException, ClassNotFoundException{
		if( m != null){
			if(m_number == this.number){
				return m.getHost() ;
			}else{
				return m.getOutGuest(m_number) ;
			}
		}
		
		//no main manager found, we we read the file ourself.		
		if(m_number == this.number){ //host
			if(filehost != null) return filehost ;
			HostFile hf = new HostFile(number) ;
			filehost = hf.read() ;
			return filehost ;			
		}
		
		if(guests == null){ //guest
			UserFileManager ufm = new UserFileManager(new Host(m_number)) ;
			guests = ufm.getGuests() ;
		}
		
		Guest g = (Guest) guests.get(new Integer(m_number)) ;
		if(g == null) g = new Guest(m_number) ;
		
		return g ;
	}
	
	public void save(TextMessage[] tms) throws IOException{
	//save it; append in fact.
		if(logfile == null) init() ;
		
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(logfile,true)) ;
		if(logfile.length() <= 0 ){
			dos.writeUTF(this.LOG_VERSION) ; //������½������ļ���д��汾��
			System.out.println("log version wrote.") ;
		}
		//first write in the from, the to, the length of the message,then then message content in byte form.
		for(int i = 0 ; i < tms.length ; i++){
			if (tms[i] == null) continue ;
			if (tms[i].getFrom() == null) continue ;
			if (tms[i].getTo() == null) continue ;
			String content = tms[i].getContent() ;
			if(content == null || content.length() == 0 ) continue ;
			byte[] b = content.getBytes() ;
			
			dos.writeInt(tms[i].getFrom().getNumber()) ;
			dos.writeInt(tms[i].getTo().getNumber()) ;	
			dos.writeInt(b.length) ;
			dos.write(b) ;
			dos.writeLong(tms[i].getReceivedTime().getTime()) ;
		}
		
		dos.flush() ;
		dos.close() ;		
		return ;
	}
	
	public Vector read(User u) throws IOException,ClassNotFoundException{
		//get the special user's chat log...	
		if(logfile == null) init() ;
		
		Vector v = new Vector() ;
		TextMessage tm = null ;
		
		DataInputStream in = new DataInputStream(new FileInputStream(logfile)) ;
		
		logversion = (String) in.readUTF() ;
		int from = Integer.MIN_VALUE ;
		int to   = Integer.MIN_VALUE ;
		byte [] b = null ;
		//now read all log
		while(in.available() > 0){
			//System.out.println("in available1:" + in.available() ) ;
			from = in.readInt() ;
			to   = in.readInt() ;
			if(to != this.number){
				System.out.println("not permit") ;
			}
			b = new byte[in.readInt()] ;
			in.read(b) ;
			tm = new TextMessage() ;
			tm.setFrom(getUser(from)) ;
			tm.setTo(getUser(to)) ;
			tm.setContent(new String(b)) ;
			tm.setReceivedTime(in.readLong()) ;
			v.add(tm) ;	
			//System.out.println("in available2:" + in.available() ) ;	
		}
		
		in.close() ;
		return v ;
	}	
		
	public Vector read(User u, int count) throws IOException,ClassNotFoundException{
		//get the special user's chat log...	
		if(logfile == null) init() ;
		
		Vector v = new Vector() ;
		TextMessage tm = null ;
		
		DataInputStream in = new DataInputStream(new FileInputStream(logfile)) ;
		
		logversion = (String) in.readUTF() ;
		int from = Integer.MIN_VALUE ;
		int to   = Integer.MIN_VALUE ;
		byte [] b = null ;
		//now read all log
		int i = 0 ;
		while(in.available() > 0 && i < count){
			//System.out.println("in available1:" + in.available() ) ;
			from = in.readInt() ;
			to   = in.readInt() ;
			if(to != this.number){
				System.out.println("not permit") ;				
			}
			b = new byte[in.readInt()] ;
			in.read(b) ;
			tm = new TextMessage() ;
			tm.setFrom(getUser(from)) ;
			tm.setTo(getUser(to)) ;
			tm.setContent(new String(b)) ;
			tm.setReceivedTime(in.readLong()) ;
			v.add(tm) ;	
			i++ ;
			//System.out.println("in available2:" + in.available() ) ;	
		}
		
		in.close() ;
		return v ;
	}
	
	public void close(){
		
	}
	
	private void test() throws Exception{
		Guest g = new Guest(number) ;
		Guest from = new Guest(3000) ;
		
		TextMessage[] tms = new TextMessage[10] ;
		for(int i = 0 ; i < 10 ; i++ ){
			TextMessage tm = new TextMessage() ;
			tm.setContent("message" + i) ;
			tm.setFrom(from) ;
			tm.setTo(g) ;
			tms[i] = tm ;
			System.out.println("from:" + tms[i].getFrom() ) ;
			System.out.println("to:" + tms[i].getTo() ) ;
		}
		this.save(tms) ;
		//read it back.
		Vector v = this.read(g,-1) ;
		System.out.println("************************************************************************************") ;
		System.out.println("result:" + v ) ;
		java.util.Enumeration e = v.elements() ;
		while(e.hasMoreElements()){
			TextMessage ttm = (TextMessage) e.nextElement() ;
			System.out.println(ttm.getContent()) ;			
		}	
		
	}
	
	public static void main(String[] args) throws Exception{
		TextMessageFile file = new TextMessageFile(4000) ;
		file.test() ;		
	}
	
	
}
