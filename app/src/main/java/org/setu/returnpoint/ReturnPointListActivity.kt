package org.setu.returnpoint

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class ReturnPointListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var searchInput: TextInputEditText
    private lateinit var adapter: ReturnPointAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_return_point_list)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))

        recyclerView = findViewById(R.id.pointsRecyclerView)
        emptyText = findViewById(R.id.emptyText)
        searchInput = findViewById(R.id.searchInput)

        adapter = ReturnPointAdapter(
            points = AppData.returnPoints.findAll(),

            onEdit = { point ->
                editPoint(point)
            },

            onDelete = { point ->
                confirmDelete(point)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchInput.doAfterTextChanged {
            refreshList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            startActivity(Intent(this, AddEditActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        val query = searchInput.text?.toString().orEmpty()
        val points = searchPoints(AppData.returnPoints.findAll(), query)

        adapter.updatePoints(points)

        val isEmpty = points.isEmpty()
        emptyText.setText(
            if (query.isBlank()) R.string.empty_list else R.string.no_matches
        )
        emptyText.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun editPoint(point: ReturnPoint) {

        val intent = Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", point.id)

        startActivity(intent)
    }

    private fun confirmDelete(point: ReturnPoint) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_dialog_title)
            .setMessage(R.string.delete_dialog_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                deletePoint(point)
            }
            .show()
    }

    private fun deletePoint(point: ReturnPoint) {
        AppData.returnPoints.delete(point.id)
        refreshList()
    }
}
