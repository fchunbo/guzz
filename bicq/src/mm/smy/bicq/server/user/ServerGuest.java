package mm.smy.bicq.server.user ;

/**
* 包含用户的详细资料。
* 
* 
* 
* 
*/

import mm.smy.bicq.user.User ;
import mm.smy.bicq.user.Host ;

import java.util.Date ;

import java.net.InetAddress ;

public class ServerGuest implements java.io.Serializable{
	//我们继承了User的很多方法。
	
	public static String UNDEFINE_STRING = "�+·￥�% SF%&^*&#%@%&*^&#%^" ;
	public static int UNDEFINE_INT = Integer.MIN_VALUE + 1223 ;
	public static long UNDEFINE_LONG = Long.MIN_VALUE + 1223 ;
	public static Date UNDEFINE_DATE = new Date(2060,3,18) ;
	
	private long recordID = ServerGuest.UNDEFINE_LONG ; //该记录在数据库中的位置（User.ID）
	
	public long getRecordID(){ return recordID ; }
	public void setRecordID(long m_record){ recordID = m_record ; }

	
	private int auth = this.UNDEFINE_INT ;
	private String leaveword = this.UNDEFINE_STRING ; //暂时离开的消息。
	private Date registertime = this.UNDEFINE_DATE ; //注册时间
	private Date lastlogintime = this.UNDEFINE_DATE ; //上次登陆时间
	private Date logintime = this.UNDEFINE_DATE ; //登陆时间
	private long totalonlinetime = this.UNDEFINE_LONG ; //总的登陆时间，以上次登陆时间为准。分 计数。
	
	public void setAuth(int m_auth){ auth = m_auth ;}
	public int getAuth(){ return auth ;}
	
	public void setLeaveWord(String word){ leaveword = word ; }
	public String getLeaveWord(){ return leaveword ; }
	
	public void setRegisterTime(Date m_time){ registertime = m_time ; }
	public Date getRegisterTime(){ return registertime ; }
	
	public void setLastLoginTime(Date m_time){ lastlogintime = m_time ; }
	public Date getLastLoginTime(){ return lastlogintime ; }
	
	public void setLoginTime(Date m_time){ logintime = m_time ; }
	public Date getLoginTime(){ return logintime ; }
	
	public long getTotalOnlineTime(){
		return totalonlinetime ;
	}
	
	public void setTotalOnlineTime(long m_min){ totalonlinetime = m_min ; }
	
		
///////////////////////////////////////////////////////////////////////////////////////////////////////
//the user's IP infor
	private InetAddress registerIP = null ;
	
	public void setRegisterIP(InetAddress m_IP){ registerIP = m_IP ;} 
	public void setRegisterIP(String m_IP){
		try{
			registerIP = InetAddress.getByName(m_IP) ;	
		}catch(Exception e){}
		return ;
	}	
	public InetAddress getRegisterIP(){return registerIP ; }

	//改IP不是及时更新的，所以不要用这个IP发送StateChangedMessage
	private InetAddress lastLoginIP = null ;
		
	public void setLastLoginIP(InetAddress m_IP){ lastLoginIP = m_IP ;} 
	public void setLastLoginIP(String m_IP){
		try{
			lastLoginIP = InetAddress.getByName(m_IP) ;	
		}catch(Exception e){}
		return ;
	}	
	public InetAddress getLastLoginIP(){ return lastLoginIP ; }

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//methods and fields from the mm.smy.bicq.user.User

	//basic infor
	protected int number = UNDEFINE_INT ;
	protected String nickname = UNDEFINE_STRING ;
	protected int state = UNDEFINE_INT ;
	
	//先前的状态，因为有些程序要求检测状态的变化。
	//所以加入该字段，当state变化时自动保存上次的state
	//用户可以选择让这两个state做暂时的一样，一使程序不会重复检测。
	protected int previousstate = this.UNDEFINE_INT ; 
	
	protected int portrait = this.UNDEFINE_INT ; //头像文件名称
	protected String mail = UNDEFINE_STRING ;
	//methods	
	public String getNickname(){
		if(nickname == null || nickname.length() == 0 )
			return new Integer(number).toString() ;
		return nickname ;
	}
	public int getNumber(){return number;}
	//两种状态。
	public int getState(){ return state ; }
	public int getPreviousState(){ return previousstate ; } 
	
