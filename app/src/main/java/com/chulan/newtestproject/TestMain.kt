package com.chulan.newtestproject

fun main() {

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

fun exist(board: Array<CharArray>, word: String): Boolean {
    var result = false
    loop@ for (i in board.indices)
        for (j in board[i].indices) {
            val temp = dfs(board, i, j, 0, word)
            if (temp) {
                result = temp
                break@loop
            }
        }
    return result
}

val xArray = arrayOf(0, 0, -1, 1)
val yArray = arrayOf(1, -1, 0, 0)

fun dfs(board: Array<CharArray>, i: Int, j: Int, index: Int, word: String): Boolean {
    // 终结 四度都没有word[index + 1](i,j not in range judge) || index == word.size - 1(已找到)
    if (i !in board.indices || j !in board[i].indices || board[i][j] != word[index] || board[i][j] == '#') {
        return false
    }
    if (index == word.length - 1) {
        return true
    }
    val temp = board[i][j]
    board[i][j] = '#'
    // 四度下沉
    var result = false
    for (n in xArray.indices) {
        val dfs = dfs(board, i + xArray[n], j + yArray[n], index + 1, word)
        if (dfs) {
            result = true
            break
        }
    }
    // 恢复当前层级处理
    board[i][j] = temp
    return result
}

