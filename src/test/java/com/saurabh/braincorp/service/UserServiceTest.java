package com.saurabh.braincorp.service;

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

import com.saurabh.braincorp.exception.ResourceNotFoundException;
import com.saurabh.braincorp.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

	@MockBean
	private UserService userService;

	@Test
	public void shouldReturnListOfUsers() throws Exception {
		List<User> expected = new ArrayList<User>();
		expected.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		expected.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		expected.add(new User("daemon", 1, 1, "System Services", "/var/root", "/usr/bin/false"));
		Mockito.when(userService.getUsers()).thenReturn(expected);
		List<User> actual = userService.getUsers();
		assertTrue(expected.equals(actual));
		assertTrue(expected.size() == actual.size());
	}

	@Test
	public void shouldReturnEmptyListOfUsers() throws Exception {
		List<User> expected = new ArrayList<User>();
		Mockito.when(userService.getUsers()).thenReturn(expected);
		List<User> actual = userService.getUsers();
		assertTrue(expected.equals(actual));
		assertTrue(expected.size() == actual.size());
	}

	@Test
	public void shouldUserWithId0() throws Exception {
		User expected = new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh");
		Mockito.when(userService.getUserById(0L)).thenReturn(expected);
		User actual = userService.getUserById(0L);
		assertTrue(expected.equals(actual));
		assertTrue(expected.getName().equals(actual.getName()));
		assertTrue(expected.getUid() == actual.getUid());
		assertTrue(expected.getGid() == actual.getGid());
		assertTrue(expected.getComment().equals(actual.getComment()));
		assertTrue(expected.getHome().equals(actual.getHome()));
		assertTrue(expected.getShell().equals(actual.getShell()));
	}

	@Test(expected = ResourceNotFoundException.class)
	public void shouldThrowResourceNotFoundExceptionForUser10() throws Exception {
		Mockito.when(userService.getUserById(10L)).thenThrow(new ResourceNotFoundException());
		userService.getUserById(10L);
	}
	
	@Test
	public void shouldReturnListOfUsersWithAnyUserParams() throws Exception {
		List<User> expected = new ArrayList<User>();
		expected.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		expected.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		expected.add(new User("daemon", 1, 1, "System Services", "/var/root", "/usr/bin/false"));
		expected.add(new User("_captiveagent", 258, 258, "captiveagent", "/var/empty", "/usr/bin/false"));

		List<String> names = Arrays.asList("root");
		List<Long> uids = Arrays.asList(-2L);
		List<Long> gids = Arrays.asList(0L);
		List<String> comments = Arrays.asList("captiveagent");
		List<String> home = Arrays.asList("/var/root");
		List<String> shell = Arrays.asList("/bin/sh");

		Mockito.when(userService.getUsersByQuery(names, uids, gids, comments, home, shell)).thenReturn(expected);
		List<User> actual = userService.getUsersByQuery(names, uids, gids, comments, home, shell);
		assertTrue(expected.equals(actual));
		assertTrue(expected.size() == actual.size());
	}
	
	@Test
	public void shouldReturnListOfUsersWithNameRootOrUid_2OrGid1OrComment() throws Exception {
		List<User> expected = new ArrayList<User>();
		expected.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		expected.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		expected.add(new User("daemon", 1, 1, "System Services", "/var/root", "/usr/bin/false"));
		expected.add(new User("_captiveagent", 258, 258, "captiveagent", "/var/empty", "/usr/bin/false"));

		List<String> names = Arrays.asList("root");
		List<Long> uids = Arrays.asList(-2L);
		List<Long> gids = Arrays.asList(1L);
		List<String> comments = Arrays.asList("captiveagent");
		Mockito.when(userService.getUsersByQuery(names, uids, gids, comments, null, null)).thenReturn(expected);
		List<User> actual = userService.getUsersByQuery(names, uids, gids, comments, null, null);
		assertTrue(expected.equals(actual));
		assertTrue(expected.size() == actual.size());
	}
	
}
