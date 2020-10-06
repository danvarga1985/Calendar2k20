package com.calendar.comparators;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.calendar.domain.Entry;

import java.util.Comparator;

public class EntrySortComparator implements Comparator<Entry> {

    @Override
    public int compare(Entry e1, Entry e2) {
        /*
        Sort order:
        1:Memo
        2:Event
        3:Task - WIP
        4:Task - UNSTARTED
        5:Task - COMPLETED
        6: Everything else
        */

        // If only one is MEMO, then float it above the other
        if (e1.getEntryType() == EntryType.MEMO && e2.getEntryType() != EntryType.MEMO) {
            return 1;
        }

        if (e2.getEntryType() == EntryType.MEMO && e1.getEntryType() != EntryType.MEMO) {
            return -1;
        }


        // If both are MEMO, then sort by Date, if those are equal, then sort by Title
        if (e1.getEntryType() == EntryType.MEMO && e2.getEntryType() == EntryType.MEMO) {
            return compareByDateOrTitle(e1, e2);
        }

        //If only one is EVENT and the other is not EVENT/MEMO, then float it above the other
        if (e1.getEntryType() == EntryType.EVENT && e2.getEntryType() != EntryType.EVENT) {
            return 1;
        }

        if (e2.getEntryType() == EntryType.EVENT && e1.getEntryType() != EntryType.EVENT) {
            return -1;
        }

        //If both are EVENT, then sort by Date, if those are equal, then sort by Title
        if (e1.getEntryType() == EntryType.EVENT && e2.getEntryType() == EntryType.EVENT) {
            return compareByDateOrTitle(e1, e2);
        }

        // Every other scenario applies to every remaining EntryType (currently it's only TASK)
        // If only one is WIP then float it abone the other
        if (e1.getEntryPhase() == EntryPhase.WIP && e2.getEntryPhase() != EntryPhase.WIP ) {
            return 1;
        }

        if (e2.getEntryPhase() == EntryPhase.WIP && e1.getEntryPhase() != EntryPhase.WIP ) {
            return -1;
        }

        // If both EntryPhase is WIP, then sort by Date, if those are equal, then sort by Title
        if (e1.getEntryPhase() == EntryPhase.WIP && e2.getEntryPhase() == EntryPhase.WIP ) {
            return compareByDateOrTitle(e1, e2);
        }

        // If only one is UNSTARTED, then float it above the other
        if (e1.getEntryPhase() == EntryPhase.UNSTARTED && e2.getEntryPhase() != EntryPhase.UNSTARTED) {
            return 1;
        }

        if (e2.getEntryPhase() == EntryPhase.UNSTARTED && e1.getEntryPhase() != EntryPhase.UNSTARTED) {
            return -1;
        }

        // If both are UNSTARTED, then sort by Date, if those are equal, then sort by Title
        if (e1.getEntryPhase() == EntryPhase.UNSTARTED && e2.getEntryPhase() == EntryPhase.UNSTARTED ) {
            return compareByDateOrTitle(e1, e2);
        }

        // If only one is COMPLETED, then float it above the other
        if (e1.getEntryPhase() == EntryPhase.COMPLETED && e2.getEntryPhase() != EntryPhase.COMPLETED) {
            return 1;
        }

        if (e2.getEntryPhase() == EntryPhase.COMPLETED && e1.getEntryPhase() != EntryPhase.COMPLETED) {
            return -1;
        }

        // If both are COMPLETED, then sort by Date, if those are equal, then sort by Title
        if (e1.getEntryPhase() == EntryPhase.COMPLETED && e2.getEntryPhase() == EntryPhase.COMPLETED ) {
            return compareByDateOrTitle(e1, e2);
        }

        // For everything else: sort by date
        else {
            return compareByDateOrTitle(e1, e2);
        }
    }

    private int compareByDateOrTitle(Entry e1, Entry e2) {
        if (e1.getDate().isEqual(e2.getDate())) {
            return e2.getTitle().compareTo(e1.getTitle());
        } else {
            return e2.getDate().compareTo(e1.getDate());
        }
    }
}
