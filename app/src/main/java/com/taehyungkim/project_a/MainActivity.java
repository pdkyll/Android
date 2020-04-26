package com.taehyungkim.project_a;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageView thumbUpImg;
    private ImageView thumbDownImg;

    private TextView likeText;
    private TextView dislikeText;

    boolean isLikeBtClicked = false;
    boolean isDislikeBtClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thumbUpImg = (ImageView) findViewById(R.id.like_img);
        thumbDownImg = (ImageView) findViewById(R.id.dislike_img);

        likeText = (TextView) findViewById(R.id.like_text);
        dislikeText = (TextView) findViewById(R.id.dislike_text);


    }

    public void onClick(View view) {
        int likeCount = Integer.parseInt(likeText.getText().toString());
        int dislikeCount = Integer.parseInt(dislikeText.getText().toString());

        switch (view.getId()) {
            case R.id.like_img:
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

            case R.id.dislike_img:
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
        }
    }

}
