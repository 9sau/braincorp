package com.saurabh.braincorp.service;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.saurabh.braincorp.model.Group;

@Service
public class GroupService implements Runnable {

	private final static Map<Long, Group> groups = new HashMap<Long, Group>();

	@Value("${app.group.filename:group}")
	private String filename;

	@Value("${app.group.directory:/private/etc}")
	private String directory;

	@PostConstruct
	public void initialize() {
		loadGroupData(directory + filename);
		new Thread(this).start();
	}

	private void loadGroupData(final String filename) {
		try (Stream<String> stream = Files.lines(Paths.get(filename))) {
			stream.forEach(line -> {
				process(line);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void process(String line) {
		if (line != null && line.charAt(0) != '#') {
			Group group = new Group();
			String words[] = line.split(":");
			if (words.length > 0 && words[0] != null)
				group.setName(words[0]);
			if (words.length > 1 && words[2] != null)
				group.setGid(Long.parseLong(words[2]));
			if (words.length > 3 && words[3] != null)
				group.setMembers(Arrays.asList(words[3].split(",")));
			else
				group.setMembers(new ArrayList<>());

			groups.put(group.getGid(), group);

		}

	}

	public void watch(final String directory, final String filename) {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(directory);
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

			System.out.println("Watch Service registered for dir: " + dir.getFileName());

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException ex) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();

					if (kind == ENTRY_MODIFY) {
						System.out.println(fileName + " MyFile: " + filename + " Directory: " + directory);

						if (fileName.toString().equals(filename)) {
							loadGroupData(directory + filename);
						}
					}
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public List<Group> getGroups() {
		return new ArrayList<Group>(groups.values());
	}

	public List<Group> getGroupsByQuery(List<String> name, List<Long> gid, List<String> member) {
		return groups.entrySet().stream().filter(entry -> {
			Group group = entry.getValue();
			if ((name != null && name.indexOf(group.getName()) != -1)
					|| (gid != null && gid.indexOf(group.getGid()) != -1)
					|| (member != null && !Collections.disjoint(member, group.getMembers())))
				return true;
			return false;
		}).map(map -> map.getValue()).collect(Collectors.toList());
	}

	public Group getGroupById(long id) {
		return groups.get(id);
	}

	@Override
	public void run() {
		System.out.println("Thread started");
		watch(directory, filename);
	}

}
