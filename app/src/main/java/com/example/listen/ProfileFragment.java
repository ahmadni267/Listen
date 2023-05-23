package com.example.listen;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private TextView textViewusername, textviewhalo;
    String username;
    ImageView fotopro;
    private FirebaseAuth authProfile;
    private Button logout;
    private static final int PICK_IMAGE_REQUEST = 1;




    private RelativeLayout relativeLayoutFavorite;
    private RelativeLayout relativeLayoutRiwayat;
    private RelativeLayout relativeLayoutDevice;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private Uri uriImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

//        textViewusername = view.findViewById(R.id.textviewusername);
        textviewhalo = view.findViewById(R.id.textviewhaloo);
        relativeLayoutFavorite = view.findViewById(R.id.Favorit);
        relativeLayoutRiwayat = view.findViewById(R.id.Riwayat);
        fotopro = view.findViewById(R.id.Fotoprofil);
        logout = view.findViewById(R.id.Keluar);



        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(getContext(), "Terjadi Kesalahan Menampilkan profil", Toast.LENGTH_LONG).show();
        }else{
            showUserProfile(firebaseUser);
        }
        relativeLayoutFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Favorite.class);
                startActivity(intent);
            }
        });
        relativeLayoutRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Riwayat.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProfile.signOut();
                Intent intent = new Intent(getActivity(), masuk.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        fotopro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UploadProfilPic.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                username = firebaseUser.getDisplayName();
                textviewhalo.setText(username);
                Uri uri = firebaseUser.getPhotoUrl();
                Picasso.get().load(uri).into(fotopro);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Terjadi Kesalahan !", Toast.LENGTH_LONG).show();
            }
        });

    }
}