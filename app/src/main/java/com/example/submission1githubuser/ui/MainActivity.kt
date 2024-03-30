package com.example.submission1githubuser.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1githubuser.data.response.GithubUser
import com.example.submission1githubuser.R
import com.example.submission1githubuser.adapter.UsersAdapter
import com.example.submission1githubuser.databinding.ActivityMainBinding
import com.example.submission1githubuser.model.MainViewModel
import com.example.submission1githubuser.ui.Activity.DarkThemeActivity
import com.example.submission1githubuser.ui.Activity.DetailActivity
import com.example.submission1githubuser.ui.Activity.FavoriteActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter : UsersAdapter

    private var dataUser = listOf<GithubUser>()

    companion object {
        const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

//      Membuat Search Bar and Search View
        with(binding) {
            searchView.setupWithSearchBar(SearchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val query = binding.searchView.text.toString()
                    mainViewModel.getDataSearch(query)
//                    SearchBar.text = searchView.text
                    binding.searchView.hide()
                    true
                }
            SearchBar.inflateMenu(R.menu.search)
            SearchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.favorite -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.darktheme -> {
                        val intent = Intent(this@MainActivity, DarkThemeActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
                true
            }
        }

//      Mengatur Layout Manager dan Pemanggilan Adapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvAdapter.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvAdapter.addItemDecoration(itemDecoration)

//      Menghubungkan Main Activity dengan MainViewModel
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.user.observe(this) { user ->
            if (user != null) {
                dataUser = user
            }
            if (user != null) {
                binding.userNoneNotice.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
                adapter = UsersAdapter(user)
                binding.rvAdapter.adapter = adapter
                adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: GithubUser) {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_USER, data)
                        startActivity(intent)
                    }

                })
            }
        }

        mainViewModel.isLoading.observe(this@MainActivity) {
            showLoading(it)
        }

        mainViewModel.listUser.observe(this@MainActivity) { listUser ->
            binding.userNoneNotice.visibility = if (listUser.isEmpty()) View.VISIBLE else View.GONE
            adapter = UsersAdapter(listUser)
            binding.rvAdapter.adapter = adapter
            adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
                override fun onItemClicked(data: GithubUser) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USER, data)
                    startActivity(intent)
                }

            })
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.darktheme -> {
                val intent = Intent(this@MainActivity, DarkThemeActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




}