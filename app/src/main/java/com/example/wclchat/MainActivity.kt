package com.example.wclchat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wclchat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: UserAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        auth = Firebase.auth
        val database = Firebase.database
        val myRef = database.getReference("message")
        binding.bSend.setOnClickListener {
            val messageText = binding.edMessage.text.toString().trim()

            if (messageText.isNotEmpty()) {
                val filteredMessageText = filterBadWords(messageText)
                val messageId = myRef.push().key ?: "Ne rabotaet("
                myRef.child(messageId).setValue(User(auth.currentUser?.uid, auth.currentUser?.displayName, filteredMessageText, messageId))
                binding.edMessage.text.clear()
            }
        }
        onChangeListener(myRef)
        initRcView()
        initAdapterDataObserver()
        initScrollDownFab()
        toggleScrollDownFabVisibility()

        // Настройка ActionBarDrawerToggle
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        binding.navView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.wcl_chat -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.gps_activity -> {
                    val intent = Intent(this, GPSActivity::class.java)
                    startActivity(intent)
                }
                R.id.sign_out -> {
                    auth.signOut()
                    finish()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRcView() {
        adapter = UserAdapter(auth.currentUser?.uid ?: "")
        binding.chatsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatsRecyclerView.adapter = adapter
    }

    private fun onChangeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                for (s in snapshot.children) {
                    val user = s.getValue(User::class.java)?.copy(messageId = s.key)
                    if (user != null) list.add(user)
                }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки
            }
        })
    }

    private fun filterBadWords(text: String): String {
        val badWords = listOf("сука", "блять", "епта") // Список запрещенных слов
        var filteredText = text
        badWords.forEach { badWord ->
            val regex = Regex("(?i)\\b$badWord\\b") // Создаем регулярное выражение, игнорируя регистр
            val replacement = "*".repeat(badWord.length) // Создаем строку для замены из звездочек
            filteredText = regex.replace(filteredText, replacement) // Заменяем запрещенные слова на звездочки
        }
        return filteredText
    }

    private fun initAdapterDataObserver() {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.chatsRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        })
    }

    private fun initScrollDownFab() {
        binding.scrollDownFab.setOnClickListener {
            binding.chatsRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun toggleScrollDownFabVisibility() {
        binding.chatsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition < adapter.itemCount - 1) {
                    binding.scrollDownFab.show()
                } else {
                    binding.scrollDownFab.hide()
                }
            }
        })
    }
}

