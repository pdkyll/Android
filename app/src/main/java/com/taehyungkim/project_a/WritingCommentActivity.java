package com.taehyungkim.project_a;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WritingCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_comment);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                Toast.makeText(this, "저장 버튼 클릭", Toast.LENGTH_LONG).show();
                break;

            case R.id.cancelButton:
                finish();
                break;
        }
    }
}
