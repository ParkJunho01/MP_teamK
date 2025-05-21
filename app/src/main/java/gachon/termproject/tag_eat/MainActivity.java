package gachon.termproject.tag_eat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import gachon.termproject.tag_eat.fragment.BookmarkFragment;
import gachon.termproject.tag_eat.fragment.HomeFragment;
import gachon.termproject.tag_eat.fragment.SearchFragment;
import gachon.termproject.tag_eat.fragment.SettingFragment;
import gachon.termproject.tag_eat.fragment.TagFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);

        nav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selected = new HomeFragment();
            } else if (itemId == R.id.nav_tag) {
                selected = new TagFragment();
            } else if (itemId == R.id.nav_bookmark) {
                selected = new BookmarkFragment();
            } else if (itemId == R.id.nav_search) {
                selected = new SearchFragment();
            } else if (itemId == R.id.nav_setting) {
                selected = new SettingFragment();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, selected)
                        .commit();
            }

            return true;
        });

        // 앱 실행 시 기본 선택은 '홈'
        nav.setSelectedItemId(R.id.nav_home);
    }
}
