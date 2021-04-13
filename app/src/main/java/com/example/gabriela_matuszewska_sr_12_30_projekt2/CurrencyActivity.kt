package com.example.gabriela_matuszewska_sr_12_30_projekt2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray

class CurrencyActivity : AppCompatActivity() {
    internal lateinit var recycler: RecyclerView
    internal lateinit var adapter: CurrencyListAdapter
    internal lateinit var dataSet: MutableList<CurrencyDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)

        val tmpData = mutableListOf(CurrencyDetails(applicationContext,"EUR",4.56), CurrencyDetails(applicationContext,"CHF",3.95))
        recycler = findViewById(R.id.recycler)
        adapter = CurrencyListAdapter(tmpData,this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        TemporaryData.prepare(this)
        makeRequestA()
    }

    private fun makeRequestA() {
        val urlA = "http://api.nbp.pl/api/exchangerates/tables/A?format=json"

        val currencyRatesRequest = JsonArrayRequest(
            Request.Method.GET, urlA, null,
            // to w glownym watku:
            Response.Listener{ response ->
                println("Success!")
                loadDataA(response)
                adapter.dataSet = dataSet
                adapter.notifyDataSetChanged()
                makeRequestB()
            },
            Response.ErrorListener { error ->
                handleNetworkError(error)
            }
        )

        TemporaryData.queue.add(currencyRatesRequest)
    }

    private fun makeRequestB() {
        val urlB = "http://api.nbp.pl/api/exchangerates/tables/B?format=json"

        val currencyRatesRequest = JsonArrayRequest(
                Request.Method.GET, urlB, null,
                // to w glownym watku:
                Response.Listener { response ->
                    println("Success!")
                    loadDataB(response)
                    adapter.dataSet = dataSet
                    adapter.notifyDataSetChanged()
                },
                Response.ErrorListener { error ->
                    handleNetworkError(error)
                }
        )

        TemporaryData.queue.add(currencyRatesRequest)

    }

    private fun loadDataA(response: JSONArray?) {
        response?.let {
            val rates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = mutableListOf<CurrencyDetails>()
            for(i in 0 until ratesCount){
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")
                val flagID = TemporaryData.getFlagForCurrency(currencyCode)

                val currencyObject = CurrencyDetails(applicationContext,currencyCode,currencyRate,flagID,tableA = true)
                tmpData.add(currencyObject)
            }
            this.dataSet = tmpData
        }
    }

    private fun loadDataB(response: JSONArray?) {
        response?.let {
            val rates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = mutableListOf<CurrencyDetails>()
            for(i in 0 until ratesCount){
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")

                val flagID = TemporaryData.getFlagForCurrency(currencyCode)
                val currencyObject = CurrencyDetails(applicationContext,currencyCode,currencyRate,flagID,tableA = false)
                tmpData.add(currencyObject)
            }
            this.dataSet.addAll(tmpData)
        }
    }

    private fun handleNetworkError(error: VolleyError) {
        println(error.networkResponse.statusCode.toString())
    }
}