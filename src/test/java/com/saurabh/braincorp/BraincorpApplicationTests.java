package com.saurabh.braincorp;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.saurabh.braincorp.exception.ResourceNotFoundException;
import com.saurabh.braincorp.model.Group;
import com.saurabh.braincorp.model.User;
import com.saurabh.braincorp.service.GroupService;
import com.saurabh.braincorp.service.UserGroupService;
import com.saurabh.braincorp.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BraincorpApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@MockBean
	private UserService userService;

	@MockBean
	private GroupService groupService;

	@MockBean
	private UserGroupService userGroupService;

	@Test
	public void shouldReturnListOfUsers() throws Exception {
		String expected = "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"},{\"name\":\"nobody\",\"uid\":-2,\"gid\":-2,\"comment\":\"Unprivileged User\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"},{\"name\":\"daemon\",\"uid\":1,\"gid\":1,\"comment\":\"System Services\",\"home\":\"/var/root\",\"shell\":\"/usr/bin/false\"}]";
		List<User> users = new ArrayList<User>();
		users.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		users.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		users.add(new User("daemon", 1, 1, "System Services", "/var/root", "/usr/bin/false"));
		Mockito.when(userService.getUsers()).thenReturn(users);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnEmptyListOfUsers() throws Exception {
		String expected = "[]";
		List<User> users = new ArrayList<User>();
		Mockito.when(userService.getUsers()).thenReturn(users);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnUserWithIdZero() throws Exception {
		String expected = "{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"}";
		User user = new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh");
		Mockito.when(userService.getUserById(0)).thenReturn(user);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/0");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnResourceNotFoundExceptionForUserId10() throws Exception {
		Mockito.when(userService.getUserById(10L)).thenThrow(new ResourceNotFoundException());
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/{id}", 10L);

		MvcResult result = mockMvc.perform(builder).andReturn();
		Exception exception = result.getResolvedException();
		assertTrue(exception instanceof ResourceNotFoundException);
		assertTrue(result.getResponse().getStatus() == 404);
	}

	@Test
	public void shouldReturnListOfUsersWithNameRootOrNobody() throws Exception {
		String expected = "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"},{\"name\":\"nobody\",\"uid\":-2,\"gid\":-2,\"comment\":\"Unprivileged User\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"}]";
		List<User> users = new ArrayList<User>();
		users.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		users.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		List<String> names = Arrays.asList("root", "nobody");
		Mockito.when(userService.getUsersByQuery(names, null, null, null, null, null)).thenReturn(users);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/query?name=root&name=nobody");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnListOfUsersWithNameRootOrUid_2() throws Exception {
		String expected = "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"},{\"name\":\"nobody\",\"uid\":-2,\"gid\":-2,\"comment\":\"Unprivileged User\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"}]";
		List<User> users = new ArrayList<User>();
		users.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		users.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		List<String> names = Arrays.asList("root");
		List<Long> uids = Arrays.asList(-2L);
		Mockito.when(userService.getUsersByQuery(names, uids, null, null, null, null)).thenReturn(users);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/query?name=root&uid=-2");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnListOfUsersWithNamesUidsGids() throws Exception {
		// String expected =
		// "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System
		// Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"},{\"name\":\"nobody\",\"uid\":-2,\"gid\":-2,\"comment\":\"Unprivileged
		// User\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"},{\"name\":\"daemon\",\"uid\":1,\"gid\":1,\"comment\":\"System
		// Services\",\"home\":\"/var/root\",\"shell\":\"/usr/bin/false\"},{\"name\":\"_captiveagent\",\"uid\":258,\"gid\":258,\"comment\":\"captiveagent\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"}]";
		String expected = "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"}, {\"name\":\"nobody\",\"uid\":-2,\"gid\":-2,\"comment\":\"Unprivileged User\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"},{\"name\":\"daemon\",\"uid\":1,\"gid\":1,\"comment\":\"System Services\",\"home\":\"/var/root\",\"shell\":\"/usr/bin/false\"}]";
		List<User> users = new ArrayList<User>();
		users.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		users.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		users.add(new User("daemon", 1, 1, "System Services", "/var/root", "/usr/bin/false"));
		// users.add(new User("_captiveagent", 258, 258, "captiveagent", "/var/empty",
		// "/usr/bin/false"));

		List<String> names = Arrays.asList("root");
		List<Long> uids = Arrays.asList(-2L);
		List<Long> gids = Arrays.asList(1L);
//		List<String> comments = Arrays.asList("captiveagent");
//		List<String> home = Arrays.asList("/var/root");
//		List<String> shell = Arrays.asList("/bin/sh");

		// Mockito.when(userService.getUsersByQuery(names, uids, gids, comments, home,
		// shell)).thenReturn(users);
		Mockito.when(userService.getUsersByQuery(names, uids, gids, null, null, null)).thenReturn(users);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/query?name=root&uid=-2&gid=1");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnListOfUsersWithNameRootOrUid_2OrGid1() throws Exception {
		// String expected =
		// "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System
		// Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"},{\"name\":\"nobody\",\"uid\":-2,\"gid\":-2,\"comment\":\"Unprivileged
		// User\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"},{\"name\":\"daemon\",\"uid\":1,\"gid\":1,\"comment\":\"System
		// Services\",\"home\":\"/var/root\",\"shell\":\"/usr/bin/false\"},{\"name\":\"_captiveagent\",\"uid\":258,\"gid\":258,\"comment\":\"captiveagent\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"}]";
		String expected = "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"}, {\"name\":\"nobody\",\"uid\":-2,\"gid\":-2,\"comment\":\"Unprivileged User\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"},{\"name\":\"daemon\",\"uid\":1,\"gid\":1,\"comment\":\"System Services\",\"home\":\"/var/root\",\"shell\":\"/usr/bin/false\"}]";
		List<User> users = new ArrayList<User>();
		users.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		users.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		users.add(new User("daemon", 1, 1, "System Services", "/var/root", "/usr/bin/false"));
		// users.add(new User("_captiveagent", 258, 258, "captiveagent", "/var/empty",
		// "/usr/bin/false"));

		List<String> names = Arrays.asList("root");
		List<Long> uids = Arrays.asList(-2L);
		List<Long> gids = Arrays.asList(1L);
//		List<String> comments = Arrays.asList("captiveagent");
//		List<String> home = Arrays.asList("/var/root");
//		List<String> shell = Arrays.asList("/bin/sh");

		// Mockito.when(userService.getUsersByQuery(names, uids, gids, comments, home,
		// shell)).thenReturn(users);
		Mockito.when(userService.getUsersByQuery(names, uids, gids, null, null, null)).thenReturn(users);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/query?name=root&uid=-2&gid=1");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnListOfUsersWithNameRootOrUid_2OrGid1OrComment() throws Exception {
		String expected = "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"}, {\"name\":\"nobody\",\"uid\":-2,\"gid\":-2,\"comment\":\"Unprivileged User\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"},{\"name\":\"daemon\",\"uid\":1,\"gid\":1,\"comment\":\"System Services\",\"home\":\"/var/root\",\"shell\":\"/usr/bin/false\"},{\"name\":\"_captiveagent\",\"uid\":258,\"gid\":258,\"comment\":\"captiveagent\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"}]";
		List<User> users = new ArrayList<User>();
		users.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		users.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		users.add(new User("daemon", 1, 1, "System Services", "/var/root", "/usr/bin/false"));
		users.add(new User("_captiveagent", 258, 258, "captiveagent", "/var/empty", "/usr/bin/false"));

		List<String> names = Arrays.asList("root");
		List<Long> uids = Arrays.asList(-2L);
		List<Long> gids = Arrays.asList(1L);
		List<String> comments = Arrays.asList("captiveagent");
		Mockito.when(userService.getUsersByQuery(names, uids, gids, comments, null, null)).thenReturn(users);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/query?name=root&uid=-2&gid=1&comment=captiveagent");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnListOfUsersWithHome() throws Exception {
		String expected = "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"},{\"name\":\"daemon\",\"uid\":1,\"gid\":1,\"comment\":\"System Services\",\"home\":\"/var/root\",\"shell\":\"/usr/bin/false\"}]";
		List<User> users = new ArrayList<User>();
		users.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		users.add(new User("daemon", 1, 1, "System Services", "/var/root", "/usr/bin/false"));
		List<String> homes = Arrays.asList("/var/root");
		Mockito.when(userService.getUsersByQuery(null, null, null, null, homes, null)).thenReturn(users);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/query?home=/var/root");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnListOfUsersWithShell() throws Exception {
		String expected = "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"}]";
		List<User> users = new ArrayList<User>();
		users.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		List<String> shells = Arrays.asList("/bin/sh");
		Mockito.when(userService.getUsersByQuery(null, null, null, null, null, shells)).thenReturn(users);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/query?shell=/bin/sh");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnListOfUsersWithAnyUserParams() throws Exception {
		String expected = "[{\"name\":\"root\",\"uid\":0,\"gid\":0,\"comment\":\"System Administrator\",\"home\":\"/var/root\",\"shell\":\"/bin/sh\"},{\"name\":\"nobody\",\"uid\":-2,\"gid\":-2,\"comment\":\"Unprivileged User\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"},{\"name\":\"daemon\",\"uid\":1,\"gid\":1,\"comment\":\"System Services\",\"home\":\"/var/root\",\"shell\":\"/usr/bin/false\"},{\"name\":\"_captiveagent\",\"uid\":258,\"gid\":258,\"comment\":\"captiveagent\",\"home\":\"/var/empty\",\"shell\":\"/usr/bin/false\"}]";
		List<User> users = new ArrayList<User>();
		users.add(new User("root", 0, 0, "System Administrator", "/var/root", "/bin/sh"));
		users.add(new User("nobody", -2, -2, "Unprivileged User", "/var/empty", "/usr/bin/false"));
		users.add(new User("daemon", 1, 1, "System Services", "/var/root", "/usr/bin/false"));
		users.add(new User("_captiveagent", 258, 258, "captiveagent", "/var/empty", "/usr/bin/false"));

		List<String> names = Arrays.asList("root");
		List<Long> uids = Arrays.asList(-2L);
		List<Long> gids = Arrays.asList(0L);
		List<String> comments = Arrays.asList("captiveagent");
		List<String> home = Arrays.asList("/var/root");
		List<String> shell = Arrays.asList("/bin/sh");

		Mockito.when(userService.getUsersByQuery(names, uids, gids, comments, home, shell)).thenReturn(users);
		String query = "/users/query?name=root&uid=-2&gid=0&comment=captiveagent&home=/var/root&shell=/bin/sh";
		RequestBuilder builder = MockMvcRequestBuilders.get(query);
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnListOfGroups() throws Exception {
		String expected = "[{\"name\":\"nogroup\",\"gid\":-1,\"members\":[]},{\"name\":\"wheel\",\"gid\":0,\"members\":[\"root\"]},{\"name\":\"nobody\",\"gid\":-2,\"members\":[]}]";
		List<Group> groups = new ArrayList<Group>();
		groups.add(new Group("nogroup", -1L, new ArrayList<String>()));
		groups.add(new Group("wheel", 0L, Arrays.asList("root")));
		groups.add(new Group("nobody", -2L, new ArrayList<String>()));
		Mockito.when(groupService.getGroups()).thenReturn(groups);
		RequestBuilder builder = MockMvcRequestBuilders.get("/groups");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnEmptyListOfGroups() throws Exception {
		String expected = "[]";
		List<Group> groups = new ArrayList<Group>();
		Mockito.when(groupService.getGroups()).thenReturn(groups);
		RequestBuilder builder = MockMvcRequestBuilders.get("/groups");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnGroupWithIdZero() throws Exception {
		String expected = "{\"name\":\"wheel\",\"gid\":0,\"members\":[\"root\"]}";
		Group group = new Group("wheel", 0L, Arrays.asList("root"));
		Mockito.when(groupService.getGroupById(0)).thenReturn(group);
		RequestBuilder builder = MockMvcRequestBuilders.get("/groups/{id}", 0L);
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test()
	public void shouldReturnResourceNotFoundExceptionForGroupId10() throws Exception {
		Mockito.when(userService.getUserById(10L)).thenThrow(new ResourceNotFoundException());
		RequestBuilder builder = MockMvcRequestBuilders.get("/groups/{id}", 10L);
		MvcResult result = mockMvc.perform(builder).andReturn();
		Exception exception = result.getResolvedException();
		assertTrue(exception instanceof ResourceNotFoundException);
		assertTrue(result.getResponse().getStatus() == 404);
	}

	@Test
	public void shouldReturnListOfGroupsWithNameMailOrGid265() throws Exception {
		String expected = "[{\"name\":\"mail\",\"gid\":6,\"members\":[\"_teamsserver\"]},{\"name\":\"_fpsd\",\"gid\":265,\"members\":[\"_fpsd\"]}]";
		List<Group> groups = new ArrayList<Group>();
		groups.add(new Group("mail", 6L, Arrays.asList("_teamsserver")));
		groups.add(new Group("_fpsd", 265L, Arrays.asList("_fpsd")));
		List<String> names = Arrays.asList("mail");
		List<Long> gids = Arrays.asList(265L);
		Mockito.when(groupService.getGroupsByQuery(names, gids, null)).thenReturn(groups);
		RequestBuilder builder = MockMvcRequestBuilders.get("/groups/query?name=mail&gid=265");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void shouldReturnListOfGroupsWithMembers_analyticsdAnd_networkd() throws Exception {
		String expected = "[{\"name\":\"_analyticsd\",\"gid\":263,\"members\":[\"_analyticsd\"]},{\"name\":\"_analyticsusers\",\"gid\":250,\"members\":[\"_analyticsd\",\"_networkd\",\"_timed\"]}]";
		List<Group> groups = new ArrayList<Group>();
		groups.add(new Group("_analyticsd", 263L, Arrays.asList("_analyticsd")));
		groups.add(new Group("_analyticsusers", 250L, Arrays.asList("_analyticsd", "_networkd", "_timed")));
		List<String> members = Arrays.asList("_analyticsd", "_networkd");
		Mockito.when(groupService.getGroupsByQuery(null, null, members)).thenReturn(groups);
		RequestBuilder builder = MockMvcRequestBuilders.get("/groups/query?member=_analyticsd&member=_networkd");
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void shouldReturnListOfGroupsForUid265() throws Exception {
		String expected = "[{\"name\":\"_fpsd\",\"gid\":265,\"members\":[\"_fpsd\"]}]";
		List<Group> groups = new ArrayList<Group>();
		groups.add(new Group("_fpsd", 265L, Arrays.asList("_fpsd")));
		Mockito.when(userGroupService.getUserGroupsByUserId(265L)).thenReturn(groups);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/{id}/groups", 265L);
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void shouldReturnEmptyListOfGroupsForUid1() throws Exception {
		String expected = "[]";
		List<Group> groups = new ArrayList<Group>();
		Mockito.when(userGroupService.getUserGroupsByUserId(1L)).thenReturn(groups);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/{id}/groups", 1L);
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void shouldReturnNullForUid2() throws Exception {
		String expected = "[]";
		List<Group> groups = new ArrayList<Group>();
		Mockito.when(userGroupService.getUserGroupsByUserId(2L)).thenReturn(groups);
		RequestBuilder builder = MockMvcRequestBuilders.get("/users/{id}/groups", 2L);
		MvcResult result = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
}
