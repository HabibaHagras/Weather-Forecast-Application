package com.example.weatherforecastapplication.view

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.weatherforecastapplication.R

class FavAdapter(private val cities: MutableList<String> = mutableListOf()) : RecyclerView.Adapter<FavAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(cities[position])
        }

        override fun getItemCount(): Int = cities.size

        fun addCity(city: String) {
            cities.add(city)
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cityTextView: TextView = itemView.findViewById(R.id.text_view_city)

            fun bind(city: String) {
                cityTextView.text = city
            }
        }
    }

