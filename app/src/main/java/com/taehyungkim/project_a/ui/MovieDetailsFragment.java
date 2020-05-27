package com.taehyungkim.project_a.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyungkim.project_a.ui.adapter.MovieDetailsAdapter;
import com.taehyungkim.project_a.CommentListActivity;
import com.taehyungkim.project_a.R;
import com.taehyungkim.project_a.WritingCommentActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MovieDetailsFragment extends Fragment {
    private ImageView thumbUpImg;
    private ImageView thumbDownImg;

    private TextView likeText;
    private TextView dislikeText;

    private Button writingBt;
    private Button seeAllBt;

    boolean isLikeBtClicked = false;
    boolean isDislikeBtClicked = false;

    public RecyclerView com_recycler_view;
    public MovieDetailsAdapter commentAdapter;

    public ArrayList<String> id = new ArrayList<>();
    public ArrayList<String> time = new ArrayList<>();
    public ArrayList<Float> rating = new ArrayList<>();
    public ArrayList<String> comment = new ArrayList<>();
    public ArrayList<String> recommendCount = new ArrayList<>();

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_details, container, false);

        thumbUpImg = (ImageView) rootView.findViewById(R.id.like_img_view);
        thumbDownImg = (ImageView) rootView.findViewById(R.id.dislike_img_view);

        writingBt = (Button) rootView.findViewById(R.id.writingBt_view);
        seeAllBt = (Button) rootView.findViewById(R.id.seeAllBt_view);

        likeText = (TextView) rootView.findViewById(R.id.like_text_view);
        dislikeText = (TextView) rootView.findViewById(R.id.dislike_text_view);


        id.add("kym71**");
        time.add("10분전");
        rating.add(5.0f);
        comment.add("적당히 재밌다. 오랜만에 잠 안오는 영화 봤네요.");
        recommendCount.add("0");

        id.add("angel**");
        time.add("15분전");
        rating.add(4.7f);
        comment.add("웃긴 내용보다는 좀 더 진지한 영화.");
        recommendCount.add("1");

        com_recycler_view = (RecyclerView) rootView.findViewById(R.id.comment_recycler_view);
        com_recycler_view.setLayoutManager(new LinearLayoutManager(context));

        // 리사이클러 뷰에 구분선 라인 추가
        com_recycler_view.addItemDecoration(new DividerItemDecoration(context, 1));

        commentAdapter = new MovieDetailsAdapter(id, time, rating, comment, recommendCount);
        com_recycler_view.setAdapter(commentAdapter);

        setListener();

        return rootView;
    }

    public void setListener() {
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.like_img_view:
                        if (!isLikeBtClicked && !isDislikeBtClicked) { // not clicked both "like" and "dislike"
                            thumbUpImg.setImageResource(R.drawable.ic_thumb_up_selected);
                            likeText.setText(String.format(Locale.US, "%d", Integer.parseInt(likeText.getText().toString()) + 1));
                            isLikeBtClicked = true;
                            break;
                        } else if (isLikeBtClicked && !isDislikeBtClicked) { // clicked "like" and not clicked "dislike"
                            break;
                        } else { // clicked "dislike" and not clicked "like"
                            thumbUpImg.setImageResource(R.drawable.ic_thumb_up_selected);
                            thumbDownImg.setImageResource(R.drawable.ic_thumb_down);

                            isLikeBtClicked = true;
                            isDislikeBtClicked = false;

                            likeText.setText(String.format(Locale.US, "%d", Integer.parseInt(likeText.getText().toString()) + 1));

                            dislikeText.setText(String.format(Locale.US, "%d", Integer.parseInt(dislikeText.getText().toString()) - 1));
                        }
                        break;

                    case R.id.dislike_img_view:
                        if (!isLikeBtClicked && !isDislikeBtClicked) { // not clicked both "like" and "dislike"
                            thumbDownImg.setImageResource(R.drawable.ic_thumb_down_selected);
                            dislikeText.setText(String.format(Locale.US, "%d", Integer.parseInt(dislikeText.getText().toString()) + 1));
                            isDislikeBtClicked = true;
                            break;
                        } else if (isDislikeBtClicked && !isLikeBtClicked) { // clicked "dislike" and not clicked "like"
                            break;
                        } else { // clicked "like" and not clicked "dislike"
                            thumbDownImg.setImageResource(R.drawable.ic_thumb_down_selected);
                            thumbUpImg.setImageResource(R.drawable.ic_thumb_up);

                            isDislikeBtClicked = true;
                            isLikeBtClicked = false;

                            dislikeText.setText(String.format(Locale.US, "%d", Integer.parseInt(dislikeText.getText().toString()) + 1));

                            likeText.setText(String.format(Locale.US, "%d", Integer.parseInt(likeText.getText().toString()) - 1));
                        }
                        break;

                    case R.id.writingBt_view:
                        Intent wiringCommentIntent = new Intent(context, WritingCommentActivity.class);
                        startActivity(wiringCommentIntent);
                        break;

                    case R.id.seeAllBt_view:
                        Intent commentListIntent = new Intent(context, CommentListActivity.class);
                        startActivity(commentListIntent);
                        break;
                }
            }
        };

        thumbUpImg.setOnClickListener(listener);
        thumbDownImg.setOnClickListener(listener);
        writingBt.setOnClickListener(listener);
        seeAllBt.setOnClickListener(listener);
    }
}
