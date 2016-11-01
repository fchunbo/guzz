package mm.smy.bicq.login ;

/**
* ע�����û�
* 
* 
* @author XF
* 
* 
* 
* 
* 
*/

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;

import mm.smy.security.Digest ;
import mm.smy.bicq.debug.BugWriter ;
import mm.smy.bicq.* ;
import mm.smy.bicq.message.* ;
import mm.smy.util.* ;
import mm.smy.text.ValidCheck ;
import mm.smy.bicq.user.User ;
import mm.smy.bicq.message.MessageListener;

public class RegisterWindow extends JFrame implements ActionListener, MessageListener{
	
	public RegisterWindow(BICQ bicq, int m_port){
		this.bicq = bicq ;
		port = m_port ;
		m = bicq.getMainManager() ;
		initWindow() ;
	}
	
	private int port = -1 ;
	
	private JTextField nickname = new JTextField(15) ;
	private JPasswordField password = new JPasswordField(15) ;
	private JPasswordField repassword = new JPasswordField(15) ;
	private JTextField mail = new JTextField(15) ;
	
	private JButton back = new JButton("��½") ;
	private JButton next = new JButton("��һ��") ;
	private JButton finish = new JButton("�ر�") ;
	
	private JPanel fillform = new JPanel() ;
	private JPanel showprocess = new JPanel() ;
	JPanel buttons = new JPanel() ;
	
	private JLabel result = new JLabel() ; //Ŀǰ��״̬��
	
	private int currentframe = -1 ;
	
	private BICQ bicq = null ;
	private MainManager m = null ;
	
