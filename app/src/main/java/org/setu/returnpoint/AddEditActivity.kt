package org.setu.returnpoint

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddEditActivity : AppCompatActivity() {

    private lateinit var nameInput: TextInputEditText
    private lateinit var addressInput: TextInputEditText
    private lateinit var townInput: TextInputEditText
    private lateinit var countyInput: AutoCompleteTextView
    private lateinit var methodInput: AutoCompleteTextView
    private lateinit var notesInput: TextInputEditText

    private lateinit var nameLayout: TextInputLayout
    private lateinit var addressLayout: TextInputLayout
    private lateinit var townLayout: TextInputLayout
    private lateinit var countyLayout: TextInputLayout

    private var original: ReturnPoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        nameInput = findViewById(R.id.nameInput)
        addressInput = findViewById(R.id.addressInput)
        townInput = findViewById(R.id.townInput)
        countyInput = findViewById(R.id.countyInput)
        methodInput = findViewById(R.id.methodInput)
        notesInput = findViewById(R.id.notesInput)

        nameLayout = findViewById(R.id.nameLayout)
        addressLayout = findViewById(R.id.addressLayout)
        townLayout = findViewById(R.id.townLayout)
        countyLayout = findViewById(R.id.countyLayout)

        countyInput.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                irishCounties
            )
        )

        methodInput.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                ReturnMethod.values().map { it.label }
            )
        )
        methodInput.setText(ReturnMethod.UNKNOWN.label, false)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        val id = intent.getLongExtra("id", -1L)

        if (id != -1L) {
            findViewById<TextView>(R.id.formTitle)
                .setText(R.string.edit_point)

            loadExistingPoint(id)
        }

        saveButton.setOnClickListener {
            savePoint()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun loadExistingPoint(id: Long) {

        val point = AppData.returnPoints.findOne(id)

        if (point == null) {
            Toast.makeText(
                this,
                "Return point not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        original = point

        nameInput.setText(point.name)
        addressInput.setText(point.address)
        townInput.setText(point.town)
        countyInput.setText(point.county, false)
        methodInput.setText(point.returnMethod.label, false)
        notesInput.setText(point.notes)
    }

    private fun savePoint() {

        val method = ReturnMethod.values()
            .find { it.label == methodInput.text.toString() }
            ?: ReturnMethod.UNKNOWN

        val draft = (original ?: ReturnPoint()).copy(
            name = nameInput.text.toString().trim(),
            address = addressInput.text.toString().trim(),
            town = townInput.text.toString().trim(),
            county = countyInput.text.toString(),
            returnMethod = method,
            notes = notesInput.text.toString().trim()
        )

        val errors = validate(draft)

        nameLayout.error = errors["name"]
        addressLayout.error = errors["address"]
        townLayout.error = errors["town"]
        countyLayout.error = errors["county"]

        if (errors.isNotEmpty()) {
            return
        }

        if (original == null) {
            AppData.returnPoints.create(draft)
        } else {
            AppData.returnPoints.update(draft)
        }

        Toast.makeText(
            this,
            "Return point saved",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}
