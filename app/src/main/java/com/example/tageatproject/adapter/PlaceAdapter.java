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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String placeId = place.getName() + "_" + place.getAddress();

        // ✅ 초기 상태: 즐겨찾기 여부 표시
        db.collection("users")
                .document(userId)
                .collection("favorites")
                .document(placeId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        holder.favoriteIcon.setImageResource(R.drawable.ic_star);
                    } else {
                        holder.favoriteIcon.setImageResource(R.drawable.ic_star_border);
                    }
                });

        // ✅ 클릭 이벤트: 즐겨찾기 추가/삭제
        holder.favoriteIcon.setOnClickListener(v -> {
            db.collection("users")
                    .document(userId)
                    .collection("favorites")
                    .document(placeId)
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            // 삭제
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
                            // 추가
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

        // 이미지 로딩
        Glide.with(holder.itemView.getContext())
                .load(place.getImageUrl())
                .into(holder.image);

        // 상세 페이지 이동
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlaceDetailActivity.class);
            intent.putExtra("place", place);
            v.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return placeList.size();
    }
}