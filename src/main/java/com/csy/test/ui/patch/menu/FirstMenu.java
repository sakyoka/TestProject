package com.csy.test.ui.patch.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * 
 * 描述：一级菜单
 * @author csy
 * @date 2020年9月27日 下午3:47:10
 */
public class FirstMenu {
	
	public JMenuBar initJMenuBar(JFrame parentJFrame){
		JMenuBar jMenuBar = new JMenuBar();
		JMenu deployMenu = new JMenu();
		deployMenu.setText("功能菜单");
		
		JMenuItem deployMenuItem = new JMenuItem("配置信息");
		deployMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DeployMenu().initDeployMenu(parentJFrame);
			}
		});
		
		JMenuItem aboutMenuItem = new JMenuItem("关于");
		aboutMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutMenu().initAboutMenu(parentJFrame);
			}
		});
		
		deployMenu.add(deployMenuItem);
		deployMenu.add(aboutMenuItem);
		jMenuBar.add(deployMenu);
		return jMenuBar;
	}
}
