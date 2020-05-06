package com.taehyungkim.project_a;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public ArrayList<String> recCount = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thumbUpImg = (ImageView) findViewById(R.id.like_img_view);
        thumbDownImg = (ImageView) findViewById(R.id.dislike_img_view);

        likeText = (TextView) findViewById(R.id.like_text_view);
        dislikeText = (TextView) findViewById(R.id.dislike_text_view);

        recCount.add("0");
        recCount.add("0");

        com_recycler_view = (RecyclerView) findViewById(R.id.comment_recycler_view);
        com_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        com_recycler_view.addItemDecoration(new DividerItemDecoration(this, 1));

        commentAdapter = new CommentItemRecyclerAdapter(recCount);
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
                Toast.makeText(this, "작성하기 버튼 클릭", Toast.LENGTH_SHORT).show();
                break;

            case R.id.seeAllBt_view:
                Toast.makeText(this, "모두 보기 버튼 클릭", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
