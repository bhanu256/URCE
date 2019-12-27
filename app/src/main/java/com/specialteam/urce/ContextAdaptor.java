package com.specialteam.urce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

class ContextAdaptor extends FirebaseRecyclerAdapter<DataFromAdaptor,ContextAdaptor.ContextHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    static Context context1;

    List<String> ids = new ArrayList<String>();

    ContextHolder contextHolder2;

    public ContextAdaptor(Context context,@NonNull FirebaseRecyclerOptions<DataFromAdaptor> options) {
        super(options);
        this.context1 = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ContextHolder contextHolder, int i, @NonNull DataFromAdaptor dataFromAdaptor) {
        contextHolder.tvname.setText(dataFromAdaptor.getCName());
        Glide.with(context1)
                .load(dataFromAdaptor.getPhotoID())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(contextHolder.ivimage);
        //ids.add(dataFromAdaptor.getCommentID());
        contextHolder.ivimage.setContentDescription(dataFromAdaptor.getPhotoID());
    }

    @NonNull
    @Override
    public ContextHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adaptorcard,parent,false);
        return new ContextHolder(v);
    }


    class ContextHolder extends RecyclerView.ViewHolder{

        TextView tvname;
        ImageView ivimage;
        TextView vac;


        public ContextHolder(@NonNull final View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.cname);
            ivimage = itemView.findViewById(R.id.img);
            vac = itemView.findViewById(R.id.vac);


            vac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ids!=null) {
                        System.out.println(ids.get(getAdapterPosition()));
                        new Home().comment(ids.get(getAdapterPosition()));
                    }
                }
            });



        }

    }

}
