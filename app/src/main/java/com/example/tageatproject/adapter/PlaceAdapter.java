package com.example.tageatproject.adapter;

import com.bumptech.glide.Glide;
import com.example.tageatproject.PlaceDetailActivity;
import com.example.tageatproject.R;
import com.example.tageatproject.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<Place> placeList;

    public PlaceAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, category;
        ImageView image, favoriteIcon;

        public PlaceViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tv_name);
            address = view.findViewById(R.id.tv_address);
            category = view.findViewById(R.id.tv_category);
            image = view.findViewById(R.id.iv_image);
            favoriteIcon = itemView.findViewById(R.id.iv_favorite);

        }
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.name.setText(place.getName());
        holder.address.setText(place.getAddress());
        holder.category.setText(place.getCategory());
        holder.favoriteIcon.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            String placeId = place.getName() + "_" + place.getAddress();

            db.collection("users")
                    .document(userId)
                    .collection("favorites")
                    .document(placeId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            db.collection("users")
                                    .document(userId)
                                    .collection("favorites")
                                    .document(placeId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        holder.favoriteIcon.setImageResource(R.drawable.ic_star_border);
                                        Log.d("Favorite", "즐겨찾기 해제됨");
                                    });
                        } else {
                            db.collection("users")
                                    .document(userId)
                                    .collection("favorites")
                                    .document(placeId)
                                    .set(place)
                                    .addOnSuccessListener(aVoid -> {
                                        holder.favoriteIcon.setImageResource(R.drawable.ic_star);
                                        Log.d("Favorite", "즐겨찾기 추가됨");
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Favorite", "즐겨찾기 상태 확인 실패", e);
                    });
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlaceDetailActivity.class);
            intent.putExtra("place", place);
            v.getContext().startActivity(intent);
        });


        Glide.with(holder.itemView.getContext())
                .load(place.getImageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }
}
