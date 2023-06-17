package images.tackled;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mcontext;
    private List<String> muploads;

    public ImageAdapter(Context contest, List<String> uploads){
        mcontext=contest;
        muploads=uploads;

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mcontext).inflate(R.layout.image_item,parent ,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {



        Glide.with(holder.itemView.getContext()).load(muploads.get(position)).into(holder.imageview);
        //Upload uploadcurrent=muploads.get(position);
    //holder.textViewName.setText(uploadcurrent.getmName());
       // Picasso.get()
             //   .load(uploadcurrent.getmImageurl())
               // .placeholder(R.mipmap.ic_launcher)
                //.fit()
                //.centerCrop()
                //.into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        return muploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageview;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName=itemView.findViewById(R.id.text_view_name);
            imageview=itemView.findViewById(R.id.image_view_upload);

        }
    }

}
