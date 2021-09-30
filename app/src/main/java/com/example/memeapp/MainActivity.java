package com.example.memeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.memeapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

     String Url = "https://meme-api.herokuapp.com/gimme";
     RequestQueue queue;
     ActivityMainBinding binding;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        queue = Volley.newRequestQueue(this);



        //making thr object of Volley library
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("preview");
                    imageUrl = jsonArray.getString(3);
                    Glide.with(MainActivity.this).load(imageUrl).into(binding.image);

                    binding.progressBar.setVisibility(View.GONE);
                    binding.image.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.getLocalizedMessage());
            }
        });

        //binding next button to the function
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.image.setVisibility(View.GONE);
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);

            }
        });

        //binding the functionality to the share button
        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT,imageUrl);
                startActivity(Intent.createChooser(sendIntent,"Share this meme using...."));


            }
        });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


}