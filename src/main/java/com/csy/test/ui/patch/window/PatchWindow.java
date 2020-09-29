package com.csy.test.ui.patch.window;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.csy.test.ui.patch.button.MainFrameGenerateButton;
import com.csy.test.ui.patch.cache.ButtonParamCache;
import com.csy.test.ui.patch.common.base.AbstractWindowCloseListener;
import com.csy.test.ui.patch.menu.FirstMenu;
import com.csy.test.ui.patch.panel.MainFrameLogPanel;
import com.csy.test.ui.patch.panel.MainFramePanel;

/**
 * 
 * 描述：界面生成
 * @author csy
 * @date 2020年9月29日 下午5:34:07
 */
public class PatchWindow {
	
	public static void main(String[] args) {
		
		JFrame jFrame  = new JFrame();
		jFrame.setSize(700, 500);
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
		
		mainFrameListener(jFrame);
		
		jFrame.setVisible(true);
	}
	
	
	/**
	 * 描述：主界面监听
	 * @author csy 
	 * @date 2020年9月29日 下午5:33:04
	 * @param jFrame
	 */
	private static void mainFrameListener(JFrame jFrame){
		jFrame.addWindowListener(new AbstractWindowCloseListener() {
			
			@Override
			public void onWindowColsed(WindowEvent e) {
				ButtonParamCache.removeAll();
			}
		});
	}
}
