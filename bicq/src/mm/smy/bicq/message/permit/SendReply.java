package mm.smy.bicq.message.permit ;

/**
* ���û�ϣ�������Ǽ�Ϊ����
* �������Ǹ����ظ���
* 
* 
* 
* 
*/
import mm.smy.bicq.user.Guest ;
import mm.smy.bicq.user.User  ;

import mm.smy.bicq.message.PermitMessage ;

import mm.smy.bicq.FaceManager ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;


public class SendReply extends JFrame implements ActionListener{
	
	private JLabel lable_nickame = new JLabel("�ǳƣ�") ;
	private JTextField nickname = new JTextField(10) ;
	
	private JLabel lable_number = new JLabel("BICQ:") ;
	private JTextField number = new JTextField(10) ;
	
	private JButton portrait = new JButton() ;
	
	private JTextArea result = new JTextArea(10,8) ;
	private JScrollPane scroll_result = new JScrollPane(result) ;
	
	private JButton send = new JButton("����") ;
	private JButton allow = new JButton("ͨ����֤") ;
	private JButton close = new JButton("�ر�") ;
	
	private Guest to = null ;
	private PermitMessageManager smm = null ;
	
	private PermitMessage receivedmessage = null ;
	
	public SendReply(PermitMessageManager m_smm,  Guest u, PermitMessage m_message){
		smm = m_smm ;		
		to = u ;
		receivedmessage = m_message ;
		initWindow() ;
	}
	
	private void initWindow(){
		if(to == null) return ;
		
		this.setTitle(to.getNickname() + " ���������Ϊ����" ) ;
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE) ;
		this.setSize(400,400) ;
		
		nickname.setText(to.getNickname()) ;
		number.setText(to.getNumber() + "") ;
		
		
		nickname.setEditable(false) ;
		number.setEditable(false) ;
		
		send.setActionCommand("refuse") ;
		send.setText("�ܾ�����") ;
		
		if(receivedmessage != null){
			result.setText(receivedmessage.getContent()) ;	
		}		
		result.setEditable(false) ;
		
		allow.setActionCommand("allow") ;
		allow.addActionListener(this) ;
		
		send.addActionListener(this) ;
		
		close.setActionCommand("close") ;
		close.addActionListener(this) ;
		
		portrait.setIcon(FaceManager.getFaceIcon(to.getPortrait(), to.getState())) ;
		portrait.setActionCommand("portrait") ;
		portrait.addActionListener(this) ;
		
		//���֡�
		JPanel top = new JPanel() ;
		top.add(this.lable_number) ;
		top.add(this.number) ;
		top.add(this.lable_nickame) ;
		top.add(this.nickname) ;
		top.add(this.portrait) ;
		
		JPanel bottom = new JPanel() ;
		bottom.add(this.send) ;
		bottom.add(this.allow) ;
		bottom.add(this.close) ;
		
		Container cp = this.getContentPane() ;
		cp.add(top, BorderLayout.NORTH) ;
		cp.add(this.scroll_result, BorderLayout.CENTER) ;
		cp.add(bottom, BorderLayout.SOUTH) ;
		
	}
	
	
	public void actionPerformed(ActionEvent e){
		String command = e.getActionCommand().trim().toLowerCase() ;
		if(command.equals("send")){ //��������
			//Container cp = this.getContentPane() ;
			PermitMessage message = new PermitMessage() ;
			message.setTo(to) ;
			message.setContent(result.getText()) ;
			message.setMintype(PermitMessage.PERMIT_REFUSED) ;
			smm.sendOutPermitMessage(message) ;
			//���ǲ�����Ϣ�Ƿ����ˣ��Ƿ��յ��ˡ���Щ�����Ժ�����
			result.setText("\n\n������Ϣ�Ѿ����ͳ�ȥ��") ;
			result.setEnabled(false) ;
			send.setText("���") ;
			send.setActionCommand("close") ;
			
			this.repaint() ;
			this.invalidate() ;
			
			return ;
		}else if(command.equals("close")){
			this.dispose() ;
			return ;	
		}else if(command.equals("portrait")){
			smm.getMainManager().getUserManager().showUserInfor(to) ;
			return ;
		}else if(command.equals("refuse")){
			result.setText("") ;
			result.setEditable(true) ;
			send.setText("���;ܾ���Ϣ") ;
			this.setTitle("�ܾ�����������ܾ����ɡ���") ;
			send.setActionCommand("send") ;
			this.repaint() ;
			this.invalidate() ;
		}else if(command.equals("allow")){
			System.out.println("allow:" + to) ;
			//System.out.println("") ;
			PermitMessage message2 = new PermitMessage() ;
			message2.setTo(to) ;
			message2.setContent(result.getText()) ;
			message2.setMintype(PermitMessage.PERMIT_ALLOWED) ;
			smm.sendOutPermitMessage(message2) ;
			//���ǲ�����Ϣ�Ƿ����ˣ��Ƿ��յ��ˡ���Щ�����Ժ�����
			result.setText("\n\n��ͨ����֤����Ϣ�Ѿ����ͳ�ȥ��") ;
			result.setEnabled(false) ;
			send.setText("���") ;
			send.setActionCommand("close") ;
				
		}
		
		return ;
	}
/*	
	public SendRequest(Guest g){
		to = g ;	
		initWindow() ;
	}
	
	public static void main(String[] args){
		SendRequest window = new SendRequest(new Guest(1000,"me")) ;
		window.show() ;	
		
	}
*/	
	
	
	
	
	
	
	
	
}
