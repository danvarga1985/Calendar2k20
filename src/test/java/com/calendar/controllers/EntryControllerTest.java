package com.calendar.controllers;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.calendar.responsedto.EntryResponseDto;
import com.calendar.service.impl.EntryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
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

    @BeforeEach
    void setUp() {
        Object[] controllers;
        mockMvc = MockMvcBuilders.standaloneSetup(entryController)
                .setControllerAdvice(new ErrorControllerAdvice())
                .build();
    }

    @Test
    void testGetEntryByIdWhenExists() throws Exception {
        EntryResponseDto responseDto = new EntryResponseDto(1, 1, "ResultDto", "A test Dto",
                LocalDateTime.now(), null, null, EntryType.PROJECT, EntryPhase.WIP, false,
                false, 0, false, true, null, 1,
                "ResultDto");

        // given
        given(entryService.getEntryById(anyInt()))
                .willReturn(responseDto);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/entry?id=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
