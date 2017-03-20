package br.com.tguide.service;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i("API", call.request().url().toString() + " called without errors");
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("API", "Call to " + call.request().url() + " failed", t);
    }
}
