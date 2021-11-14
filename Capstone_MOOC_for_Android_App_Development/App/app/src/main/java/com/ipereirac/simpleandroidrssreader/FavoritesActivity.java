package com.ipereirac.simpleandroidrssreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;

import com.ipereirac.simpleandroidrssreader.interfaces.UpdateEntriesInterface;
import com.ipereirac.simpleandroidrssreader.orm.Entry;
import com.ipereirac.simpleandroidrssreader.providers.FeedDBHelper;

import java.util.List;

/**
 * Favorites activity
 */
public class FavoritesActivity extends AppCompatActivity {

    private UpdateEntriesInterface updateEntriesInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            updateEntriesInterface = fragment;
            transaction.replace(R.id.favorites_content_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Entry> list = FeedDBHelper.getFeeds(getApplicationContext());
        updateEntriesInterface.updateEntries(list);
    }
}