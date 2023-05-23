package com.example.listen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listen.databinding.FragmentMusikOnBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class MusikOnFragment extends Fragment {
    private FragmentMusikOnBinding binding;
    private RecyclerView recyclerView;
    private List<Song> songsList;
    private SongAdapter adapter;
    private static ArrayList<String> songPaths;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMusikOnBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.navmenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.MusikOn:
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

        recyclerView = binding.listViewSong;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        songsList = new ArrayList<>();
        adapter = new SongAdapter(songsList);
        recyclerView.setAdapter(adapter);

        // Mengambil data lagu dari database Firebase
        FirebaseDatabase.getInstance().getReference("songs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                songsList.clear();
                songPaths = new ArrayList<>();
                int nomorLagu = 1; // Nomor awal lagu
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    Song song = songSnapshot.getValue(Song.class);
                    song.setNomorLagu(nomorLagu); // Mengatur nomor lagu pada objek Song
                    songsList.add(song);
                    songPaths.add(song.getSongLink()); // Menambahkan jalur lagu ke songPaths
                    nomorLagu++; // Meningkatkan nomor lagu untuk lagu berikutnya
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Menangani kegagalan pengambilan data
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public static class Song {
        private String album_art;
        private String artist;
        private String songDuration;
        private String songLink;
        private String songTitle;
        private String songsCategory;
        private int nomorLagu; // Menyimpan nomor lagu

        public Song() {
            // Diperlukan oleh Firebase Database
        }

        public String getAlbum_art() {
            return album_art;
        }

        public void setAlbum_art(String album_art) {
            this.album_art = album_art;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getSongDuration() {
            return songDuration;
        }

        public void setSongDuration(String songDuration) {
            this.songDuration = songDuration;
        }

        public String getSongLink() {
            return songLink;
        }

        public void setSongLink(String songLink) {
            this.songLink = songLink;
        }

        public String getSongTitle() {
            return songTitle;
        }

        public void setSongTitle(String songTitle) {
            this.songTitle = songTitle;
        }

        public String getSongsCategory() {
            return songsCategory;
        }

        public void setSongsCategory(String songsCategory) {
            this.songsCategory = songsCategory;
        }

        public int getNomorLagu() {
            return nomorLagu;
        }

        public void setNomorLagu(int nomorLagu) {
            this.nomorLagu = nomorLagu;
        }
    }

    private class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
        private List<Song> songs;

        public SongAdapter(List<Song> songs) {
            this.songs = songs;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_musik, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Song song = songs.get(position);
            holder.songTitle.setText(song.getSongTitle());
            holder.songArtist.setText(song.getArtist());
            holder.songNumber.setText(String.valueOf(song.getNomorLagu()));

            int durationInMillis = Integer.parseInt(song.getSongDuration());
            String durationText = createTime(durationInMillis);
            holder.songDuration.setText(durationText);

            holder.itemView.setOnClickListener(v -> {
                // Membuka pemutar musik dengan jalur lagu yang dipilih
                Song selectedSong = songsList.get(position);
                String artist = selectedSong.getArtist();
                String songName = selectedSong.getSongTitle();
                String songDuration = selectedSong.getSongDuration();

                Intent intent = new Intent(requireContext(), TampilanMusicOn.class);

                intent.putExtra("songTitle", song.getSongTitle());
                intent.putExtra("songPaths", songPaths);
                intent.putExtra("selectedSongIndex", position);
                intent.putExtra("songname", songName);
                intent.putExtra("artist", artist);
                intent.putExtra("duration", songDuration);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return songs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView songTitle;
            private TextView songArtist;
            private TextView songDuration;
            private TextView songNumber;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                songTitle = itemView.findViewById(R.id.txtsongname);
                songArtist = itemView.findViewById(R.id.ArtisLagu);
                songDuration = itemView.findViewById(R.id.DurasiLagu);
                songNumber = itemView.findViewById(R.id.textNomor); // TextView untuk nomor lagu
            }
        }
    }

    private String createTime(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        time += min + ":";
        if (sec < 10) {
            time += "0";
        }
        time += sec;
        return time;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}