package com.laioffer.vicabulary.ui.select;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laioffer.vicabulary.R;
import com.laioffer.vicabulary.database.JSONResourceReader;
import com.laioffer.vicabulary.databinding.FragmentSelectBinding;
import com.laioffer.vicabulary.model.Movie;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFragment extends Fragment {
    private FragmentSelectBinding binding;



    public SelectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JSONResourceReader jsonResourceReader = new JSONResourceReader(getResources());
        List<Movie> movies = jsonResourceReader.getAllMovies();
//        Log.d("movies", movies.get(0).toString());
        SelectAdapter movieAdapter = new SelectAdapter();
        movieAdapter.setMovies(movies);
        movieAdapter.setPlayListener(movie -> {
            SelectFragmentDirections.ActionSelectToPlayback actionSelectToPlayback = SelectFragmentDirections.actionSelectToPlayback();
            actionSelectToPlayback.setMovie(movie);
            NavHostFragment.findNavController(SelectFragment.this).navigate(actionSelectToPlayback);
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setAdapter(movieAdapter);
    }

}