package com.thkim.kotlinprogramming.kotlin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thkim.kotlinprogramming.R
import com.thkim.kotlinprogramming.java.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    private val cities = listOf(
        "Seoul",
        "Tokyo",
        "Mountain View",
        "Singapore",
        "New York"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listUpButton()
    }

    private fun testConfiguration() {
        configuration(orientation = Orientation.LANDSCAPE) {
            // 단말기가 가로 방향일 때 수행할 코드를 작성한다.
        }

        configuration(orientation = Orientation.PORTRAIT, language = "ko") {
            // 단말기가 세로 방향이면서 시스템 언어가 한국어로 설정되어 있을 때
            // 수행할 코드를 작성한다.
        }

        doFromSdk(Build.VERSION_CODES.O) {
            // 안드로이드 8.0 이상 기기에서 수행할 코드를 작성한다.
        }

        doIfSdk(Build.VERSION_CODES.N) {
            // 안드로이드 7.0 기기에서만 수행할 코드를 작성한다.
        }
    }

    private fun testIntent() {
        /* intent 선언 방법 1 */
        // DetailActivity 액티비티를 대상 컴포넌트로 지정하니는 인텐트.
        val intent = Intent(this, DetailActivity::class.java)

        // 인텐트에 부가정보를 추가합니다.
        intent.putExtra("id", 150L)
        intent.putExtra("title", "Awesome item")

        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        /* intent 선언 방법 2 */
        val secIntent = intentFor<DetailActivity>(
            // 부가 정보를 Pair 형태로 추가합니다.
            "id" to 150L, "title" to "Awesome item"
        )
            // 인텐트 플래그 설정.
            .noHistory()

        // 부가정보 없이 액티비티 실행.
        startActivity<DetailActivity>()
        // 부가정보 포함 액티비티 실행.
        startActivity<DetailActivity>("id" to 150L, "title" to "Awesome item")
        // 부가정보 없이 실행
        startService<DataSyncService>()
        // 부가정보를 포함하여 서비스 실행
        startService<DataSyncService>("id" to 1000L)

        // 전화를 거는 인텐트 실행.
        makeCall(number = "01012345678")
        // 문자메시지 발송 인텐트 실행
        sendSMS(number = "01012345678", text = "Hello, Kotlin!")
        // 웹 페이지를 여는 인텐트 실행
        browse(url = "https://google.com")
        // 이메일을 발송하는 인텐트 실행
        email(email = "exam@gmail.com", subject = "Hello !", text = "What are you doing?")
    }

    private fun listUpButton() {
        // 뷰 ID 를 사용하여 인스턴스에 바로 접근한다.
        btn_submit.setOnClickListener {
            tv_message.text = "Hello, ${et_name.text}"
        }

        // short Toast
        btn_s_toast.setOnClickListener {
            toast("Hello, Kotlin")
        }

        // long Toast
        btn_l_toast.setOnClickListener {
            longToast("Hello, long")
        }

        // dialog
        btn_dialog.setOnClickListener {
            alert(title = "Message", message = "Lets's learn Kotlin!") {

                positiveButton("Yes") {
                    toast("Yay!")
                }

                negativeButton("No") {
                    toast("No way...")
                }
            }.show()
        }

        // list dialog
        btn_list_dialog.setOnClickListener {
            selector(title = "Selector City", items = cities) { dlg, selection ->
                toast("You Selected ${cities[selection]}")
            }
        }

        // progress dialog
        btn_pg_dialog.setOnClickListener {
            val pd = progressDialog(title = "File Download", message = "Downloading...")

            // 다이어로그 표시
            pd.show()

            // 진행률 50으로 조정
            pd.progress = 50

            // 진행률을 표시하지 않는 다이어로그 생성.
            indeterminateProgressDialog(message = "Please wait...").show()
        }
    }
}