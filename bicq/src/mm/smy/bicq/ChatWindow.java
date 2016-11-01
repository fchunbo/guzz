package mm.smy.bicq ;

import mm.smy.bicq.message.* ;
import mm.smy.bicq.user.* ;

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import java.util.* ;

public class ChatWindow extends JFrame implements ActionListener{
	private ChatWindowManager cwm = null ;
	private User friend = null ;
	private Host host   = null ;
	
	private JButton send = new JButton("����") ;
	private JButton cancel = new JButton("�ر�")  ;
	private JButton viewhistory = new JButton("��ʷ��Ϣ") ;
	
	//private Icon icon = new ImageIcon("face" + java.io.File.separator + "84-1.jpg") ;
	private JLabel label = new JLabel("") ;
	private JTextArea jta = new  JTextArea(5,4) ;
	private JScrollPane jsp = new JScrollPane(jta) ;
	
	private JTextArea oldlog = new JTextArea(5,4) ;
	private JScrollPane oldlogpane = new JScrollPane(oldlog) ;
	
	private Icon icon ; //ͷ��
	
	
	private JTextArea history = new JTextArea(5,4) ;
	private JScrollPane historyscroll = new JScrollPane(history) ;
	
	private boolean isHistoryOpen = false  ; //��ʾ������ʷ��Ϣ�������Ƿ��ǿɼ��ġ�
	
	public static void main(String[] args){
		ChatWindow cw = new ChatWindow() ;
		cw.setSize(400,400) ;
		cw.show() ;
	}
	
