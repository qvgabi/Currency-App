package com.example.gabriela_matuszewska_sr_12_30_projekt2


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrencyListAdapter(var dataSet: MutableList<CurrencyDetails>, val context: Context):
    RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val currencyCodeTextView: TextView
        val rateTextView: TextView
        val flagView: ImageView
        val arrowView: ImageView


        init {
            currencyCodeTextView = view.findViewById(R.id.curName)
            rateTextView = view.findViewById(R.id.curValue)
            flagView = view.findViewById(R.id.flagView)
            arrowView = view.findViewById(R.id.arrow)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.currency_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currency = dataSet[position]
        val tableA = currency.tableA
        viewHolder.currencyCodeTextView.text = currency.currencyCode
        viewHolder.rateTextView.text = currency.rate.toString()
        viewHolder.flagView.setImageResource(currency.flag)
        if (currency.getArrow()){
            viewHolder.arrowView.setImageResource(R.drawable.green)
        }else{
            viewHolder.arrowView.setImageResource(R.drawable.red)
        }
        viewHolder.itemView.setOnClickListener{ goToDetails(currency.currencyCode, tableA)}
    }

    private fun goToDetails(currencyCode: String, tableA: Boolean){
        val intent = Intent(context, HistoricRatesActivity::class.java).apply{
            putExtra("currencyCode" , currencyCode)
            putExtra("tableA", tableA)
        }
        context.startActivity(intent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
