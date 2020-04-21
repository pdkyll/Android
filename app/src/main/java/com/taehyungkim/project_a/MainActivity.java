package com.taehyungkim.project_a;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageView thumbUp;
    private ImageView thumbDown;

    private TextView likeText;
    private TextView dislikeText;

    boolean isLikeClicked = false;
    boolean isDislikeClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thumbUp = (ImageView) findViewById(R.id.like_img);
        thumbDown = (ImageView) findViewById(R.id.dislike_img);

        likeText = (TextView) findViewById(R.id.like_text);
        dislikeText = (TextView) findViewById(R.id.dislike_text);


    }

    public void onClick(View view) {
        int likeCount = Integer.parseInt(likeText.getText().toString());
        int dislikeCount = Integer.parseInt(dislikeText.getText().toString());

        switch (view.getId()) {
            case R.id.like_img:
                if (!isLikeClicked && !isDislikeClicked) { // not clicked both "like" and "dislike"
                    thumbUp.setImageResource(R.drawable.ic_thumb_up_selected);
                    likeText.setText(String.format(Locale.US, "%d", likeCount + 1));
                    isLikeClicked = true;
                    break;
                } else if (isLikeClicked && !isDislikeClicked) { // clicked "like" and not clicked "dislike"
                    break;
                } else { // clicked "dislike" and not clicked "like"
                    thumbUp.setImageResource(R.drawable.ic_thumb_up_selected);
                    thumbDown.setImageResource(R.drawable.ic_thumb_down);

                    isLikeClicked = true;
                    isDislikeClicked = false;

                    likeText.setText(String.format(Locale.US, "%d", likeCount + 1));

                    dislikeText.setText(String.format(Locale.US, "%d", dislikeCount - 1));
                }
                break;

            case R.id.dislike_img:
                if (!isLikeClicked && !isDislikeClicked) { // not clicked both "like" and "dislike"
                    thumbDown.setImageResource(R.drawable.ic_thumb_down_selected);
                    dislikeText.setText(String.format(Locale.US, "%d", dislikeCount + 1));
                    isDislikeClicked = true;
                    break;
                } else if (isDislikeClicked && !isLikeClicked) { // clicked "dislike" and not clicked "like"
                    break;
                } else { // clicked "like" and not clicked "dislike"
                    thumbDown.setImageResource(R.drawable.ic_thumb_down_selected);
                    thumbUp.setImageResource(R.drawable.ic_thumb_up);

                    isDislikeClicked = true;
                    isLikeClicked = false;

                    dislikeText.setText(String.format(Locale.US, "%d", dislikeCount + 1));

                    likeText.setText(String.format(Locale.US, "%d", likeCount - 1));
                }
                break;
        }
    }

}
