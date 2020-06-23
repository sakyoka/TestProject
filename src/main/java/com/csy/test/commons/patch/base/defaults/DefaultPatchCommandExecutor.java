package com.csy.test.commons.patch.base.defaults;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.csy.test.commons.patch.base.AbstractPatchCommandExecutor;
import com.csy.test.commons.utils.Command;

public class DefaultPatchCommandExecutor extends AbstractPatchCommandExecutor {

	@Override
	public List<String> execute() {
		
		if (this.sourcePaths == null) 
			this.sourcePaths = new ArrayList<String>();
		
        String contents = Command.getBuilder().commandStr(this.patchInitParams.getCommandStr())
								              .isPrint(true)
								              .toStringContents();
		Pattern p = Pattern.compile("src[^\\s]*");
		Matcher matcher = p.matcher(contents);
		String path = null;
		while (matcher.find()){
			path = matcher.group();
			this.sourcePaths.add(path);
		}
		return this.sourcePaths;
	}

}
