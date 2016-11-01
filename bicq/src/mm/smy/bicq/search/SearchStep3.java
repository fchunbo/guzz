package mm.smy.bicq.search ;

import java.util.* ;
import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.table.* ;

import mm.smy.bicq.* ;
import mm.smy.bicq.message.* ;
import mm.smy.bicq.user.* ;

import mm.smy.bicq.user.manager.GuestGroupManager ;


/**
* ������ϣ��ɹ���ɡ���ʾ�����Ľ����
* ss2ȡ������һ����ת����������ѡ��
* ��һ������Ӻ���[�������ɸ���ͳһ�����Լ���sgm�ĸ�������󴫸�sgm�ɹ���Ӻ��ѵĶ���
* �ò��ָ���������֤��Ϣ�ķ��ͣ����Ǿ���Ļ�����Ϣ������sgm��ɡ�
* Ϊ�˼���sgm�ĸ��������ཫӵ��MainManager�����á�����ֱ�ӷ�����Ϣ�����ǲ����������Ϣ������ͳһ������Ϣ��
* ����ӵ��������һ��������ã�����sgm��"��һ��"���д���"���"���ǹر�һ�У�����searchguest��"��һ��"�ɸ����Լ�����
* ���ǵ������Ƿ�������
*
* �����Ŷӵ� 7 
*/

public class SearchStep3 extends JFrame implements ActionListener{
	private MainManager m = null ;
	private SearchGuestManager sgm = null ;
	private SearchGuestResultMessage sgrm = null ;
	
	private DealAdd da = null ;
	
	Vector tempusers = null ;
	/********************************************************/
	public SearchStep3(SearchGuestResultMessage m_sgrm){
		sgrm = m_sgrm ;
		tempusers = sgrm.getTempUsers() ;
		init() ;
	}
	public static void main(String[] args){
		/*SearchGuestResultMessage sgrm = new SearchGuestResultMessage() ;
		for(int i = 0 ; i < 10 ; i++ ){
			TempUser tu = new TempUser() ;
			tu.setNickname("nickname" + i) ;
			tu.setNumber(1000 + i*10) ;
			tu.setFrom("from" + i) ;
			tu.setState(User.OFFLINE) ;
			tu.setAuth(Host.MY_PERMIT) ;
			sgrm.addTempUser(tu) ;	
		}
		
		MainManager m = new MainManager() ;
		SearchGuestManager sgm = new SearchGuestManager(m) ;
		SearchStep3 ss3 = new SearchStep3(m,sgm,sgrm) ;	
		ss3.show() ;
		System.out.println("finished.") ;
		*/
		System.out.println("ggm debug starts:") ;

		GuestGroupManager ggm = new GuestGroupManager(new java.util.Hashtable()) ;
		ggm.show() ;
		System.out.println(ggm.getChoseGuestGroup()) ;
	}
	//*******************************************************
	
	public SearchStep3(){
		tempusers = sgrm.getTempUsers() ;
		init() ;
	}
	public SearchStep3(MainManager m_mm,SearchGuestManager m_sgm, SearchGuestResultMessage m_sgrm){ //�ø�����tempuser������ ��ʾ�յ����û���
		m = m_mm ;
		sgm = m_sgm ;
		sgrm = m_sgrm ; 
		tempusers = sgrm.getTempUsers() ;
		System.out.println("TempUsers.size():" + tempusers.size()) ;
		init()  ;
	}

	private JLabel explain = new JLabel("���ҽ��") ;
	private JTable result = null ;
	private JScrollPane scroll_result = null ;
	
	private JButton detail = new JButton("����") ; //��ʾ���ѵ���ϸ����
	private JButton uppage = new JButton("��һҳ") ;
	private JButton downpage = new JButton("��һҳ") ;
	
	private JButton pre = new JButton("��һ��")  ;
	private JButton next = new JButton("��һ��") ; 
	private JButton finish = new JButton("���") ;
	
