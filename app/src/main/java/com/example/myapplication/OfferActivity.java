package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Second activity, screen for show detail information of Offer
 * get parameters from main activity and set their in view.
 * Activity also parse HTML text from offer's description
 */
public class OfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        //show return button on title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        String name = intent.getStringExtra(MainActivity.EXTRA_NAME);
        String description = intent.getStringExtra(MainActivity.EXTRA_DESCRIPTION);
        String imageUrl = intent.getStringExtra(MainActivity.EXTRA_IMAGE_URL);
        String price = intent.getStringExtra(MainActivity.EXTRA_PRICE);

        ImageView imageView = findViewById(R.id.image_view_offer_activity);
        TextView textViewPrice = findViewById(R.id.text_view_offer_activity_price);
        TextView textViewDescription = findViewById(R.id.text_view_offer_activity_description);

        setTitle(name);
        Picasso.get().load(imageUrl).into(imageView);
        textViewPrice.setText(price);

        //parse and show HTML text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewDescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textViewDescription.setText(Html.fromHtml(description));
        }
    }

    /**
     * Set functional for return button in title bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
