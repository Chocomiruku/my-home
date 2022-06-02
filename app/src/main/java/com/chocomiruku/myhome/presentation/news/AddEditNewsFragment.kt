package com.chocomiruku.myhome.presentation.news

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chocomiruku.myhome.R

class AddEditNewsFragment : Fragment() {

    companion object {
        fun newInstance() = AddEditNewsFragment()
    }

    private lateinit var viewModel: AddEditNewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_edit_news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddEditNewsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}