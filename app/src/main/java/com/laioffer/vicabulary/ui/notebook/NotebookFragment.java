package com.laioffer.vicabulary.ui.notebook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.laioffer.vicabulary.MainActivity;
import com.laioffer.vicabulary.R;
import com.laioffer.vicabulary.database.DatabaseHelper;
import com.laioffer.vicabulary.databinding.FragmentNotebookBinding;
import com.laioffer.vicabulary.model.Word;
import com.mindorks.placeholderview.SwipeDecor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotebookFragment extends Fragment implements WordCard.OnSwipeListener {
    private FragmentNotebookBinding binding;
    private DatabaseHelper db;
    private List<Word> words;
    private Word w;
    public NotebookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_notebook, container, false);

        binding = FragmentNotebookBinding.inflate(inflater, container, false);
        db = MainActivity.vDb;
        words = db.getAllWord();

        binding.
                swipeView
                .getBuilder()
                .setDisplayViewCount(words.size())
                .setSwipeDecor(
                        new SwipeDecor()
                                .setPaddingTop(20)
                                .setRelativeScale(0.01f));
        binding.rejectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // NavHostFragment.findNavController(NotebookFragment.this).navigate(R.id.action_title_note_to_play);
                binding.swipeView.doSwipe(false);

            }
        });
        binding.acceptBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // NavHostFragment.findNavController(NotebookFragment.this).navigate(R.id.action_title_note_to_play);
                binding.swipeView.doSwipe(true);

            }
        });


        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Word w1 = new Word();
//        w1.setWord("good");
//        w1.setExplanation("not bad");
//        Word w2 = new Word();
//        w2.setWord("hello");
//        w2.setExplanation("hi");
//        db.insertWord("hello",20,"hi","video","srt");
//        db.insertWord("good",203,"not bad","video","srt");
//        db.insertWord("hi",30,"hello","video","srt");

        words = db.getAllWord();
            if(words.isEmpty()){
                Word w1 = new Word();
                Log.d("words","null");
                w1.setWord(" ");
                w1.setExplanation(" ");
                w1.setSrtPath(" ");
                w1.setVideoPath(" ");
                w1.setTime(0);
                WordCard Card = new WordCard(w1, this);
                binding.swipeView.addView(Card);
            }

         //   Log.d("words",words.get(0).toString());
            //     Button navi = (Button)view.findViewById(R.id.navigate);
            else {
            Iterator a = words.iterator();
            int count = 0;
            while (a.hasNext()) {
                w = (Word) a.next();
                Log.d("word1", w.toString());
                WordCard wordCard = new WordCard(w, this);
                //  Button navig =  wordCard.getButton();
                //    Log.d("wordCard",navig.toString());
                binding.swipeView.addView(wordCard);
                // Button navig = wordCard.getButton();
                if (wordCard.getButton() != null) {
                    count++;
                    Log.d("count",String.valueOf(count));
                    Log.d("navigate", "gethere");
                    wordCard.getButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // NavHostFragment.findNavController(NotebookFragment.this).navigate(R.id.action_title_note_to_play);
                            Bundle bundle = new Bundle();
                            bundle.putLong("time", w.getTime());
                            Log.d("times",String.valueOf(w.getTime()));
                            bundle.putString("videoPath",w.getVideoPath());
                            bundle.putString("srtPath",w.getSrtPath());
                            //bundle.putString("path", w.getPath());
                            NavHostFragment.findNavController(NotebookFragment.this).navigate(R.id.action_title_note_to_play, bundle);
                        }
                    });
                }
            }
        }
//        while (a.hasNext()){
//            w = (Word)a.next();
//            navi.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // NavHostFragment.findNavController(NotebookFragment.this).navigate(R.id.action_title_note_to_play);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("time", w.getTime());
//                    bundle.putInt("path",w.getVideo());
//                    NavHostFragment.findNavController(NotebookFragment.this).navigate(R.id.action_title_note_to_play,bundle);
//                }
//            });
            // WordCard wordCard = new WordCard(w);
            //  binding.swipeView.addView(wordCard);

//        }
            // WordCard wordCard = new WordCard(w1);
//        LinkedList<Word> wordLinkedList = new LinkedList();
//        wordLinkedList.add(w1);
//        wordLinkedList.add(w2);
//      //  for( Word wd: wordLinkedList){
//            navi.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // NavHostFragment.findNavController(NotebookFragment.this).navigate(R.id.action_title_note_to_play);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("time", w.getTime());
//                    bundle.putInt("path",w.getVideo());
//                    NavHostFragment.findNavController(NotebookFragment.this).navigate(R.id.action_title_note_to_play,bundle);
//                }
//            });
            //     WordCard wordCard = new WordCard(wd);
            //       binding.swipeView.addView(wordCard);

    }


    @Override
    public void unSave(Word word) {
        db.unSaveWord(word);
    }
}
