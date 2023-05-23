package com.example.listen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;

import com.example.listen.TampilanMusic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusikOffFragment extends Fragment {
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private ListView listView;
    private List<String> songsList;
    private SearchView searchView;
    private ArrayAdapter<String> adapter;
    private MediaPlayer mediaPlayer;
    TampilanMusic tampilanMusic = new TampilanMusic();
    private static final String SONGS_LIST_KEY = "songs_list";
    private List<File> cachedSongsList;
    private ArrayList<File> songFiles; // Deklarasikan variabel baru untuk menyimpan daftar file lagu
    private ArrayList<Integer> songNumbers; // Deklarasikan variabel baru untuk menyimpan nomor lagu

    private boolean isAscending = true;
    private int currentSongIndex = 0;

    private TextView artistTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musikoff, container, false);
        listView = view.findViewById(R.id.listViewSong);
        searchView = view.findViewById(R.id.searchView);
        songsList = new ArrayList<>();
        ImageButton urutkanButton = view.findViewById(R.id.urutkan);
        Button putarSemuaButton = view.findViewById(R.id.btnPutarSemua);
        adapter = new ArrayAdapter<String>(requireContext(), R.layout.list_item_musik, R.id.txtsongname, songsList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textNomor = view.findViewById(R.id.textNomor);
                textNomor.setText(String.valueOf(songNumbers.get(position))); // Mengatur nomor lagu

                return view;
            }
        };
        listView.setAdapter(adapter);

        if (savedInstanceState != null) {
            songsList = savedInstanceState.getStringArrayList(SONGS_LIST_KEY);
            if (songsList != null) {
                adapter.addAll(songsList);
            }
        } else {
            if (cachedSongsList != null) { // Menggunakan data yang sudah diambil sebelumnya jika tersedia
                songsList.clear();
                for (File song : cachedSongsList) {
                    songsList.add(song.getName());
                }
                adapter.notifyDataSetChanged();
            } else {
                checkPermissions();
            }
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Tindakan yang akan dilakukan ketika teks pencarian di-submit
                filterSongs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Tindakan yang akan dilakukan ketika teks pencarian berubah
                filterSongs(newText);
                return true;
            }
        });


        putarSemuaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putarSemuaLagu();
            }
        });
        urutkanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urutkanLagu();
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SONGS_LIST_KEY, new ArrayList<>(songsList));
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                displaySongs();
            }
        } else {
            displaySongs();
        }
    }

    private void filterSongs(String searchText) {
        List<String> filteredSongsList = new ArrayList<>();
        for (String song : songsList) {
            if (song.toLowerCase().contains(searchText.toLowerCase())) {
                filteredSongsList.add(song);
            }
        }
        adapter.clear();
        adapter.addAll(filteredSongsList);
        adapter.notifyDataSetChanged();
    }

    private void urutkanLagu() {
        if (isAscending) {
            // Mengurutkan secara ascending (A-Z)
            Collections.sort(songsList);
            isAscending = false;
        } else {
            // Mengurutkan secara descending (Z-A)
            Collections.sort(songsList, Collections.reverseOrder());
            isAscending = true;
        }
        adapter.notifyDataSetChanged();
    }

    private void displaySongs() {
        if (cachedSongsList != null) {
            songsList.clear();
            songNumbers.clear();
            for (int i = 0; i < cachedSongsList.size(); i++) {
                songsList.add(cachedSongsList.get(i).getName());
                songNumbers.add(i + 1);
            }
            adapter.notifyDataSetChanged();
        } else {
            songFiles = findSongs(Environment.getExternalStorageDirectory());
            cachedSongsList = songFiles;

            songsList.clear();
            songNumbers = new ArrayList<>();
            for (int i = 0; i < songFiles.size(); i++) {
                songsList.add(songFiles.get(i).getName());
                songNumbers.add(i + 1);
            }
            adapter = new ArrayAdapter<String>(requireContext(), R.layout.list_item_musik, R.id.txtsongname, songsList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textNomor = view.findViewById(R.id.textNomor);
                    TextView textArtis = view.findViewById(R.id.ArtisLagu);
                    TextView textDurasi = view.findViewById(R.id.DurasiLagu);
                    textNomor.setText(String.valueOf(songNumbers.get(position))); // Mengatur nomor lagu

                    File songFile = cachedSongsList.get(position);
                    String artist = getArtist(songFile);
                    if (artist != null) {
                        textArtis.setText(artist);
                    } else {
                        textArtis.setText("Tidak diketahui");
                    }
                    String durasi = getDurasi(songFile);
                    textDurasi.setText(durasi);


                    return view;
                }
            };
            listView.setAdapter(adapter);
        }

        currentSongIndex = 0; // Set indeks lagu saat daftar lagu diperbarui

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName = (String) listView.getItemAtPosition(i);
                File songFile = cachedSongsList.get(i);
                String artist = getArtist(songFile);
                if (artist != null) {
                    Intent intent = new Intent(requireContext(), TampilanMusic.class);
                    intent.putExtra("songs", songFiles);
                    intent.putExtra("songname", songName);
                    intent.putExtra("artist", artist);
                    intent.putExtra("pos",i);
                    startActivity(intent);
                } else {
                    // Jika tidak ada informasi artis, tampilkan "Tidak diketahui" ke TampilanMusic
                    Intent intent = new Intent(requireContext(), TampilanMusic.class);
                    intent.putExtra("songs", songFiles);
                    intent.putExtra("songname", songName);
                    intent.putExtra("artist", "Tidak diketahui");
                    intent.putExtra("pos",i);
                    startActivity(intent);
                }
            }
        });
    }
    private String getDurasi(File songFile) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(songFile.getAbsolutePath());
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            long durationInMillis = Long.parseLong(duration);
            int minutes = (int) (durationInMillis / 1000) / 60;
            int seconds = (int) (durationInMillis / 1000) % 60;

            retriever.release();

            return String.format("%02d:%02d", minutes, seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00:00";
    }

    private void putarSemuaLagu() {
        if (!cachedSongsList.isEmpty()) {
            currentSongIndex = 0; // Set indeks lagu ke 0 (awal daftar lagu)

            // Putar lagu pertama
            File songFile = cachedSongsList.get(currentSongIndex);
            playSong(songFile);

            // Tambahkan perulangan untuk memutar lagu berikutnya saat lagu selesai
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentSongIndex++;
                    if (currentSongIndex < cachedSongsList.size()) {
                        File nextSongFile = cachedSongsList.get(currentSongIndex);
                        playSong(nextSongFile);
                    } else {
                        currentSongIndex = 0; // Kembali ke lagu pertama setelah memutar semua lagu
                    }
                }
            });
        }
    }

    private ArrayList<File> findSongs(File file) {
        ArrayList<File> songFiles = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    songFiles.addAll(findSongs(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                        songFiles.add(singleFile);
                    }
                }
            }
        }
        return songFiles;
    }

    private void playSong(File songFile) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(requireContext(), Uri.fromFile(songFile));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getArtist(File songFile) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(songFile.getAbsolutePath());
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            retriever.release();
            return artist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}