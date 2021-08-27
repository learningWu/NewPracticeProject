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
}


fun addLinkedList(linkedList: SelfLinkedList<String>) {
    linkedList.put("啊")
    linkedList.put("哦")
    linkedList.put("饿")
    linkedList.put("已")
    linkedList.put("无")
    linkedList.put("与")
    println(linkedList.toString())
}
