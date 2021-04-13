package com.example.gabriela_matuszewska_sr_12_30_projekt2

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

data class CurrencyDetails(val context: Context, var currencyCode: String, var rate: Double, var flag: Int = 0, var tableA: Boolean = true) {
    private var arrow: Boolean = false
    var queue: RequestQueue = Volley.newRequestQueue(context)

    init {
        getPastRates()
    }

    private fun getPastRates() {
        if(tableA){
            val url = "http://api.nbp.pl/api/exchangerates/rates/A/%s/last/2/".format(currencyCode)
            val pastRatesRequest = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    Response.Listener { response ->
                        println("Success!!!")
                        loadData(response)
                    }, Response.ErrorListener { error ->
                handleNetworkError(error) }
            )
            queue.add(pastRatesRequest)
        }else{
            val url = "http://api.nbp.pl/api/exchangerates/rates/B/%s/last/2/".format(currencyCode)
            val pastRatesRequest = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    Response.Listener { response ->
                        println("Success!!!")
                        loadData(response)
                    }, Response.ErrorListener { error ->
                handleNetworkError(error)}
            )
            queue.add(pastRatesRequest)
        }
    }

    private fun handleNetworkError(error: VolleyError?) {
        throw NoConnectionError(error)
    }

    private fun loadData(response: JSONObject?) {
        response?.let {
            val rates = response.getJSONArray("rates")
            val rate1 = rates.getJSONObject(0).getDouble("mid")
            val rate2 = rates.getJSONObject(1).getDouble("mid")
            this.arrow = rate2 > rate1
        }
    }

    fun getArrow(): Boolean {
        return arrow
    }

}