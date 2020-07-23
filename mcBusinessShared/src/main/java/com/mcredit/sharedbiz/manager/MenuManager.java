package com.mcredit.sharedbiz.manager;

import java.util.List;
import java.util.stream.Collectors;

import com.mcredit.model.object.TreeMenu;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.TreeMenuDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class MenuManager extends BaseManager{
	

	public List<TreeMenuDTO> getUserTreeMenu(String loginId) throws Exception {
		if( StringUtils.isNullOrEmpty(loginId) )
			throw new ValidationException("Login ID không được để trống.");
		
		List<TreeMenuDTO> treeLst = CacheManager.UserPermission().findUserTreeMenuByUser(loginId);
		for (TreeMenuDTO treeMenuDTO : treeLst) {
			List<TreeMenu> treeItem = treeMenuDTO.getLstChild().stream().filter(item -> !"MFS_MOBILE".equals(item.getMenuCode())).collect(Collectors.toList());
			treeMenuDTO.setLstChild(treeItem);
		}

		if(treeLst == null || treeLst.size() == 0)
			throw new ValidationException("Không tìm thấy Menu cho tài khoản này.");

		return treeLst;
	}

}
