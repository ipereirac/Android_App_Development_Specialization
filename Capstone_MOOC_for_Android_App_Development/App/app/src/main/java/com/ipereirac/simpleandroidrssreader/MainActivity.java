package com.ipereirac.simpleandroidrssreader;

import static com.ipereirac.simpleandroidrssreader.MainActivity.DownloadStateReceiver.BROADCAST_ACTION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ipereirac.simpleandroidrssreader.interfaces.UpdateEntriesInterface;
import com.ipereirac.simpleandroidrssreader.orm.Entry;
import com.ipereirac.simpleandroidrssreader.services.DownloadAtomFeedService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private static final String STACKOVERFLOW_FEED_URL = "https://api.stackexchange.com/questions?order=desc&sort=activity&site=stackoverflow";
    private DownloadStateReceiver mDownloadStateReceiver;

    private final static int REQUEST_STACKOVERFLOW_ENTRIES = 1;

    private UpdateEntriesInterface updateEntriesInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDownload(Uri.parse(STACKOVERFLOW_FEED_URL));

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            updateEntriesInterface = fragment;
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<Entry> entries = updateEntriesInterface.getEntries();
        outState.putParcelableArrayList("Entries", entries);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (updateEntriesInterface != null) {
            ArrayList<Entry> entries =
                    savedInstanceState.getParcelableArrayList("Entries");
            updateEntriesInterface.updateEntries(entries);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Create an IntentFilter. The filter's action is BROADCAST_ACTION
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);


        // Instantiates a new DownloadStateReceiver
        mDownloadStateReceiver = new DownloadStateReceiver();


        // Registers the DownloadStateReceiver and its intent filters via an
        // istance of LocalBroadcastManager
        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // via the LocalBroadcastManager, unregister the receiver currently registered.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadStateReceiver);

        super.onPause();
    }

    private void startDownload(Uri url) {
        // Create an intent to download the YouTube Atom Feed for CNN.
        // This will involve calling DownloadAtomFeedService's makeIntent.
        // In addition, you will pass REQUEST_YOUTUBE_ENTRIES and
        // the URI parsed version of CNN_YOUTUBE_ATOM_FEED_URL obtained via Uri.parse(...)
        Intent intent = DownloadAtomFeedService.makeIntent(this, REQUEST_STACKOVERFLOW_ENTRIES, url);


        Log.d(TAG,
                "starting the DownloadAtomFeedService for "
                        + url.toString()
        );

        // call startService on that Intent.
        startService(intent);
    }
    
    /**
     * This method handles the data returned from the Intent service. This method will parse out
     * the information from the Bundle passed in, and then act accordingly if the download was
     * successful or not.
     *
     * @param data from the Intent returned by the IntentService.
     */
    private void serviceIntentReceived(Bundle data) {
        // create an 'int' with the name 'resultCode' via calling
        // DownloadAtomFeedService's getDownloadResultsCode(Bundle) method.
        int resultCode = DownloadAtomFeedService.getDownloadResultsCode(data);

        // Otherwise **resultCode == Activity.RESULT_OK**
        // Handle a successful download.
        // Log to both the on-screen & logcat logs the requestUri from the data.


        // Get the Entries from the 'data' and store them.
        // Log to the on-screen and logcat logs the number of entries downloaded.
        ArrayList<Entry> entries = DownloadAtomFeedService.getEntries(data);


        // Update the RecyclerView fragment via calling updateEntries(...) on it.
        updateEntriesInterface.updateEntries(entries);

    }

    public void goToFavorites(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    /**
     * This class defines the BroadcastReceiver that we will use locally within our App
     * to receive an Intent from the Service. This Intent will notify us of the results of the
     * Download operation. The broadcast receiver 'receives' the Intent and then notifies the
     * currently running instance of MainActivity
     */
    public class DownloadStateReceiver extends BroadcastReceiver {
        // Defines a custom Intent action
        public static final String BROADCAST_ACTION =
                "edu.vandy.mooc.aad_3_assg_1.assignment.activities.BROADCAST";

        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        public void onReceive(Context context, Intent intent) {

            // use 'MainActivity.this' to call serviceIntentReceived(Bundle) to notify the
            // instance of MainActivity currently running that the service has returned
            // the results via the BroadcastReceiver. Note: Intent.getExtras will return the
            // Bundle stored with putExtras(Bundle)
            serviceIntentReceived(intent.getExtras());

        }
    }

    public static Intent makeBroadcastReceiverIntent() {
        // Create a new Intent. Then call 'setAction' on it, passing the 'BROADCAST_ACTION'
        // variable from the DownloadStateReceiver class. Then return the new Intent.
        Intent intent= new Intent();
        intent.setAction(BROADCAST_ACTION);
        return intent;
    }
}