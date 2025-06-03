package com.example.tageatproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tageatproject.adapter.TagAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminTagActivity extends AppCompatActivity {

    private EditText tagInput;
    private Button addTagButton;
    private RecyclerView tagListView;

    private TagAdapter tagAdapter;
    private List<String> tagList = new ArrayList<>();

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tag);

        tagInput = findViewById(R.id.tag_input);
        addTagButton = findViewById(R.id.btn_add_tag);
        tagListView = findViewById(R.id.tag_list);

        db = FirebaseFirestore.getInstance();

        tagAdapter = new TagAdapter(tagList, this::deleteTag); // 삭제 콜백 연결
        tagListView.setLayoutManager(new LinearLayoutManager(this));
        tagListView.setAdapter(tagAdapter);

        loadTags();

        addTagButton.setOnClickListener(v -> {
            String newTag = tagInput.getText().toString().trim();
            if (!newTag.isEmpty()) {
                addTag(newTag);
            }
        });
    }
    private void addTag(String tag) {
        db.collection("tags").document(tag)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Toast.makeText(this, "이미 존재하는 태그입니다", Toast.LENGTH_SHORT).show();
                    } else {
                        db.collection("tags").document(tag)
                                .set(new Tag(tag))
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "태그 추가됨", Toast.LENGTH_SHORT).show();
                                    tagInput.setText("");
                                    loadTags();
                                })
                                .addOnFailureListener(e -> Log.e("AdminTag", "태그 추가 실패", e));
                    }
                });
    }


    private void loadTags() {
        tagList.clear();
        db.collection("tags")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {
                        tagList.add(doc.getId());
                    }
                    tagAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("AdminTag", "태그 불러오기 실패", e));
    }

    private void deleteTag(String tag) {
        db.collection("tags").document(tag)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "태그 삭제됨", Toast.LENGTH_SHORT).show();
                    loadTags();
                })
                .addOnFailureListener(e -> Log.e("AdminTag", "태그 삭제 실패", e));
    }

    public static class Tag {
        public String name;
        public Tag() {} // Firestore용 기본 생성자
        public Tag(String name) {
            this.name = name;
        }
    }
}
