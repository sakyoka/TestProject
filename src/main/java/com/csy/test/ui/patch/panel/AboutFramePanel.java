package com.csy.test.ui.patch.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.csy.test.ui.patch.common.base.InitInterface;

/**
 * 
 * 描述：about panel
 * @author csy
 * @date 2020年9月29日 上午11:31:59
 */
public class AboutFramePanel implements InitInterface{

	@Override
	public Component init(Window window) {
		JPanel panel = new JPanel();
		JLabel jLabel = new JLabel();
		jLabel.setText("swing测试及简单封装对PatchUtils可视化配置");
		jLabel.setBackground(Color.white);
		jLabel.setFont(new Font(null, 17, 20));
		panel.add(jLabel);
		return panel;
	}
}
