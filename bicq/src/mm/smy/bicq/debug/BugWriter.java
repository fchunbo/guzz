package mm.smy.bicq.debug ;

/**
* �����������ԵĴ���ʱ�������ø����¼��
*
* @author XF
* @atthor e-mail:myreligion@163.com
* @title �����̳�
* @date 2003-10-17
* @copyright Copyright XF 2003 All Rights Reserved
*
*/

public class BugWriter{
	
	/**
	* log the explains
	* @param from �������Դ�༰����ķ�����
	* @param exception ���׳����쳣
	* @param myexplain �����Ա��ӵĶԸ��쳣�Ľ��͡�
	*/	
	public static void log(String from, Exception exception, String myexplain){
		System.out.println(myexplain + "===============>" + exception.getMessage() ) ;
	}
	public static void log(Exception e){
		System.out.println("Exception==>" + e.getMessage() ) ;
	}
	public static void log(Exception e, String myexplain){
		System.out.println(myexplain + "==>" + e.getMessage() ) ;	
	}
	
	public static void log(Object from, Exception e, String myexplain){
		System.out.println( from.getClass().getName() + "||"  + myexplain + "==>" + e.getMessage() ) ;		
	}
	
	
	
}
