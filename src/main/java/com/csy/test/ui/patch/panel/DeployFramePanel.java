package com.csy.test.ui.patch.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.ui.patch.bean.DeployBean;
import com.csy.test.ui.patch.cache.DeloyParamCache;
import com.csy.test.ui.patch.common.base.AbstractWindowCloseListener;
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

		List<JTextComponent> list = new ArrayList<JTextComponent>();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weighty = 0.2;
		constraints.weightx = 0.3;
		
		constraints.gridwidth = 3;
		constraints.insets = new Insets(15 , 5 , 0 , 0);
		JLabel label1 = new JLabel("数据源:");
		label1.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField jTextField1 = new JTextField();
		jTextField1.setName("sourcePathPrefix");
		list.add(jTextField1);
		jTextField1.setPreferredSize(new Dimension (300 , 30));
		gridBagLayout.setConstraints(label1, constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(jTextField1, constraints);
		panel.add(label1);
		panel.add(jTextField1);
		
		constraints.gridwidth = 3;
		constraints.insets = new Insets(15 , 5 , 0 , 0);
		JLabel label2 = new JLabel("编译空间:");
		label2.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField jTextField2 = new JTextField();
		jTextField2.setName("compilePathPrefix");
		list.add(jTextField2);
		jTextField2.setPreferredSize(new Dimension (300 , 30));
		gridBagLayout.setConstraints(label2, constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(jTextField2, constraints);
		panel.add(label2);
		panel.add(jTextField2);
		
		constraints.gridwidth = 3;
		constraints.insets = new Insets(15 , 5 , 0 , 0);
		JLabel label3 = new JLabel("缓存空间:");
		label3.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField jTextField3 = new JTextField();
		jTextField3.setName("cachePathPrefix");
		list.add(jTextField3);
		jTextField3.setPreferredSize(new Dimension (300 , 30));
		gridBagLayout.setConstraints(label3, constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(jTextField3, constraints);
		panel.add(label3);
		panel.add(jTextField3);
		
		constraints.gridwidth = 3;
		constraints.insets = new Insets(15 , 5 , 0 , 0);
		JLabel label4 = new JLabel("补丁格式:");
		label4.setHorizontalAlignment(SwingConstants.RIGHT);
		JRadioButton patchStyleBox1 = new JRadioButton("列表");
		JRadioButton patchStyleBox2 = new JRadioButton("树状");
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
		
		constraints.gridwidth = 3;
		constraints.insets = new Insets(15 , 5 , 0 , 0);
		JLabel label5 = new JLabel("项目列表:");
		label5.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField jTextField5 = new JTextField();
		jTextField5.setName("projectNames");
		list.add(jTextField5);
		jTextField5.setPreferredSize(new Dimension (250 , 30));
		jTextField5.setEditable(false);
		gridBagLayout.setConstraints(label5, constraints);
		gridBagLayout.setConstraints(jTextField5, constraints);
		JButton editProjectButton = new JButton();
		editProjectButton.setText("编辑");
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(editProjectButton, constraints);
		panel.add(label5);
		panel.add(jTextField5);
		panel.add(editProjectButton);

		constraints.insets = new Insets(80 , 5 , 0 , 0);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		JButton saveButton = new JButton("保存配置");
		gridBagLayout.setConstraints(saveButton, constraints);
		panel.add(saveButton);
		
		this.updateJTextComponentValue(list);
		this.updateRadioValue(patchStyleBox1, patchStyleBox2);
		
		//保存配置
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					DeloyParamCache.storage(JSON.toJSONString(DeployBean.getBuilder()
																		.sourcePathPrefix(jTextField1.getText())
																		.compilePathPrefix(jTextField2.getText())
																		.patchStyle(patchStyleBox1.isSelected() ? 1 : 2)
																		.cachePathPrefix(jTextField3.getText())
																		.projectNames(jTextField5.getText())
																		.projectString2List()
																		.projectNames(null)));
					
					JOptionPane.showConfirmDialog(window ,"保存成功!" , "信息提示" , JOptionPane.PLAIN_MESSAGE);
				} catch (Exception e2) {

					logger.error("保存配置失败" , e2);
					JOptionPane.showConfirmDialog(window ,"保存失败!" , "错误提示" , JOptionPane.PLAIN_MESSAGE);
				}
				
			}
		});
		

		this.addEditProjectButtonEvent(window , editProjectButton , list , patchStyleBox1 , patchStyleBox2);
		
		return panel;
	}
	
	/**
	 * 描述：自动设置文本值
	 * @author csy 
	 * @date 2020年9月29日 上午11:08:44
	 * @param list
	 */
	private void updateJTextComponentValue(List<JTextComponent> list){
		DeployBean cacheDeployBean = DeployBean.readCache(true);
		list.forEach((e) -> {
			Object v = ClassUtils.getBeanValue(e.getName() , cacheDeployBean);
			e.setText(v == null ? null : v.toString());
		});
	}
	
	/**
	 * 描述：设置radio
	 * @author csy 
	 * @date 2020年9月29日 上午11:11:38
	 * @param r1
	 * @param r2
	 */
	private void updateRadioValue(JRadioButton r1 , JRadioButton r2){
		DeployBean cacheDeployBean = DeployBean.readCache(true);
		if (cacheDeployBean.getPatchStyle() == 1){
			r1.setSelected(true);
		}else{
			r2.setSelected(true);
		}
	}
	
	/**
	 * 描述：EditProjectButton点击事件
	 * @author csy 
	 * @date 2020年9月29日 上午11:25:37
	 * @param window
	 * @param editProjectButton
	 * @param list
	 * @param radioButton1
	 * @param radioButton2
	 */
	private void addEditProjectButtonEvent(Window window ,JButton editProjectButton , 
			List<JTextComponent> list , JRadioButton radioButton1 , JRadioButton radioButton2){
		//编辑项目列表
		editProjectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//弹出编辑框
				JDialog deployJframe = new JDialog();
				Dimension size = window.getSize();
				deployJframe.setSize(size.width, size.height);
				deployJframe.setTitle("编辑项目");
				deployJframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				deployJframe.setLocationRelativeTo(window);
				deployJframe.setResizable(false);
				deployJframe.setLayout(new BorderLayout());
				deployJframe.add(new DeployFrameEditProjectPanel().init(deployJframe));
				deployJframe.setVisible(true);
				deployJframe.addWindowListener(new AbstractWindowCloseListener() {
					
					@Override
					public void onWindowColsed(WindowEvent e) {
						
						updateJTextComponentValue(list);
						
						updateRadioValue(radioButton1, radioButton2);
					}
				});
			}
		});
	}
}
