package com.example.weatherforecastapplication.view

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.TextView
    import androidx.cardview.widget.CardView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.weatherforecastapplication.R

class FavAdapter(private val context: FavFragment,private val listener: FavListener) : RecyclerView.Adapter<FavAdapter.FavAdapterViewHolder>() {

    private var listOfCities =mutableListOf<String>()

    fun setData(data: MutableList<String>) {
        listOfCities = data
        notifyDataSetChanged()
    }
    fun addCity(city: String) {
        listOfCities.add(city)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdapterViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_city, parent, false)
        return FavAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavAdapterViewHolder, position: Int) {
        val currentItem = listOfCities[position]

        holder.name.text = currentItem
        holder.card.setOnClickListener {
            listener.OnCLickIteamFav(currentItem)
        }
    }
    override fun getItemCount(): Int {
        return listOfCities.size
    }

    private fun getIconUrl(iconCode: String): String {
        return "https://openweathermap.org/img/w/$iconCode.png"
    }

    class FavAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.city_name)
        var card:CardView =view.findViewById(R.id.FAV_card_view)
    }

}