	public int getPortrait() { return portrait ;}
	public String getMail() { return mail ;}
	
	public void setNickname(String m_nickname){
		if (m_nickname.length() == 0 || m_nickname == null)
			return ;// no null or empty nickname is allowed.
		nickname = m_nickname ;
	}
	public void setNumber(int m_number){ 
		number = m_number ;
		//we check the valid of nickname here....
	 	if(nickname == null || nickname.length() == 0){
	 		nickname = new Integer(number).toString() ;
	 	}
	}
	public void setState(int m_state){
		previousstate = state ; //自动的保存上次的state.
		state = m_state ; 
	}
	//设置上次的状态
	public void setPreviousState( int m_state){
		previousstate = m_state ;
	}
	
	
	public void setPortrait(int m_portrait){portrait = m_portrait;}
	public void setMail(String m_mail){ mail = m_mail ;}
	
	//after a long thought, I think we should put this in the main memory.
	protected String realname = UNDEFINE_STRING ;
	protected String homepage = UNDEFINE_STRING ;
	protected String telephone = UNDEFINE_STRING ;
	protected int zip = this.UNDEFINE_INT ;
	protected String address = UNDEFINE_STRING ;
	protected String country = UNDEFINE_STRING ;
	protected String province = UNDEFINE_STRING ;
	protected String explain = UNDEFINE_STRING ;

	protected int gender = UNDEFINE_INT ; // -1:not sure ; 0:girl  ; 1:boy
	
	// the following is some methods to support this main memory waste rubblish!
	public int getGender(){ return gender ; }
	public String getRealname(){return realname ;}
	public String getHomepage(){return homepage ;}
	public String getTelephone(){return telephone ;}
	public int getZip(){ return zip ;}
	public String getAddress(){return address ;}
	public String getCountry(){return country ;}
	public String getProvince(){return province ;}
	public String getExplain(){return explain ;}

	
	public void setGender(int m_gender){ gender = m_gender ; }
	public void setRealname(String m_realname){realname = m_realname ;}
	public void setHomepage(String m_homepage){homepage = m_homepage ;}
	public void setTelephone(String m_telephone){telephone = m_telephone ;}
	public void setZip(int m_zip){zip = m_zip ;}
	public void setAddress(String m_address){address = m_address ;}
	public void setCounty(String m_country){country = m_country ;}
	public void setProvince(String m_province){province = m_province ;}
	public void setExplain(String m_explain){explain = m_explain ;}

	//year, month, day is really rubbish design. And here we change it to "birthday"
	
	private Date birthday = this.UNDEFINE_DATE ;
	
	public Date getBirthday(){ return birthday ;}
	public void setBirthday(Date m_date){ birthday = m_date ; }
	public void setBirthday(int year, int month, int day){
		birthday = new Date( year - 1900 ,month - 1 ,day) ;		
	}
	
	private String password = this.UNDEFINE_STRING ;
	public void setPassword(String m_password){
		password = m_password ; 	
	}
	public String getPassword(){ return password ; }
		
	
	//overrided methods
	public boolean equals(ServerGuest m_user){
		if(m_user == null) return false ;
		
		return number == m_user.getNumber() ;
	}
	public boolean equalsIngoreCase(ServerGuest m_user){
		if(m_user == null) return false ;
		
		return this.equals(m_user) ;
	}
	
	
	public void setUser(User u){
		if(u == null) return ;
		
		address = u.getAddress() ;
		country = u.getCountry() ;
		explain = u.getExplain() ;
		gender  = u.getGender() ;
		homepage = u.getHomepage() ;
		mail    = u.getMail() ;
		nickname =u.getNickname() ;
		number  = u.getNumber() ;
		portrait = u.getPortrait() ;
		previousstate = u.getPreviousState() ;
		province = u.getProvince() ;
		realname = u.getRealname() ;
		state = u.getState() ;
		telephone = u.getTelephone() ;
		zip = u.getZip() ;
		
		birthday = new Date(u.getYear(), u.getMonth(), u.getDay() ) ;
		leaveword = u.getLeaveWord() ;
		
		
	}
	
	
}
