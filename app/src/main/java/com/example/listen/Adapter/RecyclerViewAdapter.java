package com.example.listen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.listen.Model.UploadAlbum2;
import com.example.listen.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<UploadAlbum2> uploads;
    private OnItemClickListener listener;

    public RecyclerViewAdapter(Context mContext, List<UploadAlbum2> uploads) {
        this.mContext = mContext;
        this.uploads = uploads;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_album, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final UploadAlbum2 upload = uploads.get(position);
        holder.albumTitle.setText(upload.getName());
        Glide.with(mContext).load(upload.getUrl()).into(holder.albumImage);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView albumImage;
        TextView albumTitle;
        RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.imageAlbum);
            albumTitle = itemView.findViewById(R.id.textIdAlbum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}