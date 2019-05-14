package com.saurabh.braincorp.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.saurabh.braincorp.model.Group;
import com.saurabh.braincorp.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserGroupServiceTest {

	@MockBean
	private UserGroupService userGroupService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private GroupService groupService;
	
	@Test
	public void shouldReturnListOfGroupsForUid0() throws Exception {
		List<Group> allGroups = new ArrayList<Group>();
		allGroups.add(new Group("nogroup", -1L, Arrays.asList("root", "daemon")));
		allGroups.add(new Group("wheel", 0L, Arrays.asList("root")));
		allGroups.add(new Group("nobody", -2L, Arrays.asList("nobody")));
		Mockito.when(groupService.getGroups()).thenReturn(allGroups);
		
		User user = new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh");
		Mockito.when(userService.getUserById(0L)).thenReturn(user);
		
		List<Group> expected = new ArrayList<Group>();
		expected.add(new Group("nogroup", -1L, Arrays.asList("root", "daemon")));
		expected.add(new Group("wheel", 0L, Arrays.asList("root")));
		
		Mockito.when(userGroupService.getUserGroupsByUserId(0L)).thenReturn(expected);
		List<Group> actual = userGroupService.getUserGroupsByUserId(0L);
		assertTrue(expected.equals(actual));
		assertTrue(expected.size() == actual.size());
	}
	
	@Test
	public void shouldReturnNullForUid10() throws Exception {
		Mockito.when(userService.getUserById(10L)).thenReturn(null);
		Mockito.when(userGroupService.getUserGroupsByUserId(10L)).thenReturn(null);
		List<Group> actual = userGroupService.getUserGroupsByUserId(10L);
		assertNull(actual);
	}
}
