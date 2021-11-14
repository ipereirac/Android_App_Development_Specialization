package com.ipereirac.simpleandroidrssreader.interfaces;

import androidx.annotation.NonNull;

import com.ipereirac.simpleandroidrssreader.orm.Entry;

import java.util.ArrayList;
import java.util.List;

public interface UpdateEntriesInterface {

    /**
     * Displays the specified list of entries in the recycler view.
     * @param entries List of entries to display.
     */
    void updateEntries(List<Entry> entries);

    /**
     * Returns the list of currently displayed enteries.
     * @return An array list of entries (possibly empty).
     */
    @NonNull
    ArrayList<Entry> getEntries();
}
