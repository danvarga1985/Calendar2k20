package com.calendar.bootstrap;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.calendar.domain.Entry;
import com.calendar.domain.User;
import com.calendar.repository.EntryRepository;
import com.calendar.repository.UserRepository;
import com.calendar.requestdto.ProjectDto;
import com.calendar.requestdto.RegistrationDto;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
public class DBInit  implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;
    private EntryRepository entryRepository;
    private PasswordEncoder passwordEncoder;

    public DBInit(UserRepository userRepository, EntryRepository entryRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.entryRepository = entryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.count() == 0) {
            loadUsers();
        }

        if (entryRepository.count() == 0) {
            loadEntries();
        }
    }

    private void loadUsers() {
        //Create a test user
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setEmail("testuser@testuser.com");
        registrationDto.setPassword("Test001");

        User user = new User(
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                validationTokenGeneration());

        userRepository.save(user);

    }

    private void loadEntries() {
        //Create first project
        ProjectDto projectDto = new ProjectDto();
        projectDto.setTitle("Project Entry 1");
        projectDto.setDescription("First Project");
        projectDto.setDeadline(LocalDateTime.now());

        Entry firstProject = new Entry(projectDto.getTitle(), projectDto.getDescription(), null, null,
                projectDto.getDeadline(),
                EntryType.PROJECT, EntryPhase.WIP);

        firstProject.setUserId(1);
        firstProject.setSortNumber(0);

        entryRepository.save(firstProject);

        //Create first child-entry
        Entry firstChildEntry = new Entry(
                "Child Entry 1",
                "Fist child entry of the first project",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2),
                EntryType.TASK,
                EntryPhase.WIP);

        firstChildEntry.setUserId(1);
        firstChildEntry.setParentEntry(firstProject);
        firstChildEntry.setChild(true);
        firstChildEntry.setSortNumber(0);

        entryRepository.save(firstChildEntry);

        //Create second child-entry
        Entry secondChildEntry = new Entry(
                "Child Entry 2",
                "Second child entry of the first project",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3),
                EntryType.TASK,
                EntryPhase.COMPLETED);

        secondChildEntry.setUserId(1);
        secondChildEntry.setParentEntry(firstProject);
        secondChildEntry.setChild(true);
        secondChildEntry.setSortNumber(1);
        secondChildEntry.setClosed(true);
        secondChildEntry.setExpanded(false);

        entryRepository.save(secondChildEntry);

        //Create third child-entry
        Entry thirdChildEntry = new Entry(
                "Child Entry 3",
                "First child entry of the first child entry.",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(4),
                EntryType.TASK,
                EntryPhase.UNSTARTED);

        thirdChildEntry.setUserId(1);
        thirdChildEntry.setParentEntry(firstChildEntry);
        thirdChildEntry.setChild(true);
        thirdChildEntry.setSortNumber(0);

        entryRepository.save(thirdChildEntry);

        //Create second project
        ProjectDto projectDto2 = new ProjectDto();
        projectDto2.setTitle("Project Entry 2");
        projectDto2.setDescription("Second Project");
        projectDto2.setDeadline(LocalDateTime.now());

        Entry secondProject = new Entry(projectDto2.getTitle(), projectDto2.getDescription(), null, null,
                projectDto2.getDeadline(),
                EntryType.PROJECT, EntryPhase.COMPLETED);

        secondProject.setUserId(1);
        secondProject.setSortNumber(1);
        secondProject.setClosed(true);
        secondProject.setExpanded(false);

        entryRepository.save(secondProject);

    }

    private String validationTokenGeneration() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String token = bytes.toString();
        return token;
    }
}
