package com.example.gabriela_matuszewska_sr_12_30_projekt2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONArray

class GoldActivity : AppCompatActivity() {
    internal lateinit var todayGoldRate: TextView
    internal lateinit var lineChart: LineChart
    internal lateinit var data: Array<Pair<String,Double>>

    lateinit var queue1: RequestQueue
    lateinit var queue2: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold)

        todayGoldRate = findViewById(R.id.goldRate)
        lineChart = findViewById(R.id.goldChart)

        queue1 = Volley.newRequestQueue(this)
        queue2 = Volley.newRequestQueue(this)

        getGoldRates()

    }

    private fun getGoldRates() {
        val url1 = "http://api.nbp.pl/api/cenyzlota?format=json"
        val url2 = "http://api.nbp.pl/api/cenyzlota/last/30?format=json"

        val goldRate = JsonArrayRequest(
            Request.Method.GET, url1, null,
            Response.Listener { response ->
                println("Success url1")
                loadGoldRate(response)
            },
                Response.ErrorListener { error ->
                    handleNetworkError(error)
            }
        )

        val historicRates = JsonArrayRequest(
                Request.Method.GET, url2, null,
                Response.Listener { response ->
                    println("Success url2")
                    loadHistoric(response)
                    showData()
                },
                Response.ErrorListener { error ->
                    handleNetworkError(error)
                }
        )

        queue1.add(goldRate)
        queue2.add(historicRates)
    }

    private fun showData(){
        val entries = ArrayList <Entry>()
        for ((index, element) in data.withIndex()){
            entries.add(Entry(index.toFloat(),element.second.toFloat()))
        }

        val lineData = LineData(LineDataSet(entries, "Kurs"))
        lineChart.data = lineData
        val description = Description()
        description.text = "Kurs złota z ostatnich 30 dni"
        lineChart.description = description
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(data.map{it.first}.toTypedArray())
        lineChart.invalidate()
    }

    private fun loadGoldRate(response: JSONArray?) {
        response?.let {
            val rate = response.getJSONObject(0).getDouble("cena")
            todayGoldRate.text = getString(R.string.todayGoldRate,rate)
        }
    }

    private fun loadHistoric(response: JSONArray?) {
        response?.let {
            val ratesCount = response.length()
            val tmpData = arrayOfNulls<Pair<String,Double>>(ratesCount)
            for(i in 0 until ratesCount){
                val date = response.getJSONObject(i).getString("data")
                val rate = response.getJSONObject(i).getDouble("cena")

                tmpData[i] = Pair(date,rate)
            }
            this.data = tmpData as Array<Pair<String,Double>>
        }
    }

    private fun handleNetworkError(error: VolleyError) {
        throw NoConnectionError(error)
    }
}