package mm.smy.bicq ;

/**
* ר����Windowsϵͳ����windows������������ʾһ��Сͼ��
* 
* 
* @author XF
* @date 2004-4-10
* 
* 
*/

import com.jeans.trayicon.*;
import java.io.* ;
import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;

public class WindowTray{
	
	private MainManager m  ;
	private WindowsTrayIcon tray ;
	
	public WindowTray(MainManager m){
		this.m = m ;
		
		// Set callback method to send windows messages through Tray Icon library (see WindowsMessageCallback)
		WindowsTrayIcon.setWindowsMessageCallback(new WindowsMessageCallback());		
		
		init() ;
	}
	
	public void init(){
		Image icon = null ;
		try{
			initTray() ;			
			icon = FaceManager.loadBitmap("symbol.bmp") ;		
							
			tray = new WindowsTrayIcon(icon, 32, 32) ;
			
		}catch(Exception e){
			System.out.println("loading symbol.bmp failed.") ;
			e.printStackTrace() ;
		}
		
		tray.addMouseListener(new DoubleMouseListener()) ;
		
		tray.setPopup(makePopup()) ;
		
		tray.setVisible(true) ;	
		WindowsTrayIcon.keepAlive();
		return ;
	}
	
	public TrayIconPopup makePopup() {
		
		// Make new popup menu
		TrayIconPopup popup = new TrayIconPopup();
		// Add about, configure  & exit item
		TrayIconPopupSimpleItem item = new TrayIconPopupSimpleItem("�����趨");
		item.addActionListener(new HostListener());
		popup.addMenuItem(item);
		// Add configure item
		item = new TrayIconPopupSimpleItem("��ʾ������");
		item.addActionListener(new ShowListener());
		popup.addMenuItem(item);
		
		item = new TrayIconPopupSimpleItem("����������");
		item.addActionListener(new SystemSettingListener());
		popup.addMenuItem(item);
		
		item = new TrayIconPopupSimpleItem("�ĵ���Դ��");
		item.addActionListener(new SourceListener());
		popup.addMenuItem(item);
		
		//add about
		item = new TrayIconPopupSimpleItem("����BICQ") ;
		item.addActionListener(new AboutListener()) ;
		popup.addMenuItem(item) ;
		
		// Add a separator
		TrayIconPopupSeparator sep = new TrayIconPopupSeparator();
		popup.addMenuItem(sep);		
		
		// Add exit item
		item = new TrayIconPopupSimpleItem("&�˳�");
		item.addActionListener(new ExitListener());
		popup.addMenuItem(item);
		return popup;
	}
		
	private void initTray() throws Exception{
	//	try {							
			String appName = "bicq";
			// First check if there's another instance of our app running by sending a windows message
			// to a hidden icon window "TestTray" - each Tray Icon app has a hidden window that receives
			// the mouse/menu messages for it's Tray Icons
			long result = WindowsTrayIcon.sendWindowsMessage(appName, 1234);
			if (result != -1) {
				// If window exists, there's already an instance of our app running
				// Print message and exit (other app will restore its window when receiving 
				// our message - see WindowsMessageCallback
				System.out.println("Already running other instance of "+appName+" (returns: "+result+")");
				return;
			}
			// Init the Tray Icon library given the name for the hidden window
			WindowsTrayIcon.initTrayIcon(appName);
			// Show our main window
		//	TrayDialogDemo demo = new TrayDialogDemo();			
//		} catch (TrayIconException e) {
//			System.out.println("Error: "+e.getMessage());
//		} catch (InterruptedException e) {
//		}
		
	}
	
	public class HostListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			m.getUserManager().showUserInfor(m.getHost()) ;
		}		
	}
	
	public class ShowListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			m.getMainFrame().show() ;
		}
	}
	
	public class SystemSettingListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			BICQ.exec("notepad " + System.getProperty("user.dir") + "/server.ini") ;
		}
	}
	
	public class SourceListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			BICQ.openURL("http://nic.biti.edu.cn/vbb/showthread.php?s=&threadid=123565") ;
		}
	}
	
	public class ExitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			m.getMainFrame().close() ;
		}
	}
	
	public class AboutListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			AboutFrame frame = new AboutFrame() ;
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocation(300,300) ;
			frame.show() ;
		}
	}
	
	// Callback listener handles incomming windows messages. In this demo, a windows message is send when the
	// user tries to start a second instance of the demo app. You can try this one by opening two MS-DOS prompts
	// and say in each one "java demo.TestTrayIcon"
	// MS-DOS 1:
	// 	C:\TrayIcon>java demo.TestTrayIcon
	//	...
	//	Other instance started (parameter: 1234)
	//
	// MS-DOS 2:
	// 	C:\TrayIcon>java demo.TestTrayIcon
	// 	Already running other instance of TestTray (returns: 4321)	
	public class WindowsMessageCallback implements TrayIconCallback {
		
		public int callback(int param) {
			// Param contains the integer value send with sendWindowsMessage(appName, param)
			System.out.println("Other instance started (parameter: "+param+")");
			// Return integer value to other process
			return 4321;
		}
		
	}
	
	public static void main(String[] args){
		WindowTray tray = new WindowTray(null) ;
		
	//	Frame frame = new Frame() ;
	//	frame.show() ;
	
	//	WindowTray.AboutFrame f = new WindowTray.AboutFrame() ;
	//	f.show() ;
		
	}
	
	public class DoubleMouseListener extends MouseAdapter{
		
		public void mousePressed(MouseEvent evt) {
			if ((evt.getModifiers() & MouseEvent.BUTTON1_MASK) != 0 && evt.getClickCount() == 2) {
				System.out.println("[Tray icon double clicked].");
				
				m.getMainFrame().show() ;
			}
		}
	}
	
	public void close(){
		WindowsTrayIcon.cleanUp();
	}
	
	public static class AboutFrame extends JFrame{
		public static final int WIDTH=400;
		public static final int HEIGHT=400;

		public AboutFrame(){
			initWindow() ;
		}
	
		private JFrame outer = this ;
	
		private void initWindow(){
			setTitle("����BICQ");
			setSize(WIDTH,HEIGHT);
			
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;
		
			Container contentPane=getContentPane();
   			JButton checkButton1=new JButton("ȷ��");
        
       		checkButton1.addActionListener(
        		new ActionListener(){
        			public void actionPerformed(ActionEvent e){
        				outer.dispose() ;
        			}
        		}
        	) ;

			String copyright = "<html><head></head><body><pre>" + 
    		"	��������Java��д������Դ����\n" +
			"	�����Ҫ������ʲô�������������ϵ��\n" +
    		"	��Ϊ�������ޣ����кܶ�Ĵ�����Ҫ����\n" +
			"	���ص�ַ��ftp://211.68.35.240//Upload/=[temp]=/\n" +
			"</pre><hr><pre>" +
    		"	���ߣ�XF\n" + 
    		"	e-mail: xf@mail.biti.edu.cn" +
			"</pre><hr>" +
			"<center> Copyright 2003-2004 XF All Rights Reserved. <br>" + 
			"Some copyrights belong to their respected owners" +
			"</center></body></html>" ;
			
			JLabel right = new JLabel(copyright) ;
			//authorpanel.setBackground(Color.pink);
        	contentPane.add(right,BorderLayout.CENTER);
        	contentPane.add(checkButton1, BorderLayout.SOUTH) ;        
	}
		
	}
	
	
	
	
}
