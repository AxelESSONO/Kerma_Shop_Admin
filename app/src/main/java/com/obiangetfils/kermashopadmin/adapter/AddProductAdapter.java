package com.obiangetfils.kermashopadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.obiangetfils.kermashopadmin.R;

import java.util.ArrayList;

public class AddProductAdapter extends RecyclerView.Adapter<AddProductAdapter.AddProductViewHolder> {

    private Context context;
    private ArrayList<Uri> mArrayUri;
    private LayoutInflater mInflater;

    public AddProductAdapter(Context context, ArrayList<Uri> mArrayUri) {
        this.context = context;
        this.mArrayUri = mArrayUri;
        this.mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public AddProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.category_image, parent, false);
        return new AddProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddProductViewHolder holder, final int position) {

        Glide.with(context).load(mArrayUri.get(position)).into(holder.image);
        holder.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArrayUri.remove(position);
                notifyDataSetChanged();
                if (mArrayUri.size() == 0) {

                    Intent intent = new Intent("custom-message");
                    intent.putExtra("Size", mArrayUri.size());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayUri.size();
    }

    public class AddProductViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageView removeImage;

        public AddProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            removeImage = itemView.findViewById(R.id.removeImage);
        }
    }
}