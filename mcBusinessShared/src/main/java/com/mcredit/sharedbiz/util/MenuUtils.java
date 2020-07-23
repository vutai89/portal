package com.mcredit.sharedbiz.util;

import java.util.ArrayList;
import java.util.List;

import com.mcredit.sharedbiz.dto.TreeMenuDTO;

public class MenuUtils {
	
	public static List<TreeMenuDTO> createRecursiveList(List<TreeMenuDTO> menuList) {
		
		//String result = "[]";
		List<TreeMenuDTO> mainMenuList = null;
		
		if( menuList!=null && menuList.size()>0 ) {
			
			mainMenuList = new ArrayList<TreeMenuDTO>();
			
			TreeMenuDTO mainTreeMenuDTO = null;
	
			int index = 0;
	
			for (TreeMenuDTO menuView : menuList) {
				if (menuView.getParentId() == null) {//Fix '1' is rootId of Application
					List<TreeMenuDTO> childMenuList = new ArrayList<TreeMenuDTO>();
					findChildIndex(index, menuList, childMenuList);
	
					if (childMenuList.size() > 0)
						mainMenuList.add(childMenuList.get(0));
					else {
						mainTreeMenuDTO = new TreeMenuDTO(menuView.getParentId(), menuView.getParentCode(), menuView.getParentTitle(), menuView.getParentModule(), menuView.getParentType()
								, menuView.getMenuId(), menuView.getMenuCode(), menuView.getMenuTitle(), menuView.getMenuModule(), menuView.getMenuType(), menuView.getTreeLevel(), childMenuList);
						mainMenuList.add(mainTreeMenuDTO);
					}
				}
				index++;
			}
		}
		
		//System.out.println( JSONConverter.toJSON(mainMenuList); );
		
		return mainMenuList;
	}

	public static void findChildIndex(int parentIndex, List<TreeMenuDTO> menuList, List<TreeMenuDTO> childMenuList) {
		TreeMenuDTO mView = null;
		List<Integer> childIdxList = getChildIndex(parentIndex, menuList);

		if (childIdxList.size() > 0) {
			List<TreeMenuDTO> childMenuLst = new ArrayList<TreeMenuDTO>();

			for (Integer parentIdx : childIdxList) {
				findChildIndex(parentIdx, menuList, childMenuLst);

				if (!hasChilds(parentIdx, menuList)) {
					mView = new TreeMenuDTO(menuList.get(parentIdx).getParentId(), menuList.get(parentIdx).getParentCode(), menuList.get(parentIdx).getParentTitle(), menuList.get(parentIdx).getParentModule(), menuList.get(parentIdx).getParentType()
							, menuList.get(parentIdx).getMenuId(), menuList.get(parentIdx).getMenuCode(), menuList.get(parentIdx).getMenuTitle(), menuList.get(parentIdx).getMenuModule(), menuList.get(parentIdx).getMenuType(), menuList.get(parentIdx).getTreeLevel()
							, new ArrayList<TreeMenuDTO>());// menuList.get(parentIdx).getChilds());
					childMenuLst.add(mView);
				}
			}
			
			mView = new TreeMenuDTO(menuList.get(parentIndex).getParentId(), menuList.get(parentIndex).getParentCode(), menuList.get(parentIndex).getParentTitle(), menuList.get(parentIndex).getParentModule(), menuList.get(parentIndex).getParentType()
					, menuList.get(parentIndex).getMenuId(), menuList.get(parentIndex).getMenuCode(), menuList.get(parentIndex).getMenuTitle(), menuList.get(parentIndex).getMenuModule(), menuList.get(parentIndex).getMenuType(), menuList.get(parentIndex).getTreeLevel()
					, childMenuLst);
			childMenuList.add(mView);

		}
	}

	public static List<Integer> getChildIndex(int parentIndex, List<TreeMenuDTO> menuList) {
		List<Integer> childIdxList = new ArrayList<Integer>();
		int index = 0;

		Integer parentId = menuList.get(parentIndex).getMenuId();

		for (TreeMenuDTO menuView : menuList) {
			if (menuView.getParentId() != null
					&& menuView.getParentId().equals(parentId))
				childIdxList.add(index);

			index++;
		}

		return childIdxList;
	}

	public static boolean hasChilds(int parentIndex, List<TreeMenuDTO> menuList) {
		Integer parentId = menuList.get(parentIndex).getMenuId();

		for (TreeMenuDTO menuView : menuList) {
			if (menuView.getParentId() != null
					&& menuView.getParentId().equals(parentId))
				return true;
		}

		return false;
	}
}
