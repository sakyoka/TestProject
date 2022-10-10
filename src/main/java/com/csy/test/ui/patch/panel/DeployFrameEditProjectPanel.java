package com.csy.test.ui.patch.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.csy.test.ui.patch.bean.DeployBean;
import com.csy.test.ui.patch.cache.DeloyParamCache;
import com.csy.test.ui.patch.common.base.InitInterface;

/**
 * 
 * 描述：编辑项目信息
 * @author csy
 * @date 2020年9月29日 上午10:39:44
 */
public class DeployFrameEditProjectPanel implements InitInterface{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Component init(Window window) {
		JPanel panel = new JPanel();
		DeployBean cacheDeployBean = DeployBean.readCache(true);
        JTextArea jTextArea = new JTextArea();
        jTextArea.setText(cacheDeployBean
        		               .projectList2String()
        		               .getProjectNames());
        jTextArea.setLineWrap(true);
        jTextArea.setForeground(Color.BLACK);
        jTextArea.setBackground(Color.white);
        Dimension parenDimension = window.getSize();
        jTextArea.setPreferredSize(new Dimension(parenDimension.width - 20 , parenDimension.height - 100));
        JScrollPane jsp = new JScrollPane(jTextArea);
        jsp.setBounds(0 , 0 , parenDimension.width , parenDimension.height);
        panel.setPreferredSize(jTextArea.getPreferredSize());
        
        JButton saveButton = new JButton("保存");
        saveButton.setHorizontalAlignment(SwingConstants.CENTER);
        
        saveButton.addActionListener(new ActionListener() {
        	
			@Override
			public void actionPerformed(ActionEvent e) {
				String contents = jTextArea.getText();
				if (StringUtils.isBlank(contents)){
					JOptionPane.showConfirmDialog(window ,"保存失败，内容不能不为空!" , "错误提示" , 0);
				}else{
					try {
						DeloyParamCache.storage(JSON.toJSONString(DeployBean.readCache(true)
                                .projectNames(contents)
                                .projectString2List()));
						JOptionPane.showConfirmDialog(window ,"保存成功!" , "信息提示" , JOptionPane.PLAIN_MESSAGE);
						window.dispose();
					} catch (Exception e2) {
						logger.error("保存项目信息失败" , e2);
						JOptionPane.showConfirmDialog(window ,"保存项目信息失败 , 详情看日志" , "错误提示" , JOptionPane.PLAIN_MESSAGE);
					}
				}
			}
		});
        
        panel.add(jsp);
        panel.add(saveButton);
		return panel;
	}

}
