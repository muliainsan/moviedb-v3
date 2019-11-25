package com.project.insan.kehadiran.fragment;

import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.project.insan.kehadiran.R;
import com.project.insan.kehadiran.adapter.MoviesAdapter;
import com.project.insan.kehadiran.java.Movie;
import com.project.insan.kehadiran.services.ApiClient;
import com.project.insan.kehadiran.services.ApiInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieFragment extends Fragment implements View.OnClickListener {
    public static Object SavedState;
    private static String API_Key = "b22f426e41174091c4c6bfa16086e1db";
    private ActionBar toolbar;
    private SwipeRefreshLayout swipeLayout;
    private ProgressBar pBar;
    private Movie.ResultsBean mm;
    List<Movie.ResultsBean> movies;
    private RecyclerView rv;
    public MovieFragment() {
        // Required empty public constructor
    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("list", (Serializable) movies);
//
//    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("ffff", String.valueOf(savedInstanceState));
        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("list");
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(layoutManager);
            MoviesAdapter madapter = new MoviesAdapter(movies, getActivity());
            rv.setAdapter(madapter);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie, container, false);
//        Log.e("1q1q", "asdf");


        swipeLayout = view.findViewById(R.id.swipe_container);
        rv = view.findViewById(R.id.rv_movie);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        pBar = view.findViewById(R.id.pBar);
        List<String> categories = new ArrayList<String>();
        pBar.setVisibility(view.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pBar.setVisibility(view.GONE);
                swipeLayout.setRefreshing(false);
            }
        }, 1000);


        categories.add(getResources().getString(R.string.now_playing));
        categories.add(getResources().getString(R.string.up_coming));
        categories.add(getResources().getString(R.string.top_rated));

        final ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(dataAdapter);

            Log.i("ffffggg", "asd");
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {

                    if (position == 0) {
                        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        Call<Movie> call = apiService.getNowPlaying(API_Key);
                        call.enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {
                                int statusCode = response.code();
                                movies = response.body().getResults();
                                Log.e("1q1q", String.valueOf(statusCode));
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                rv.setLayoutManager(layoutManager);
                                MoviesAdapter madapter = new MoviesAdapter(movies, getActivity());
                                rv.setAdapter(madapter);

                            }

                            @Override
                            public void onFailure(Call<Movie> call, Throwable t) {
                                Log.e("xxx", t.toString());
                            }
                        });
                    } else if (position == 1) {
                        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        Call<Movie> call = apiService.getUpComing(API_Key);
                        call.enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {

                                int statusCode = response.code();
                                movies = response.body().getResults();
                                Log.e("1q1q", String.valueOf(statusCode));

                                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                                MoviesAdapter madapter = new MoviesAdapter(movies, getActivity());
                                rv.setAdapter(madapter);

                            }

                            @Override
                            public void onFailure(Call<Movie> call, Throwable t) {
                                Log.e("xxx", t.toString());
                            }
                        });
                    } else if (position == 2) {
                        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        Call<Movie> call = apiService.getTopRated(API_Key);
                        call.enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {
                                int statusCode = response.code();
                                movies = response.body().getResults();
                                Log.e("1q1q", String.valueOf(statusCode));
                                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                                MoviesAdapter madapter = new MoviesAdapter(movies, getActivity());
                                rv.setAdapter(madapter);
                            }

                            @Override
                            public void onFailure(Call<Movie> call, Throwable t) {
                                Log.e("xxx", t.toString());
                            }
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                    setupViewPager(viewPager);
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });


        return view;
    }


    @Override
    public void onClick(View v) {

    }
}
