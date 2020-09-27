package com.androidhuman.example.simplegithub.ui.search

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.androidhuman.example.simplegithub.R
import com.androidhuman.example.simplegithub.api.model.GithubRepo
import com.androidhuman.example.simplegithub.api.provideGithubApi
import com.androidhuman.example.simplegithub.data.provideSearchHistoryDao
import com.androidhuman.example.simplegithub.extensions.plusAssign
import com.androidhuman.example.simplegithub.extensions.runOnIoScheduler
import com.androidhuman.example.simplegithub.rx.AutoClearedDisposable
import com.androidhuman.example.simplegithub.ui.repo.RepositoryActivity
import com.jakewharton.rxbinding3.appcompat.queryTextChangeEvents
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity

class SearchActivity : AppCompatActivity(), SearchAdapter.ItemClickListener {

    internal lateinit var menuSearch: MenuItem

    internal lateinit var searchView: SearchView

    internal val adapter by lazy { SearchAdapter().apply { setItemClickListener(this@SearchActivity) } }

    internal val api by lazy { provideGithubApi(this) }

//    internal var searchCall: Call<RepoSearchResponse>? = null

    // 여러 디스포저블 객체를 관리할 수 있는 CompositeDisposable 객체를 초기화합니다.
    // CompositeDisposable 에서 AutoClearedDisposable 로 변경합니다.
    internal val disposables = AutoClearedDisposable(this)

    // viewDisposables 프로퍼티를 추가합니다.
    // CompositeDisposable 에서 AutoClearedDisposable 로 변경합니다.
    // 액티비티가 완전히 종료되기 전까지 이벤트를 계속 받기 위해 추가합니다.
    internal val viewDisposables = AutoClearedDisposable(lifecycleOwner = this,
            alwaysClearOnStop = false)

    // SearchViewModel 을 생성할 때 필요한 뷰모델 팩토리 클래스의 인스턴스를 생성합니다.
    internal val viewModelFactory by lazy {
        SearchViewModelFactory(
                provideGithubApi(this),
                provideSearchHistoryDao(this)
        )
    }

    // 뷰모델의 인스턴스는 onCreate() 에서 받으므로, lateinit 으로 선언합니다.
    lateinit var viewModel: SearchViewModel

    // SearchHistoryDao 의 인스턴스를 받아옵니다.
    internal val searchHistoryDao by lazy { provideSearchHistoryDao(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // SearchViewModel 의 인스턴스 받습니다.
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]

        // Lifecycle.addObserver() 함수를 사용하여 각 객체를 옵저버로 등록합니다.
        lifecycle += disposables

        // viewDisposables 에서 이 액티비티의 생명주기 이벤트를 받도록 하비다.
        lifecycle += viewDisposables

        // with() 함수를 사용하여 rvActivitySearchList 범위 내에서 작업을 수행합니다.
        with(rvActivitySearchList) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.adapter
        }

        // 검색 결과 이벤트를 구독합니다.
        viewDisposables += viewModel.searchResult
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items ->
                    with(adapter) {
                        if (items.isEmpty) {
                            // 빈 이벤트를 받으면 표시되고 있던 항목을 제거합니다.
                            clearItems()
                        } else {
                            // 유효한 이벤트를 받으면 데이터를 화면에 표시합니다.
                            setItems(items.value)
                        }
                        notifyDataSetChanged()
                    }
                }

        // 메시지 이벤트를 구독합니다.
        viewDisposables += viewModel.message
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { message ->
                    if (message.isEmpty) {
                        // 빈 이벤트를 받으면 화면에 표시되고 있던 메시지를 숨깁니다.
                        hideError()
                    } else {
                        // 유효한 이벤트를 받으면 화면에 메시지를 표시합니다.
                        showError(message.value)
                    }
                }

        // 작업 진행 여부 이벤트를 구독합니다.
        viewDisposables += viewModel.isLoading
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isLoading ->
                    // 작업 진행 여부 이벤트에 따라 프로그레스바의 표시 상태를 변경합니다.
                    if (isLoading) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                }
    }

    // onStop() 함수는 더 이상 오버라이드하지 않아도 됩니다.
