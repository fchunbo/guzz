package mm.smy.text ;

/**
* �ִ�����
* 
* @author XF
* @date 2003-11-26
* 
* 
* 
*/

import java.io.UnsupportedEncodingException ;

public class StringFormat{
	
	public static String iso2gb(String str) { //תiso->gb�����ڽ�����ת�����ġ�database,post,get
      if (str != null) {
          byte[] tmpbyte=null;
          try {
              tmpbyte=str.getBytes("ISO8859_1");
          }
          catch (UnsupportedEncodingException e) {
              System.out.println("mm.smy.util.StringFommat:iso2gb(String)-->" + e.getMessage());
          }
          try {
              str=new String(tmpbyte,"GBK");
          }
          catch(UnsupportedEncodingException e) {
              System.out.println("mm.smy.util.StringFommat:iso2gb(String)-->" + e.getMessage());
          }
      }
      return str;
	}

	public static String gb2iso(String string){ //תgb->iso�����ڴ洢�����ݿ���
      if (string != null) {
          byte[] tmpbyte=null;
          try {
              tmpbyte=string.getBytes("GBK");
          }
          catch(UnsupportedEncodingException e1) {
              System.out.println("mm.smy.util.StringFommat:gb2iso(String):e1-->"+e1.getMessage());
          }
          try {
              string=new String(tmpbyte,"ISO8859_1");
          }
          catch(UnsupportedEncodingException e2) {
              System.out.println("mm.smy.util.StringFommat:gb2iso(String):e1-->"+e2.getMessage());
          }
      }
      return string;
	}
	
	/**
	* Ϊ�˽����"/211.68.47.45"֮���IP�޷��������⣬���Ǳ��������ֵ�IP��ַ��
	* �����"Cathy/211.68.47.45"�޸�Ϊ"211.68.47.45"
	* 
	* @param ip Ҫת����IP
	* @return ת����ɵ�IP
	*/
	public static String formatIP(String ip){
		if(ip == null|| ip.length() == 0) return ip ;

		int start = ip.indexOf("/") ;
		String temp_ip = null ;
		if(start != -1){
			temp_ip = ip.substring(start + 1 ,ip.length()) ;	
		}
		
		return temp_ip ;		
	}
	
	public static String formatIP(java.net.InetAddress ip){
		if(ip == null) return null ;
		
		return formatIP(ip.toString()) ;	
	}
	
	
	
	
	
	
	
}