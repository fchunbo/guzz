package mm.smy.mail ;

/**
* �û��ļ����쳣��
* 
* @author XF  <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/6-7
*/

public class UserFolderException extends Exception implements java.io.Serializable{
	
	public static final int READ_FORBID = 2345 ;
	public static final int WRITE_FORBID = 2346 ;
	public static final int ACCESS_FORBID = 2347 ;
	public static final int USER_TYPE_ERROR = 2348 ;
	public static final int NO_HOLD_USERS = 2350 ;
	public static final int NO_HOLD_FOLDERS = 2351 ;

	
	private int type  ;
	
	public UserFolderException(String s, int type){
		super(s) ;
		this.type = type ;
	}
	
	public UserFolderException(int type){
		super() ;
		this.type = type ;
	}
	
	public UserFolderException(Throwable t, String s, int type){
		super(s, t) ;
		this.type= type ;
	}
	
	public UserFolderException(Throwable t, int type){
		super(t) ;
		this.type = type ;
	}
	
	public int getType(){ return type ; }
	
	/**
	* ��ö��쳣��ϵͳ���ͣ��ý�����Ҫ�����ڹ��캯���д����int type���͡�
	* ��type�޷�ʶ���ʱ�򣬷��ظ����Ľ��ͣ����û�н��ͣ�ʹ��ϵͳĬ�ϵ��޷�ʶ����Ϣ��
	* @return String ϵͳ����
	*/
	public String getSystemExplain(){
		switch(type){
			case READ_FORBID :
				return explains[0] ;
			case WRITE_FORBID :
				return explains[1] ;
			case ACCESS_FORBID :
				return explains[2] ;
			case NO_HOLD_USERS :
				return explains[3] ;
			case NO_HOLD_FOLDERS :
				return explains[4] ;
			case USER_TYPE_ERROR :
				return explains[5] ;
			default:
				return super.getMessage() == null?explains[6]:super.getMessage() ;
		}
	}
	
	private static String[] explains = {
		"Cannot read from the special folder",
		"Cannot write to the special folder",
		"Cannot access to the special folder",
		"The folder cannot hold contacts",
		"The folder cannot hold subfolders",
		"The folder doesn't accept the given contact type",
		"Unknown exception type"
	} ;
	
	
}
