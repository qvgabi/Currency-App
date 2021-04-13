package com.example.gabriela_matuszewska_sr_12_30_projekt2

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.blongho.country_data.Country
import com.blongho.country_data.World

object TemporaryData {
    lateinit var queue: RequestQueue
    private lateinit var countriesList: List<Country>

    fun prepare(context: Context){
        World.init(context)
        queue = Volley.newRequestQueue(context)
        countriesList = World.getAllCountries().distinctBy{ it.currency.code}
    }

    fun getFlagForCurrency(currencyCode: String): Int{
        if(currencyCode == "USD"){
            return R.drawable.us
        }else if(currencyCode == "EUR"){
            return R.drawable.eu
        }else if(currencyCode == "GBP"){
            return R.drawable.gb
        }else if(currencyCode == "CHF"){
            return R.drawable.ch
        }else if(currencyCode == "HKD"){
            return R.drawable.hk
        }else if(currencyCode == "IDR") {
            return R.drawable.id
        }else if(currencyCode == "XDR"){
            return R.drawable.us
        }else if(currencyCode == "STN"){
            return R.drawable.st
        }else if(currencyCode == "ZWL") {
            return R.drawable.zw
        }else if(currencyCode == "XPF") {
            return R.drawable.pf
        }else if(currencyCode == "GIP") {
            return R.drawable.gi
        }else if(currencyCode == "GHS") {
            return R.drawable.gh
        }else if(currencyCode == "ZMW") {
            return R.drawable.zm
        }else if(currencyCode == "ERN") {
            return R.drawable.er
        }else if(currencyCode == "TMT") {
            return R.drawable.tm
        }else if(currencyCode == "MRU") {
            return R.drawable.mr
        }else if(currencyCode == "MOP") {
            return R.drawable.mo
        }else if(currencyCode == "BYN") {
            return R.drawable.by
        }else if(currencyCode == "WST") {
            return R.drawable.ws
        }
        return countriesList.find { it.currency.code == currencyCode }?.flagResource ?:World.getWorldFlag()
    }

}