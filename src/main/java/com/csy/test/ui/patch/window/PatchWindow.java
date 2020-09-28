package com.csy.test.ui.patch.window;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.csy.test.ui.patch.button.MainFrameGenerateButton;
import com.csy.test.ui.patch.menu.FirstMenu;
import com.csy.test.ui.patch.panel.MainFrameLogPanel;
import com.csy.test.ui.patch.panel.MainFramePanel;

public class PatchWindow {
	
	public static void main(String[] args) {
		
		JFrame jFrame  = new JFrame();
		jFrame.setSize(800, 500);
		jFrame.setLocationRelativeTo(null);
		jFrame.setTitle("补丁生成窗口");
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jFrame.setLayout(new BorderLayout());
		
		//panel
		jFrame.add(new MainFramePanel().init(jFrame), BorderLayout.NORTH);
		
		//panel log
		jFrame.add(new MainFrameLogPanel().init(jFrame), BorderLayout.CENTER);
		
		//按钮
		jFrame.add(new MainFrameGenerateButton().init(jFrame), BorderLayout.SOUTH);
		
		//菜单
		jFrame.setJMenuBar(new FirstMenu().initJMenuBar(jFrame));
		
		jFrame.setVisible(true);
	}
}
