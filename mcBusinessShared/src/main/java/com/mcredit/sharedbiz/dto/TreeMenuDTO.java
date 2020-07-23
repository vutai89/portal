package com.mcredit.sharedbiz.dto;

import java.util.List;

import org.modelmapper.ModelMapper;

import com.google.common.reflect.TypeToken;
import com.mcredit.model.object.TreeMenu;

public class TreeMenuDTO extends TreeMenu {

	public TreeMenuDTO() {
		super();
	}
	
	public TreeMenuDTO(Integer parentId, String parentCode, String parentTitle, String parentModule, String parentType, Integer menuId,
			String menuCode, String menuTitle, String menuModule, String menuType, Integer treeLevel, List<TreeMenuDTO> lstChild) {
		
		super(parentId, parentCode, parentTitle, parentModule, parentType, menuId, menuCode, menuTitle, menuModule, menuType, treeLevel
				, new ModelMapper().map(lstChild, new TypeToken<List<TreeMenu>>(){private static final long serialVersionUID = 282404850380011013L;}.getType()));
		
	}
}
