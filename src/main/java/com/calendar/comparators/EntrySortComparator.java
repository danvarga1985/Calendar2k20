package com.calendar.comparators;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.calendar.domain.Entry;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EntrySortComparator implements Comparator<Entry> {

    /*
    Sort order:
    1:Memo
    2:Event
    3:Task - WIP
    4:Task - UNSTARTED
    5:Task - COMPLETED
    6: Everything else
    */

    private final List<EntryType> entryTypeOrder = Arrays.asList(EntryType.MEMO, EntryType.EVENT, EntryType.TASK);
    private final List<EntryPhase> taskPhaseOrder = Arrays.asList(EntryPhase.WIP, EntryPhase.UNSTARTED, EntryPhase.COMPLETED);

    @Override
    public int compare(Entry e1, Entry e2) {

        EntryType t1 = e1.getEntryType();
        EntryType t2 = e2.getEntryType();
        int indexOfT1 = entryTypeOrder.indexOf(t1);
        int indexOfT2 = entryTypeOrder.indexOf(t2);

        //If neither Entry's entryType is in the entryTypeOrder list
        // 'indexOf' returns with the index-number, or with -1 in case the the list doesn't contain the element
        if (indexOfT1 == -1 && indexOfT2 == -1) {
            return compareByDateOrTitle(e1, e2);
            // If one Entry's EntryType is not in the entryTypeOrder list
        } else if (indexOfT1 == -1) {
            return 1;
        } else if (indexOfT2 == -1) {
            return -1;
        } else {
            int compareTypeResult = indexOfT1 - indexOfT2;

            // If both Entry's type is in the entryTypeOrder list, and are not the same
            if (compareTypeResult != 0) {
                return compareTypeResult;
            } else {
                // If the two Entry has the same type, but aren't TASK
                if (t1 != EntryType.TASK) {
                    return compareByDateOrTitle(e1, e2);
                    //If both Entry is TASK
                } else {
                    int indexOfP1 = taskPhaseOrder.indexOf(e1.getEntryPhase());
                    int indexOfP2 = taskPhaseOrder.indexOf(e2.getEntryPhase());

                    if (indexOfP1 == -1 && indexOfP2 == -1) {
                        return compareByDateOrTitle(e1, e2);
                    }
                    if (indexOfP1 == -1) {
                        return 1;
                    }

                    if (indexOfP2 == -1) {
                        return -1;
                    }

                    int comparePhaseResult = indexOfP1 - indexOfP2;

                    if (comparePhaseResult == 0) {
                        return compareByDateOrTitle(e1, e2);
                    } else {
                        return comparePhaseResult;
                    }
                }
            }
        }
    }

    private int compareByDateOrTitle(Entry e1, Entry e2) {
        if (e1.getDate().isEqual(e2.getDate())) {
            return e1.getTitle().compareTo(e2.getTitle());
        } else {
            return e1.getDate().compareTo(e2.getDate());
        }
    }
}
