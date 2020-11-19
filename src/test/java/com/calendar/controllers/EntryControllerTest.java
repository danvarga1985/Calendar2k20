package com.calendar.controllers;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.calendar.domain.Entry;
import com.calendar.exceptions.EntryNotFoundException;
import com.calendar.responsedto.EntryResponseDto;
import com.calendar.responsedto.FullProjectResponseDto;
import com.calendar.responsedto.ProjectEntriesResponseDto;
import com.calendar.service.impl.EntryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("EntryController Test - ")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EntryControllerTest {

    private MockMvc mockMvc;

    @Mock
    EntryServiceImpl entryService;

    @InjectMocks
    EntryController entryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(entryController)
                .setControllerAdvice(new ErrorControllerAdvice())
                .build();
    }

    @DisplayName("GET Tests - ")
    @Nested
    class GetTests {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm");
        LocalDateTime deadline = LocalDateTime.parse("2019-03-27@10:15", formatter);
        LocalDateTime date = LocalDateTime.parse("2019-02-12@09:15", formatter);

        @DisplayName("GetEntryByIdTests")
        @Nested
        class GetOwnerByIdTests {

            EntryResponseDto responseDto;


            @BeforeEach
            public void setUp() {

                responseDto = new EntryResponseDto(1, 1, "ResultDto", "A test Dto",
                        date, null, deadline, EntryType.PROJECT, EntryPhase.WIP, false,
                        false, 0, false, true, null, 1,
                        "ResultDto");

            }

            @Test
            public void testGetEntryByIdWhenExists() throws Exception {
                // given
                given(entryService.getEntryById(1))
                        .willReturn(responseDto);

                mockMvc.perform(get("/entry?id=1")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id", is(responseDto.getId())))
                        .andExpect(jsonPath("$.userId", is(responseDto.getUserId())))
                        .andExpect(jsonPath("$.title", is(responseDto.getTitle())))
                        .andExpect(jsonPath("$.description", is(responseDto.getDescription())))
                        .andExpect(jsonPath("$.date", is(formatter.format(responseDto.getDate()))))
                        .andExpect(jsonPath("$.duration", is(responseDto.getDuration())))
                        .andExpect(jsonPath("$.deadline", is(formatter.format(responseDto.getDeadline()))))
                        .andExpect(jsonPath("$.entryType", is(responseDto.getEntryType().toString())))
                        .andExpect(jsonPath("$.entryPhase", is(responseDto.getEntryPhase().toString())))
                        .andExpect(jsonPath("$.child", is(responseDto.isChild())))
                        .andExpect(jsonPath("$.closed", is(responseDto.isClosed())))
                        .andExpect(jsonPath("$.sortNumber", is(responseDto.getSortNumber())))
                        .andExpect(jsonPath("$.deleted", is(responseDto.isDeleted())))
                        .andExpect(jsonPath("$.expanded", is(responseDto.isExpanded())))
                        .andExpect(jsonPath("$.parentId", is(responseDto.getParentId())))
                        .andExpect(jsonPath("$.projectId", is(responseDto.getProjectId())))
                        .andExpect(jsonPath("$.projectTitle", is(responseDto.getProjectTitle())))
                        .andReturn();
            }

            @Test
            public void testGetEntryByIdWhenDoesNotExist() throws Exception {
                // given
                given(entryService.getEntryById(2))
                        .willThrow(new EntryNotFoundException("Entry not found!"));

                mockMvc.perform(
                        get("/entry/?id=2")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.code", is(404)))
                        .andExpect(jsonPath("$.status", is("Not Found")))
                        .andReturn();

            }
        }

        @DisplayName("GetProjectsTests")
        @Nested
        class GetProjectsTests {

            ProjectEntriesResponseDto projectResponse1;
            ProjectEntriesResponseDto projectResponse2;
            ArrayList<ProjectEntriesResponseDto> projects;

            @BeforeEach
            public void setUp() {
                projectResponse1 = new ProjectEntriesResponseDto(1, "ProjectResult1", true, 0);
                projectResponse2 = new ProjectEntriesResponseDto(2, "ProjectResult2", false, 1);
                projects = new ArrayList<>();
                projects.add(projectResponse1);
                projects.add(projectResponse2);
            }

            @Test
            void testGetProjectsFoundTwo() throws Exception {
                given(entryService.getProjects())
                        .willReturn(projects);

                mockMvc.perform(get("/projects").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("[0].entryId", is(projectResponse1.getEntryId())))
                        .andExpect(jsonPath("[0].title", is(projectResponse1.getTitle())))
                        .andExpect(jsonPath("[0].closed", is(projectResponse1.isClosed())))
                        .andExpect(jsonPath("[0].sortNumber", is(projectResponse1.getSortNumber())))
                        .andExpect(jsonPath("[1].entryId", is(projectResponse2.getEntryId())))
                        .andExpect(jsonPath("[1].title", is(projectResponse2.getTitle())))
                        .andExpect(jsonPath("[1].closed", is(projectResponse2.isClosed())))
                        .andExpect(jsonPath("[1].sortNumber", is(projectResponse2.getSortNumber())));
            }

            @Test
            void testGetProjectsFoundNone() throws Exception {
                given(entryService.getProjects())
                        .willReturn(new ArrayList<ProjectEntriesResponseDto>());

                mockMvc.perform(get("/projects").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(0)));
            }
        }

        @DisplayName("GetFullProjectByIdTests")
        @Nested
        class GetFullProjectByIdTests {
            FullProjectResponseDto fullProjectResponseDto;

            @Test
            void testGetFullProjectByIdHasChildEntries() throws Exception {
                Set<Entry> childEntries = new HashSet<>();
                Entry entry1 = new Entry("ResultDto", "A test Dto", date, null, deadline,
                        EntryType.TASK, EntryPhase.WIP);
                Entry entry2 = new Entry("SecondDto", "The second test Dto", date, null,
                        deadline, EntryType.EVENT, EntryPhase.UNSTARTED);
                childEntries.add(entry1);
                childEntries.add(entry2);

                fullProjectResponseDto = new FullProjectResponseDto(1, 1, "TestProject",
                        "Big Test Project", date, null, deadline, EntryType.PROJECT, EntryPhase.WIP,
                        false, false, false, 0, true, childEntries);

                given(entryService.getFullProjectById(5))
                        .willReturn(fullProjectResponseDto);

                mockMvc.perform(get("/project/?id=5").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id", is(fullProjectResponseDto.getId())))
                        .andExpect(jsonPath("$.userId", is(fullProjectResponseDto.getUserId())))
                        .andExpect(jsonPath("$.title", is(fullProjectResponseDto.getTitle())))
                        .andExpect(jsonPath("$.description", is(fullProjectResponseDto.getDescription())))
                        .andExpect(jsonPath("$.date", is(formatter.format(fullProjectResponseDto.getDate()))))
                        .andExpect(jsonPath("$.duration", is(fullProjectResponseDto.getDuration())))
                        .andExpect(jsonPath("$.deadline", is(formatter.format(fullProjectResponseDto
                                .getDeadline()))))
                        .andExpect(jsonPath("$.entryType", is(fullProjectResponseDto.getEntryType()
                                .toString())))
                        .andExpect(jsonPath("$.entryPhase", is(fullProjectResponseDto.getEntryPhase()
                                .toString())))
                        .andExpect(jsonPath("$.child", is(fullProjectResponseDto.isChild())))
                        .andExpect(jsonPath("$.closed", is(fullProjectResponseDto.isClosed())))
                        .andExpect(jsonPath("$.deleted", is(fullProjectResponseDto.isDeleted())))
                        .andExpect(jsonPath("$.sortNumber", is(fullProjectResponseDto.getSortNumber())))
                        .andExpect(jsonPath("$.expanded", is(fullProjectResponseDto.isExpanded())))
                        .andExpect(jsonPath("$.childEntries", hasSize(2)))
                        .andExpect(jsonPath("$.childEntries[0].title", is(entry1.getTitle())))
                        .andExpect(jsonPath("$.childEntries[0].description", is(entry1.getDescription())))
                        .andExpect(jsonPath("$.childEntries[0].date",
                                is(formatter.format(entry1.getDate()))))
                        .andExpect(jsonPath("$.childEntries[0].duration", is(entry1.getDuration())))
                        .andExpect(jsonPath("$.childEntries[0].deadline",
                                is(formatter.format(entry1.getDeadline()))))
                        .andExpect(jsonPath("$.childEntries[0].entryType", is(entry1.getEntryType()
                                .toString())))
                        .andExpect(jsonPath("$.childEntries[0].entryPhase", is(entry1.getEntryPhase()
                                .toString())))
                        .andExpect(jsonPath("$.childEntries[0].child", is(entry1.isChild())))
                        .andExpect(jsonPath("$.childEntries[0].closed", is(entry1.isClosed())))
                        .andExpect(jsonPath("$.childEntries[0].deleted", is(entry1.isDeleted())))
                        .andExpect(jsonPath("$.childEntries[0].expanded", is(entry1.isExpanded())))
                        .andExpect(jsonPath("$.childEntries[1].title", is(entry2.getTitle())))
                        .andExpect(jsonPath("$.childEntries[1].description", is(entry2.getDescription())))
                        .andExpect(jsonPath("$.childEntries[1].date",
                                is(formatter.format(entry2.getDate()))))
                        .andExpect(jsonPath("$.childEntries[1].duration", is(entry2.getDuration())))
                        .andExpect(jsonPath("$.childEntries[1].deadline",
                                is(formatter.format(entry2.getDeadline()))))
                        .andExpect(jsonPath("$.childEntries[1].entryType", is(entry2.getEntryType()
                                .toString())))
                        .andExpect(jsonPath("$.childEntries[1].entryPhase", is(entry2.getEntryPhase()
                                .toString())))
                        .andExpect(jsonPath("$.childEntries[1].child", is(entry2.isChild())))
                        .andExpect(jsonPath("$.childEntries[1].closed", is(entry2.isClosed())))
                        .andExpect(jsonPath("$.childEntries[1].deleted", is(entry2.isDeleted())))
                        .andExpect(jsonPath("$.childEntries[1].expanded", is(entry2.isExpanded())))
                        .andReturn();
            }

            @Test
            void testGetFullProjectByIdHasNoChildEntries() throws Exception {
                fullProjectResponseDto = new FullProjectResponseDto(1, 1, "TestProject",
                        "Big Test Project", date, null, deadline, EntryType.PROJECT, EntryPhase.WIP,
                        false, false, false, 0, true, new HashSet<Entry>());

                given(entryService.getFullProjectById(5))
                        .willReturn(fullProjectResponseDto);

                mockMvc.perform(get("/project/?id=5").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id", is(fullProjectResponseDto.getId())))
                        .andExpect(jsonPath("$.userId", is(fullProjectResponseDto.getUserId())))
                        .andExpect(jsonPath("$.title", is(fullProjectResponseDto.getTitle())))
                        .andExpect(jsonPath("$.description", is(fullProjectResponseDto.getDescription())))
                        .andExpect(jsonPath("$.date", is(formatter.format(fullProjectResponseDto.getDate()))))
                        .andExpect(jsonPath("$.duration", is(fullProjectResponseDto.getDuration())))
                        .andExpect(jsonPath("$.deadline", is(formatter.format(fullProjectResponseDto
                                .getDeadline()))))
                        .andExpect(jsonPath("$.entryType", is(fullProjectResponseDto.getEntryType()
                                .toString())))
                        .andExpect(jsonPath("$.entryPhase", is(fullProjectResponseDto.getEntryPhase()
                                .toString())))
                        .andExpect(jsonPath("$.child", is(fullProjectResponseDto.isChild())))
                        .andExpect(jsonPath("$.closed", is(fullProjectResponseDto.isClosed())))
                        .andExpect(jsonPath("$.deleted", is(fullProjectResponseDto.isDeleted())))
                        .andExpect(jsonPath("$.sortNumber", is(fullProjectResponseDto.getSortNumber())))
                        .andExpect(jsonPath("$.expanded", is(fullProjectResponseDto.isExpanded())))
                        .andExpect(jsonPath("$.childEntries", hasSize(0)))
                        .andReturn();
            }
        }


    }


}
