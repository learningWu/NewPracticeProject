package com.chulan.newtestproject.specialstructure

import java.util.LinkedHashMap

/**
 * Created by wuzixuan on 2021/5/6
 */
class LruCache<K, V>(var maxSize: Int) {

    /**
     * accessOrder ：LinkedHashMap支持以访问先后作为排列顺序
     */
    val linkedHashMap = FixedSizeLinkedHashMap<K, V>(maxSize, 0, 0.75f, true)

    fun get(key: K): V? {
        // 取出元素
        // 将元素移到头部 linkedHashMap 中会完成移动
        val res = linkedHashMap[key]
        return res
    }

    fun save(key: K, value: V) {
        // 往头部添加此元素，最近使用
        // 如果空间溢出，移除最后最远使用的元素
        linkedHashMap[key] = value
    }

}

class FixedSizeLinkedHashMap<K, V>(var maxSize: Int, initialCapacity: Int,
                                   loadFactor: Float,
                                   accessOrder: Boolean) : LinkedHashMap<K, V>(initialCapacity, loadFactor, accessOrder) {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > maxSize
    }
}