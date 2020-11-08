package com.calendar.controllers;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.calendar.exceptions.EntryNotFoundException;
import com.calendar.responsedto.EntryResponseDto;
import com.calendar.service.impl.EntryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EntryControllerTest {

    private MockMvc mockMvc;

    @Mock
    EntryServiceImpl entryService;

    @InjectMocks
    EntryController entryController;

    private JacksonTester<EntryResponseDto> jsonEntryDto;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(entryController)
                .setControllerAdvice(new ErrorControllerAdvice())
                .build();
    }

    @Test
    void testGetEntryByIdWhenExists() throws Exception {
        // TODO: implement date-formatting
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm");
//        String time1 = "2019-03-27@10:15";
//        LocalDateTime localTimeObj1 = LocalDateTime.parse(time1, formatter);

        EntryResponseDto responseDto = new EntryResponseDto(1, 1, "ResultDto", "A test Dto",
                null, null, null, EntryType.PROJECT, EntryPhase.WIP, false,
                false, 0, false, true, null, 1,
                "ResultDto");

        // given
        given(entryService.getEntryById(1))
                .willReturn(responseDto);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/entry?id=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotEmpty();
        assertThat(response.getContentAsString()).isEqualTo(jsonEntryDto.write(new EntryResponseDto(1, 1,
                "ResultDto", "A test Dto", null, null, null,
                EntryType.PROJECT, EntryPhase.WIP, false, false, 0, false,
                true, null, 1, "ResultDto")).getJson());
    }

    @Test
    public void testGetEntryByIdWhenDoesNotExist() throws Exception {
        // given
        given(entryService.getEntryById(2))
                .willThrow(new EntryNotFoundException("Entry not found!"));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/entry/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    void testGetProjects() {

    }
}
