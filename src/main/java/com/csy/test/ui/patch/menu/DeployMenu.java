package com.csy.test.ui.patch.menu;

import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.csy.test.ui.patch.panel.DeployFramePanel;

public class DeployMenu {
	
	public void initDeployMenu(JFrame parentJFrame){
		JDialog deployJframe = new JDialog();
		deployJframe.setSize(600, 400);
		deployJframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		deployJframe.setLocationRelativeTo(parentJFrame);
		deployJframe.setResizable(false);
		deployJframe.setLayout(new FlowLayout());
		deployJframe.add(new DeployFramePanel().init(deployJframe));
		deployJframe.setVisible(true);
	}
}
