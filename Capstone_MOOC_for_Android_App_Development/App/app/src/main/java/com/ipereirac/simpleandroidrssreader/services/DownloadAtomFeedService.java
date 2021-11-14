package com.ipereirac.simpleandroidrssreader.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipereirac.simpleandroidrssreader.MainActivity;
import com.ipereirac.simpleandroidrssreader.net.Item;
import com.ipereirac.simpleandroidrssreader.orm.Entry;
import com.ipereirac.simpleandroidrssreader.net.SOResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * This service downloads the metadata of the last 15 videos from CNN's YouTube page,
 * converts that information into a List of {@link Entry} objects and returns it via an Intent
 * and Broadcast Receiver.
 */
public class DownloadAtomFeedService extends IntentService {

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Debugging tag used by the Android logger.
     */
    private static final String TAG = DownloadAtomFeedService.class.getCanonicalName();

    /**
     * String constant used to extract the request code.
     */
    private static final String REQUEST_CODE = "REQUEST_CODE";

    /**
     * String constant used to extract the results code.
     */
    private static final String DOWNLOAD_RESULTS = "DOWNLOAD RESULTS";

    /**
     * String constant used to extract the URL to an ATOM Feed from a Bundle.
     */
    private static final String FEED_URL = "FEED_URL";

    /**
     * String constant used to extract the parcelable array of Entry(s) from a Bundle.
     */
    private static final String ENTRY_ARRAY_KEY = "ENTRY_ARRAY_KEY";

    /**
     * Constructor that sets the name of the IntentService.
     */
    public DownloadAtomFeedService() {
        super("DownloadAtomFeedService");
    }

    /**
     * Factory method that returns an explicit Intent for downloading Entry(s) from a YouTube Feed.
     */
    public static Intent makeIntent(Context context, int requestCode, Uri url) {
        // Create an intent that will download the Entry(s) from the web.
        // which involves (1) setting the URL as "data" to the
        // intent, (2) putting the request code as an "extra" to the
        // intent, (3) creating and putting a Messenger as an "extra"
        // to the intent so the DownloadAtomFeedService can send the
        // Entry Object back to the Calling Activity
        Intent intent = new Intent(context, DownloadAtomFeedService.class);
        intent.setData(url)
                .putExtra(REQUEST_CODE, requestCode)
                .putExtra("message", new Messenger(new Handler()));
        return intent;
    }

    /**
     * Helper method to ease in using this class by localizing values needed for extracting data
     * from reply messages. Helps in getting the Entry(s) from the Bundle.
     *
     * @param data Bundle originally returned by this Service, which contains the Entry(s).
     * @return ArrayList of Entry(s)
     */
    public static ArrayList<Entry> getEntries(@NonNull Bundle data) {
        // we give you this method since it isn't covered in full detail since it is beyond the
        // scope of this course.
        return data.getBundle("reply").getParcelableArrayList(ENTRY_ARRAY_KEY);
    }

    /**
     * Hook method dispatched by the IntentService framework to
     * download the YouTube feed requested via data in an intent,
     * and return the Entry(s) back to the MainActivity via a
     * broadcasted Intent.
     */
    @Override
    public void onHandleIntent(Intent intent) {
        // Get the URL associated with the Intent data.
        String url = intent.getData().toString();


        // Download the requested YouTube Atom Feed.
        List<Entry> result = downloadAtomFeed(url);

        // Extract the request code.
        int requestCode = intent.getIntExtra(REQUEST_CODE, 0);


        // Send the YouTube Atom Feed Entries back to the
        // MainActivity via the messenger via the sendEntries(...) method.
        sendEntries(new ArrayList<>(result), Uri.parse(url), requestCode);
    }

