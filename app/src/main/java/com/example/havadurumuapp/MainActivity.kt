package com.example.havadurumuapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener{
    lateinit var derece:TextView
    lateinit var sehir:TextView
    lateinit var aciklamaa:TextView
    lateinit var imgSimge:ImageView
    lateinit var tarih:TextView
    lateinit var spinner:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        derece=findViewById(R.id.derece)
        sehir=findViewById<TextView>(R.id.txtSehir)
        aciklamaa=findViewById<TextView>(R.id.txtAciklama)
        imgSimge=findViewById<ImageView>(R.id.imgSimge)
        tarih=findViewById<TextView>(R.id.txtTarih)
        spinner=findViewById(R.id.spinner)

        var sehirisimleri=resources.getStringArray(R.array.sehirisimleri)
        val adapter=ArrayAdapter(this,R.layout.spinner_teksatir,sehirisimleri)
            spinner.adapter=adapter
            spinner.setOnItemSelectedListener(this)

    }

    private fun verileriGetir(sehirr:String) {
        var url="https://api.openweathermap.org/data/2.5/weather?q="+sehirr+"&appid=4a853c19a4416ad4820cab8d5771bfde&lang=tr&units=Metric"
        val havaDurumuObje=JsonObjectRequest(Request.Method.GET,url,null,object:Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject?) {
                val main=response?.getJSONObject("main")
                var sicaklik=main?.getString("temp")
                derece.text=sicaklik
                var city=response?.getString("name")
                sehir.text=city
                var weather=response?.getJSONArray("weather")
                var aciklama=weather?.getJSONObject(0)?.getString("description")
                aciklamaa.text=aciklama?.toUpperCase()
                val icon =weather?.getJSONObject(0)?.getString("icon")
                var resimDosyaAdi=resources.getIdentifier("icon_"+icon?.sonKarakteriSil(),"drawable",packageName)
                imgSimge.setImageResource(resimDosyaAdi)
                tarih.text= tarih()
            }

        },object:Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
            }
        })
        MySingleton.getInstance(this).addToRequestQueue(havaDurumuObje)
    }
    fun tarih():String{
        var takvim= Calendar.getInstance().time
        var formatlaiyici=SimpleDateFormat("EEEE", Locale("tr"))
        var tarih=formatlaiyici.format(takvim)
        return tarih
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var secilensehir=parent?.getItemAtPosition(position).toString()
        verileriGetir(secilensehir)
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
private fun String.sonKarakteriSil(): String {
return  this.substring(0,this.length-1)
}