	private void init(){
		this.setSize(400,400) ;
		this.setTitle("�����������") ;
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					if(da != null){
						da.dispose() ;	
					}
					sgm.report(7,SearchGuestManager.STEP_CLOSE) ;
				}
			}
		) ;
		
		//init JTable result...
		System.out.println("********************************************* 11") ;
		Enumeration e = tempusers.elements() ;
		Vector rows = new Vector(11) ;
		Vector head = new Vector(4) ;
		head.add("��") ;
		head.add("�ǳ�") ;
		head.add("�Ա�") ;
		//row.add("����") ;
		head.add("����") ;
		//rows.add(row) ;
		TempUser tu = null ;
		Vector row = null ;
		while(e.hasMoreElements()){
			tu = (TempUser) e.nextElement() ;
			row = new Vector(4) ;
			row.add(new Integer(tu.getNumber())) ;
			row.add(tu.getNickname()) ;
			row.add(new Integer(tu.getGender())) ;
			row.add(tu.getFrom()) ;
			rows.add(row) ;
		}
		///////////////////////////////////////////////////////////// init components
		System.out.println("********************************************* 12") ;
		result = new JTable(rows,head)  ;
		result.setSelectionMode(ListSelectionModel.SINGLE_SELECTION) ;
		//result.setCellSelectionEnabled(false) ;
		result.setDragEnabled(false) ;
		
		//result.setPreferredScrollableViewportSize(new Dimension(300,300)) ;
		scroll_result = new JScrollPane(result) ;
		
		Panel labels = new Panel() ;
		labels.add(explain) ;		
		
		Panel centerbuttons = new Panel() ;
		detail.setActionCommand("detail") ;
		detail.addActionListener(this) ;
		uppage.setActionCommand("uppage") ;
		uppage.addActionListener(this) ;
		downpage.setActionCommand("downpage") ;
		downpage.addActionListener(this) ;
		centerbuttons.add(detail) ;
		centerbuttons.add(uppage) ;
		centerbuttons.add(downpage) ;
		System.out.println("********************************************* 13") ;
		Panel center = new Panel(new BorderLayout()) ;
		center.add(scroll_result,BorderLayout.CENTER) ;
		center.add(centerbuttons,BorderLayout.SOUTH) ;
		
		Panel buttons = new Panel() ;
		buttons.add(pre) ;
		buttons.add(next) ;
		buttons.add(finish) ;
		
		Container cp = this.getContentPane() ;
		cp.setLayout(new BorderLayout()) ;
		cp.add(labels,BorderLayout.NORTH) ;
		cp.add(center,BorderLayout.CENTER) ;
		cp.add(buttons,BorderLayout.SOUTH) ;
		
		pre.setActionCommand("pre") ;
		next.setActionCommand("next") ;
		finish.setActionCommand("finish") ;
		System.out.println("********************************************* 14") ;
		pre.addActionListener(this) ;
		next.addActionListener(this) ;
		finish.addActionListener(this) ;
		
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equalsIgnoreCase("pre")){
			if(da != null){
				da.dispose() ;	
			}
			sgm.report(7,SearchGuestManager.STEP_PREVIOUS) ;
		}else if(e.getActionCommand().equalsIgnoreCase("finish")){
			if(da != null){
				da.dispose() ;	
			}
			sgm.report(7,SearchGuestManager.STEP_FINISH) ;
		}
		
		//����������ɸ����Լ������
		if(e.getActionCommand().equalsIgnoreCase("uppage")){
			//��һҳ	
			
		}else if(e.getActionCommand().equalsIgnoreCase("downpage")){
			//��һҳ
		}
		
		////////////////////////////////////////////////////////////////////
		//��ȡѡ���û�
		int chooseline = result.getSelectedRow() ;
		TempUser chooseuser = null ;
			
		System.out.println("chooseline:" + chooseline) ;
		if(chooseline < 0){ //select nothing at all
			explain.setText("��ѡ����һ�����ѣ�") ;
			return ;
		}else{
			Enumeration en = tempusers.elements() ;
			while(en.hasMoreElements()){
				TempUser tu = (TempUser) en.nextElement() ;
				System.out.println("choose number is: " + result.getValueAt(chooseline,0) ) ;
				
				if(tu.getNumber() == ((Integer) result.getValueAt(chooseline,0)).intValue()){
					chooseuser = tu ;
				}	
			}
		}
		
		if(chooseuser == null) return ;
		
		Guest guest = new Guest() ;
		guest.setNumber(chooseuser.getNumber()) ;
		guest.setNickname(chooseuser.getNickname()) ;
		guest.setIP(chooseuser.getIP()) ;
		guest.setPort(chooseuser.getPort()) ;
		guest.setProvince(chooseuser.getFrom()) ;
		guest.setGender(chooseuser.getGender()) ;
		guest.setPortrait(chooseuser.getPortrait()) ;
		guest.setState(chooseuser.getState()) ;
		
		if(e.getActionCommand().equalsIgnoreCase("next")){
			if(da == null){
				da = new DealAdd(m,this,sgm) ;
			}
			da.setTo(guest) ;
			da.setCurrent(chooseuser.getAuth()) ;
			da.show() ;
		}else if(e.getActionCommand().equalsIgnoreCase("detail")){
			//��ʾָ���û�����ϸ���ϡ�
			GuestInforWindow window_detail = new GuestInforWindow(guest, m) ;
			window_detail.show() ;
			return ;
		}
		
	}

}

class DealAdd extends JFrame implements ActionListener{
	private MainManager m = null ;
	private SearchStep3 ss3 = null ;
	private SearchGuestManager sgm = null ;
	private int current = Integer.MIN_VALUE ;
	private Guest toadd = null ;
		
	private JButton pre = new JButton("��һ��") ;
	private JButton next = new JButton("��һ��") ;
	private JButton finish = new JButton("���") ;
	