	public ChatWindow(){
		friend = new Guest(2000,"xiao ling") ;
		host = new Host(4000,"����") ;	
		
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(0) ;	
				}				
			}
		) ;
		label.setText("Chat With:" + friend.getNickname()) ;
		send.setAction(new SendAction("����")) ;
	//	send.setActionCommand("send") ;
		cancel.setActionCommand("cancel") ;
	//	send.addActionListener(this) ;
		cancel.addActionListener(this) ;
		paintWindow() ;	
		
		addBlindings() ;
	}
	
	protected void addBlindings(){
		InputMap input = jta.getInputMap() ;
		
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Event.CTRL_MASK) ;
		input.put(key, new SendAction("����")) ;
		
	}
	
	public ChatWindow(ChatWindowManager	 m_cwm,User u){
		cwm = m_cwm ;
		friend = u ;
		System.out.println("ChatWindow reports,friend number:" + friend.getNumber() + "||IP:" + friend.getIP()) ;
		host = cwm.getHost() ;
		paintWindow() ;
		loadUnreadMessages() ;
		cwm.getMainManager().getMainFrame().fix() ;
	}
	
	class SendAction extends AbstractAction{
		public SendAction(String name){
			super(name) ;
		}
		
		public void actionPerformed(ActionEvent evt){
//			System.out.println("work well") ;
			TextMessage tm = new TextMessage() ;
			tm.setTo(friend) ;
			tm.setContent(jta.getText()) ;
			tm.setSecurity(false) ;
			cwm.sendOutTextMessage(tm) ;
			textMessageProcess(tm) ;
			System.out.println("debug: chatwindow reports: textmessage sends to:" + friend.getNumber() ) ;
			jta.setText("") ;			
		}		
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equalsIgnoreCase("send")){ //send message out
			//hehe
		}else if(e.getActionCommand().equalsIgnoreCase("cancel")){ //cancel send
			this.dispose() ;
		}else if(e.getActionCommand().equalsIgnoreCase("viewhistory")){ // �쿴��ʷ��Ϣ
			System.out.println("view history") ;
			if(history.getText() == null || history.getText().length() == 0){ //û����Ϣ��������ǰ��
				loadHistoryFile(30) ;
			}
			isHistoryOpen = !isHistoryOpen ;
			historyscroll.setVisible(isHistoryOpen) ; //����ʷ��Ϣ�ɼ���
			this.hide() ;
			this.show() ;
		}
	}
	
	private void loadHistoryFile(int size){
		Vector v = cwm.getTextMessages(friend, size) ;
		if(v == null) return ;
		Iterator i = v.iterator() ;
		StringBuffer sb2 = new StringBuffer() ;
		
		while(i.hasNext()){
			TextMessage mess = (TextMessage) i.next() ;
			
			StringBuffer sb = new StringBuffer() ;
			sb.append("\n") ;
			sb.append(mess.getFrom() == null?" ": mess.getFrom().getNickname()) ;
			sb.append("��") ;
			sb.append(mess.getTo() == null?" ": mess.getTo().getNickname()) ;
			sb.append("˵[") ;
			sb.append(mess.getReceivedTime().toLocaleString()) ;
			sb.append("]:\n") ;
			sb.append(mess.getContent()) ;
			
			
			sb.append(sb2) ;
			sb2 = sb ;
			i.remove() ;
		}
		history.setText(sb2.toString()) ;
//		historyscroll.scrollRectToVisible(new Rectangle(0,0,history.getWidth(), history.getHeight())) ;
		System.out.println("load file ok") ;
	}
	
	/**
	* ��ȡδ������Ϣ��
	*
	*/
	private void loadUnreadMessages(){
		if(friend.getUnreadMessages() <= 0 ) return ;
		
		while(true){
			TextMessage temp_tm = cwm.getUnreadTextMessage(friend) ;
			System.out.println("unreadMessages:" + friend.getUnreadMessages()) ;
			//if(temp_tm == null) return ;			
			System.out.println("loading unread messages....") ;
			friend.decUnreadMessages() ;
			
			textMessageProcess(temp_tm) ;
			if(friend.getUnreadMessages() <= 0 ){
				//û��δ����Ϣ�ˣ�ˢ�º�����
				cwm.getMainManager().getMainFrame().fix() ;
				return ;
			}
		}
	}
	
	private void textMessageProcess(TextMessage tm){ //ʵ�ֶ��յ�message�Ĵ������getFrom() == null�����Ƿ��͵���Ϣ����
		if(tm == null) return ;
		
		User temp_user = tm.getFrom() ;
		StringBuffer sb = new StringBuffer() ;
		
		if(temp_user == null || temp_user.equals(host)){ // send out. we can use diffent color to separate this.
			sb.append("\n") ;
			sb.append(host.getNickname()) ;
			sb.append("��") ;
			sb.append(tm.getTo().getNickname()) ;
			sb.append("˵��") ;
			sb.append(tm.getReceivedTime().toLocaleString()) ;
			sb.append("����\n ") ;
			sb.append(tm.getContent()) ;

			oldlog.append(sb.toString()) ;
			this.history.setText(sb.toString() + history.getText() ) ;
		}else{ //receive. 
			sb.append("\n") ;
			sb.append(temp_user.getNickname()) ;
			sb.append("��") ;
			sb.append(host.getNickname()) ;
			sb.append("˵��") ;
			sb.append(tm.getReceivedTime().toLocaleString()) ;
			sb.append("����\n ") ;
			sb.append(tm.getContent()) ;
			
			oldlog.append(sb.toString()) ;
			this.history.setText(sb.toString() + history.getText() ) ;
		}
		
//		historyscroll.scrollRectToVisible(new Rectangle(0,0,history.getWidth(), history.getHeight())) ;
		
		return ;
	}
	
	public void show(){
		super.show() ;
		refreshIcon() ;
	}
	
	//ˢ��ͷ��
	private void refreshIcon(){
		icon = FaceManager.getFaceIcon(friend.getPortrait(), friend.getState()) ; ;
		label.setIcon(icon) ;
		label.repaint() ;
		label.invalidate() ;		
	}
	
	private void paintWindow(){
		this.setSize(400,400) ;
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					cwm.closeSingeWindow(new Integer(friend.getNumber())) ;
				//	outer = null ;
				}
				public void windowActivated(WindowEvent e){
					loadUnreadMessages() ;	
				}
			}
		) ;
		
		this.setTitle("��" + friend.getNickname() + "������") ;
		label.setText("����:" + friend.getNickname()) ;
		
		send.setAction(new SendAction("����")) ;

		cancel.setActionCommand("cancel") ;
		cancel.addActionListener(this) ;
		oldlog.setAutoscrolls(true) ;		
		
		viewhistory.setActionCommand("viewhistory") ;
		viewhistory.addActionListener(this) ;
		
		history.setAutoscrolls(true) ;
		history.setEditable(false) ;
		
		jta.setAutoscrolls(true) ;
		
		
		//���������е���ʷ��Ϣ
		JPanel old = new JPanel() ;
		old.setLayout(new BorderLayout()) ;
		oldlog.setEditable(false) ;
		oldlogpane.setAutoscrolls(true) ;
		old.add(oldlogpane,BorderLayout.CENTER) ;
		
		//����������
		icon = FaceManager.getFaceIcon(friend.getPortrait(), friend.getState()) ;
		
		label.setIcon(icon) ;
		if (icon == null){
			System.out.println("******************icon is null*********************") ;
		}
		label.setHorizontalAlignment(SwingConstants.LEFT) ;
		label.addMouseListener( //�������ʱ��ʾ�û����ϡ�
			new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					if(e.getClickCount() == 1){
						cwm.getMainManager().getUserManager().showUserInfor((Guest) friend) ;	
					}
				}
			}
		) ;
		//�������ݵ������ı����������ϡ�
		JPanel doc = new JPanel() ;
		doc.setLayout(new BorderLayout()) ;
		doc.add(label,BorderLayout.NORTH) ;
		doc.add(jsp,BorderLayout.CENTER) ;
		
		//��ť
		JPanel buttons = new JPanel() ;
		buttons.add(send) ;
		buttons.add(cancel) ;
		buttons.add(viewhistory) ;

		//��ʷ��¼
		JPanel hpane = new JPanel(new BorderLayout()) ;
		hpane.add(buttons, BorderLayout.NORTH) ;
		hpane.add(historyscroll, BorderLayout.CENTER) ;
		
		historyscroll.setVisible(false) ;
		
		Container cp = this.getContentPane() ;
		cp.setLayout(new BorderLayout()) ;
		cp.add(old, BorderLayout.NORTH) ;
		cp.add(doc, BorderLayout.CENTER) ;
		cp.add(hpane, BorderLayout.SOUTH) ;
		
		addBlindings() ;
		
	}

	/**
	* methods to gets messages.
	* ����ChatWindowManager����෢����Ϣ��
	*/
	public void sendTextMessage(TextMessage tm){
		textMessageProcess(tm) ;
		return ;
	}
	
	public void sendStateChangedMessage(StateChangedMessage scm){
		refreshIcon() ;
	}
	
}
