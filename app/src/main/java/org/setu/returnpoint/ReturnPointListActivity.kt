package org.setu.returnpoint

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReturnPointListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var adapter: ReturnPointAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_return_point_list)

        recyclerView = findViewById(R.id.pointsRecyclerView)
        emptyText = findViewById(R.id.emptyText)

        adapter = ReturnPointAdapter(
            points = AppData.returnPoints.findAll(),

            onEdit = { point ->
                editPoint(point)
            },

            onDelete = { point ->
                deletePoint(point)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val returnButton = findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        val points = AppData.returnPoints.findAll()

        adapter.updatePoints(points)

        val isEmpty = points.isEmpty()
        emptyText.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun editPoint(point: ReturnPoint) {

        val intent = Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", point.id)

        startActivity(intent)
    }

    private fun deletePoint(point: ReturnPoint) {
        AppData.returnPoints.delete(point.id)
        refreshList()
    }
}
