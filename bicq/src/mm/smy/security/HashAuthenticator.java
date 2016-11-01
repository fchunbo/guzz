package mm.smy.security ;

/**
* ����hash�����������Ҫ��Ϊ��˫����֤������֤hash�Ƿ���ȷ���پ������ܽ��ܡ�
* �����ں���ɢ���㷨�뵥����ܡ���Ҫ���ṩ����֮�á�
* ����ʵ�ʵ�ԭ�򣬸����е����������ı��档
* ���ڰ�ȫ�ĽǶȣ���Ϊopen source�ƻ�������һ��д��㲻��ȡ�ء�
*
* @author XF <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/8
*/

import java.io.UnsupportedEncodingException ;
import java.io.ByteArrayInputStream ;
import java.io.ByteArrayOutputStream ;
import java.io.DataInputStream ;
import java.io.DataOutputStream ;

//import javax.crypto.KeyGenerator ;
//import javax.crypto.SecretKey ;
//import javax.crypto.Cipher ;
//import javax.crypto.IllegalBlockSizeException ;
import javax.crypto.* ;
import javax.crypto.spec.* ;

import java.security.NoSuchAlgorithmException ;
import java.security.InvalidKeyException ;
import java.security.spec.InvalidKeySpecException ;

import mm.smy.mail.channel.NetItem ;
import mm.smy.mail.channel.FormatException ;

public class HashAuthenticator implements java.io.Serializable, NetItem{
	
	/**
	* ����һ���µ�HashAuthenticator
	* @param promt ��ʾ����
	* @param echo  ����������Ĺ������Ƿ������������
	* @exception UnsupportedEncodingException ���ϵͳ��"ISO-8859-1"���벻֧�ֵĻ��׳�����ʱ������ȫ�����⣬���ٿ��á�
	* @exception NoSuchAlgorithmException ���ϵͳ���ܽ���������ɢ��[MD5]��
	*/
	public HashAuthenticator(String promt, boolean echo) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		this.promt = promt ;
		this.echo = echo ;
		init() ;
		hashcode = defaulthashcode ;
	}
	
	public String getPromt(){
		return promt ;
	}
	
	public void setPromt(String promt){this.promt = promt ;}
	
	/**
	* �������룬�����뽫���ڼ��ܽ��ܣ�ͬʱ������֤�������ɢ����������Ƿ�һ�£��μ�isHashOK(String)
	* @exception UnsupportedEncodingException ���ϵͳ��֧��"ISO-8859-1"���롣
	* @exception NoSuchAlgorithmException ���ϵͳ���ܽ���������ɢ��[MD5]��
	* @param byte[] �µ����룬Ϊnull����������롣
	*/
	public void setPassword(char[] p) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		password = p ;
		if(p == null)
			hashcode = defaulthashcode ;
		hashcode = Digest.MD5(new String(p)) ;
	}
	
	/**
	* �������
	*/
	public void clearPassword(){
		password = null ;
		hashcode = defaulthashcode ;
	}
	
	public boolean isPasswordOK(char[] m_password){
		if(password == null && m_password == null) return true ;
		return m_password.equals(password) ;
	}
	
	public boolean isHashOK(String m_hashcode){
		if(m_hashcode == null) return false ;
		if(password == null && m_hashcode.equals(defaulthashcode)) return true ;
		return m_hashcode.equals(hashcode) ;
	}
	
	public String getHashcode(){
		return hashcode ;
	}
	
	public byte[] encrypt(byte[] b) throws UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,NoSuchPaddingException, BadPaddingException{
		//KeyGenerator keygen = KeyGenerator.getInstance("DES"); 
		// = keygen.generateKey(); 
		//PBEKeySpec pbe = new PBEKeySpec("test".toCharArray()) ;
		char[] temp = password ;
		if(temp == null)
			temp = this.defaultpassword ;
		
		DESKeySpec pbe = new DESKeySpec(Digest.SHA_1(new String(temp)).getBytes()) ;
		SecretKey deskey = SecretKeyFactory.getInstance("DES").generateSecret(pbe) ;

		Cipher c1 = Cipher.getInstance("DES"); 
		c1.init(Cipher.ENCRYPT_MODE, deskey); 
		byte[] cipherByte=c1.doFinal(b);
		return cipherByte ;
	}
	
	public byte[] decrypt(byte[] b) throws UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,NoSuchPaddingException, BadPaddingException{
	//	KeyGenerator keygen = KeyGenerator.getInstance("DES"); 
	//	SecretKey deskey = keygen.generateKey(); 
	//	PBEKeySpec pbe = new PBEKeySpec("test".toCharArray()) ;
		char[] temp = password ;
		if(temp == null)
			temp = this.defaultpassword ;
//		byte[] ht = new byte[b.length - 1] ;
//		for(int i = 0 ; i < ht.length ; i ++){
//			ht[i] = b[i + 1] ;
//		}
		
		DESKeySpec pbe = new DESKeySpec(Digest.SHA_1(new String(temp)).getBytes()) ;
		SecretKey deskey = SecretKeyFactory.getInstance("DES").generateSecret(pbe) ;
	
		Cipher c1 = Cipher.getInstance("DES"); 
		c1.init(Cipher.DECRYPT_MODE,deskey); 
		byte[] clearByte=c1.doFinal(b); 
		return clearByte ;
	}
	
	public boolean isEchoOn(){
		return echo ;
	}
	
	public void setEchoOn(boolean ison){
		echo = ison ;
	}
