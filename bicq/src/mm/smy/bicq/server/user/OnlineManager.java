package mm.smy.bicq.server.user ;

/**
* �ṩ�����ߺ��ѵĹ���
*
*
*
*
*
*/
import java.util.* ;

public class OnlineManager{
	
	private Hashtable onlines = new Hashtable() ; //number VS OnlineUser. ���������Ӧ����AVL Tree���Ժ��д��
	
	
	public OnlineManager(){
		
	}
	
	public void addOnlineUser(OnlineUser user){
		if(user == null) return ;
		onlines.put(new Integer(user.getNumber()),user) ;
		return ;
	}
	
	public void removeOnlineUser(int number){
		onlines.remove(new Integer(number)) ;
		return ;	
	}
	
	public OnlineUser getOnlineUser( int number){
		return (OnlineUser) onlines.get(new Integer(number)) ;
	}
	
	public boolean isExsit(OnlineUser user){
		return onlines.containsKey(new Integer(user.getNumber())) ;	
	}
	
	/**
	* �������������OnlineUser[]����
	* @param num ���صĸ��������ܻ᲻��Ҫ��ĸ�������Ϊ�ῼ�ǵ�Ч�ʵ����⡣
	*/
	public OnlineUser[] getRandomOnlineUser(int num){
		if(num <= 0 ) return new OnlineUser[0] ;
		if(num >= onlines.size())
			return (OnlineUser[]) onlines.values().toArray(new OnlineUser[0]) ;
		
		//���ǰ���˳�򷵻أ������������^_^		
		OnlineUser[] t = new OnlineUser[num] ;
		
		Vector v = new Vector(onlines.values()) ;
		
		try{
			for(int i = 0 ; i < num ; i++){
				t[i] = (OnlineUser) v.get(i) ;			
			}
		}catch(Exception e){ //�����ڲ��ҵ�ͬʱ���û����ߣ�����Խ�硣
			//
		}
		return t ;		
	}
	
	
}
