package com.saurabh.braincorp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saurabh.braincorp.model.Group;
import com.saurabh.braincorp.model.User;

@Service
public class UserGroupService {

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	public List<Group> getUserGroupsByUserId(long id) {
		User user = userService.getUserById(id);
		if(user == null)
			return null;
		List<Group> groups = groupService.getGroups();
		return getGroupsByUsername(groups, user.getName());
	}

	private List<Group> getGroupsByUsername(List<Group> groups, String username) {
		List<Group> groupList = new ArrayList<>();
		for (Group group : groups) {
			if (group != null && group.getMembers() != null && group.getMembers().indexOf(username) != -1) {
				groupList.add(group);
			}
		}
		return groupList;
	}
}
