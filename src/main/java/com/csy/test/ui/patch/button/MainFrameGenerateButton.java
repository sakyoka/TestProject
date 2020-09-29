package com.csy.test.ui.patch.button;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csy.test.commons.patch.base.defaults.DefaultPackPatchFile;
import com.csy.test.commons.patch.base.defaults.DefaultPatchStandardGenerate;
import com.csy.test.commons.patch.base.defaults.TomcatPatchOriginalGenerate;
import com.csy.test.commons.patch.base.defaults.TomcatWriteFileRecordFile;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.state.sourcepath.state.defaults.tomcat.TomcatSrcJavaPathState;
import com.csy.test.commons.patch.utils.PatchUtils;
import com.csy.test.commons.valid.bean.ParamValidResult;
import com.csy.test.commons.valid.utils.ValidUtils;
import com.csy.test.ui.patch.bean.DeployBean;
import com.csy.test.ui.patch.cache.ButtonParamCache;
import com.csy.test.ui.patch.common.base.InitInterfaceFrame;

/**
 * 
 * 描述：保存按钮
 * @author csy
 * @date 2020年9月29日 下午4:20:04
 */
public class MainFrameGenerateButton implements InitInterfaceFrame{
 
	//private static final String PATCH_STYLLE_TREE = "树状";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Component init(JFrame window) {
		
		JButton createButton = new JButton("Click it generate");
		createButton.setPreferredSize(new Dimension(800, 80));
		createButton.setFont(new Font(null, 17, 30));
		
//		Component[] components = window.getRootPane().getContentPane().getComponents();
//		DeployBean deployBean = DeployBean.readCache(true);
//		List<String> projectList = new ArrayList<String>();
//		for (Component component:components){
//			if (ComponentNameConstant.MAIN_FRAME_MESSAGE_LIST.equals(component.getName())){
//				JPanel jPanel = (JPanel)component;
//				Component[] jPanelComponents = jPanel.getComponents();
//				for (Component jPanelComponent:jPanelComponents){
//					if (jPanelComponent instanceof JToggleButton){
//						JToggleButton jToggleButton = (JToggleButton)jPanelComponent;
//						if (ComponentNameConstant.FIELD_PATCHSTYLE.equals(jToggleButton.getName())){
//							int selected  = (jToggleButton.isSelected() && PATCH_STYLLE_TREE.equals(jToggleButton.getText()) ? 2 : 1);
//							deployBean.patchStyle(selected);
//						}
//						jToggleButton.addActionListener(new ActionListener() {
//
//							@Override
//							public void actionPerformed(ActionEvent e) {
//								if (ComponentNameConstant.FIELD_PATCHSTYLE.equals(jToggleButton.getName())){
//									int selected  = (PATCH_STYLLE_TREE.equals(jToggleButton.getText()) ? 2 : 1);
//									deployBean.patchStyle(selected);
//									System.out.println(jToggleButton.getText());
//								}
//								
//								if (ComponentNameConstant.FIELD_PROJECTENNAMELIST.equals(jToggleButton.getName())){
//									if (jToggleButton.isSelected()){
//										if (!projectList.contains(jToggleButton.getText())){
//											projectList.add(jToggleButton.getText());
//										}
//									}else{
//										if (projectList.contains(jToggleButton.getText())){
//											projectList.remove(jToggleButton.getText());
//										}
//									}
//								}
//							}
//						});
//
//					}
//				}
//			}
//		}
		
		createButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DeployBean deployBean = DeployBean.readCache(true);
				List<String> projectList = ButtonParamCache.getCheckBox();
				if (projectList.isEmpty()){
					JOptionPane.showConfirmDialog(window ,"请勾选项目!" , "错误提示" , JOptionPane.PLAIN_MESSAGE);
					return ;
				}
				
				try {
					System.out.println(ButtonParamCache.getRadio());
					generatePatch(deployBean.projectEnNameList(projectList)
							                .patchStyle(ButtonParamCache.getRadio()));
					
					JOptionPane.showConfirmDialog(window ,"生成完毕." , "信息提示" , JOptionPane.PLAIN_MESSAGE);
				} catch (Exception e2) {
					logger.error("生成补丁失败" , e2);
					JOptionPane.showConfirmDialog(window , e2.getMessage() , "错误提示" , JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		
		return createButton;
	}
	
	/**
	 * 描述：生成补丁
	 * @author csy 
	 * @date 2020年9月29日 下午4:12:55
	 * @param deployBean
	 */
	private void generatePatch(DeployBean deployBean){
		PatchInitParams pachInitParams =  PatchInitParams.getBuilder()
				.sourcePathPrefix(deployBean.getSourcePathPrefix())//工作空间根目录
				.compilePathPrefix(deployBean.getCompilePathPrefix())//编译空间根目录
				.cachePathPrefix(deployBean.getCachePathPrefix())//补丁缓存地址
				.projectChName("测试项目")
				.sourcePathStateClazz(TomcatSrcJavaPathState.class)//解析路径开始
				.patchGenerateClazz(deployBean.getPatchStyle() == 1 ? 
						 DefaultPatchStandardGenerate.class : TomcatPatchOriginalGenerate.class)//DefaultPatchStandardGenerate 、TomcatPatchOriginalGenerate
				.writeRecordFileClazz(TomcatWriteFileRecordFile.class)//补丁记录文件
				.packPatchFileClazz(DefaultPackPatchFile.class)//打包
				.useSamePatchRecordFile(true)//是否用同一个记录文件
				.build();
		List<String> projectList = deployBean.getProjectEnNameList();
		for (String projectName:projectList){
			pachInitParams.projectEnName(projectName) //项目包名
			              .useGitCommand();//git 命令获取更改文件
			
			ParamValidResult validResult = ValidUtils.valid(pachInitParams);
			if(validResult.getHasError()) {
				throw new RuntimeException(validResult.getErrorMessageMap().toString());
			}

			PatchUtils.generate(pachInitParams);//生成补丁文件
		}		
	}
	
}
