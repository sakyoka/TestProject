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
 * 描述：参数配置
 * @author csy
 * @date 2020年9月28日 下午1:10:25
 */
public class MainFramePanel implements InitInterface{

	@Override
	public Component init(Window window) {
		JPanel deployPanel = new JPanel();
		deployPanel.setPreferredSize(new Dimension(800, 100));
		deployPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		return deployPanel;
	}

}
