package com.csy.test.ui.patch.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.csy.test.ui.patch.cache.ButtonParamCache;
import com.csy.test.ui.patch.common.base.AbstractWindowCloseListener;
import com.csy.test.ui.patch.constants.ComponentNameConstant;
import com.csy.test.ui.patch.panel.DeployFramePanel;
import com.csy.test.ui.patch.panel.MainFramePanel;

/**
 * 
 * 描述：配置
 * @author csy
 * @date 2020年9月29日 上午11:31:15
 */
public class DeployMenu {
	
	public void initDeployMenu(JFrame parentJFrame){
		
		JDialog deployJframe = new JDialog();
		Dimension dimension = parentJFrame.getSize();
		deployJframe.setSize(dimension.width - 200, dimension.height - 100);
		deployJframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		deployJframe.setLocationRelativeTo(parentJFrame);
		deployJframe.setResizable(false);
		deployJframe.setLayout(new FlowLayout());
		deployJframe.add(new DeployFramePanel().init(deployJframe));
		deployJframe.setVisible(true);
		
		deployJframe.addWindowListener(new AbstractWindowCloseListener() {
			
			@Override
			public void onWindowColsed(WindowEvent e) {
				Component[] components = parentJFrame.getRootPane().getContentPane().getComponents();
				for (Component component:components){
					if (ComponentNameConstant.MAIN_FRAME_MESSAGE_LIST.equals(component.getName())){
						JPanel jPanel = (JPanel)component;
						jPanel.removeAll();//清除内容
						ButtonParamCache.removeAll();//清除所有button记录缓存
						MainFramePanel.autoCreateLabelComponent(jPanel);//重新生成
						jPanel.updateUI();//更新面板
					}
				}
			}
		});
	}
}