	private void initWindow(){
		this.setSize(400,400) ;
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(0) ;
				}				
			}		
		) ;
		
		back.setActionCommand("back") ;
		back.addActionListener(this) ;
		back.setName("back") ;
		
		next.setActionCommand("next") ;
		next.addActionListener(this) ;
		next.setName("next") ;
		
		finish.setActionCommand("finish") ;
		finish.addActionListener(this) ;
		finish.setName("finish") ;
		
		

		buttons.add(back) ;
		buttons.add(next) ;
		buttons.add(finish) ;
				
		fillform.setLayout(new GridLayout(5,1)) ;
		fillform.add(getTextPanel("�ǳƣ�","nickname",nickname)) ;
		fillform.add(getPasswordPanel("���룺","password",password)) ;
		fillform.add(getPasswordPanel("ȷ�����룺","repassword",repassword)) ;
		fillform.add(getTextPanel("���䣺","mail",mail)) ;
		fillform.add(buttons) ;
		
		showprocess.setLayout(new BorderLayout()) ;
		showprocess.add(result,BorderLayout.CENTER) ;
		showprocess.add(buttons,BorderLayout.SOUTH) ;
		
		setFrame(1) ;
	}
	
	/**
	*
	* @param m_number: 1, fillform 2, showprocess, 3, error, 4:register success, show the number.
	*
	*
	*/	
	private void setFrame(int m_number){ //starts at 1 
		if(currentframe == m_number) return ;
		
		Container cp = this.getContentPane() ;
		
	//	cp.removeAll() ;
		cp.setLayout(new BorderLayout()) ;
		
		switch (m_number){
			case 1 :
				this.back.setText("��½") ;
				this.next.setEnabled(true) ;
				if(currentframe != 1) cp.removeAll() ;
				
				cp.add(fillform,BorderLayout.CENTER) ;
				cp.add(buttons,BorderLayout.SOUTH) ;
				break ;
			case 2 :
				this.back.setText("��һ��") ;
				this.next.setEnabled(false) ;
				
				if(currentframe == 1){
					cp.removeAll() ;
					cp.add(showprocess,BorderLayout.CENTER) ;
					cp.add(buttons,BorderLayout.SOUTH) ;
					result.setText("���ڷ���������ȴ�����") ;
				}
				break ;
			case 3 : //���������������
				this.back.setText("��һ��") ;
				this.finish.setText("���") ;
				if(currentframe == 1){
					cp.removeAll() ;
					cp.add(showprocess,BorderLayout.CENTER) ;
					cp.add(buttons,BorderLayout.SOUTH) ;
				}
				break ;
			case 4 :
				this.back.setText("��һ��") ;
				this.finish.setText("��½") ;
				this.finish.setActionCommand("login") ;
				if(currentframe == 1){
					cp.removeAll() ;
					cp.add(showprocess,BorderLayout.CENTER) ;
					cp.add(buttons,BorderLayout.SOUTH) ;
				} 
				break ;
		}
		cp.repaint() ;
		cp.invalidate() ;
		//this.invalidate() ;	
		this.currentframe = m_number ;
		return ;
	}
	
	public void actionPerformed(ActionEvent e){
		String command = e.getActionCommand().trim().toLowerCase() ;
		if(command.equals("back")){
			if(currentframe == 1){
				this.dispose() ;
				LoginWindow window = new LoginWindow(bicq, port) ;
				window.show() ;
			}else if(currentframe == 2){	
				setFrame(1) ;
			}else if(currentframe == 3 || currentframe == 4){
				setFrame(1) ;
			}
		}else if(command.equals("finish")){
			bicq.getMainManager().close() ;
			return ;
		}else if(command.equals("next")){
			if(currentframe == 1){
				sendRequest() ;	
				//setFrame(2) ;
			}
		}else if(command.equals("login")){
			this.dispose() ;
			LoginWindow window2 = new LoginWindow(bicq, port) ;
			window2.show() ;			
		}
	}
	
	//��������ͬʱ�򿪼�����
	private void sendRequest(){
		//check password
		if(password.getText() == null || password.getText().length() == 0 ){
			password.setText("") ;
			repassword.setText("") ;
			System.out.println("getText():" + password.getText() ) ;
			JOptionPane.showMessageDialog(this,"�����Ǳ�����д�ģ�����������","����������",JOptionPane.ERROR_MESSAGE) ;	
			setFrame(1) ;
			return ;
		}
		if(!(password.getText().equals(repassword.getText() ) )){
			password.setText("") ;
			repassword.setText("") ;
			JOptionPane.showMessageDialog(this,"������������벻��ͬ�����������롣","���벻һ��",JOptionPane.ERROR_MESSAGE) ;	
			setFrame(1) ;
			return ;
		}
		
		String m_nickname = nickname.getText() ;
		//mail format checking
		String m_mail = mail.getText() ;
		if(!mm.smy.text.ValidCheck.isMail(m_mail)){
			JOptionPane.showMessageDialog(this,"������Ч������������","�������",JOptionPane.ERROR_MESSAGE) ;	
			setFrame(1) ;
			return ;
		}
		
		//SHA1 password
		String m_password = "" ;
		try{
			m_password = Digest.SHA_1(password.getText()) ;			
		}catch(java.io.UnsupportedEncodingException e1){
			System.out.println("un supported encoding exception detected.==>" + e1.getMessage() ) ;	
			BugWriter.log(this,e1,"�����㷨���뷽ʽϵͳ��֧��") ;
			JOptionPane.showMessageDialog(this,"�����㷨���뷽ʽϵͳ��֧��") ;
			return ;
		}catch(java.security.NoSuchAlgorithmException e2){
			BugWriter.log(this,e2,"ȱ�ټ����㷨SHA1") ;
			System.out.println("no such algorithm exception detected==>" + e2.getMessage() ) ;	
			JOptionPane.showMessageDialog(this,"ȱ�ټ����㷨SHA1") ;
			return ;
		}
		
		User u = new User() ;
		
		u.setNickname(m_nickname) ;
		u.setMail(m_mail) ;		
		
		RegisterMessage message = new RegisterMessage() ;
		message.setPassword(m_password) ;
		message.setUser(u) ;
		message.setPort(port) ;
		
		openListener() ; // �򿪼���		
		m.sendOutMessage(message) ;	//������Ϣ	
		setFrame(2) ;
	}
	
	//������Ϣ�������ȴ���Ӧ���򿪼���.....
	private void openListener(){
		m.addMessageListener(this) ;
		
		SmyTimer timer = new SmyTimer() ;
		timer.setTimerListener(new StateCheck(timer)) ;
		timer.setInterval(50) ;
		timer.setTotalTime(5*1000) ;
		timer.startTimer() ;
	}
	
	private MessageListener listener = this ;
	
	private class StateCheck implements TimerListener{
		SmyTimer timer = null ;
		public StateCheck(SmyTimer m_timer){
			timer = m_timer ;	
		}
		
		public void timeElapsed(){
			System.out.println("timer elasped.") ;
			if(registerstate == 2){
				timer.stopTimer() ;
				m.removeMessageListener(listener) ;	
				setFrame(4) ;				
			}else if(registerstate == 4){
				timer.stopTimer() ;
				m.removeMessageListener(listener) ;
				setFrame(3) ;
			}
		}
		
		public void timeOut(){
			timer.stopTimer() ;
			m.removeMessageListener(listener) ;
			result.setText("���糬ʱ���������硣") ;
			setFrame(3) ;
			System.out.println("timeout") ; 
		}
		
	}
	
	private int registerstate = 1 ; //1:preparing  2:ok, the number has got, 3:net error,, timeout, 4:server net.
	
	public void messageAction(ReceivedMessage message){
		if(message.getType() == MessageType.ICMP_MESSAGE){
			ICMPMessage mess = new ICMPMessage() ;
			mess.setByteContent(message.getContent()) ;
			
			if(mess.getMinType() == ICMPMessage.REGISTER_RESULT_SUCCESS){
				result.setText("ע��ɹ�������BICQ���ǣ�" + mess.getContent() ) ;
				this.registerstate = 2 ;
			}else if(mess.getMinType() == ICMPMessage.REGISTER_RESULT_FAIL){
				result.setText("����������" + mess.getContent() ) ;
				this.registerstate = 4 ;
			}
		}
	}
	
		
	// @parame length -1 means using default or default_value's length
	private JPanel getTextPanel(String label_string, String text_name, JTextField field){
		JPanel panel = new JPanel() ;
		JLabel label = new JLabel(label_string) ;
			
		if ( text_name!= null)
			field.setName(text_name) ;
		label.setLabelFor(field) ;
		panel.add(label) ;
		panel.add(field) ;
		return panel ;
	}
	
	// @parame length -1 means using default or default_value's length
	private JPanel getPasswordPanel(String label_string, String text_name, JPasswordField field){
		JPanel panel = new JPanel() ;
		JLabel label = new JLabel(label_string) ;

		if ( text_name!= null)
			field.setName(text_name) ;
		label.setLabelFor(field) ;
		panel.add(label) ;
		panel.add(field) ;
		return panel ;
	}


//	public static void main(String[] agrs){
//		MainManager m = new MainManager() ;
//		RegisterWindow window = new RegisterWindow(m) ;	
//		window.setVisible(true) ;
//	}

	
	
	
	
	
}
