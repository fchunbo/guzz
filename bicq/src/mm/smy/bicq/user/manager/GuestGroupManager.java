package mm.smy.bicq.user.manager ;

import java.awt.* ;
import java.awt.event.* ;

import javax.swing.* ;

import java.util.Vector ;
import java.util.Hashtable ;
import java.util.Enumeration ;

import mm.smy.bicq.user.GuestGroup ;
import mm.smy.bicq.user.Guest ;

import mm.smy.util.* ;

/**
* ʵ�ֶ�GuestGroup�Ĺ���.
* 
* @author XF
* @author e-mail:myreligion@163.com
* @date 2003-11-29
* @copyright Copyright 2003 XF All Rights Reserved
*/

public class GuestGroupManager extends JFrame implements ActionListener,MouseListener{
	
	private JList list = null ;
	private JScrollPane pane = null ;
	
	private JButton select = new JButton("ѡ��") ;
	private JButton add    = new JButton("�������") ;
	private JButton delete = new JButton("ɾ��") ;
	
	
	private	Vector data = new Vector() ;	
	
	private Hashtable guestgroups = null ;
	
	private boolean isSelected = false ;  //��Ҫ����һ���û���ʱ��������־�û��Ƿ�ѡ�����û���
	private GuestGroup gg_return = null ; //��Ҫ����һ���û���ʱ������������ʾѡ����û��顣
	
	private GuestGroupManager outer = this ;

	
	public static void main(String[] args){
		GuestGroup gg = new GuestGroup("�ҵĺ���") ;
		gg.setIsSystemic(true) ;
		GuestGroup gg2 = new GuestGroup("gg2") ;
		GuestGroup gg3 = new GuestGroup("gg3") ;
		Hashtable h = new Hashtable() ;
		h.put(gg.getGroupname(), gg) ;
		h.put(gg2.getGroupname(), gg2) ;
		h.put(gg3.getGroupname(), gg3) ;
		
		GuestGroupManager t = new GuestGroupManager(h) ;
		t.show() ;
		System.out.println("chose:" + t.getChoseGuestGroup().getGroupname()) ;
		System.out.println("h:     " + h) ;
	}


	public GuestGroupManager(Hashtable m_guestgroups){
		this.guestgroups = m_guestgroups ;
		
		this.setSize(300,400) ;
		this.setLocation(200,200) ;
		this.setTitle("�û������") ;
		
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					gg_return = null ;	
					isSelected = true ;
					outer.dispose() ;
				}
			}
		) ;
		
		init() ;
	}
	
	private void init(){
		
		Enumeration e = guestgroups.keys() ;
		while(e.hasMoreElements()){
			data.add(e.nextElement()) ;
		}
		
		//Vector
		list = new JList(data) ;
		pane = new JScrollPane() ;
		pane.getViewport().setView(list) ;
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION) ;
		list.addMouseListener(this) ;
		
		select.setActionCommand("select") ;
		select.addActionListener(this) ;
		
		add.setActionCommand("add") ;
		add.addActionListener(this) ;
		
		delete.setActionCommand("delete") ;
		delete.addActionListener(this) ;
		
		Container cp = this.getContentPane() ;
		cp.add(pane,BorderLayout.CENTER) ;
		
		JPanel buttons = new JPanel() ;
		buttons.add(select) ;
		buttons.add(add) ;
		buttons.add(delete) ;
		cp.add(buttons, BorderLayout.SOUTH) ;
			
	}
	
	/**
	* �÷���Ϊ��ֹ������ֱ���û�������ѡ����ǹر��˴���Ϊֹ��
	* ����û��ر��˴��ڣ����᷵�� "�ҵĺ���" �顣
	* �û������ڵ�����������Ժ��޸���guestgroups����Ϣ��
	* ���Ǹ���û���ṩˢ��mf�ķ����������ڵ��ø÷�����Ӧ��ˢ��mf��
	*/
	public synchronized GuestGroup getChoseGuestGroup(){
		
		while(!isSelected){		
					try{
						wait(50) ;	
					//	System.out.println("ggm waiting......") ;
					}catch(Exception error){
						System.out.println("error:" + error.getMessage() ) ;
					}
			
		}

		//����û���ѡ��Ļ������Ƿ��� �ҵĺ��� 
		if(gg_return == null){
			return (GuestGroup) guestgroups.get("�ҵĺ���") ;
		}
		
		
			
		return gg_return ;
		/*
		this.dispose() ;
		return (GuestGroup) guestgroups.get("�ҵĺ���") ;
		*/
	}
	
	public void mouseClicked(MouseEvent e){
		if(e.getClickCount() >= 2){
			gg_return = (GuestGroup) guestgroups.get(list.getSelectedValue()) ;
			isSelected = true ;
			this.dispose() ;
			return ;
		}
	}
	
	public void mouseReleased(MouseEvent e){
		
	}
	
	public void mousePressed(MouseEvent e){
		
	}
	
	public void mouseEntered(MouseEvent e){
		
	}
	
	public void mouseExited(MouseEvent e){
		
	}
	
	
	public void actionPerformed(ActionEvent e){
		
		String command = e.getActionCommand() ;
		if(command.equals("select")){
			if(list.isSelectionEmpty()) return ;
			
			gg_return = (GuestGroup) guestgroups.get(list.getSelectedValue()) ;
			isSelected = true ;
			this.dispose() ;
			return ;
		}else if(command.equals("add")){
			String newname = JOptionPane.showInputDialog(this,"��������������", "��������", JOptionPane.PLAIN_MESSAGE) ;
			if(newname == null) return ;
			newname = newname.trim() ;
			if(newname.length() == 0 ) return ;
			
			if(!guestgroups.containsKey(newname)){
				GuestGroup gg = new GuestGroup(newname) ;
				gg.setIsSystemic(false) ;
				guestgroups.put(newname, gg) ;
				data.add(newname) ;	
			}else{
				return ;
			}			
			
		}else if(command.equals("delete")){
			if(list.isSelectionEmpty()) return ;
			
			Object name = list.getSelectedValue() ;
			GuestGroup gg2 = (GuestGroup) guestgroups.get(name) ;
			if(gg2 == null) return ;
			
			//ϵͳ�飬����ɾ����
			if(gg2.isSystemic()){
				JOptionPane.showMessageDialog(this, gg2.getGroupname() + " ��ϵͳ�飬����ɾ��", "ɾ�������", JOptionPane.ERROR_MESSAGE) ;
				return ;
			}
			//��ɾ��֮ǰ���ǰ� �����е��û��Ƶ� "�ҵĺ���" �С�
			Vector friends = gg2.getAllGuests() ;
			if(friends != null){
				Enumeration fe = friends.elements() ;
				GuestGroup myfriend = (GuestGroup) guestgroups.get("�ҵĺ���") ;
				
				while(fe.hasMoreElements()){
					((Guest) fe.nextElement()).joinGuestGroup(myfriend) ;	
				}
			}
			
			guestgroups.remove(name) ;
			data.remove(name) ;
			//System.out.println("guestgroups:" + guestgroups) ;
		}
		
		list.setListData(data) ;
		list.repaint() ;
		pane.repaint() ;
		pane.invalidate() ;
	}
	
	
}