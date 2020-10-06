package com.calendar.service.impl;

import com.calendar.data.enums.EntryType;
import com.calendar.domain.Entry;
import com.calendar.repository.EntryRepository;
import com.calendar.repository.custom.CustomEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EntryServiceImplTest {

    @MockBean
    EntryRepository entryRepository;

    @MockBean
    CustomEntryRepository customEntryRepository;

    @MockBean
    UserServiceImpl userService;

    @InjectMocks
    EntryServiceImpl service;


    @BeforeEach
    void setUp() {

    }

    @DisplayName("Sort Entries")
    @Nested
    class testGetSortedEntries {

        @BeforeEach
        void setUp() {
            Entry memo1 = new Entry();
            memo1.setEntryType(EntryType.MEMO);
            memo1.setDate(LocalDateTime.of(2020, Month.OCTOBER, 01, 12, 00, 01));
            memo1.setTitle("ABC");

            Entry memo2 = new Entry();
            memo1.setEntryType(EntryType.MEMO);
            memo1.setDate(LocalDateTime.of(2020, Month.OCTOBER, 01, 12, 00, 01));
            memo1.setTitle("BAC");

            Entry memo3 = new Entry();
            memo1.setEntryType(EntryType.MEMO);
            memo1.setDate(LocalDateTime.of(2020, Month.OCTOBER, 01, 12, 00, 01));
            memo1.setTitle("ABC");

            List<Entry> entries = new ArrayList<>();
            entries.add(memo1);
            entries.add(memo2);
            entries.add(memo3);

            given(customEntryRepository.getSortedEntries(anyInt(), any(), any()));

        }

        @Test
        void name() {
        }
    }


}
