package com.ich.pullgo.data.utils

class ListCompareUtil {
    companion object{
        fun <E>isEqualList(list1: List<E>, list2: List<E>): Boolean{
            if(list1.size != list2.size) return false

            for(i in list1.indices){
                if(list1[i] != list2[i])
                    return false
            }
            return true
        }
    }
}