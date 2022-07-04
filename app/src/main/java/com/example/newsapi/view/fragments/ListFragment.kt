package com.example.newsapi.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.gson.Article
import com.example.newsapi.view.RecyclerArticleAdapter
import com.example.newsapi.viewmodel.ActivityViewModel
import com.example.newsapi.viewmodel.ListFragmentViewModel

class ListFragment : Fragment(), Observer<List<Article>> {

    private var articlesList = ArrayList<Article>()
    private lateinit var fragmentViewModel: ListFragmentViewModel
    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        fragmentViewModel = ViewModelProvider(requireActivity())[ListFragmentViewModel::class.java]
        fragmentViewModel.getAllArticles().observe(viewLifecycleOwner, this)

        activityViewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]

        adapter = RecyclerArticleAdapter(articlesList, activityViewModel)
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        recyclerView = view.findViewById(R.id.recycler)

        val rvLayoutManager = LinearLayoutManager(this.requireContext())
        val decoration = DividerItemDecoration(this.requireContext(), rvLayoutManager.orientation)

        recyclerView.apply {
            adapter = adapter
            layoutManager = rvLayoutManager
            addItemDecoration(decoration)
        }

        return view
    }

    override fun onChanged(list: List<Article>) {
        recyclerView.adapter = RecyclerArticleAdapter(list, activityViewModel)
    }
}