	private Label explain = new Label() ;
	private TextArea leaveword = new TextArea(10,30) ;
	private JScrollPane scroll_leaveword = new JScrollPane(leaveword) ;
	
	public DealAdd(MainManager m_mm, SearchStep3 m_ss3, SearchGuestManager m_sgm ){
		m = m_mm ;
		ss3 = m_ss3 ;
		ss3.hide() ;
		sgm = m_sgm ;
		init() ;
	}
	public void setCurrent(int m_current){
		current = m_current ;
		paintWindow() ;	
	}
	public void setTo(Guest u){
		toadd = u ;
	}
	
	private void init(){
		this.setSize(400,400) ;
		this.setTitle("��Ӻ���") ;
		
		top.add(explain) ;
		
		pre.setActionCommand("pre") ;
		pre.addActionListener(this) ;
		next.setActionCommand("next") ;
		next.addActionListener(this) ;
		finish.setActionCommand("finish") ;
		finish.addActionListener(this) ;
		
		buttom.add(pre) ;
		buttom.add(next) ;
		buttom.add(finish) ;
	}
	
	private Panel top = new Panel() ;
	private Panel center = new Panel() ;
	private Panel buttom = new Panel() ;
	
	
	private void paintWindow(){
		
		System.out.println("current:" + current) ;
		System.out.println("toadd:" + toadd) ;
		
		center.removeAll() ;		
		
		Container cp = this.getContentPane() ;
		cp.removeAll() ;
		//////////////////////////////////////////////////////////////////////////////////////////////
		PermitMessage pm = new PermitMessage() ;
		if(current == Host.ALLOW_ANYONE){
			
				//������Ϣ����������������ӳɹ���
				ICMPMessage addsuccess = new ICMPMessage() ;
				addsuccess.setMinType(ICMPMessage.ADD_FRIEND) ;
				addsuccess.setContent(m.getHost().getNumber() + ":" + toadd.getNumber() ) ;
				m.sendOutMessage(addsuccess) ;
			
			/* �ò����ɷ��������͡�
				pm.setMintype(PermitMessage.PERMIT_SEND) ;
				pm.setFrom(m.getHost()) ;
				pm.setTo(toadd) ;
				m.sendOutMessage(pm) ;
			*/
			
			//the followwing 3 lines will cause deadlock
			//
			//System.out.println("gg:" + gg.getGroupname() ) ;
			//m.addGuest(toadd,gg) ;
			
			/*����Ĵ�����Ϊ�����û��ܹ�ѡ�����Ǹ��飬�������������⡣
				GuestGroupManager ggm = new GuestGroupManager(m.getGuestGroups()) ;
				ggm.show() ;
				
				
				GuestGroup gg = ggm.getChoseGuestGroup() ;
			*/
				GuestGroup gg = m.getGuestGroup("�ҵĺ���") ;
				m.addGuest(toadd, gg) ;
				
				explain.setText("��ӳɹ�") ;
				m.getMainFrame().fix() ;
				
		}else if(current == Host.NO_DISTURB){
			pm = null ;
			explain.setText("��Ҫ��ӵĺ��Ѿܾ��κ��˰���/����Ϊ���ѣ����Ժ����ԡ�") ;			
		}else if(current == Host.MY_PERMIT){
			explain.setText("�Է�Ҫ�������֤��") ;	
			center.add(scroll_leaveword) ;
		}else{
			explain.setText("�޷���ӣ��Է���ʾ����ȷ��������ϵͳ���ݿ�����뱨�������������ǣ�лл��") ;	
		}
		
		cp.setLayout(new BorderLayout()) ;
		cp.add(top,BorderLayout.NORTH) ;
		cp.add(center,BorderLayout.CENTER) ;
		cp.add(buttom,BorderLayout.SOUTH) ;
		
		cp.invalidate() ;
	}
	

	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equalsIgnoreCase("pre")){
			this.hide() ;	
			ss3.show() ;
		}else if(e.getActionCommand().equalsIgnoreCase("finish")){
			//////////////////////////////////////////////////????????????????????????????????????care!!!!!!!!!!!!!!!!!
			sgm.report(7,SearchGuestManager.STEP_FINISH) ;
			this.dispose() ;
		}else if(e.getActionCommand().equalsIgnoreCase("next")){
			if(current == Host.MY_PERMIT){ //���������֤
				PermitMessage pm = new PermitMessage() ;
				pm.setMintype(PermitMessage.PERMIT_REQUEST) ;
				pm.setFrom(m.getHost()) ;
				pm.setTo(toadd) ;
				pm.setContent(leaveword.getText()) ;
				m.sendOutMessage(pm) ;
				
				//repaint compoments
				center.removeAll() ;
				explain.setText("�����Ѿ����ͣ���ȴ���Ӧ����") ;
				
				this.getContentPane().invalidate() ;
			}
			else{
				this.hide() ;	
				ss3.show() ;
			}
		}
	}
	
}