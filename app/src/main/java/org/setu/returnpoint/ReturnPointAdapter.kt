package org.setu.returnpoint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReturnPointAdapter(
    private var points: List<ReturnPoint>,
    private val onEdit: (ReturnPoint) -> Unit,
    private val onDelete: (ReturnPoint) -> Unit,
    private val onMap: (ReturnPoint) -> Unit
) : RecyclerView.Adapter<ReturnPointAdapter.PointViewHolder>() {

    class PointViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val nameText: TextView =
            itemView.findViewById(R.id.nameText)

        val locationText: TextView =
            itemView.findViewById(R.id.locationText)

        val methodText: TextView =
            itemView.findViewById(R.id.methodText)

        val demoText: TextView =
            itemView.findViewById(R.id.demoText)

        val editButton: Button =
            itemView.findViewById(R.id.editButton)

        val deleteButton: Button =
            itemView.findViewById(R.id.deleteButton)

        val mapButton: Button =
            itemView.findViewById(R.id.mapButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PointViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_return_point,
                parent,
                false
            )

        return PointViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PointViewHolder,
        position: Int
    ) {

        val point = points[position]

        holder.nameText.text = point.name

        holder.locationText.text =
            "${point.town}, Co. ${point.county}"

        holder.methodText.text = point.returnMethod.label

        holder.demoText.visibility =
            if (point.isDemo) View.VISIBLE else View.GONE

        holder.editButton.setOnClickListener {
            onEdit(point)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(point)
        }

        holder.mapButton.setOnClickListener {
            onMap(point)
        }
    }

    override fun getItemCount(): Int {
        return points.size
    }

    fun updatePoints(newPoints: List<ReturnPoint>) {
        points = newPoints
        notifyDataSetChanged()
    }
}
