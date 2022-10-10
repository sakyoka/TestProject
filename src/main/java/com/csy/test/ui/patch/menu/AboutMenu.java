package com.csy.test.ui.patch.menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import com.csy.test.ui.patch.panel.AboutFramePanel;

/**
 * 
 * 描述：关于
 * @author csy
 * @date 2020年9月29日 上午11:31:00
 */
public class AboutMenu {
	
	public void initAboutMenu(Window window){
		JDialog deployJframe = new JDialog();
		Dimension dimension = window.getSize();
		deployJframe.setSize(dimension.width - 200, dimension.height - 100);
		deployJframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		deployJframe.setLocationRelativeTo(window);
		deployJframe.setResizable(false);
		deployJframe.setLayout(new FlowLayout());
		deployJframe.add(new AboutFramePanel().init(deployJframe));
		deployJframe.setVisible(true);		
	}
}