    /**
     * Send the pathname back to the MainActivity via the
     * messenger.
     */
    private void sendEntries(ArrayList<Entry> entries,
                             Uri url,
                             int requestCode) {
        // Call the makeReplyBundle(...) method to create a new Bundle containing the results.
        Bundle replayBundle = makeReplyBundle(entries, url, requestCode);


        // Create a new Intent via calling MainActivity's makeBroadcastReceiverIntent()
        // Then, store the newly created bundle in the Intent via putExtras(...)
        Intent intent = MainActivity.makeBroadcastReceiverIntent();
        intent.putExtra("reply", replayBundle);

        // Now we will need to launch the Intent containing the results Bundle.
        // Get an instance of LocalBroadcastManager to send the Broadcast of the Intent via
        // sendBroadcast(...).
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * A factory method that creates a Bundle to return to the
     * MainActivity with the list of Entry(s) downloaded.
     */
    private Bundle makeReplyBundle(ArrayList<Entry> entries,
                                   Uri url,
                                   int requestCode) {

        // Create a new Bundle named 'data' to handle the result.
        Bundle data = new Bundle();


        // use 'putParcelableArrayList(...)' to store the ArrayList of Entry(s) in the bundle.
        data.putParcelableArrayList(ENTRY_ARRAY_KEY, entries);


        // Put the requestCode into the Bundle via the REQUEST_CODE key.
        data.putString(FEED_URL, url.toString());


        // Put the requestCode into the Bundle via the REQUEST_CODE key.
        data.putInt(REQUEST_CODE, requestCode);


        // Set a field in the Message to indicate whether the download
        // succeeded or failed.
        // Check if entries == null, if it is store Activity.RESULT_CANCELED otherwise store
        // Activity.RESULT_OK as an int with the key 'DOWNLOAD_RESULTS'
        if (entries == null) {
            data.putInt(DOWNLOAD_RESULTS, Activity.RESULT_CANCELED);
        }else{
            data.putInt(DOWNLOAD_RESULTS, Activity.RESULT_OK);
        }


        // return the bundle 'data'.
        return data;
    }

    /**
     * Extract the results code from the bundle originally created by this service.
     *
     * @param bundle the bundle originally created by this Service containing the results code
     * @return the download results code
     */
    public static int getDownloadResultsCode(Bundle bundle) {
        return bundle.getInt(DOWNLOAD_RESULTS);
    }

    /*
     * These Helper Methods are provided for you, so that you can focus on implementing the
     * service, and not have to worry about the specifics of downloading and parsing an ATOM feed
     * from YouTube.
     */

    /**
     * Download the YouTube AtomFeed and generate the Entry(s) that were in the Feed.
     *
     * @param feedURL The url of the YouTube feed
     * @return a List of type Entry that were in the Feed url provided.
     */
    private List<Entry> downloadAtomFeed(final String feedURL) {
        android.util.Log.d(TAG, "downloadAtomFeed()");
        final List<Entry> entries = new ArrayList<>();

        // Download Entries
        try {
            // Make sure that this thread has not been interrupted.
            checkHaMeRThreadNotInterupted();
            // download and process the feed from the given url.
            List<Item> netEntries = downloadNetEntries(feedURL);
            Log.d(TAG, "netEntries size = " + netEntries.size());
            List<Entry> newEntries = convertToOrmEntries(netEntries);
            Log.d(TAG, "entries size = " + newEntries.size());
            entries.addAll(newEntries);
            // check that thread wasn't interrupted again before returning results.
            checkHaMeRThreadNotInterupted();
        } catch (Exception ex) {
            Log.e(TAG, "Exception downloading Atom Feed: " + ex.getMessage());
        }

        // Return the List<Entry> return object created above.
        return entries;
    }

    /**
     * Helper method to check if thread has been interrupted & throw exception if it has
     *
     * @throws Exception Thrown Exception notifying that the HaMeR Thread was interrupted.
     */
    private void checkHaMeRThreadNotInterupted() throws Exception {
        if (Thread.currentThread().isInterrupted()) {
            Log.d(TAG, "HaMer thread interrupted");
            throw new Exception("HaMer thread interrupted, halting execution.");
        }
    }

    /**
     * Helper Method to convert from "NetEntry"(s) to "Entry"(s). We have two different POJO
     * (Plain Old Java Object(Classes that do not inherit from anything othe than Object))
     * Classes because this allows for better de-coupling of our internal 'Entry's and external
     * (Web) 'Entry's. This allows us to more easily make changes to either one of the two
     * classes in the future if need be without impacting the other.
     *
     * @param netEntries ArrayList of NetEntry(s) to convert to ArrayList of Entry(s)
     * @return converted ArrayList of Entry(s)
     */
    private List<Entry> convertToOrmEntries(List<Item> netEntries) {
        // Create an empty ArrayList of type "Entry".
        List<Entry> entries = new ArrayList<>();
        // Loop through each 'NetEntry', and convert it to an 'Entry' object.
        for (Item netEntry : netEntries) {
            entries.add(
                    new Entry(netEntry.getQuestion_id(),
                            netEntry.getTitle()
                    )
            ); // end of 'add'
        } // end of loop through each NetEntry
        // return the ArrayList of newly created 'Entry' Objects.
        return entries;
    }

    /**
     * Download the NetEntries from a provided URL
     *
     * @param urlString the url to grab the ATOM feed from.
     * @return the NetEntrys that were in the ATOM feed.
     * @throws IOException
     * @throws XmlPullParserException
     */
    @SuppressWarnings("ThrowFromFinallyBlock")
    private List<Item> downloadNetEntries(final String urlString)
            throws IOException, XmlPullParserException {

        Request request = new Request.Builder()
                .url(urlString)
                .build();
        SOResponse soResponse ;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            ObjectMapper mapper=new ObjectMapper();
            soResponse = mapper.readValue(response.body().string() , SOResponse.class);

            Log.d(TAG,soResponse.toString());

        }
        return soResponse.getItems();
    }
}