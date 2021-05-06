package com.chulan.newtestproject.specialstructure

/**
 * Created by wuzixuan on 2021/5/6
 * 并查集
 */
class UnionFind(var n: Int) {
    /**
     * 独立集合数量
     */
    var count = 0

    /**
     * 最顶层的 parent 相同即是同一个集合
     */
    var parent = IntArray(n)

    init {
        for (i in 0 until n) {
            parent[i] = i
        }
    }

    /**
     * 找到集合标志
     */
    fun find(value: Int): Int {
        var temp = value
        while (parent[temp] != temp) {
            temp = parent[temp]
        }
        return temp
    }

    fun union(a: Int, b: Int) {
        val aMark = find(a)
        val bMark = find(b)
        if (aMark == bMark) {
            // 同一个集合
            return
        }
        parent[aMark] = bMark
        count--
    }

}