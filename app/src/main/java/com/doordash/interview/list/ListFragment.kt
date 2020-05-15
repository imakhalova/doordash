package com.doordash.interview.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.doordash.interview.DoorDashApplication
import com.doordash.interview.R
import com.doordash.interview.arch.getViewModel
import com.doordash.interview.db.FavStorage
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A fragment representing a list items received from backend in list/recycler view
 */
class ListFragment : Fragment() {

    private lateinit var _adapter : ItemAdapter
    private val favStorage: FavStorage by lazy {
        FavStorage(requireContext())
    }
    private val viewModel: MainViewModel by lazy {
        requireActivity().getViewModel() {
            MainViewModel((context?.applicationContext as DoorDashApplication).getDataFetcher(), favStorage)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        _adapter = ItemAdapter();
        recycler_view.adapter = _adapter;
        // show details view
        _adapter.setListener( object: ItemAdapter.AdapterListener{
            override fun onClick(v: View, item: ListItem) {
                viewModel.updateFavourite(item)
//                viewModel.selectedItem.value = item
//                findNavController().navigate(R.id.detail_fragment)
            }
        })
        viewModel.refresh()

        swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
            swipe_refresh.isRefreshing = false
        }

        viewModel.listItems.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                empty_list.text = getString(R.string.loading);
                empty_list.visibility = View.VISIBLE;
                showProgress(true);
            } else {
                showProgress(false);
                empty_list.text = getString(R.string.no_data);
                empty_list.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
                _adapter.submitList(it.sortedBy { item -> !item.isFav })
            }
        })
    }

    private fun showProgress(refreshing: Boolean) {
        progress_bar.visibility = if (refreshing) View.VISIBLE else View.GONE
        swipe_refresh.isRefreshing = false
    }
}
