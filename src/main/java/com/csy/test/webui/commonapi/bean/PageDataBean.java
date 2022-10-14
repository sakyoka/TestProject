package com.csy.test.webui.commonapi.bean;

import java.util.Collections;
import java.util.List;

import com.csy.test.commons.utils.Objects;

import lombok.Data;

/**
 * 
 * 描述：分页数据对象
 * @author csy
 * @date 2022年9月12日 下午3:03:20
 * @param <T>
 */
@Data
public class PageDataBean<T> {

	private Integer rows;
	
	private Integer currentPage;
	
	private List<T> datas;
	
	public static <T> PageDataBean<T> pageDataBean(Integer rows, 
			Integer currentPage, List<T> datas){
		PageDataBean<T> pageDataBean = new PageDataBean<T>();
		pageDataBean.setCurrentPage(Objects.ifNullDefault(currentPage, 1));
		List<T> defaultList = Collections.emptyList();
		pageDataBean.setDatas(Objects.ifNullDefault(datas, defaultList));
		pageDataBean.setRows(Objects.ifNullDefault(rows, 0));
		return pageDataBean;
	}
	
	public static <T> PageDataBean<T> listToPageDataBean(PageBean pageBean, List<T> allDatas){
		int currentPage = pageBean.getPageIndex();
		int pageSize = pageBean.getPageSize();
		int startIndex = (currentPage - 1) * pageSize;
		int endIndex = (currentPage == 1 ? pageSize : (startIndex + pageSize));
		int size = allDatas.size();
		List<T> datas = null;
		if (size >= startIndex){
			endIndex = (size <= endIndex ? size : endIndex);
			datas = allDatas.subList(startIndex, endIndex);
		}else{
			datas = Collections.emptyList();
		}
		
		return pageDataBean(size, currentPage, datas);
	}

	public static <T> PageDataBean<T> pageDataBean(PageBean pageBean, 
			List<T> taskInfoPageVos) {
		return pageDataBean(Integer.valueOf(pageBean.getPage().getTotal() + ""), 
				pageBean.getPageIndex(), taskInfoPageVos);
	}
}
