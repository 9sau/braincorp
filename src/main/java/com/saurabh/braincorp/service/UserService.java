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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.saurabh.braincorp.exception.ResourceNotFoundException;
import com.saurabh.braincorp.model.User;

@Service
public class UserService implements Runnable {

	private final static Map<Long, User> users = new HashMap<Long, User>();

	@Value("${app.passwd.filename:passwd}")
	private String filename;

	@Value("${app.passwd.directory:/private/etc/}")
	private String directory;

	@PostConstruct
	public void initialize() {
		loadUserData(directory + filename);
		new Thread(this).start();
	}

	private void loadUserData(String filename) {
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
			User user = new User();
			String words[] = line.split(":");
			if (words.length > 0) {
				if (words[0] != null)
					user.setName(words[0]);
				if (words[2] != null)
					user.setUid(Long.parseLong(words[2]));
				if (words[3] != null)
					user.setGid(Long.parseLong(words[3]));
				if (words[4] != null)
					user.setComment(words[4]);
				if (words[5] != null)
					user.setHome(words[5]);
				if (words[6] != null)
					user.setShell(words[6]);
				users.put(user.getUid(), user);
			}
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
							loadUserData(directory + filename);
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

	public List<User> getUsers() {
		return new ArrayList<User>(users.values());
	}

	public User getUserById(long id)throws ResourceNotFoundException {
		return users.get(id);
	}

	public List<User> getUsersByQuery(List<String> name, List<Long> uid, List<Long> gid, List<String> comment,
			List<String> home, List<String> shell) {

		return users.entrySet().stream().filter(entry -> {
			User user = entry.getValue();
			if ((name != null && name.indexOf(user.getName()) != -1)
					|| (uid != null && uid.indexOf(user.getUid()) != -1)
					|| (gid != null && gid.indexOf(user.getGid()) != -1)
					|| (comment != null && comment.indexOf(user.getComment()) != -1)
					|| (home != null && home.indexOf(user.getHome()) != -1)
					|| (shell != null && shell.indexOf(user.getShell()) != -1))
				return true;
			return false;
		}).map(map -> map.getValue()).collect(Collectors.toList());
	}

	@Override
	public void run() {
		System.out.println("Thread started");
		watch(directory, filename);
	}
}
