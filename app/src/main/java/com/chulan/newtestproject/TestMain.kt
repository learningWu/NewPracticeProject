package com.chulan.newtestproject

import org.w3c.dom.Node

fun main() {
    val array = Array(4) { CharArray(5) }
    array[0] = charArrayOf('1', '1', '1', '1', '0')
    array[1] = charArrayOf('1', '1', '0', '1', '0')
    array[2] = charArrayOf('1', '1', '0', '0', '0')
    array[3] = charArrayOf('0', '0', '0', '0', '0')
    numIslands(array)
}

internal class Trie
/** Initialize your data structure here.  */
{
    val children: Array<Trie?> by lazy {
        Array<Trie?>(26) { null }
    }

    var isEnd = false

    /** Inserts a word into the trie.  */
    fun insert(word: String) {
        var node = this
        for (i in word.indices) {
            // 找到对应的子树
            val c = word[i]
            val index = c - 'a'
            if (node.children[index] == null) {
                node.children[index] = Trie()
            }
            node = node.children[index]!!
        }
        // 最后一个节点标识单词末尾
        node.isEnd = true
    }

    /** Returns if the word is in the trie.  */
    fun search(word: String): Boolean {
        val node = startsWithPrefix(word)
        return node != null && node.isEnd
    }

    /** Returns if there is any word in the trie that starts with the given prefix.  */
    fun startsWith(prefix: String): Boolean {
        return startsWithPrefix(prefix) != null
    }

    fun startsWithPrefix(prefix: String): Trie? {
        var node: Trie? = this
        for (i in prefix.indices) {
            val c = prefix[i]
            val index = c - 'a'
            if (node != null) {
                node = node.children[index]
            } else {
                break
            }
        }
        return node
    }
}

class UnionFind(var n: Int) {
    // n 为独立集合数量
    var count = 0
    val parent = IntArray(n)

    init {
        // 构造独立集合
        for (i in 0 until n) {
            parent[i] = i
        }
    }

    /**
     * 找到集合中的代表。（最顶层的node）
     */
    fun find(node: Int): Int {
        var tmpNode = node
        while (parent[tmpNode] != tmpNode) {
            tmpNode = parent[tmpNode]
        }
        return tmpNode
    }

    /**
     * 合并两个集合（将其中一个的代表的parent嫁接到另一个代表下）
     */
    fun union(a: Int, b: Int) {
        val aMark = find(a)
        val bMark = find(b)
        if (aMark == bMark) return
        parent[aMark] = bMark
        count--
    }
}

val xDirection = intArrayOf(0, 0, 1, -1)
val yDirection = intArrayOf(1, -1, 0, 0)

fun numIslands(grid: Array<CharArray>): Int {
    // 岛屿中所有元素建立为 独立集合
    // 将 二维i,j 转换为 一维 index
    val n = grid.size * grid[0].size - 1
    val uf = UnionFind(n)
    // 遍历二维数组，左下右上判断是否为1 ，1即合并（区域联通）
    var result = 0
    for (i in grid.indices)
        for (j in grid[i].indices) {
            if (grid[i][j] == '1') uf.count ++
        }
    for (i in grid.indices)
        for (j in grid[i].indices) {
            if (grid[i][j] == '1') {
                for (k in xDirection.indices) {
                    val directionX = i + xDirection[k]
                    val directionY = j + yDirection[k]
                    if (directionX in grid.indices && directionY in grid[i].indices && grid[directionX][directionY] == '1') {
                        val a = i * grid[0].size + j
                        val b = directionX* grid[0].size + directionY
                        uf.union(a, b)
                    }
                }
            }
        }
    return uf.count
}