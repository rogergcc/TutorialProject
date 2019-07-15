package com.software3000.s3000.tutorialproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.software3000.s3000.tutorialproject.model.Post;
import com.software3000.s3000.tutorialproject.services.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String b;

    private String[] dataArray;

    private ProgressDialog pd;
    private List<Post> posts;
    private Gson gson;
    private GsonBuilder builder;
    RecyclerView recyclerView;
    ConsumoApiAdapter adapterapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataArray = getResources().getStringArray(R.array.arr_list);
        recyclerView= findViewById(R.id.milista);

//        pd = new ProgressDialog(this);
        pd = new ProgressDialog(this,R.style.MyAlertDialogStyle);

        pd.setTitle("Obteniendo Registros");
        pd.setMessage("Recibiendo Datos");
        pd.setCancelable(false);
        pd.show();

        posts = new ArrayList<>();
        //ConsumoAdapter adapter = new ConsumoAdapter(dataArray);
//        recyclerView.setAdapter(adapter);
        servicoListarPosts();



        //pd.dismiss();

    }
    public void servicoListarPosts() {

        posts.clear();
        String url ="http://jsonplaceholder.typicode.com/posts";

        //http://192.168.1.38/SysLudopatas/Login/ValidacionLoginExternoJson?usuLogin=admin&usuPassword=102030
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);
//                        progressDialog.dismiss();
                        pd.dismiss();
                        if (response!=null){
                            builder = new GsonBuilder();
                            gson = builder.create();
                            Post anfitrionasPedidos = new Post();
//                            posts = gson.fromJson(response, Post[].class);
                            List<Post> posts1 = Arrays.asList(gson.fromJson(response, Post[].class));
                            posts= Arrays.asList(gson.fromJson(response, Post[].class));

                            adapterapi = new ConsumoApiAdapter(posts);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                            recyclerView.setAdapter(adapterapi);

                            adapterapi.notifyDataSetChanged();

                        }

                        //if no error in response


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        pd.dismiss();
                        Log.e("Error","Error Volley");
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            DynamicToast.makeWarning(getBaseContext(), "Error Tiempo de Respuesta Inicio de Sesión", Toast.LENGTH_LONG).show();
                            Log.e("Error","Error Tiempo Respuest");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("nroImpresion", nr_ticket);


                return params;
            }
        };

        //VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        //AppSin
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    //
    private class ConsumoApiAdapter extends RecyclerView.Adapter<QuoteApiHolder>{
        private List<Post> dataSourcePost;
        public ConsumoApiAdapter(List<Post> dataPost){
            dataSourcePost = dataPost;

        }
        @Override
        public QuoteApiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater
                    .inflate(R.layout.item_comsumo, parent, false);
            return new QuoteApiHolder(view);
        }

        @Override
        public void onBindViewHolder(QuoteApiHolder holder, int position) {

            Glide.with(holder.itemView.getContext())
//                    .load(dataSourcePost.get(position).getIdDrawable())
                    .load(R.drawable.descarga)
//                    .load("https://via.placeholder.com/150/92c952")

                    .apply(RequestOptions.centerCropTransform())

                    //.placeholder(R.drawable.load)
                    .into(holder.imageView);
            holder.textView.setText(dataSourcePost.get(position).getTitle());
            holder.textBody.setText(dataSourcePost.get(position).getBody());
        }


        @Override
        public int getItemCount() {
            return dataSourcePost.size();
        }
    }
    public static class QuoteApiHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public TextView textBody;
        public RelativeLayout relativeLayout;
        public QuoteApiHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageMovie);
            this.textView = (TextView) itemView.findViewById(R.id.textMovieTitle);
            this.textBody = (TextView) itemView.findViewById(R.id.textMovieBody);
        }
    }


    //todo String statico
    private class ConsumoAdapter extends RecyclerView.Adapter<QuoteHolder>{
        private String[] dataSource;
        public ConsumoAdapter(String[] dataArgs){
            dataSource = dataArgs;

        }
        @Override
        public QuoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater
                    .inflate(R.layout.item_comsumo, parent, false);
            return new QuoteHolder(view);
        }

        @Override
        public void onBindViewHolder(QuoteHolder holder, int position) {
            holder.textView.setText(dataSource[position]);
        }


        @Override
        public int getItemCount() {
            return dataSource.length;
        }
    }
    public static class QuoteHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public QuoteHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textMovieTitle);
        }
    }
}

