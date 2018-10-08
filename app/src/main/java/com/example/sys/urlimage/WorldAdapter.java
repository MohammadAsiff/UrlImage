package com.example.sys.urlimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WorldAdapter extends RecyclerView.Adapter<WorldAdapter.MyViewHolder>{
    Context context;


    List<JobListItem> jobList =new ArrayList<>(  );
    public WorldAdapter(Context context, List<JobListItem> jobList) {
        this.context = context;
        this.jobList = jobList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.worldlist, viewGroup, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.rank.setText(jobList.get(i).getRank());
        myViewHolder.country.setText(jobList.get(i).getCountry());
        myViewHolder.population.setText(jobList.get(i).getPopulation());

        String url=jobList.get(i).getFlag();
        new Download(myViewHolder.imageView).execute( url );



    }
    private class Download extends AsyncTask<String, Void, Bitmap> {
      ImageView imageView;

        public Download(ImageView imageView) {
           this.imageView=imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String image_url = strings[0];
            StringBuilder sb = new StringBuilder(image_url);

            sb.insert(4, 's');

            String url1=sb.toString();
            Bitmap bitmap = null;
            try {
                InputStream input = new URL(url1 ).openStream();
                bitmap = BitmapFactory.decodeStream( input );
                input.close();
                File storagePath = Environment.getExternalStorageDirectory();
                OutputStream bytes = new FileOutputStream( new File( storagePath, "name.jpg" ) );

                bitmap.compress( Bitmap.CompressFormat.JPEG, 100, bytes );
                bytes.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            imageView.setImageBitmap( bitmap );

        }
    }
    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rank,country,population;
        ImageView imageView;

        public MyViewHolder(@NonNull  final View itemView) {
            super( itemView );
            rank=itemView.findViewById( R.id.rank );
            country=itemView.findViewById( R.id.country );
            population=itemView.findViewById( R.id.population );
            imageView=itemView.findViewById( R.id.flag );

        }
    }
}
