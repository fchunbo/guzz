package mm.smy.bicq.server.user ;

/**
* �����û����ϵ����ݿ⡣
* 
* 
* 
* 
* 
*/

import mm.smy.bicq.server.db.* ;

import mm.smy.text.StringFormat ;

import java.util.Date ;

import java.sql.Statement ;
import java.sql.SQLException ;

public class UpdateUserDB{
	private ServerGuest guest = null ;
	private StringBuffer sb = null ; //sql���
	
	private ReadWriteStatement rw = null ;
	private Statement stmt = null ;
	
	public UpdateUserDB(){
		guest = new ServerGuest() ;	
	}
	
	public UpdateUserDB(ServerGuest m_guest){
		guest = m_guest ;	
	}
	
	public void setServerGuest(ServerGuest m_guest){
		guest = null ;
		guest = m_guest ;	
		return ;
	}
	
	/**
	* ��������ʵ���뵽���ݿ⡣
	*/
	public int update() throws SQLException{
		init() ;
//		System.out.println("sb:" + sb) ;
//		return -1 ;
		
		if(sb == null) return -1 ;
		
		if(rw == null){
			rw = new ReadWriteStatement("user") ;		
		}
		if(stmt == null){
			stmt = rw.getStatement() ;
		}
		
		System.out.println("update user information to database. sql statement:" + sb ) ;
		
		return stmt.executeUpdate(sb.toString()) ;		
	}
	
	public void close(){
		if(rw != null){
			rw.close() ;
		}
		
		if(stmt != null){
			try{
				stmt.close() ;
				stmt = null ;
			}catch(Exception e){
			}
		}
	}
	
	//��ServerGuest��Fields����sql��䡣�������û����ServerGuest�������޸ģ���ô���������ݿ��еĸ��ֶβ���
	private void init() throws SQLException{
		if(guest.getNumber() == ServerGuest.UNDEFINE_INT && guest.getRecordID() == ServerGuest.UNDEFINE_LONG) throw new SQLException("UpdateUserDB.class::number�Ƿ�,��������ȷ��number") ;
		
		sb = new StringBuffer() ;
		sb.append("update user set ") ;
	
		sb.append(doNull("nickname", guest.getNickname()) ) ;
		sb.append(doNull("portrait", guest.getPortrait()) ) ;
		sb.append(doNull("mail", guest.getMail()) ) ;
		sb.append(doNull("realname", guest.getRealname()) ) ;
		sb.append(doNull("homepage", guest.getHomepage()) ) ;
		sb.append(doNull("zip", guest.getZip()) ) ;
		sb.append(doNull("address", guest.getAddress()) ) ;
		sb.append(doNull("country", guest.getCountry()) ) ;
		sb.append(doNull("province", guest.getProvince()) ) ;
		sb.append(doNull("myexplain", guest.getExplain()) ) ;
		sb.append(doNull("gender", guest.getGender()) ) ;
		sb.append(doNull("birthday", guest.getBirthday()) ) ;
		sb.append(doNull("auth", guest.getAuth()) ) ;
		sb.append(doNull("lastlogintime", guest.getLastLoginTime()) ) ;
		sb.append(doNull("logintime", guest.getLoginTime()) ) ;
		sb.append(doNull("totalonlinetime", guest.getTotalOnlineTime()) ) ;
		sb.append(doNull("password", guest.getPassword())) ;
		//��������ֶ��������޸ĵĻ�����ô���ʱ��sb.toString�����Ϊ", "������Ļ���û�����κβ�����
		if(!sb.toString().endsWith(", ")){
			sb.delete(0,sb.length()) ;
			sb = null ;	
		}else{
			sb = sb.deleteCharAt(sb.length() - 2) ; //ɾ��","
			/********************************************************************************************/
			//�������ͨ��number + recordID������
			if(guest.getNumber() != ServerGuest.UNDEFINE_INT){
				sb.append(" where number = ") ;
				sb.append(guest.getNumber()) ;	
			}else{
				sb.append(" where ID = ") ;
				sb.append(guest.getRecordID()) ;	
			}
		}		
		
		return ;
	}
	
	//������ĸ�������Ҫ��������sql��䡣
	private String doNull(String name, int n){
		if(n ==  ServerGuest.UNDEFINE_INT) //���û�иı䣬���� ��
			return "" ;
		return (name + " = " + n + ", ") ;
	}
	
	private String doNull(String name, String s){
		if( s== null) return "" ;
		if( s.equals(ServerGuest.UNDEFINE_STRING)) return "" ;
		return (name + " = \"" + StringFormat.gb2iso( s ) + "\", ") ;
	}
	
	private String doNull(String name, long l){
		if(l == ServerGuest.UNDEFINE_LONG) return "" ;	
		return (name + " = " + l + ", ") ;
	}
	
	private String doNull(String name, Date d){
		if(d.equals(ServerGuest.UNDEFINE_DATE)) return "" ;
		return (name + " = " + d.getTime() + ", ") ;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//methods like ServerGuest

	public void setRecordID(int m_record){ guest.setRecordID(m_record) ; }
	
	public void setAuth(int m_auth){ guest.setAuth(m_auth) ; }
	
	public void setLeaveWord(String word){ guest.setLeaveWord(word) ; }
	
	public void setRegisterTime(Date m_time){ guest.setRegisterTime(m_time) ; }
	
	public void setLastLoginTime(Date m_time){ guest.setLastLoginTime(m_time) ; }
	
	public void setLoginTime(Date m_time){ guest.setLoginTime(m_time) ; }
	
	public void setTotalOnlineTime(long m_min){ guest.setTotalOnlineTime(m_min) ; }

	public void setNickname(String m_nickname){guest.setNickname(m_nickname) ; }
	
	public void setNumber(int m_number){ guest.setNumber(m_number) ; }
	
	public void setPortrait(int m_portrait){ guest.setPortrait(m_portrait) ; }
	
	public void setMail(String m_mail){ guest.setMail(m_mail) ;}
	
	public void setGender(int m_gender){ guest.setGender(m_gender) ; }
	
	public void setRealname(String m_realname){ guest.setRealname(m_realname) ; }
	
	public void setHomepage(String m_homepage){ guest.setHomepage(m_homepage) ; }
	
	public void setTelephone(String m_telephone){ guest.setTelephone(m_telephone) ; }
	
	public void setZip(int m_zip){ guest.setZip(m_zip) ; }
	
	public void setAddress(String m_address){ guest.setAddress(m_address) ; }
	
	public void setCounty(String m_country){ guest.setCounty(m_country) ; }
	
	public void setProvince(String m_province){ guest.setProvince(m_province) ; }
	
	public void setExplain(String m_explain){ guest.setExplain(m_explain) ; }

	public void setBirthday(Date m_date){ guest.setBirthday(m_date) ; }
	
	public void setBirthday(int year, int month, int day){ guest.setBirthday(year,month,day) ; }
	
	public void setPassword(String password){ guest.setPassword(password) ; }
	
	/********************************Test Part**********************************/
	
	public static void main(String args[]) throws SQLException{
		UpdateUserDB db = new UpdateUserDB() ;
		db.setNumber(1001) ;
		db.setNickname("����") ;
		db.setAddress("��ɭ��") ;
		db.setAuth(21) ;
		db.update() ;
		
		
	}

	
}
