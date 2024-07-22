package com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.search

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseBottomSheetFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.FragmentBottomSheetSearchBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.DialogCallBack
import com.knd.duantotnghiep.phonetrackerlocation.utils.AdapterCallBack
import java.lang.reflect.Type


class BottomSheetSearch(private val dialogCallBack: DialogCallBack.OnResultListener<UserRequest>) :
    BaseBottomSheetFragment<FragmentBottomSheetSearchBinding>(R.layout.fragment_bottom_sheet_search),
    AdapterCallBack.OnClickItemListener<UserRequest> {
    override fun getViewBinding(view: View): FragmentBottomSheetSearchBinding {
        return FragmentBottomSheetSearchBinding.bind(view)
    }

    private lateinit var listFriends: MutableList<UserRequest>
    val LIST_FRIENDS: String = "LIST_FRIENDS"


    override fun initView() {
        with(binding) {
            val listType: Type = object : TypeToken<MutableList<UserRequest>>() {}.type
            listFriends = Gson().fromJson(arguments?.getString(LIST_FRIENDS), listType)

            val searchAdapter = SearchAdapter()
            searchAdapter.setOnClickItemListener(this@BottomSheetSearch)
            searchAdapter.setData(listFriends)
            rcy.layoutManager = LinearLayoutManager(requireContext());

            rcy.adapter = searchAdapter

            searchView.requestFocus()

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchAdapter.filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchAdapter.filter.filter(newText)
                    return true
                }
            })

        }
    }

    override fun onClickItem(item: UserRequest) {
        dialogCallBack.onResult(item)
        dismiss()

    }
}