package com.example.listen;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity {

    private MeowBottomNavigation meowBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meowBottomNavigation = findViewById(R.id.meowBottom);

        //add item menu
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.musikonline));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.musicksaya));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.playlist));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.akun));
        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;

                switch (item.getId()){
                    case 1 :
                        fragment = new MusikOnFragment();
                        break;
                    case 2 :
                        fragment = new MusikOffFragment();
                        break;
                    case 3 :
                        fragment = new PlaylistFragment();
                        break;
                    case 4 :
                        fragment = new ProfileFragment();
                        break;
                }

                loadFragment(fragment);
            }
        });


        //set default
        meowBottomNavigation.show(1, true);

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }
}