package com.example.listen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.listen.databinding.FragmentPenyanyiBinding;
public class PenyanyiFragment extends Fragment {
    private FragmentPenyanyiBinding binding;
    private RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPenyanyiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.navmenu.setSelectedItemId(R.id.Penyanyi);
        binding.navmenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Lagu:
                    replaceFragment(new MusikOnFragment());
                    break;
                case R.id.Penyanyi:
                    replaceFragment(new PenyanyiFragment());
                    break;
                case R.id.Album:
                    replaceFragment(new AlbumFragment());
                    break;
            }
            return true;
        });

        relativeLayout = view.findViewById(R.id.relativeLayout3);
        relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Artis.class);
            startActivity(intent);
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}