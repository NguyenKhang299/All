package com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.viewbinding.ViewBinding
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseAdapter
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ActivityMainBinding
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ItemFriendsBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.utils.AdapterCallBack
import java.util.Locale

class SearchAdapter() : BaseAdapter<ItemFriendsBinding, UserRequest>(), Filterable {
    private var listPreventive = mutableListOf<UserRequest>()

    override fun getItemBinding(parent: ViewGroup): ItemFriendsBinding {
        return ItemFriendsBinding.inflate(LayoutInflater.from(parent.context), null, false)
    }


    override fun bind(data: UserRequest, binding: ItemFriendsBinding) {
        if (listPreventive.isEmpty()) {
            listPreventive.addAll(listData)
        }
        with(binding) {
            user = data
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (!constraint.isNullOrEmpty()) {
                    val filterList = listData.filter { user ->
                        user.name.lowercase(Locale.ROOT)
                            .contains(constraint.toString().lowercase(Locale.ROOT))
                    }
                    filterResults.values = filterList
                    filterResults.count = filterList.size
                } else {
                    setData(listPreventive)
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.values != null) {
                    setData(results.values as MutableList<UserRequest>)
                }
            }
        }
    }
}