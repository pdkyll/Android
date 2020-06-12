package com.taehyungkim.project_a.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import com.taehyungkim.project_a.R;

/**
 * Created by kth on 2020-06-12.
 */
public class MovieWritingCommentFragment extends Fragment {

    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (context != null) {
            context = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_writing_comment, container, false);

        Button bt_save = (Button) rootView.findViewById(R.id.saveButton);
        Button bt_cancel = (Button) rootView.findViewById(R.id.cancelButton);

        bt_save.setOnClickListener(v -> {
            Toast.makeText(context, "저장 버튼 클릭", Toast.LENGTH_LONG).show();
        });

        bt_cancel.setOnClickListener(v -> {
            Navigation.findNavController(rootView).navigate(R.id.action_nav_writing_comment_to_nav_movie_details);
        });

        return rootView;
    }
}
