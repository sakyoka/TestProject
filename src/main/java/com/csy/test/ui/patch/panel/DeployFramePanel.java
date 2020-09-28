package com.csy.test.ui.patch.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.csy.test.ui.patch.bean.DeployBean;
import com.csy.test.ui.patch.cache.DeloyParamCache;
import com.csy.test.ui.patch.common.base.InitInterface;

/**
 * 
 * 描述：配置界面
 * @author csy
 * @date 2020年9月28日 下午12:48:32
 */
public class DeployFramePanel implements InitInterface{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Component init(Window window) {
		
		JPanel panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		panel.setLayout(gridBagLayout);

		DeployBean cacheDeployBean = DeployBean.readCache(true);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weighty = 0.2;
		constraints.weightx = 0.3;
		
		constraints.gridwidth = 1;
		constraints.insets = new Insets(15 , 5 , 0 , 0);
		JLabel label1 = new JLabel("数据源:");
		label1.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField jTextField1 = new JTextField();
		jTextField1.setText(cacheDeployBean.getCachePathPrefix());
		jTextField1.setPreferredSize(new Dimension (300 , 30));
		gridBagLayout.setConstraints(label1, constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(jTextField1, constraints);
		panel.add(label1);
		panel.add(jTextField1);
		constraints.gridwidth = 1;
		constraints.insets = new Insets(15 , 5 , 0 , 0);
		
		JLabel label2 = new JLabel("编译空间:");
		label2.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField jTextField2 = new JTextField();
		jTextField2.setText(cacheDeployBean.getCompilePathPrefix());
		jTextField2.setPreferredSize(new Dimension (300 , 30));
		gridBagLayout.setConstraints(label2, constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(jTextField2, constraints);
		panel.add(label2);
		panel.add(jTextField2);
		constraints.gridwidth = 1;
		constraints.insets = new Insets(15 , 5 , 0 , 0);
		
		JLabel label3 = new JLabel("缓存空间:");
		label3.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField jTextField3 = new JTextField();
		jTextField3.setText(cacheDeployBean.getCachePathPrefix());
		jTextField3.setPreferredSize(new Dimension (300 , 30));
		gridBagLayout.setConstraints(label3, constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(jTextField3, constraints);
		panel.add(label3);
		panel.add(jTextField3);
		constraints.gridwidth = 3;
		constraints.insets = new Insets(15 , 5 , 0 , 0);
		
		JLabel label4 = new JLabel("补丁格式:");
		label2.setHorizontalAlignment(SwingConstants.RIGHT);
		JRadioButton patchStyleBox1 = new JRadioButton("列表");
		JRadioButton patchStyleBox2 = new JRadioButton("树状");
		if(cacheDeployBean.getPatchStyle() == 1) {
			patchStyleBox1.setSelected(true) ;
		}else {
			patchStyleBox2.setSelected(true);
		}
        ButtonGroup patchGroups = new ButtonGroup();
        patchGroups.add(patchStyleBox1);
        patchGroups.add(patchStyleBox2);

		gridBagLayout.setConstraints(label4, constraints);
		gridBagLayout.setConstraints(patchStyleBox1, constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(patchStyleBox2, constraints);
		panel.add(label4);
		panel.add(patchStyleBox1);
		panel.add(patchStyleBox2);

		constraints.gridwidth = 2;
		constraints.insets = new Insets(80 , 5 , 0 , 0);
		
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		JButton saveButton = new JButton("保存配置");
		gridBagLayout.setConstraints(saveButton, constraints);
		panel.add(saveButton);
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				boolean isSuccess = true;
				String sourcePathPrefix = jTextField1.getText();
				String compilePathPrefix = jTextField2.getText();
				String cachePathPrefix = jTextField3.getText();
				int selected = patchStyleBox1.isSelected() ? 1 : 2;
				
				try {
					DeloyParamCache.storage(JSON.toJSONString(DeployBean.getBuilder()
							.sourcePathPrefix(sourcePathPrefix)
							.compilePathPrefix(compilePathPrefix)
							.patchStyle(selected)
							.cachePathPrefix(cachePathPrefix)));
				} catch (Exception e2) {
					// TODO: handle exception
					isSuccess = false;
					logger.error("保存配置失败" , e2);
				}
				
				if (isSuccess){
					JOptionPane.showConfirmDialog(window ,"保存成功!" , "信息提示" , JOptionPane.PLAIN_MESSAGE);
					//window.dispose();
				}else{
					JOptionPane.showConfirmDialog(window ,"保存失败!" , "错误提示" , 0);
				}
			}
		});
		
		return panel;
	}

}
