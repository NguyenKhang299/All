package com.knd.duantotnghiep.testsocket.my_interface

interface AdapterCallback  {
    interface OnClickListener<O> {
        fun onClick(item: O)
    }

    interface OnLongClickListener<O> {
        fun onLongClick(item: O)
    }
}
