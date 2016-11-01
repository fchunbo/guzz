package mm.smy.bicq.server ;

/**
* send all messages interfaced from Message
* message send default to port : 5201
*                        using : 5200
* @author XF
* @e-mail myreligion@163.com
* @date   2003-8-13   
* @copyright Copyright 2003 XF All Rights Reserved.
*/
import java.net.* ;
import java.io.* ;
import java.util.* ;

public class ServerSendMessage{
	//public datas
	public static final byte[] NULL_BYTES = "##null___".getBytes() ; //�����͵�����Ϊnullʱ���ø����ݴ���null
	public static final int MAX_PACKAGE_SIZE = 400 ; //UDP �����ݵ����ݴ�С��������UDPͷ���֣�
	
	private DatagramSocket sendSocket = null ;
	private int port = -1 ;

	private	int maxsize = this.MAX_PACKAGE_SIZE ; //���UDP������ݱ���С
	
	//the default send.
	private int host = 1000 ;
	
	public void setHost(int h){ //�����ʼ��
		host = h ;
	}
	
//constructors
	public ServerSendMessage(int m_port)throws IOException,SecurityException{
		port = m_port ;
		maxsize = this.MAX_PACKAGE_SIZE - 28 ; //ȥ�����ݲ���from,to,size,type,hashcode,page�Ĵ�С����ʱmaxsize��content�Ĵ�С

		sendSocket = new DatagramSocket(port) ;	
		System.out.println("$$$$$$$open SendMessage port at:" + port) ;
	}
//one UDP packet's max size.	
	public void setMaxSize(int m_size){
		maxsize = m_size - 28 ; //ȥ�����ݲ���from,to,size,type,hashcode,page�Ĵ�С����ʱmaxsize��content�Ĵ�С	
	}
	
	public int getMaxSize(){ return maxsize + 28 ; }
	
	/**
	* �����Ѿ��ܹ��� ������� �����ĸ������������Ĵ��롣
	* ���Ծ����������ʹ������Ͷ��ȡ�ɵİ취��
	* @param content ��Ҫ���͵��������ݣ�����message.getByteContent()��ã�����ͻ��޷��ָ���
	* @param type    ��Ϣ����
	* @param from    �����ߺ��룬���<=0��ʹ��Ĭ��(1000)
	* @param to      �����ߺ��룬���<=0���򷵻�.
	* @param toIP    ������IP�����Ϊnull��ֱ�ӷ���
	* @param toPort  ������Port�����<0 >65535 �׳�IOException
	* 
	*/
	public synchronized void send(byte[] content, int type,  int from, int to, InetAddress toIP, int toPort) throws IOException{
		if( from <= 0 ) from = host ; // from the server.
		if(toPort < 0 || toPort > 65535)
			throw new IOException("toPort is out of range:" + toPort) ;
		if(to <= 0 ) return ;
		if(toIP == null) return ;
			
		System.out.println("++++++++++++++sendmessage debug+++++++++++++++++++++++") ;
		System.out.println("toIP:" + toIP) ;
		System.out.println("toPort:" + toPort) ;

		if(content == null){
			content = this.NULL_BYTES ;
		}
		int length = content.length  ; //��Ϣ����
		
		if (length <= maxsize ){ //OK, can send in one packet
			ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
			DataOutputStream dos = new DataOutputStream(baos) ;
			try{
				//�������Ҫ����Ϣת���Ļ�����ô��Ӧ�ÿ���from��˭�������null�Ļ�����host
				//�����if�жϵ�������Ҫ���ڷ������ϣ�client�ο�����ʱ�ò�����Ҳ��һ����ģ�����Ϣ�İ�ȫ���ա�

				dos.writeInt(from) ;
				dos.writeInt(to) ;         //to
				dos.writeInt(type) ;//message type
				System.out.println("SendOut indicates the message type is " + type ) ;
				dos.writeInt(content.length) ;   //content size
				dos.writeInt(-1) ;               //page
				dos.writeLong(new Date().getTime()) ; //hashcode
				dos.write(content,0,length) ;        //content
				content = baos.toByteArray() ;
			}catch(IOException e){
				throw new IOException("Failed to convert message to bytes") ;
			}finally{
				try{
					baos.close() ;
					dos.close() ;
				}catch(Exception e){
					//ignore
				}
			}
			DatagramPacket packet = new DatagramPacket(content,content.length,toIP,toPort) ; // the content has changed!!!!
			sendSocket.send(packet) ;
			
			System.out.println("SendMessage sends OK ") ;
			
		}else{
			//page:�ӵ�1ҳ�����ҳ��
			int totalpage = 1 ;
			if (length%maxsize == 0 ){
				totalpage = length/maxsize ;
			}else{
				totalpage = (length - (length%maxsize))/maxsize + 1 ;
			}
			System.out.println("SendOut message too lager. divided to " + totalpage + " parts.") ;
			byte[] tempbyte = null ;
			int currentpage = 1 ;
			long hashcode = new Date().getTime() ;
			while(currentpage <= totalpage ){
				System.out.println("SendOut sends message part " + currentpage ) ;
				int leavenumber = length - (currentpage - 1)*maxsize ; //δ���͵��ֽ���Ŀ
			
				ByteArrayOutputStream baos2 = new ByteArrayOutputStream() ;
				DataOutputStream dos2 = new DataOutputStream(baos2) ;			
				try{
					dos2.writeInt(from) ; //from
					dos2.writeInt(to) ;         //to
					dos2.writeInt(type) ;//message type
					dos2.writeInt(leavenumber>maxsize?maxsize:leavenumber) ;   //content size
					int temp = currentpage*10000 + totalpage ;
					dos2.writeInt(temp) ;               //page
					System.out.println("!!!!!!!!!!Send Out reports outpage in_packet:" + temp ) ;
					dos2.writeLong(hashcode) ; //hashcode
					dos2.write(content,(currentpage - 1)*maxsize, leavenumber>maxsize?maxsize:leavenumber) ; //content
					tempbyte = baos2.toByteArray() ;
				}catch(IOException e){
					throw new IOException("Failed to convert message to bytes") ;
				}finally{
					try{
						baos2.close() ;
						dos2.close() ;
					}catch(Exception e){
						//ignore
					}
				}			
				DatagramPacket packet2 = new DatagramPacket(tempbyte,tempbyte.length,toIP,toPort) ;
				sendSocket.send(packet2) ;
				currentpage++ ;
				try{
					wait(1000) ;
				}catch(Exception e){
					System.out.println("SendOut wait(long) throws==>" + e.getMessage() ) ;
				}
			}
		}
	}
	
	public void close(){
		sendSocket.close() ;
	}
	
}
