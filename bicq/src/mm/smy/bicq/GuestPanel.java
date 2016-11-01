package mm.smy.bicq ;

/**
* �û�����Guestͷ����mf�еİ�ť��
* ����ֱ�Ӽ̳�JButton���������Լ��Ļ��Ʒ���������Լ��İ�ť��
* 
* ע�⵽״̬�ı䣬��Сͷ��ȵȣ����Ի���ʱ������User�����Լ��Ǵ�ͷ����Сͷ��
* 
* ͷ��Ᵽ����/faceĿ¼���档
* number-1.bmp :����ͷ��
* number-2.bmp :������״̬
* number-3.bmp :�뿪״̬
* 
* 
* 
* �����Ѿ���User�������˴����������µı�������δ������Ϣ��
* ��Ϊ�չ˵� ���Լ���Ϊ���ѻ��ǵ���ʱʹ������������û����Guest���󱣴�����ϣ�
* �Ժ�Ϊ�˷�ֹ�Լ����Լ���Ϊ���ѿ���ǿ���Կ��ǡ�
*/

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import java.io.* ;

import mm.smy.bicq.user.User ;

public class GuestPanel extends JPanel{
	
	private User u = null ;
	boolean isSmallPortrait = false ;
	private JButton b = null ;
	private int number = -1 ;

	//public GuestPanel(){}
	
	public GuestPanel(User u, boolean isSmallPortrait){
		this.u = u ;
		this.isSmallPortrait = isSmallPortrait ;
		fresh() ;
	}
	
	
    public ImageIcon createImageIcon(int portrait) {
		return FaceManager.getFaceIcon(portrait, u.getState()) ;
    }
    
    //we can paint out panel specially here.
    
    public void paintComponent(Graphics g){
    	super.paintComponents(g) ;
    	//g.drawLine(b.getX(),b.getY(),b.getX() + 40,b.getY() + 40) ;
	   //	System.out.println("paintComponent has been invoked....") ;   	
    }
	
	/**
	* ���Ƹ���塣
	*
	*
	*
	*/
	private void fresh(){
		if(u == null) return ;
		
		number = u.getNumber() ;
		
		this.setSize(150,60) ;
		
		//b = new JButton(this.getNickname(),this.getImage()) ;
		b = new JButton(this.getNickname(),this.createImageIcon(u.getPortrait())) ;
		
		if(u.getUnreadMessages() > 0 ) b.setText(this.getNickname() + "[����Ϣ]") ;
		
		b.setActionCommand(u.getNumber() + "") ;
		b.setSize(150,50) ;
		
//		JLabel label = new JLabel(this.getImage()) ;
//		label.setLabelFor(b) ;
		
//		this.add(label) ;
		this.add(this.getNickname(),b) ;
	}
	
	public void addMouseListener(MouseListener listener){
		b.addMouseListener(listener) ;
	}
	
	public void addActionListener(ActionListener listener){
		b.addActionListener(listener) ;	
	}
	
	public String getNickname(){
		if(u == null) return "" ;
		
		if(u.getNickname() == null || u.getNickname().length() == 0 )
			return u.getNumber() + "" ;
		return u.getNickname() ;		
	}
	
	private Icon getImage(){
		if( u == null) return null ;
		
//		String nickname = u.getNickname() ;
//		if(nickname == null || nickname.length() == 0 )
//			nickname = u.getNumber() + "" ;
		
		ImageIcon icon = null ;
		icon = new ImageIcon("face" + File.separator + u.getPortrait() + "-1.jpg" ) ;
		
		if(icon == null){
			System.out.println("no such image") ;
			icon = new ImageIcon("face" + File.separator + "1-1.jpg")	;
		}
		
		return icon ;
//		b = new JButton(nickname,icon) ;
//		if(isSmallPortrait){
		//	b.setSize(50,40) ;
			//icon.paintIcon(b,b.getGraphics(),0,0) ;
//			b.setHorizontalTextPosition(AbstractButton.RIGHT) ;
			
//			b.setActionCommand((number > 0 ?number:u.getNumber()) + "") ;
			
//		}else{
		//	b.setSize(100,100) ;
//			b.setVerticalTextPosition(AbstractButton.BOTTOM) ;
			
//			b.setActionCommand((number>0?number:u.getNumber()) + "") ;	
//		}
		
//		this.add(b) ;
		
	}
	
	//���غ��ѵ�BICQ�š�
	public int getNumber(){
		return number ;
	}
	
	public String getText(){
		if(u==null) return "N/A" ;	
		
		if(u.getNickname() == null || u.getNickname().length() == 0 )
			return u.getNumber() + "" ;
		return u.getNickname() ;
	}
	
	protected void setNumber(int m_number){
		number = m_number ;		
	}
	
}
