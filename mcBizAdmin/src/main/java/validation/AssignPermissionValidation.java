package validation;

import java.util.List;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.assign.FunctionRolePermission;
import com.mcredit.model.dto.assign.RoleServiceIdsDTO;
import com.mcredit.model.dto.assign.SearchUserDTO;
import com.mcredit.model.dto.assign.UserListDTO;
import com.mcredit.model.dto.assign.UserPermission;
import com.mcredit.model.dto.assign.UserSearchDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class AssignPermissionValidation {
	
	public void validateSetRolesServices(List<RoleServiceIdsDTO> rolesPermisions) throws ValidationException {
		if (rolesPermisions.size() < 1) {
			throw new ValidationException(Messages.getString("ad.setRolesServiceSize"));
		}
	}

	public void validateUserPermission(List<UserPermission> userPermissions) throws ValidationException {
		if (null == userPermissions || userPermissions.isEmpty()) {
			throw new ValidationException(
					Messages.getString("validation.field.notFound", Labels.getString("label.assign.permission.data")));
		}
		
	}

	public void validateGetRoleMenus(String roleId) throws ValidationException {
		// check role info
		if (StringUtils.isNullOrEmpty(roleId)) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.cancelCase.loginId")));
		}
		
	}

	public void validateSetMenuRole(List<FunctionRolePermission> functionRoles) throws ValidationException {
		if (null == functionRoles || functionRoles.isEmpty()) {
			throw new ValidationException(
					Messages.getString("validation.field.notFound", Labels.getString("label.assign.permission.data")));
		}
	}

	public void validateSearchCase(SearchUserDTO searchDTO) throws ValidationException {

		if (searchDTO.getKeyword() == null) {
			throw new ValidationException(Messages.getString("mfs.get.keyword.required"));
		}

		boolean isPageNumberValid = (searchDTO.getPageNumber() != null && searchDTO.getPageNumber() > 0);
		if (!isPageNumberValid) {
			throw new ValidationException(Messages.getString("mfs.get.pagenumber.required"));
		}

		boolean isPageSizeValid = (searchDTO.getPageSize() != null && searchDTO.getPageSize() > 0);
		if (!isPageSizeValid) {
			throw new ValidationException(Messages.getString("mfs.get.pagesize.required"));
		}
		
	}

	public void validateChangeStatus(Long userId, boolean isActive) throws ValidationException {
		if (userId == null || userId < 0) {
			throw new ValidationException(Messages.getString("user.id.required"));
		};
	}
	
	public void validateInsertUser(UserSearchDTO searchDTO) throws ValidationException {

		if (searchDTO == null) {
//			throw new ValidationException(Messages.getString("mfs.get.keyword.required"));
			throw new ValidationException(
					Messages.getString("validation.field.notFound", Labels.getString("label.assign.permission.data")));
		}
		
		if (searchDTO.getLoginId().equals("")) {
			throw new ValidationException(Messages.getString("mfs.get.pagenumber.required"));
		}

		
	}
	
	public void validateInsertUserList(List<UserListDTO> lstUser) throws ValidationException {
		if (lstUser.size() == 0) {
			throw new ValidationException(
					Messages.getString("validation.field.notFound", Labels.getString("label.assign.permission.data")));
		}
	}
}
