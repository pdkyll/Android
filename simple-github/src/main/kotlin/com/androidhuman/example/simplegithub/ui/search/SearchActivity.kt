package com.androidhuman.example.simplegithub.ui.search

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import com.androidhuman.example.simplegithub.R
import com.androidhuman.example.simplegithub.api.GithubApi
import com.androidhuman.example.simplegithub.api.GithubApiProvider
import com.androidhuman.example.simplegithub.api.model.GithubRepo
import com.androidhuman.example.simplegithub.api.model.RepoSearchResponse
import com.androidhuman.example.simplegithub.ui.repo.RepositoryActivity
import com.androidhuman.example.simplegithub.ui.search.SearchAdapter.ItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), ItemClickListener {
    internal lateinit var rvList: RecyclerView
    internal lateinit var progress: ProgressBar
    internal lateinit var tvMessage: TextView
    internal lateinit var menuSearch: MenuItem
    internal lateinit var searchView: SearchView
    internal lateinit var adapter: SearchAdapter
    internal lateinit var api: GithubApi
    internal lateinit var searchCall: Call<RepoSearchResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rvList = findViewById(R.id.rvActivitySearchList)
        progress = findViewById(R.id.pbActivitySearch)
        tvMessage = findViewById(R.id.tvActivitySearchMessage)

        // 검색 결과를 표시할 어댑터를 리사이클러뷰에 설정합니다.
        adapter = SearchAdapter()
        adapter.setItemClickListener(this)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = adapter

        api = GithubApiProvider.provideGithubApi(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_search, menu)
        menuSearch = menu.findItem(R.id.menu_activity_search_query)

        // menuSearch.actionView 를 SearchView 로 캐스팅합니다.
        searchView = menuSearch.actionView as SearchView

        // 검색어를 처리할 SearchView 를 설정합니다.
        // SearchView.OnQueryTextListener 인터페이스를 구현하는
        // 익명 클래스의 인스턴스를 생성합니다.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                updateTitle(query)
                hideSoftKeyboard()
                collapseSearchView()
                searchRepository(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        // menuSearch 내 액션뷰인 SearchView 를 펼칩니다.
        menuSearch.expandActionView()
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
        val intent = Intent(this, RepositoryActivity::class.java)
        intent.putExtra(RepositoryActivity.KEY_USER_LOGIN, repository.owner.login)
        intent.putExtra(RepositoryActivity.KEY_REPO_NAME, repository.name)
        startActivity(intent)
    }

    private fun searchRepository(query: String) {
        clearResults()
        hideError()
        showProgress()
        searchCall = api.searchRepository(query)

        // Call 인터페이스를 구현하는 익명 클래스의 인스턴스를 생성합니다.
        searchCall.enqueue(object : Callback<RepoSearchResponse?> {
            override fun onResponse(call: Call<RepoSearchResponse?>,
                                    response: Response<RepoSearchResponse?>) {
                hideProgress()
                val searchResult = response.body()
                if (response.isSuccessful && null != searchResult) {
                    adapter.setItems(searchResult.items)
                    adapter.notifyDataSetChanged()
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
        })
    }

    private fun updateTitle(query: String) {
        val ab = supportActionBar
        if (null != ab) {
            ab.subtitle = query
        }
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

    private fun collapseSearchView() {
        menuSearch.collapseActionView()
    }

    private fun clearResults() {
        adapter.clearItems()
        adapter.notifyDataSetChanged()
    }

    private fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress.visibility = View.GONE
    }

    private fun showError(message: String?) {
        // message 가 널 값인 경우 "Unexpected error." 메시지를 표시합니다.
        tvMessage.text = message ?: "Unexpected error."
        tvMessage.visibility = View.VISIBLE
    }

    private fun hideError() {
        tvMessage.text = ""
        tvMessage.visibility = View.GONE
    }
}