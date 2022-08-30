package com.example.njweather

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    //url do api para pegar o JSON
    var weatherUrl = ""
    //api key para a url
    var apiId = "a29a7ac2575941128cb68ca5aaa891ce"
    private lateinit var textView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //link do textView em cada temperatura que vai ser mostrado
        textView = findViewById(R.id.textView)
        //cria uma instancia que prove uma localização do cliente
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("lat", weatherUrl)
        //no clique do botão chama a função de pegar as coordenadas
        btVar1.setOnClickListener {
            Log.e("lat", "onClick")
            //função que acha as coordenadas da ultima localização
            obtainLocation()
        }

    }
    @SuppressLint("MissingPermission")
    private fun obtainLocation(){
        Log.e("lat", "function")
        //pega a ultima localização
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                //pega a latitude e longitude e cria o https
                weatherUrl = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude +"&lon="+ location?.longitude + "&key="+ apiId
                Log.e("lat", weatherUrl.toString())
                //essa função vai dar um fetch
                getTemp()
            }
    }

    fun getTemp() {
        // instancia a RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = weatherUrl
        Log.e("lat", url)
        // Request a resposta da string para prover a URL.
        val stringReq = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.e("lat", response.toString())
                //pega o objeto JSON
                val obj = JSONObject(response)
                //pega o Array do obj
                val arr = obj.getJSONArray("data")
                Log.e("lat obj1", arr.toString())
                //pega no Json o array na posição 0
                val obj2 = arr.getJSONObject(0)
                Log.e("lat obj2", obj2.toString())
                //seta a temperatura e a cidade
                textView.text = obj2.getString("temp")+" graus Celsius em "+obj2.getString("city_name")
            },
            //em caso de algum erro
            Response.ErrorListener { textView!!.text = "Não foi possivel localizar" })
        queue.add(stringReq)
    }
}