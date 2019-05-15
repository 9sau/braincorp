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
import com.saurabh.braincorp.model.Group;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GroupServiceTest {

	@MockBean
	private GroupService groupService;

	@Test
	public void shouldReturnListOfGroups() throws Exception {
		List<Group> expected = new ArrayList<Group>();
		expected.add(new Group("nogroup", -1L, new ArrayList<String>()));
		expected.add(new Group("wheel", 0L, Arrays.asList("root")));
		expected.add(new Group("nobody", -2L, new ArrayList<String>()));
		Mockito.when(groupService.getGroups()).thenReturn(expected);
		List<Group> actual = groupService.getGroups();
		assertTrue(expected.equals(actual));
		assertTrue(expected.size() == actual.size());
	}

	@Test
	public void shouldReturnEmptyListOfGroups() throws Exception {
		List<Group> expected = new ArrayList<Group>();
		Mockito.when(groupService.getGroups()).thenReturn(expected);
		List<Group> actual = groupService.getGroups();
		assertTrue(expected.equals(actual));
		assertTrue(expected.size() == actual.size());
	}

	@Test
	public void shouldReturnGroupWithId0() throws Exception {
		Group expected = new Group("wheel", 0L, Arrays.asList("root"));
		Mockito.when(groupService.getGroupById(0L)).thenReturn(expected);
		Group actual = groupService.getGroupById(0L);
		assertTrue(expected.equals(actual));
		assertTrue(expected.getName().equals(actual.getName()));
		assertTrue(expected.getGid() == actual.getGid());
		assertTrue(expected.getMembers().equals(actual.getMembers()));
	}

	@Test(expected = ResourceNotFoundException.class)
	public void shouldThrowResourceNotFoundExceptionForGroup10() throws Exception {
		Mockito.when(groupService.getGroupById(10L)).thenThrow(new ResourceNotFoundException());
		groupService.getGroupById(10L);
	}

	@Test
	public void shouldReturnListOfGroupsWithNameMailOrGid265() throws Exception {
		List<Group> expected = new ArrayList<Group>();
		expected.add(new Group("mail", 6L, Arrays.asList("_teamsserver")));
		expected.add(new Group("_fpsd", 265L, Arrays.asList("_fpsd")));
		List<String> names = Arrays.asList("mail");
		List<Long> gids = Arrays.asList(265L);
		Mockito.when(groupService.getGroupsByQuery(names, gids, null)).thenReturn(expected);
		List<Group> actual = groupService.getGroupsByQuery(names, gids, null);
		assertTrue(expected.equals(actual));
		assertTrue(expected.size() == actual.size());
	}

	@Test
	public void shouldReturnListOfGroupsWithMembers_analyticsdAnd_networkd() throws Exception {
		List<Group> expected = new ArrayList<Group>();
		expected.add(new Group("_analyticsd", 263L, Arrays.asList("_analyticsd")));
		expected.add(new Group("_analyticsusers", 250L, Arrays.asList("_analyticsd", "_networkd", "_timed")));
		List<String> members = Arrays.asList("_analyticsd", "_networkd");
		Mockito.when(groupService.getGroupsByQuery(null, null, members)).thenReturn(expected);
		List<Group> actual = groupService.getGroupsByQuery(null, null, members);
		assertTrue(expected.equals(actual));
		assertTrue(expected.size() == actual.size());
	}
}
