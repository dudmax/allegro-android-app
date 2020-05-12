package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.offer.Offer;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Adapter class for Offers's RecycleView
 * In this adapter we create a ViewHolder for RecycleView,
 * create on item click listener and set him on View Holder
 */
public class AllegroOffersAdapter extends RecyclerView.Adapter<AllegroOffersAdapter.AllegroOfferViewHolder> {
    private List<Offer> offerList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AllegroOffersAdapter(Context context, List<Offer> offerList) {
        this.context = context;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public AllegroOfferViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutIdOfferListView = R.layout.offer_list_item;

        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(layoutIdOfferListView, viewGroup, false);

        return new AllegroOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllegroOfferViewHolder allegroOfferViewHolder, int i) {
        Offer offerItem = this.offerList.get(i);
        StringBuilder price = new StringBuilder();
        price.append(offerItem.getAmount()).append(" ").append(offerItem.getCurrency());
        allegroOfferViewHolder.bind(offerItem.getName(), price.toString(), offerItem.getThumbnailUrl());
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    /**
     * ViewHolder of offer for our RecycleView
     */
    class AllegroOfferViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName;
        TextView textViewPrice;

        /**
         * Create view holder and set on click listener
         * @param itemView
         */
        AllegroOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_offer_holder);
            textViewName = itemView.findViewById(R.id.text_view_offer_holder_name);
            textViewPrice = itemView.findViewById(R.id.text_view_offer_holder_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        /**
         * bind information in view of view holder
         * @param name
         * @param price
         * @param imageUrl
         */
        void bind(String name, String price, String imageUrl) {
            textViewName.setText(name);
            textViewPrice.setText(price);
            Picasso.get().load(imageUrl).into(imageView);
        }
    }
}
