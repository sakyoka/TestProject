package com.csy.test.ui.patch.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csy.test.ui.patch.annotation.JComponent;
import com.csy.test.ui.patch.bean.DeployBean;
import com.csy.test.ui.patch.cache.ButtonParamCache;
import com.csy.test.ui.patch.common.base.InitInterface;
import com.csy.test.ui.patch.constants.ComponentNameConstant;

/**
 * 
 * 描述：参数配置
 * @author csy
 * @date 2020年9月28日 下午1:10:25
 */
public class MainFramePanel implements InitInterface{

	private static Logger logger = LoggerFactory.getLogger(MainFramePanel.class);
	
	private static final String PATCH_STYLLE_TREE = "树状";
	
	@Override
	public Component init(Window window) {
		JPanel mainFramePanel = new JPanel();
		mainFramePanel.setName(ComponentNameConstant.MAIN_FRAME_MESSAGE_LIST);
		mainFramePanel.setPreferredSize(new Dimension(800, 100));
		mainFramePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		autoCreateLabelComponent(mainFramePanel);
		mainFramePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		return mainFramePanel;
	}
	
	/**
	 * 描述：自动创建组件
	 * @author csy 
	 * @date 2020年9月29日 下午12:33:25
	 * @param panel
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void autoCreateLabelComponent(JPanel panel){
		DeployBean deployBean = DeployBean.readCache(true);
		Class clazz = deployBean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
			for (Field field:fields){
				field.setAccessible(true);
				if (field.isAnnotationPresent(JComponent.class)){
					Object vObject = field.get(deployBean);
					JComponent jComponent = field.getAnnotation(JComponent.class);
					
					JLabel jLabelName = new JLabel();
					jLabelName.setText(jComponent.name() + ":");
					panel.add(jLabelName);
					
					if (field.getType() == String.class){
						JLabel jLabelValue = new JLabel();
						jLabelValue.setText(vObject == null ? null : vObject.toString());
						jLabelValue.setName(jComponent.id());
						panel.add(jLabelValue);
					}
					
					if (field.getType() == Integer.class && vObject != null){
											
						JRadioButton r1 = new JRadioButton("列表");
						JRadioButton r2 = new JRadioButton("树状");
						
						if (vObject.equals(1)){
							r1.setSelected(true);
						}else{
							r2.setSelected(true);
						}
						//设置到缓存中
						ButtonParamCache.setRadio((Integer)vObject);
						
				        ButtonGroup patchGroups = new ButtonGroup();
				        patchGroups.add(r1);
				        patchGroups.add(r2);
				        radioListener(r1);
				        radioListener(r2);
						r1.setName(jComponent.id());
						r2.setName(jComponent.id());
						panel.add(r1);
						panel.add(r2);
					}
					
					if (field.getType() == List.class && vObject != null){
						@SuppressWarnings("unchecked")
						List<String> list = (List<String>) vObject;
						list.forEach((e) -> {
							JCheckBox jCheckBox = new JCheckBox(e);
							jCheckBox.setName(jComponent.id());
							checkBoxListener(jCheckBox);
							panel.add(jCheckBox);
						});
					}
				}
				field.setAccessible(false);
			}
		} catch (Exception e) {
			logger.error("创建列表信息失败"  , e);
			throw new RuntimeException("创建列表信息失败");
		}

	}
	
	/**
	 * 描述：radio监听
	 * @author csy 
	 * @date 2020年9月29日 下午5:18:30
	 * @param r1
	 */
	private static void radioListener(JRadioButton r1){
		r1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (r1.isSelected())
					ButtonParamCache.setRadio(PATCH_STYLLE_TREE.equals(r1.getText()) ? 2 : 1);
			}
		});
	}
	
	/**
	 * 描述：checkbox监听
	 * @author csy 
	 * @date 2020年9月29日 下午5:24:53
	 * @param jCheckBox
	 */
	private static void checkBoxListener(JCheckBox jCheckBox){
		jCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> list = ButtonParamCache.getCheckBox();
				if (jCheckBox.isSelected()){
					if (!list.contains(jCheckBox.getText())){
						list.add(jCheckBox.getText());
					}
				}else{
					if (list.contains(jCheckBox.getText())){
						list.remove(jCheckBox.getText());
					}
				}
			}
		});
	}
}
