package com.example.gabriela_matuszewska_sr_12_30_projekt2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import java.math.RoundingMode

class CalculatorCurrencyActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{
    internal lateinit var currencyScroll1: Spinner
    internal lateinit var plnValue1: EditText
    internal lateinit var otherValue1: TextView
    internal lateinit var calculateFromPLN: Button

//    internal lateinit var dataSet: Array<CurrencyDetails>
    internal lateinit var dataSetCode: MutableList<String?>
    internal var currentCurFromPLN: Double = 0.0
    internal lateinit var data: MutableList<Pair<String,Double>>

    private var plnValueText: Double = 0.0

    internal lateinit var currencyScroll2: Spinner
    internal lateinit var plnValue2: TextView
    internal lateinit var otherValue2: EditText
    internal lateinit var calculateToPLN: Button

    private var otherValueText: Double = 0.0
    internal var currentCurToPLN: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_currency)

        currencyScroll1 = findViewById(R.id.spinnerFromPLN)
        plnValue1 = findViewById(R.id.plnInput)
        otherValue1 = findViewById(R.id.valFromPLN1)
        calculateFromPLN = findViewById(R.id.buttonFromPLN)
        calculateFromPLN.setOnClickListener { calculateFromPLNfunction() }

        currencyScroll2 = findViewById(R.id.spinnerFromPLN2)
        plnValue2 = findViewById(R.id.valToPLN)
        otherValue2 = findViewById(R.id.otherInput)
        calculateToPLN = findViewById(R.id.buttonToPLN)
        calculateToPLN.setOnClickListener { calculateToPLNfunction() }

        TemporaryData.prepare(this)

        makeRequestA()

    }

    private fun calculateToPLNfunction() {
        otherValueText = otherValue2.text.toString().toDouble()
        plnValue2.text = (otherValueText * currentCurToPLN).toBigDecimal().setScale(2,RoundingMode.UP).toDouble().toString()
    }

    private fun calculateFromPLNfunction() {
        plnValueText = plnValue1.text.toString().toDouble()
        otherValue1.text = (plnValueText / currentCurFromPLN).toBigDecimal().setScale(2,RoundingMode.UP).toDouble().toString()

    }

    private fun makeRequestA() {
        val urlA = "http://api.nbp.pl/api/exchangerates/tables/A?format=json"

        val currencyRatesRequestA = JsonArrayRequest(
            Request.Method.GET, urlA, null,
            // to w glownym watku:
            Response.Listener{ response ->
                println("Success!")
                loadDataA(response)
                makeRequestB()
            },
            Response.ErrorListener { error ->
                handleNetworkError(error)
            }
        )
        TemporaryData.queue.add(currencyRatesRequestA)
    }

    private fun makeRequestB(){
        val urlB = "http://api.nbp.pl/api/exchangerates/tables/B?format=json"

        val currencyRatesRequestB = JsonArrayRequest(
                Request.Method.GET, urlB, null,
                // to w glownym watku:
                Response.Listener{ response ->
                    println("Success!")
                    loadDataB(response)
                },
                Response.ErrorListener { error ->
                    handleNetworkError(error)
                }
        )
        TemporaryData.queue.add(currencyRatesRequestB)

    }

    private fun loadDataA(response: JSONArray?) {
        response?.let {
            val rates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()

            val tmpCode = mutableListOf<String?>()
            val tmpD = mutableListOf<Pair<String,Double>>()

            for(i in 0 until ratesCount){
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")

                tmpCode.add(currencyCode)
                tmpD.add(Pair(currencyCode,currencyRate))
            }
            this.dataSetCode = tmpCode
            this.data = tmpD
        }
    }

    private fun loadDataB(response: JSONArray?) {
        response?.let {
            val rates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()

            val tmpCode = mutableListOf<String?>()
            val tmpD = mutableListOf<Pair<String,Double>>()

            for(i in 0 until ratesCount){
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")

                tmpCode.add(currencyCode)
                tmpD.add(Pair(currencyCode,currencyRate))
            }
            this.dataSetCode.addAll(tmpCode)
            this.data.addAll(tmpD)

        }
        val aa = ArrayAdapter(this,R.layout.my_spinner,dataSetCode)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencyScroll1.adapter = aa
        currencyScroll1.onItemSelectedListener = this
        currencyScroll2.adapter = aa
        currencyScroll2.onItemSelectedListener = this
    }

    private fun handleNetworkError(error: VolleyError) {
        println(error.networkResponse.statusCode.toString())
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View, position: Int, id: Long) {
        // use position to know the selected item
        val item1 = currencyScroll1.selectedItem.toString()
        val item2 = currencyScroll2.selectedItem.toString()
        for (element in data) {
            if(element.first == item1){
                this.currentCurFromPLN = element.second
            }
            if(element.first == item2){
                this.currentCurToPLN = element.second
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}