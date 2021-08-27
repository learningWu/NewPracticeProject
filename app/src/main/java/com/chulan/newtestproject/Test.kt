package com.chulan.newtestproject

import com.chulan.newtestproject.specialstructure.SelfLinkedList

/**
 * Created by wuzixuan on 2021/8/27
 */
fun main(){
    val linkedList = SelfLinkedList<String>()
    linkedList.insert(0, "嘻嘻哈哈")
    println(linkedList)
    addLinkedList(linkedList)
    linkedList.insert(3,"搞笑")
    println(linkedList)
    linkedList.search("搞笑")?.apply {
        println(this.value)
    }
    linkedList.search("不搞笑")?.apply {
        println(this.value)
    }
}


fun addLinkedList(linkedList: SelfLinkedList<String>) {
    linkedList.add("啊")
    linkedList.add("哦")
    linkedList.add("饿")
    linkedList.add("已")
    linkedList.add("无")
    linkedList.add("与")
    println(linkedList.toString())
}
