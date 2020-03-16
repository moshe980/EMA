package com.example.ema.classs;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ema.R;
import com.squareup.picasso.Picasso;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<String> uploads;
    private OnItemClicklistener mLisitener;


    public ImageAdapter(Context context, List<String> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        String uploadCurrent=uploads.get(position);
        Picasso.get()
                .load(uploadCurrent)
                .placeholder(R.drawable.pdf_icon)
                .fit()
                .centerCrop()
                .into(holder.imageView);

    }





    @Override
    public int getItemCount() {
        return uploads.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.image_view_upload);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mLisitener!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    mLisitener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("בחר אפשרות");
            MenuItem download=menu.add(Menu.NONE,1,1,"להוריד");
            MenuItem delete=menu.add(Menu.NONE,2,2,"למחוק");

            download.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mLisitener != null) {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mLisitener.onDownloadClick(position);
                            return true;
                        case 2:
                            mLisitener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public void setOnItemClicklistener(OnItemClicklistener listener){
        mLisitener=listener;
    }
    public interface OnItemClicklistener{
        void onItemClick(int position);
        void onDownloadClick(int position);
        void onDeleteClick(int position);
    }
}


