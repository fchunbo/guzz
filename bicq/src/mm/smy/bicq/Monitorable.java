package mm.smy.bicq ;

/**
* �ɶ�Monitor���м�ء�
*
*
*
*/

import mm.smy.bicq.message.ReceivedMessage ;

public interface Monitorable{
	public void sendReceivedMessage(ReceivedMessage rm)	;
	public void sendMonitorException(Exception e) ;
	public void close() ;

}
