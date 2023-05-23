package com.example.listen;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView textViewImage, title, artist, album, durations, dataa;
    ProgressBar progressBar;
    Uri audioUri;
    StorageReference mStorageref;
    StorageTask mUploadTask;
    DatabaseReference refenceSongs;
    String songsCategory, title1, artist1, album_art1 = "", durations1;
    MediaMetadataRetriever mediaMetadataRetriever;
    byte[] art;
    ImageView album_art;
    private String selectedAlbum;
    private String selectedAlbumId;
    String url;
    String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textViewImage = findViewById(R.id.textViewSongs);
        progressBar = findViewById(R.id.progressBar);
        title = findViewById(R.id.title1);
        artist = findViewById(R.id.artist);
        durations = findViewById(R.id.duration);
        album = findViewById(R.id.album);
        dataa = findViewById(R.id.dataa);
        album_art = findViewById(R.id.imageViewOn);


        mediaMetadataRetriever = new MediaMetadataRetriever();
        refenceSongs = FirebaseDatabase.getInstance().getReference().child("songs");
        mStorageref = FirebaseStorage.getInstance().getReference().child("songs");

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<>();
        categories.add("Love songs");
        categories.add("Sad songs");
        categories.add("Party songs");
        categories.add("Birthday Songs");
        categories.add("Good song");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        songsCategory = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, "Selected :" + songsCategory, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    public void openAlbumUpload(View view) {
        // Lakukan tindakan yang diperlukan untuk pindah ke UploadAlbum
        Intent intent = new Intent(MainActivity2.this, UploadAlbum.class);
        startActivity(intent);
    }

    public void openAudioFiles(View v) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i, 101);
    }

    public void pilihAlbum(View v) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> albumList = new ArrayList<>();
                for (DataSnapshot albumSnapshot : dataSnapshot.getChildren()) {
                    String albumName = albumSnapshot.getKey();
                    albumList.add(albumName);
                }
                showAlbumDialog(albumList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }

    private void showAlbumDialog(List<String> albumList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        builder.setTitle("Pilih Album")
                .setItems(albumList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectedAlbum = albumList.get(which);
                        TextView textAlbumYangDipilih = findViewById(R.id.textAlbumYangDipilih);
                        textAlbumYangDipilih.setText("Album yang dipilih: " + selectedAlbum);

                        // Ambil albumId berdasarkan indeks yang dipilih
                        DatabaseReference albumRef = FirebaseDatabase.getInstance().getReference("uploads");
                        albumRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int index = 0;
                                for (DataSnapshot albumSnapshot : dataSnapshot.getChildren()) {
                                    if (index == which) {
                                        selectedAlbumId = albumSnapshot.getKey();
                                        break;
                                    }
                                    index++;
                                }

                                TextView textAlbumYangDipilih = findViewById(R.id.textAlbumYangDipilih);
                                textAlbumYangDipilih.setText("Album yang dipilih: " + selectedAlbum);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle database error if needed
                            }
                        });
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data.getData() != null) {
            audioUri = data.getData();
            String fileNames = getFileName(audioUri);
            textViewImage.setText(fileNames);
            mediaMetadataRetriever.setDataSource(this, audioUri);

            art = mediaMetadataRetriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            album_art.setImageBitmap(bitmap);
            album.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            artist.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            dataa.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            durations.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            title.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            artist1 = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            title1 = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            durations1 = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }

            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void uploadFileTofirebase(View v) {
        if (textViewImage.getText().equals("No file selected")) {
            Toast.makeText(this, "Please select a file.", Toast.LENGTH_SHORT).show();
        } else {
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(this, "Upload in progress. Please wait.", Toast.LENGTH_SHORT).show();
            } else {
                uploadFiles();
            }
        }
    }
    private void uploadFiles() {
        if (audioUri != null) {
            Toast.makeText(this, "Uploading, please wait...", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);

            final StorageReference storageReference = mStorageref.child(System.currentTimeMillis() + "." + getFileExtension(audioUri));

            mUploadTask = storageReference.putFile(audioUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UploadSong uploadSong;

                                    if (!selectedAlbumId.isEmpty()) {
                                        // Jika album dipilih, unggah ke album yang dipilih
                                        uploadSong = new UploadSong(songsCategory, title1, artist1, album_art1, durations1, uri.toString());
                                        uploadSong.setAlbumId(selectedAlbumId);

                                        // Simpan objek uploadSong langsung ke Firebase Database dengan albumId yang tepat
                                        DatabaseReference albumIdRef = FirebaseDatabase.getInstance().getReference("uploads").child(selectedAlbumId);
                                        String uploadId = albumIdRef.push().getKey();
                                        albumIdRef.setValue(uploadSong);
                                    } else {
                                        // Jika tidak ada album yang dipilih, unggah ke "songs"
                                        uploadSong = new UploadSong(songsCategory, title1, artist1, album_art1, durations1, uri.toString());

                                        DatabaseReference referenceSongs = FirebaseDatabase.getInstance().getReference("songs");
                                        String uploadId = referenceSongs.push().getKey();
                                        referenceSongs.child(uploadId).setValue(uploadSong);
                                    }
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected for upload.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}