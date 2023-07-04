package com.kelompok6.newsxpress.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kelompok6.newsxpress.R
import com.kelompok6.newsxpress.adapter.NewsAdapter
import com.kelompok6.newsxpress.model.ModelArticle
import com.kelompok6.newsxpress.model.ModelNews
import com.kelompok6.newsxpress.networking.ApiEndpoint.getApiClient
import com.kelompok6.newsxpress.networking.ApiInterface
import com.kelompok6.newsxpress.util.Utils.getCountry
import kotlinx.android.synthetic.main.fragement_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragmentSports : Fragment() {

    companion object {
        const val API_KEY = "4fbbf81fbd764d0487ea339cebe0d832"
    }

    var strCategory = "sports"
    var strCountry: String? = null
    var modelArticle: MutableList<ModelArticle> = ArrayList()
    var newsAdapter: NewsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragement_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle.setText("Berita Olahraga")

        rvListNews.setLayoutManager(LinearLayoutManager(context))
        rvListNews.setHasFixedSize(true)
        rvListNews.showShimmerAdapter()

        //reload news
        imageRefresh.setOnClickListener {
            rvListNews.showShimmerAdapter()
            getListNews()
        }

        //get news
        getListNews()
    }

    //set api
    private fun getListNews() {

            //get country/
            strCountry = getCountry()

            //set api
            val apiInterface = getApiClient().create(ApiInterface::class.java)
            val call = apiInterface.getSports(strCountry, strCategory, API_KEY)
            call.enqueue(object : Callback<ModelNews> {
                override fun onResponse(call: Call<ModelNews>, response: Response<ModelNews>) {
                    if (response.isSuccessful && response.body() != null) {
                        modelArticle = response.body()?.modelArticle as MutableList<ModelArticle>
                        newsAdapter = NewsAdapter(modelArticle, context!!)
                        rvListNews.adapter = newsAdapter
                        newsAdapter?.notifyDataSetChanged()
                        rvListNews.hideShimmerAdapter()
                    }
                }

                override fun onFailure(call: Call<ModelNews>, t: Throwable) {
                    Toast.makeText(context, "Oops, jaringan kamu bermasalah.", Toast.LENGTH_SHORT).show()
                }
            })
        }

}