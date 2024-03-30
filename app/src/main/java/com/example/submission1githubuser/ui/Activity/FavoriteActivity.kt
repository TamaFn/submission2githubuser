package com.example.submission1githubuser.ui.Activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1githubuser.R
import com.example.submission1githubuser.adapter.UsersAdapter
import com.example.submission1githubuser.data.response.GithubUser
import com.example.submission1githubuser.databinding.ActivityFavoriteBinding
import com.example.submission1githubuser.model.FavoriteViewModel
import com.example.submission1githubuser.model.FavoriteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavUserBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavUserBinding
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFavUserBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        actionBar?.title = resources.getString(R.string.favoriteUser)

        binding?.rvUsersFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvUsersFavorite?.setHasFixedSize(true)

        val favoriteUserViewModel = obtainViewModel(this)
        favoriteUserViewModel.getAllUsers().observe(this) { userList ->
            if (userList.isNotEmpty()) {
                val adapter = UsersAdapter(userList)
                binding?.rvUsersFavorite?.adapter = adapter

                adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: GithubUser) {
                        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_USER, data)
                        startActivity(intent)
                    }
                })

            } else {
                binding?.rvUsersFavorite?.visibility = View.GONE
                binding?.tvNonedata?.visibility = View.VISIBLE
            }
        }
    }

    //   Berfungsi Untuk Menyalakan Fungsi Anak Panah Ke Home
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel{
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    //   Berfungsi Untuk Memanggil Fungsi Menu Option
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_favorite_menu, menu)
        return true
    }

    // Berfungsi Untuk Option Memilih Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this@FavoriteActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.darktheme -> {
                val intent = Intent(this@FavoriteActivity, DarkThemeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.deleteall -> {
                MaterialAlertDialogBuilder(this@FavoriteActivity)
                    .setTitle("Hapus Semua Favorite ?")
                    .setCancelable(true)
                    .setIcon(R.drawable.baseline_delete_24)
                    .setPositiveButton("Hapus") { _, _ ->
                        viewModel.removeAllUsers()  // <-- Menggunakan viewModel tanpa inisialisasi
                        Toast.makeText(this, R.string.favorite_remove, Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Batalkan") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}