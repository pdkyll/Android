package com.taehyungkim.project_a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class CommentListActivity extends AppCompatActivity {

    public RecyclerView writingRecyclerView;
    public CommentItemRecyclerAdapter commentAdapter;

    public ArrayList<String> id = new ArrayList<>();
    public ArrayList<String> time = new ArrayList<>();
    public ArrayList<Float> rating = new ArrayList<>();
    public ArrayList<String> comment = new ArrayList<>();
    public ArrayList<String> recommendCount = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

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

        id.add("beaut**");
        time.add("16분전");
        rating.add(4.5f);
        comment.add("연기가 부족한 느낌이 드는 배우도 있다. 그래도 전체적으로는 재밌다.");
        recommendCount.add("1");

        id.add("sales**");
        time.add("18분전");
        rating.add(5.0f);
        comment.add("마지막이 비극인 영화.");
        recommendCount.add("3");

        id.add("pargo**");
        time.add("19분전");
        rating.add(5.0f);
        comment.add("스트레스가 해소되는 영화네요.");
        recommendCount.add("0");

        id.add("mommy**");
        time.add("22분전");
        rating.add(3.5f);
        comment.add("아무 생각없이 보면 되는 여화. 하지만 생각하면 안되는 영화.");
        recommendCount.add("1");

        id.add("cute1**");
        time.add("23분전");
        rating.add(5.0f);
        comment.add("적당히 웃기고 적당히 재밌어요.");
        recommendCount.add("2");

        id.add("add11**");
        time.add("27분전");
        rating.add(4.5f);
        comment.add("요즘 제대로 만든 영화 없더니 잘 만들었습니다.");
        recommendCount.add("4");

        id.add("note7**");
        time.add("30분전");
        rating.add(4.8f);
        comment.add("즐길 수 있는 영화입니다.");
        recommendCount.add("5");

        writingRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        writingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러 뷰에 구분선 라인 추가
        writingRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));

        commentAdapter = new CommentItemRecyclerAdapter(id, time, rating, comment, recommendCount);
        writingRecyclerView.setAdapter(commentAdapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.writingBt_view:
                finish();
                break;

            default:
                break;
        }
    }
}
