package com.csy.test.ui.patch.common.base;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * 
 * 描述：实现WindowListener
 * @author csy
 * @date 2020年9月29日 下午5:40:33
 */
public abstract class AbstractWindowCloseListener implements WindowListener{

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		this.onWindowColsed(e);
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 描述：只需要这个
	 * @author csy 
	 * @date 2020年9月29日 下午5:37:31
	 * @param e
	 */
	public abstract void onWindowColsed(WindowEvent e);
}
