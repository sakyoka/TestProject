package com.csy.test.commons.patch.base;

import com.csy.test.commons.patch.bean.PatchInitParams;

/**
 * 
 * 描述：压缩补丁文件夹
 * @author csy
 * @date 2020年7月3日 下午3:42:57
 */
public abstract class AbstractPackPatchFile {

	public abstract void packFile(PatchInitParams patchInitParams);
}
