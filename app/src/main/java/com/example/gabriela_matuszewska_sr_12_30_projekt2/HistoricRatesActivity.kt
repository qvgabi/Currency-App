package com.example.gabriela_matuszewska_sr_12_30_projekt2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject

class HistoricRatesActivity : AppCompatActivity() {
    internal lateinit var todayRate: TextView
    internal lateinit var yesterdayRate: TextView
    internal lateinit var lineChartWeekly: LineChart
    internal lateinit var lineChartMonthly: LineChart
    internal lateinit var currencyCode: String
    internal var tableA: Boolean = true
    internal lateinit var data1: Array<Pair<String,Double>>
    internal lateinit var data2: Array<Pair<String,Double>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic_rates)

        todayRate = findViewById(R.id.todayRate)
        yesterdayRate = findViewById(R.id.yesterdayRate)
        lineChartWeekly = findViewById(R.id.weeklyRates)
        lineChartMonthly = findViewById(R.id.monthlyRates)
        currencyCode = intent.getStringExtra("currencyCode")!!
        tableA = intent.getBooleanExtra("tableA",true)

        println(tableA)

        val urlA1 = "http://api.nbp.pl/api/exchangerates/rates/A/%s/last/7/".format(currencyCode)
        val urlA2 = "http://api.nbp.pl/api/exchangerates/rates/A/%s/last/30/".format(currencyCode)

        val urlB1 = "http://api.nbp.pl/api/exchangerates/rates/B/%s/last/7/".format(currencyCode)
        val urlB2 = "http://api.nbp.pl/api/exchangerates/rates/B/%s/last/30/".format(currencyCode)

        TemporaryData.prepare(this)
        if (tableA){
//            getHistoricRates(urlA1,urlA2)
            val queue1 = TemporaryData.queue
            val queue2 = TemporaryData.queue

            val historicRatesRequest1 = JsonObjectRequest(
                    Request.Method.GET, urlA1, null,
                    Response.Listener { response ->
                        println("Success!!!")
                        loadData1(response)
                        showData1()
                    }, Response.ErrorListener { error ->
                handleNetworkError(error) }
            )

            val historicRatesRequest2 = JsonObjectRequest(
                    Request.Method.GET, urlA2, null,
                    Response.Listener { response ->
                        println("Success!!!")
                        loadData2(response)
                        showData2()
                    }, Response.ErrorListener { error ->
                handleNetworkError(error) }
            )

            queue1.add(historicRatesRequest1)
            queue2.add(historicRatesRequest2)

        }else{
//            getHistoricRates(urlB1,urlB2)
            val queue1 = TemporaryData.queue
            val queue2 = TemporaryData.queue

            val historicRatesRequest1 = JsonObjectRequest(
                    Request.Method.GET, urlB1, null,
                    Response.Listener { response ->
                        println("Success!!!")
                        loadData1(response)
                        showData1()
                    }, Response.ErrorListener { error ->
                handleNetworkError(error) }
            )

            val historicRatesRequest2 = JsonObjectRequest(
                    Request.Method.GET, urlB2, null,
                    Response.Listener { response ->
                        println("Success!!!")
                        loadData2(response)
                        showData2()
                    }, Response.ErrorListener { error ->
                handleNetworkError(error) }
            )

            queue1.add(historicRatesRequest1)
            queue2.add(historicRatesRequest2)

        }
    }

//    private fun getHistoricRates(url1: String, url2: String) {
//        val queue1 = TemporaryData.queue
//        val queue2 = TemporaryData.queue
//
//        val historicRatesRequest1 = JsonObjectRequest(
//            Request.Method.GET, url1, null,
//            Response.Listener { response ->
//                println("Success!!!")
//                loadData1(response)
//                showData1()
//            }, Response.ErrorListener { error ->
//            handleNetworkError(error) }
//        )
//
//        val historicRatesRequest2 = JsonObjectRequest(
//                Request.Method.GET, url2, null,
//                Response.Listener { response ->
//                    println("Success!!!")
//                    loadData2(response)
//                    showData2()
//                }, Response.ErrorListener { error ->
//            handleNetworkError(error) }
//        )
//
//        queue1.add(historicRatesRequest1)
//        queue2.add(historicRatesRequest2)
//    }

    private fun handleNetworkError(error: VolleyError?) {
        if (error != null) {
            println(error.networkResponse.statusCode.toString())
        }
    }

    private fun showData1(){
        todayRate.text = getString(R.string.todayRateText,data1.last().second)
        yesterdayRate.text = getString(R.string.yesterdayRateText,data1[data1.size-2].second)
        val entries = ArrayList < Entry>()
        for ((index, element) in data1.withIndex()){
            entries.add(Entry(index.toFloat(),element.second.toFloat()))
        }
        val lineDataSet = LineDataSet(entries, "Kurs")
        val lineData = LineData(lineDataSet)

        lineChartWeekly.data = lineData
        val description = Description()
        description.text = "Kurs %s z ostatnich 7 dni".format((currencyCode))
        lineChartWeekly.description = description
        lineChartWeekly.xAxis.valueFormatter = IndexAxisValueFormatter(data1.map{it.first}.toTypedArray())
        lineChartWeekly.setBorderColor(Color.rgb(255, 215, 0))
        lineChartWeekly.invalidate()
    }

    private fun showData2(){
        val entries = ArrayList < Entry>()
        for ((index, element) in data2.withIndex()){
            entries.add(Entry(index.toFloat(),element.second.toFloat()))
        }

        val lineData = LineData(LineDataSet(entries, "Kurs"))
        lineChartMonthly.data = lineData
        val description = Description()
        description.text = "Kurs %s z ostatnich 30 dni".format((currencyCode))
        lineChartMonthly.description = description
        lineChartMonthly.xAxis.valueFormatter = IndexAxisValueFormatter(data2.map{it.first}.toTypedArray())
        lineChartMonthly.invalidate()
    }


    private fun loadData1(response: JSONObject?) {
        response?.let {
            val rates = response.getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String,Double>>(ratesCount)
            for(i in 0 until ratesCount){
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val rate = rates.getJSONObject(i).getDouble("mid")

                tmpData[i] = Pair(date,rate)
            }
            this.data1 = tmpData as Array<Pair<String,Double>>
        }
    }

    private fun loadData2(response: JSONObject?) {
        response?.let {
            val rates = response.getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String,Double>>(ratesCount)
            for(i in 0 until ratesCount){
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val rate = rates.getJSONObject(i).getDouble("mid")

                tmpData[i] = Pair(date,rate)
            }
            this.data2 = tmpData as Array<Pair<String,Double>>
        }
    }
}