/*
 * Copyright (C) 2019 Yubico.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doordash.interview.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import com.doordash.interview.DoorDashApplication
import com.doordash.interview.R
import com.doordash.interview.arch.getViewModel
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A fragment representing a list of Authenticators registered on web service.
 */
class ListFragment : Fragment() {

    private lateinit var _adapter : ItemAdapter
    private val viewModel: MainViewModel by lazy {
        getViewModel() {
            MainViewModel((context?.applicationContext as DoorDashApplication).getRepository())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = ItemAdapter();
        recycler_view.adapter = _adapter;
        // show details view
//        _adapter.setListener( object: ItemAdapter.AdapterListener{
//            override fun onClick(v: View, item: NetworkItem) {
//                viewModel.selectedItem.value = item
//            }
//        })
        viewModel.refresh()

        swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
            swipe_refresh.isRefreshing = false
        }

        viewModel.listItems.observe(this, Observer {
            if (it == null) {
                empty_list.text = "Loading..."
                empty_list.visibility = View.VISIBLE;
                showProgress(true);
            } else {
                showProgress(false);
                empty_list.text = getString(R.string.no_data);
                empty_list.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
                _adapter.submitList(it)
            }
        })
    }

    private fun showProgress(refreshing: Boolean) {
        progress_bar.visibility = if (refreshing) View.VISIBLE else View.GONE
        swipe_refresh.isRefreshing = false
    }
}
