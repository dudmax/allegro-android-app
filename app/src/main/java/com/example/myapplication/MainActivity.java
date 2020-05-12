package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.myapplication.offer.Offer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class of Main screen (activity_main.xml).
 * We make list of offers with help of RecycleView
 *
 * In this class we:
 * 1. execute our AsyncTask to get answer from Allegro API
 * 2. In AsyncTask thread we get response from Allegro (class JsonAllegroResponse) and parse this JSON
 * 3. Make offers's array from offer in JSON
 * 4. This array go to constructor of our Adapter of RecycleView
 * 5. When set Listner on item click on our RecycleView
 * 6. And in the end when click event make we create Intent with paramets on our second Activity (item details show)
 */
public class MainActivity extends AppCompatActivity implements AllegroOffersAdapter.OnItemClickListener {

    /**
     * Constants for create paramets for out intent in second Activity
     */
    static final String EXTRA_NAME = "name";
    static final String EXTRA_PRICE = "price";
    static final String EXTRA_DESCRIPTION = "description";
    static final String EXTRA_IMAGE_URL = "thumbnailUrl";


    /**
     * Our uses views from main activity
     */
    private TextView errorView;
    private ProgressBar loadingIndicator;
    private RecyclerView offerListRecyclerView;
    private List<Offer> offerArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorView = findViewById(R.id.tv_error);
        loadingIndicator = findViewById(R.id.pb_bar);

        new AllegroQueryTask().execute();
    }

    /**
     * Async task
     * In this thread
     * 1. In AsyncTask thread we get response from Allegro (class JsonAllegroResponse) and parse this JSON
     * 2. Make offers's array from offer in JSON
     * 3. Sort and filter this array (only element with price between 50 and 1000
     * 3. This array go to constructor of our Adapter of RecycleView
     */
    @SuppressLint("StaticFieldLeak")
    public class AllegroQueryTask extends AsyncTask<Void, Void, String> {


        /**
         * Before out async task start we show for user Progression bar
         */
        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        /**
         * In Background we get response (JSON) from Allegro API
         */
        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            try {
                response = JsonAllegroResponse.getResponseFromURL();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        /**
         * Get JSON is string and parse it on Offer's ArrayList
         * Create Adapter and set on RecycleView
         * @param s - JSON string
         */
        @Override
        protected void onPostExecute(String s) {
            String id, name, thumbnailUrl, currency, description;
            double amount;

            if (s != null && !s.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("offers");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonOffer = jsonArray.getJSONObject(i);

                        id = jsonOffer.getString("id");
                        name = jsonOffer.getString("name");
                        thumbnailUrl = jsonOffer.getString("thumbnailUrl");
                        description = jsonOffer.getString("description");

                        JSONObject price = jsonOffer.getJSONObject("price");
                        amount = Double.parseDouble(price.getString("amount"));
                        currency = price.getString("currency");

                        Offer offer = new Offer(id, name, thumbnailUrl, amount, currency, description);
                        offerArrayList.add(offer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Sort and filter this array (only element with price between 50 and 1000
                offerArrayList = sortAndFilterArrayList(offerArrayList);

                offerListRecyclerView = findViewById(R.id.rv_offers);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                offerListRecyclerView.setLayoutManager(linearLayoutManager);
                offerListRecyclerView.setHasFixedSize(true);

                AllegroOffersAdapter allegroOffersAdapter = new AllegroOffersAdapter(MainActivity.this, offerArrayList);
                offerListRecyclerView.setAdapter(allegroOffersAdapter);
                allegroOffersAdapter.setOnItemClickListener(MainActivity.this);
                showResultTextView();
            }
            else {
                showErrorTextView();
            }
        }
    }

    /**
     * On click on RecycleView item - we get position of item in Array
     * And in the end when click event make we create Intent with paramets on our second Activity (item details show)
     * @param position - position Offer in ArrayList
     */
    @Override
    public void onItemClick(int position) {
        Intent offerIntent = new Intent(this, OfferActivity.class);
        Offer offer = offerArrayList.get(position);

        offerIntent.putExtra(EXTRA_NAME, offer.getName());
        offerIntent.putExtra(EXTRA_DESCRIPTION, offer.getDescription());
        offerIntent.putExtra(EXTRA_IMAGE_URL, offer.getThumbnailUrl());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(offer.getAmount()).append(" ").append(offer.getCurrency());
        offerIntent.putExtra(EXTRA_PRICE, stringBuilder.toString());

        startActivity(offerIntent);
    }

    /**
     * Hide error message and progress bar
     * Show our RecycleView with offers
     */
    private void showResultTextView() {
        offerListRecyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    /**
     * Hide RecycleView and progress bar
     * Show error message
     */
    private void showErrorTextView() {
        offerListRecyclerView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    /**
     * Sort and filter this array (only element with price between 50 and 1000
     * @param arrayList full array list
     * @return sorted and filtered array list
     */
    private List<Offer> sortAndFilterArrayList(List<Offer> arrayList) {
        ArrayList<Offer> newArrayList = new ArrayList<>();
        Collections.sort(arrayList);
        for (Offer offer : arrayList) {
            if (offer.getPrice().getAmount() >=50 && offer.getPrice().getAmount() <= 1000) {
                newArrayList.add(offer);
            }
        }
        return newArrayList;
    }
}

