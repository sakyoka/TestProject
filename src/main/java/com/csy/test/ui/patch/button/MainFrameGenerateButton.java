package com.csy.test.ui.patch.button;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.csy.test.ui.patch.common.base.InitInterface;

public class MainFrameGenerateButton implements InitInterface{

	@Override
	public Component init(Window window) {
		JButton createButton = new JButton("Click it generate");
		createButton.setPreferredSize(new Dimension(800, 80));
		createButton.setFont(new Font(null, 17, 30));
		createButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		return createButton;
	}
	
}
