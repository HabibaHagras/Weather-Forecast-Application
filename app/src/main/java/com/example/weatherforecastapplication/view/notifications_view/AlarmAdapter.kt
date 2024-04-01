package com.example.weatherforecastapplication.view.notifications_view
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.databinding.ItemAlarmBinding
import com.example.weatherforecastapplication.model2.Alarm

class AlarmAdapter(
    private val context: AlarmSoundFragment,
    private val listener: AlarmListener
) : RecyclerView.Adapter<AlarmAdapter.AlarmAdapterViewHolder>() {
    private var alarms = listOf<Alarm>()

    fun setData(data: List<Alarm>) {
        alarms = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlarmBinding.inflate(inflater, parent, false)
        return AlarmAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmAdapterViewHolder, position: Int) {
        val currentItem = alarms[position]

        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            listener.deleteAlarm(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return alarms.size
    }

    inner class AlarmAdapterViewHolder(private val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.alarmName.text = "Clock : ${alarm.hour.toString()} Minute: ${alarm.minute.toString()}"
        }
    }
}



