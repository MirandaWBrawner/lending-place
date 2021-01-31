package lendingplace.library.service;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lendingplace.library.dao.RoleDao;
import lendingplace.library.model.UserRole;
import lendingplace.library.model.User;

@Service
public class UserRoleService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRoleService.class);
	
	@Autowired
	private RoleDao roleDao;

	public Set<UserRole> searchByName(String searchTerm) {
		Set<UserRole> union = new HashSet<>();
		for (Method method: RoleDao.class.getDeclaredMethods()) {
			if (method.getName().startsWith("findBy")
					&& !method.getName().endsWith("Containing")
					&& List.class.isAssignableFrom(method.getReturnType())) {
				try {
					Object rawMethodResult = method.invoke(roleDao, searchTerm);
					List<UserRole> partialResultPage = (List<UserRole>) rawMethodResult;
					for (UserRole record: partialResultPage) {
						union.add(record);
					}
				} catch (Exception exception) {
					logger.error("The app ran into a problem while trying to select "
							+ "a user role from the database.\n" + exception.toString());
				}
			}
		}
		if (union.isEmpty()) {
			logger.info(String.format("The search for '%s' in the user role table did not "
					+ "return any results.", searchTerm));
		} else {
			int count = union.size();
			logger.info(String.format("The search for '%s' in the user "
					+ "role table returned %d result%s.", searchTerm,
					count, count == 1? "" : "s"));
		}
		return union;
	}
	
	public boolean userHasRoleNamed(User user, String roleName) {
		if (user.getRoles() == null) return false;
		for (UserRole nextRole: user.getRoles()) {
			if (nextRole.getNameSet().contains(roleName)) {
				return true;
			}
		}
		return false;
	}
}
