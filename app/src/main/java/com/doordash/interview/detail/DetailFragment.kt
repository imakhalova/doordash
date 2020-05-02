package com.doordash.interview.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.doordash.interview.DoorDashApplication

import com.doordash.interview.R
import com.doordash.interview.arch.getViewModel
import com.doordash.interview.list.MainViewModel
import kotlinx.android.synthetic.main.detail_fragment.*
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private val viewModel: MainViewModel by lazy {
        requireActivity().getViewModel() {
            MainViewModel((context?.applicationContext as DoorDashApplication).getDataFetcher())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.selectedItem.observe(viewLifecycleOwner, Observer {
            viewModel.getDetails(it).observe(viewLifecycleOwner, Observer {
                text.text = String.format(Locale.getDefault(), "%s : %s",
                    it.name, it.phone_number)
            })
        })
    }
}