//    override fun onStop() {
//        super.onStop()
//        // 액티비티가 화면에서 사라지는 시점에 API 호출 객체가 생성되어 있다면
//        // API 요청을 취소합니다.
////        searchCall?.run { cancel() }
//
//        // 관리하고 있던 디스포저블 객체를 모두 해제합니다.
//        disposable.clear()
//
//        // 액티비티가 완전히 종료되는 경우에만 관리하고 있는 디스포저블을 해제합니다.
//        // 화면이 꺼지거나 다른 액티비티를 호출하여 액티비티가 화면에서 사라지는 경우에는
//        // 해제하지 않습니다.
//        if (isFinishing) {
//            viewDisposable.clear()
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_search, menu)
        menuSearch = menu.findItem(R.id.menu_activity_search_query)


        // 검색어를 처리할 SearchView 를 설정합니다.
        // SearchView.OnQueryTextListener 인터페이스를 구현하는
        // 익명 클래스의 인스턴스를 생성합니다.
        // apply() 함수를 사용하여 객체 생성과 리스터 지정을 동시에 수행합니다.
        // menuSearch.actionView 를 SearchView 로 캐스팅합니다.
        searchView = (menuSearch.actionView as SearchView)

        // SearchView 에서 발생하는 이벤트를 옵저버블 형태로 받습니다.
        viewDisposables += searchView.queryTextChangeEvents()
                // 검색을 수행했을 떄 발생한 이벤트만 받습니다.
                .filter { it.isSubmitted }

                // 이벤트에서 검색어 텍스트(CharSequence)를 추출합니다.
                .map { it.queryText }

                // 빈 문자열이 아닌 검색어만 받습니다.
                .filter { it.isNotEmpty() }

                // 검색어를 String 형태로 변환합니다.
                .map { it.toString() }

                // 이 이후에 수행되는 코드는 모두 메인 스레드에서 실행합니다.
                // RxAndroid 에서 제공하는 스케줄러인 AndroidSchedulers.mainThread() 를 사용합니다.
                .observeOn(AndroidSchedulers.mainThread())

                // 옵저버블을 구독합니다.
                .subscribe { query ->
                    // 검색 절차를 수행합니다.
                    updateTitle(query)
                    hideSoftKeyboard()
                    collapseSearchView()
                    searchRepository(query)
                }

        // with() 함수를 사용하여 menuSearch 범위 내에서 작업을 수행합니다.
        with(menuSearch) {
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    if ("" == searchView.query) {
                        finish()
                    }
                    return true
                }
            })

            expandActionView()
        }

        // 마지막으로 검색한 검색어 이벤트를 구독합니다.
        viewDisposables += viewModel.lastSearchKeyword
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { keyword ->
                    if (keyword.isEmpty) {
                        // 아직 검색을 수행하지 않은 경우 SearchView 를 펼친 상태로 유지합니다.
                        menuSearch.expandActionView()
                    } else {
                        // 검색어가 있는 경우 해당 검색ㅇ어를 액티비티의 제목으로 표시합니다.
                        updateTitle(keyword.value)
                    }
                }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_activity_search_query == item.itemId) {
            // 검색 버튼이 눌리면 SearchView 를 펼칩니다.
            item.expandActionView()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(repository: GithubRepo) {
        // 검색 결과를 선택하면 자세한 정보를 표시하는 액티비티를 실행합니다.
        // apply() 함수를 사용하여 객체 생성과 extra 를 추가하는 작업을 동시에 수행합니다.
        /*val intent = Intent(this, RepositoryActivity::class.java).apply {

            // 인텐트 부가 정보에 저장소 소유자 정보와 저자오 이름을 추가합니다.
            putExtra(RepositoryActivity.KEY_USER_LOGIN, repository.owner.login)
            putExtra(RepositoryActivity.KEY_REPO_NAME, repository.name)
        }
        startActivity(intent)*/

        // 데이터베이스에 저장소를 추가합니다
        // 데이터 조작 코드를 메인 스레드에서 호출하면 에러가 발생하므로,
        // RxJava 의 Completable 을 사용하여
        // IO 스레드에서 데이터 추가 작업을 수행하도록 합니다.
        // Completable : 옵저버블의 한 종류이며, 일반적인 Observable 과 달리
        //               이벤트 스트림에 자료를 전달하지 않습니다. 따라서
        //               SearchHistoryDao.add() 함수처럼 반환하는 값이 없는
        //               작업을 옵저버블 혀태로 표현할 때 유용합니다.
        /*disposable += Completable
                .fromCallable { searchHistoryDao.add(repository) }
                .subscribeOn(Schedulers.io())
                .subscribe()*/
        // runOnIoScheduler 함수로 IO 스케줄러에서 실행할 작업을 간단히 표현합니다.
        // ViewModel 패턴 적용하며 주석처리.
//        disposables += runOnIoScheduler { searchHistoryDao.add(repository) }

        // 선택된 저장소 정보를 데이터베이스에 추가합니다.
        disposables += viewModel.addToSearchHistory(repository)

        // --> Anko 를 사용하여 코드를 간결화하면 아래와 같다.
        // 부가정보로 전달할 항목을 함수의 인자로 바로 넣어줍니다.
        startActivity<RepositoryActivity>(
                RepositoryActivity.KEY_USER_LOGIN to repository.owner.login,
                RepositoryActivity.KEY_REPO_NAME to repository.name
        )
    }

    private fun searchRepository(query: String) {
        // Retrofit Call 객체를 사용하였을 때 아래와 같이 사용하였다.
        /*clearResults()
        hideError()
        showProgress()

        searchCall = api.searchRepository(query)
        // 앞에서 API 호출에 필요한 객체를 받았으므로,
        // 이 시점에서 searchCall 객체의 값은 널이 아닙니다.
        // 따라서 비 널 값 보증(!!)을 사용하여 이 객체를 사용합니다.
        // Call 인터페이스를 구현하는 익명 클래스의 인스턴스를 생성합니다.
        searchCall!!.enqueue(object : Callback<RepoSearchResponse?> {
            override fun onResponse(call: Call<RepoSearchResponse?>,
                                    response: Response<RepoSearchResponse?>) {
                hideProgress()
                val searchResult = response.body()
                if (response.isSuccessful && null != searchResult) {
                    with(adapter) {
                        setItems(searchResult.items)
                        notifyDataSetChanged()
                    }
                    if (0 == searchResult.totalCount) {
                        showError(getString(R.string.no_search_result))
                    }
                } else {
                    showError("Not successful: " + response.message())
                }
            }

            override fun onFailure(call: Call<RepoSearchResponse?>, t: Throwable) {
                hideProgress()

                // showError() 함수는 널 값을 허용하지 않으나,
                // t.message 는 널 값을 반환할 수 있다.
                showError(t.message)
            }
        })*/

        // REST API 를 통해 검색 결과를 요청합니다.
        // '+=' 연산자로 디스포저블을 CompositeDisposable 에 추가합니다.
        // ViewModel 패턴 적용으로 주석처리.
//        disposables += api.searchRepository(query)
//
//                // Observable 형태로 결과를 바꿔주기 위해 flatMap 을 사용합니다.
//                .flatMap {
//                    if (0 == it.totalCount) {
//                        // 검색 결과가 없을 경우
//                        // 에러를 발생시켜 에러 메시지를 표시하도록 합니다.
//                        // (곧바로 에러 블록이 실행됩니다.)
//                        Observable.error(IllegalStateException("No search result."))
//                    } else {
//                        // 검색 결과 리스트를 다음 스트림으로 전달합니다.
//                        Observable.just(it.items)
//                    }
//                }
//
//                // 이 이후에 수행되는 코드는 모두 메인 스레드에서 실행합니다.
//                // RxAndroid 에서 제공하는 스케줄러인
//                // AndroidSchedulers.mainThread() 를 사용합니다.
//                .observeOn(AndroidSchedulers.mainThread())
//
//                // 구독할 때 수행할 작업을 구현합니다.
//                .doOnSubscribe {
//                    clearResults()
//                    hideError()
//                    showProgress()
//                }
//
//                // 스트림이 종료될 때 수행할 작업을 구현합니다.
//                .doOnTerminate { hideProgress() }
//
//                // 옵저버블을 구독합니다. items : List<GithubRepo>
//                .subscribe({ items ->
//                    // API 를 통해 검색 결과를 정상적으로 받았을 때 처리할 작업을 구현합니다.
//                    // 작업 중 오류가 발생하면 이 블록은 호출되지 않습니다.
//                    with(adapter) {
//                        setItems(items)
//                        notifyDataSetChanged()
//                    }
//                }) {
//                    // 에러 블록
//                    // 네트워크 오류나 데이터 처리 오류 등
//                    // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
//                    showError(it.message)
//                }

        // 전달받은 검색어로 검색 결과를 요청합니다.
        disposables += viewModel.searchRepository(query)

    }

    private fun updateTitle(query: String) {
        // 별도의 변수 선언 없이,
        // getSupportActionBar() 의 반환값이 널이 아닌 경우에만 작업을 수행합니다.
        supportActionBar?.run { subtitle = query }
    }

    private fun hideSoftKeyboard() {
        // 별도의 변수 선언 없이 획득한 인스턴스의 범위 내에서 작업을 수행합니다.
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(searchView.windowToken, 0)
        }
    }

    private fun collapseSearchView() {
        menuSearch.collapseActionView()
    }

    private fun clearResults() {
        // with() 함수를 사용하여 adapter 범위 내에서 작업을 수행합니다.
        with(adapter) {
            clearItems()
            notifyDataSetChanged()
        }
    }

    private fun showProgress() {
        pbActivitySearch.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        pbActivitySearch.visibility = View.GONE
    }

    private fun showError(message: String?) {
        // message 가 널 값인 경우 "Unexpected error." 메시지를 표시합니다.
        // with() 함수를 사용하여 tvActivitySearchMessage 범위 내에서 작업을 수행합니다.
        with(tvActivitySearchMessage) {
            text = message ?: "Unexpected error."
            visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        // with() 함수를 사용하여 tvActivitySearchMessage 범위 내에서 작업을 수행합니다.
        with(tvActivitySearchMessage) {
            text = ""
            visibility = View.GONE
        }
    }
}