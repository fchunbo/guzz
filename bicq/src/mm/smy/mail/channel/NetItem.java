package mm.smy.mail.channel ;

/**
* Bicq��׼���Լ����л���������ҪΪ�˱���ʹ��Java�����л������������ľ޴���������������ѹ����
* �Ӷ���������������������������ʱ������Դ��ƿ����
* 
* @author XF <a href="mailto:myreligion@163.com">myreligion@163.com</a>
* @date 2004/2/6
*/

public interface NetItem extends java.io.Serializable{
	/**
	* �ö���Ĺؼ��������һ���ֽ����顣
	* @exception FormatException ���쳣Ϊ�����쳣��ͨ�ñ�ʾ����ʱ�󲿷�Ӧ����IOException
	*/
	public byte[] toBytes() throws FormatException;	
	/**
	* �ø������ֽ����鷴���л���һ������
	*/
	public Object toObject(byte[] b) throws FormatException;
}