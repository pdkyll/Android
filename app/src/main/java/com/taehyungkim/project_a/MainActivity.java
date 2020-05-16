package com.taehyungkim.project_a;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageView thumbUpImg;
    private ImageView thumbDownImg;

    private TextView likeText;
    private TextView dislikeText;

    boolean isLikeBtClicked = false;
    boolean isDislikeBtClicked = false;

    public RecyclerView com_recycler_view;
    public CommentItemRecyclerAdapter commentAdapter;

    public ArrayList<String> id = new ArrayList<>();
    public ArrayList<String> time = new ArrayList<>();
    public ArrayList<Float> rating = new ArrayList<>();
    public ArrayList<String> comment = new ArrayList<>();
    public ArrayList<String> recommendCount = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thumbUpImg = (ImageView) findViewById(R.id.like_img_view);
        thumbDownImg = (ImageView) findViewById(R.id.dislike_img_view);

        likeText = (TextView) findViewById(R.id.like_text_view);
        dislikeText = (TextView) findViewById(R.id.dislike_text_view);


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

        com_recycler_view = (RecyclerView) findViewById(R.id.comment_recycler_view);
        com_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러 뷰에 구분선 라인 추가
        com_recycler_view.addItemDecoration(new DividerItemDecoration(this, 1));

        commentAdapter = new CommentItemRecyclerAdapter(id, time, rating, comment, recommendCount);
        com_recycler_view.setAdapter(commentAdapter);
    }

    public void onClick(View view) {
        int likeCount = Integer.parseInt(likeText.getText().toString());
        int dislikeCount = Integer.parseInt(dislikeText.getText().toString());

        switch (view.getId()) {
            case R.id.like_img_view:
                if (!isLikeBtClicked && !isDislikeBtClicked) { // not clicked both "like" and "dislike"
                    thumbUpImg.setImageResource(R.drawable.ic_thumb_up_selected);
                    likeText.setText(String.format(Locale.US, "%d", likeCount + 1));
                    isLikeBtClicked = true;
                    break;
                } else if (isLikeBtClicked && !isDislikeBtClicked) { // clicked "like" and not clicked "dislike"
                    break;
                } else { // clicked "dislike" and not clicked "like"
                    thumbUpImg.setImageResource(R.drawable.ic_thumb_up_selected);
                    thumbDownImg.setImageResource(R.drawable.ic_thumb_down);

                    isLikeBtClicked = true;
                    isDislikeBtClicked = false;

                    likeText.setText(String.format(Locale.US, "%d", likeCount + 1));

                    dislikeText.setText(String.format(Locale.US, "%d", dislikeCount - 1));
                }
                break;

            case R.id.dislike_img_view:
                if (!isLikeBtClicked && !isDislikeBtClicked) { // not clicked both "like" and "dislike"
                    thumbDownImg.setImageResource(R.drawable.ic_thumb_down_selected);
                    dislikeText.setText(String.format(Locale.US, "%d", dislikeCount + 1));
                    isDislikeBtClicked = true;
                    break;
                } else if (isDislikeBtClicked && !isLikeBtClicked) { // clicked "dislike" and not clicked "like"
                    break;
                } else { // clicked "like" and not clicked "dislike"
                    thumbDownImg.setImageResource(R.drawable.ic_thumb_down_selected);
                    thumbUpImg.setImageResource(R.drawable.ic_thumb_up);

                    isDislikeBtClicked = true;
                    isLikeBtClicked = false;

                    dislikeText.setText(String.format(Locale.US, "%d", dislikeCount + 1));

                    likeText.setText(String.format(Locale.US, "%d", likeCount - 1));
                }
                break;

            case R.id.writingBt_view:
                Intent wiringCommentIntent = new Intent(getApplicationContext(), WritingCommentActivity.class);
                startActivity(wiringCommentIntent);
                break;

            case R.id.seeAllBt_view:
                Intent commentListIntent = new Intent(getApplicationContext(), CommentListActivity.class);
                startActivity(commentListIntent);
                break;
        }
    }

}
