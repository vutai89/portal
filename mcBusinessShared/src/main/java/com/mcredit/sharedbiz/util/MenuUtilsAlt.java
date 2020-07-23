package com.mcredit.sharedbiz.util;

import java.util.ArrayList;
import java.util.List;

import com.mcredit.model.object.MenuView;
import com.mcredit.util.JSONConverter;

public class MenuUtilsAlt {
	
	public static void createRecursiveList(List<MenuView> menuList) {
		MenuView mainMenuView = null;
		ArrayList<MenuView> mainMenuList = new ArrayList<MenuView>();

		int index = 0;

		for (MenuView menuView : menuList) {
			if (menuView.getParentId() == null) {
				ArrayList<MenuView> childMenuList = new ArrayList<MenuView>();
				findChildIndex(index, menuList, childMenuList);

				if (childMenuList.size() > 0)
					mainMenuList.add(childMenuList.get(0));
				else {
					mainMenuView = new MenuView(menuView.getId(),
							menuView.getMenuName(), menuView.getParentId(),
							childMenuList);
					mainMenuList.add(mainMenuView);
				}
			}
			index++;
		}

		String result = JSONConverter.toJSON(mainMenuList);

		System.out.println(result);
	}

	public static void findChildIndex(int parentIndex, List<MenuView> menuList, List<MenuView> childMenuList) {
		MenuView mView = null;
		List<Integer> childIdxList = getChildIndex(parentIndex, menuList);

		if (childIdxList.size() > 0) {
			List<MenuView> childMenuLst = new ArrayList<MenuView>();

			for (Integer parentIdx : childIdxList) {
				findChildIndex(parentIdx, menuList, childMenuLst);

				if (!hasChilds(parentIdx, menuList)) {
					mView = new MenuView(menuList.get(parentIdx).getId(),
							menuList.get(parentIdx).getMenuName(), menuList
									.get(parentIdx).getParentId(),
							new ArrayList<MenuView>());// menuList.get(parentIdx).getChilds());
					childMenuLst.add(mView);
				}
			}

			mView = new MenuView(menuList.get(parentIndex).getId(), menuList
					.get(parentIndex).getMenuName(), menuList.get(parentIndex)
					.getParentId(), childMenuLst);
			childMenuList.add(mView);

		}
	}

	public static List<Integer> getChildIndex(int parentIndex, List<MenuView> menuList) {
		List<Integer> childIdxList = new ArrayList<Integer>();
		int index = 0;

		Long parentId = menuList.get(parentIndex).getId();

		for (MenuView menuView : menuList) {
			if (menuView.getParentId() != null
					&& menuView.getParentId().equals(parentId))
				childIdxList.add(index);

			index++;
		}

		return childIdxList;
	}

	public static boolean hasChilds(int parentIndex, List<MenuView> menuList) {
		Long parentId = menuList.get(parentIndex).getId();

		for (MenuView menuView : menuList) {
			if (menuView.getParentId() != null
					&& menuView.getParentId().equals(parentId))
				return true;
		}

		return false;
	}
}
