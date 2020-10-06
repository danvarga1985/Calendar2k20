package com.calendar.comparators;

import com.calendar.data.enums.EntryType;
import com.calendar.domain.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

class EntrySortComparatorTest {

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

    }

    @Test
    void compare() {
    }
}
