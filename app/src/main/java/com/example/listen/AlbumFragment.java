package com.example.listen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listen.Adapter.RecyclerViewAdapter;
import com.example.listen.Model.UploadAlbum2;
import com.example.listen.databinding.FragmentAlbumBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment {
    private FragmentAlbumBinding binding;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private List<UploadAlbum2> uploads;
    int spanCount = 2;
    int verticalSpacing = 16;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.recylcerViewOnAlbum;
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);

        uploads = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("uploads");
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                    UploadAlbum2 upload = postsnapshot.getValue(UploadAlbum2.class);
                    uploads.add(upload);
                }
                adapter = new RecyclerViewAdapter(getContext(), uploads);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Mendapatkan data album yang dipilih
                        UploadAlbum2 selectedAlbum = uploads.get(position);

                        // Membuat Intent untuk berpindah ke JudulAlbum.class
                        Intent intent = new Intent(requireContext(), JudulAlbum.class);
                        intent.putExtra("name", selectedAlbum.getName());
                        intent.putExtra("url", selectedAlbum.getUrl());
                        intent.putExtra("albumId", selectedAlbum.getAlbumId()); // Tambahkan albumId ke Intent
                        // Jika diperlukan, Anda dapat menambahkan data tambahan ke Intent

                        // Memulai aktivitas JudulAlbum
                        startActivity(intent);
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                // Tangani pembatalan operasi database di sini (jika diperlukan)
            }
        });

        binding.navmenu.setSelectedItemId(R.id.Album);
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
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}