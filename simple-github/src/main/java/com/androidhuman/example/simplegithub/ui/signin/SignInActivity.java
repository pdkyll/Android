package com.androidhuman.example.simplegithub.ui.signin;

import com.androidhuman.example.simplegithub.BuildConfig;
import com.androidhuman.example.simplegithub.R;
import com.androidhuman.example.simplegithub.api.AuthApi;
import com.androidhuman.example.simplegithub.api.GithubApiProvider;
import com.androidhuman.example.simplegithub.api.model.GithubAccessToken;
import com.androidhuman.example.simplegithub.data.AuthTokenProvider;
import com.androidhuman.example.simplegithub.ui.main.MainActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    Button btnStart;

    ProgressBar progress;

    AuthApi api;

    AuthTokenProvider authTokenProvider;

    Call<GithubAccessToken> accessTokenCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnStart = findViewById(R.id.btnActivitySignInStart);
        progress = findViewById(R.id.pbActivitySignIn);

        /*
         * 1. 버튼 클릭.
         * 버튼 초기화 과정.
         * 버튼을 누르면 Github 사용자 인증 웹페이지로 이동하게된다.
         * 웹페이지 주소는 Github 애플리케이션의 Client ID를 넣어준다.
         * 웹피이지는 크롬 커스텀 탭을 사용하여 표시한다.
         */
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri authUri = new Uri.Builder().scheme("https").authority("github.com")
                        .appendPath("login")
                        .appendPath("oauth")
                        .appendPath("authorize")
                        .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
                        .build();

                CustomTabsIntent intent = new CustomTabsIntent.Builder().build();
                intent.launchUrl(SignInActivity.this, authUri);
            }
        });

        /*
         * authTokenProvider 를 사용하여 사용자 인증 토큰이 있는지 여부를 확인하고,
         * 만약 인증 토큰이 있다면 메인 액티비티로 이동한다.
         */
        api = GithubApiProvider.provideAuthApi();
        authTokenProvider = new AuthTokenProvider(this);

        if (null != authTokenProvider.getToken()) {
            launchMainActivity();
        }
    }

    /*
     * 2. 액세스 토큰 요청
     * 버튼을 눌러 웹페이지에서 사용자 인증이 완료되면
     * simplegithub://authrize?code={액세스 토큰 교환용 코드} 형태의 주소로 페이지가 리디렉션됩니다.
     * 이때 SignInActivity 가 이 형태의 주소를 열 수 있도록 매니페스트에 선언했으므로 SignInActivity 가 열리며,
     * 액티비티가 열리는 시점에 SignInActivity 가 화면에 표시되고 있는 상태이므로 onCreate() 콜백 대신
     * onNewIntent() 콜백이 호출됩니다.
     * 이때, 인텐트 내 data 필드에는 이 액티비티를 호출하기 위해 사용한 주소가 들어 있으며,
     * 이 주소에는 액세스 토큰과 교환할 수 있는 코드가 쿼리 매개변수(query parameter) 형태로 포함되어 있습니다.
     * 따라서 코드를 주소에서 추출하여 액세스 토큰을 요청합니다.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        showProgress();

        Uri uri = intent.getData();
        if (null == uri) {
            throw new IllegalArgumentException("No data exists");
        }

        String code = uri.getQueryParameter("code");
        if (null == code) {
            throw new IllegalStateException("No code exists");
        }

        getAccessToken(code);
    }

    /*
     * 3. 액세스 토큰 추출
     * 액세스 토큰은 REST API 를 사용하여 발급받으며, Github 애플리케이션의 Client ID 와 Client Secret,
     * 그리고 앞에서 사용자 인증이 완료된 후 받은 코드를 함께 전달합니다.
     * 액세스 토큰을 정상적으로 발급받았다면 이후 이를 계속 사용할 수 있도록 저장한 후 메인 액티비티로 이동합니다.
     */
    private void getAccessToken(@NonNull String code) {
        showProgress();

        accessTokenCall = api.getAccessToken(
                BuildConfig.GITHUB_CLIENT_ID, BuildConfig.GITHUB_CLIENT_SECRET, code);

        accessTokenCall.enqueue(new Callback<GithubAccessToken>() {
            @Override
            public void onResponse(Call<GithubAccessToken> call,
                    Response<GithubAccessToken> response) {
                hideProgress();

                GithubAccessToken token = response.body();
                if (response.isSuccessful() && null != token) {
                    authTokenProvider.updateToken(token.accessToken);

                    launchMainActivity();
                } else {
                    showError(new IllegalStateException(
                            "Not successful: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<GithubAccessToken> call, Throwable t) {
                hideProgress();
                showError(t);
            }
        });
    }

    private void showProgress() {
        btnStart.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        btnStart.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    private void showError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void launchMainActivity() {
        startActivity(new Intent(
                SignInActivity.this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
