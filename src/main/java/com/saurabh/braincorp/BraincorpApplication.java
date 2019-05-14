package com.saurabh.braincorp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saurabh.braincorp.exception.ResourceNotFoundException;
import com.saurabh.braincorp.model.Group;
import com.saurabh.braincorp.model.User;
import com.saurabh.braincorp.service.GroupService;
import com.saurabh.braincorp.service.UserGroupService;
import com.saurabh.braincorp.service.UserService;

@RestController
@SpringBootApplication
public class BraincorpApplication {

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserGroupService userGroupService;

	public static void main(String[] args) {
		SpringApplication.run(BraincorpApplication.class, args);
	}

	@GetMapping("/users")
	public List<User> getUsers() {
		return userService.getUsers();
	}

	@GetMapping("/users/query")
	public List<User> getUsersByQuery(@RequestParam(required = false) List<String> name,
			@RequestParam(required = false) List<Long> uid, @RequestParam(required = false) List<Long> gid,
			@RequestParam(required = false) List<String> comment, @RequestParam(required = false) List<String> home,
			@RequestParam(required = false) List<String> shell) {
		 List<User> users = userService.getUsersByQuery(name, uid, gid, comment, home, shell);
		 return users;
	}

	@GetMapping("/users/{id}")
	public User getUser(@PathVariable("id") long id) throws ResourceNotFoundException {
		User user = userService.getUserById(id);
		if (user == null)
			throw new ResourceNotFoundException();
		return user;

	}

	@GetMapping("/users/{id}/groups")
	public List<Group> getUserGroupsByUserId(@PathVariable("id") long id) {
		return userGroupService.getUserGroupsByUserId(id);

	}

	@GetMapping("/groups")
	public List<Group> getGroups() {
		return groupService.getGroups();
	}

	@GetMapping("/groups/query")
	public List<Group> getGroupsByQuery(@RequestParam(required = false) List<String> name,
			@RequestParam(required = false) List<Long> gid, @RequestParam(required = false) List<String> member) {
		return groupService.getGroupsByQuery(name, gid, member);
	}

	@GetMapping("/groups/{id}")
	public Group getGroupById(@PathVariable("id") long id) throws ResourceNotFoundException {
		Group group = groupService.getGroupById(id);
		if (group == null)
			throw new ResourceNotFoundException();
		return group;
	}
}
