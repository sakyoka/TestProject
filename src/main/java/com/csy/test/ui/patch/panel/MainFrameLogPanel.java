package com.csy.test.ui.patch.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.csy.test.ui.patch.common.base.InitInterface;

/**
 * 
 * 描述：日志显示panel
 * @author csy
 * @date 2020年9月28日 下午1:08:21
 */
public class MainFrameLogPanel implements InitInterface{

	@Override
	public Component init(Window window) {
		JPanel logPannel = new JPanel();
		logPannel.setPreferredSize(new Dimension(800, 200));
		logPannel.setBackground(Color.white);
		logPannel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		return logPannel;
	}

}
