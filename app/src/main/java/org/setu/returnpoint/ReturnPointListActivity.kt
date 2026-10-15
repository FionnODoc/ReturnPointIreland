package org.setu.returnpoint

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var countyFilter: AutoCompleteTextView
    private lateinit var methodFilter: AutoCompleteTextView
    private lateinit var adapter: ReturnPointAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_return_point_list)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))

        recyclerView = findViewById(R.id.pointsRecyclerView)
        emptyText = findViewById(R.id.emptyText)
        searchInput = findViewById(R.id.searchInput)
        countyFilter = findViewById(R.id.countyFilter)
        methodFilter = findViewById(R.id.methodFilter)

        adapter = ReturnPointAdapter(
            points = AppData.returnPoints.findAll(),

            onEdit = { point ->
                editPoint(point)
            },

            onDelete = { point ->
                confirmDelete(point)
            },

            onMap = { point ->
                openInMaps(point)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchInput.doAfterTextChanged {
            refreshList()
        }

        setUpFilters()

        findViewById<Button>(R.id.resetFiltersButton).setOnClickListener {
            resetFilters()
        }
    }

    private fun setUpFilters() {
        countyFilter.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                listOf(getString(R.string.all_counties)) + irishCounties
            )
        )

        methodFilter.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                listOf(getString(R.string.all_methods)) +
                    ReturnMethod.values().map { it.label }
            )
        )

        countyFilter.setText(getString(R.string.all_counties), false)
        methodFilter.setText(getString(R.string.all_methods), false)

        countyFilter.setOnItemClickListener { _, _, _, _ ->
            refreshList()
        }

        methodFilter.setOnItemClickListener { _, _, _, _ ->
            refreshList()
        }
    }

    private fun resetFilters() {
        countyFilter.setText(getString(R.string.all_counties), false)
        methodFilter.setText(getString(R.string.all_methods), false)
        searchInput.text?.clear()
        refreshList()
    }

    private fun selectedCounty(): String? {
        val text = countyFilter.text.toString()
        return if (text in irishCounties) text else null
    }

    private fun selectedMethod(): ReturnMethod? {
        val text = methodFilter.text.toString()
        return ReturnMethod.values().find { it.label == text }
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
        val county = selectedCounty()
        val method = selectedMethod()

        val searched = searchPoints(AppData.returnPoints.findAll(), query)
        val points = filterPoints(searched, county, method)

        adapter.updatePoints(points)

        val isFiltered = query.isNotBlank() || county != null || method != null
        val isEmpty = points.isEmpty()
        emptyText.setText(
            if (isFiltered) R.string.no_matches else R.string.empty_list
        )
        emptyText.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun editPoint(point: ReturnPoint) {

        val intent = Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", point.id)

        startActivity(intent)
    }

    private fun openInMaps(point: ReturnPoint) {
        val uri = Uri.parse("geo:0,0?q=" + Uri.encode(mapsQuery(point)))
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_maps_app, Toast.LENGTH_SHORT).show()
        }
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