/*-------implements methods--------------------*/
	
	public byte[] toBytes() throws FormatException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream() ;
		DataOutputStream dos = null ;
		byte[] back = null ;
	
		try{
			if(!isInited) init() ;
			
			dos = new DataOutputStream(bout) ;
			dos.writeUTF(promt == null?"":promt) ;
			dos.writeUTF(new String(password)) ;
			dos.writeBoolean(echo) ;
			back = bout.toByteArray() ;	
		}catch(Exception e){
			throw new FormatException(e) ;
		}finally{
			try{
				bout.close() ;
				dos.close() ;
			}catch(Exception e){}
		}			
		return back ;
	}
	
	public Object toObject(byte[] b) throws FormatException{
		ByteArrayInputStream bin = new ByteArrayInputStream(b) ;
		DataInputStream dis = null ;
		try{
			dis = new DataInputStream(bin) ;
			promt = dis.readUTF() ;
			setPassword(dis.readUTF().toCharArray()) ;
			echo = dis.readBoolean() ;			
		}catch(Exception e){
			throw new FormatException(e) ;
		}finally{
			try{
				bin.close() ;
				dis.close() ;
			}catch(Exception e){}
		}
		return this ;
	}
	
	
	private static void init() throws UnsupportedEncodingException, NoSuchAlgorithmException{
		if(!isInited){
			defaultpassword = "$%tgGS*()5^%$gj<k>?HGSGEjygjy75af:".toCharArray() ;
			defaulthashcode = Digest.MD5("$%tgGS*()5^%$gj<k>?HGSGEjygjy75af:") ;
			isInited = true ;
		}
	}	
	
	public static String byte2hex(byte[] b){  //������ת�ַ��� 
		String hs= "" ; 
		String stmp= "" ; 
		for (int n=0;n<b.length;n++){ 
			stmp=(java.lang.Integer.toHexString(b[n] & 0XFF)) ; 
			if (stmp.length()==1)
				hs=hs+"0"+stmp ; 
			else
				hs=hs+stmp ; 
		} 
		return hs.toUpperCase(); 
	}
	
	public static void main(String[] args) throws Exception{
		HashAuthenticator hash = new HashAuthenticator("what's your name", false) ;
		String infor = "This is ���� characters." ;
		System.out.println("org String:" + infor) ;
		System.out.println("org bytes:" + byte2hex(infor.getBytes())) ;
		byte[] en = hash.encrypt(infor.getBytes()) ;
		System.out.println("en String:" + new String(en)) ;
		System.out.println("en bytes:" + byte2hex(en)) ;
		byte[] de = hash.decrypt(en) ;
		System.out.println("de String:" + new String(de)) ;
		System.out.println("de bytes:" + byte2hex(de)) ;
		
	}
	
	private static boolean isInited = false ;
	private static char[] defaultpassword = null ;
	private static String defaulthashcode = null ;
	private String promt = null ;
	private boolean echo = true ;
	private char[] password = null ;
	private String hashcode = null ;
}