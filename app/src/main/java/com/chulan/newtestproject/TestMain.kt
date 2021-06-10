package com.chulan.newtestproject

import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun main() {
//    val array = Array(4) { CharArray(5) }
//    array[0] = charArrayOf('1', '1', '1', '1', '0')
//    array[1] = charArrayOf('1', '1', '0', '1', '0')
//    array[2] = charArrayOf('1', '1', '0', '0', '0')
//    array[3] = charArrayOf('0', '0', '0', '0', '0')
//    numIslands(array)
//    val bloomFilter = BloomFilter(3, 1024)
//    bloomFilter.add(360)
//    if (bloomFilter.contains(360)) {
//        print("有360")
//    }
//    if (bloomFilter.contains(361)) {
//        print("有361")
//    } else {
//        print("没有361 ")
//    }
//    bloomFilter.clear()
//    if (bloomFilter.contains(360)) {
//        print("有360")
//    } else {
//        print("没有360")
//    }
    lengthOfLastWord("Hello world")
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

fun solveNQueens(n: Int): List<List<String>> {
    // 把皇后放到一个空位，校验是否可以，可以进入下一层，下一层返回ok就成功，否则清除当前皇后，尝试下一个可放置的皇后位置
    // 构建棋盘
    val matrix = Array(n) { CharArray(n) { '.' } }
    solve(matrix)
    return result
}

val visited = HashSet<Int>()
val result = LinkedList<LinkedList<String>>()
fun solve(matrix: Array<CharArray>): Boolean {
    for (i in matrix.indices)
        for (j in matrix[i].indices) {
            if (!visited.contains(i * matrix[0].size + j) && matrix[i][j] == '.') {
                visited.add(i * matrix[0].size + j)
                // 空地可以下皇后
                if (isValidQueen(i, j, matrix)) {
                    matrix[i][j] = 'Q'
                    if (solve(matrix)) {
                        printChessBoardString(matrix)
                        return true
                    }
                    matrix[i][j] = '.'
                }
            }
        }
    return false
}

fun printChessBoardString(matrix: Array<CharArray>) {
    val linkedList = LinkedList<String>()
    for (i in matrix.indices) {
        linkedList.add(matrix[i].joinToString { it.toString() })
    }
    result.add(linkedList)
}

val xPieNa = intArrayOf(1, 1, -1, -1)
val yPieNa = intArrayOf(1, -1, 1, -1)

fun isValidQueen(i: Int, j: Int, matrix: Array<CharArray>): Boolean {
    var result = true
    // i 行有皇后
    if (matrix[i].contains('Q')) result = false
    // j 列有皇后
    for (k in matrix.indices) {
        if (matrix[k][j] == 'Q')
            result = false
    }
    // 斜线
    for (k in xPieNa) {
        val xDir = i + xPieNa[k]
        val yDir = j + yPieNa[k]
        if (assertXYValid(xDir, yDir, arrayOf(matrix)) { matrix[xDir][yDir] == 'Q' }) {
            result = false
        }
    }
    return result
}

fun assertXYValid(x: Int, y: Int, matrix: Array<Array<*>>, block: () -> Boolean): Boolean {
    var result = false
    if (x in matrix.indices && y in matrix[x].indices) {
        result = block()
    }
    return result
}


class UnionFind(var n: Int) {
    var count = 0
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


val xDirection = intArrayOf(0, 0, 1, -1)
val yDirection = intArrayOf(1, -1, 0, 0)

fun numIslands(grid: Array<CharArray>): Int {
    val n = grid.size * grid[0].size
    val uf = UnionFind(n)
    for (i in grid.indices)
        for (j in grid[i].indices) {
            if (grid[i][j] == '1')
                uf.count++
        }
    for (i in grid.indices)
        for (j in grid[i].indices) {
            if (grid[i][j] == '1') {
                for (k in xDirection.indices) {
                    val xForward = i + xDirection[k]
                    val yForward = j + yDirection[k]
                    if (xForward in grid.indices && yForward in grid[i].indices && grid[xForward][yForward] == '1') {
                        //相同集合，合并
                        uf.union(
                            matrix2FlatPoi(i, j, grid[i].size),
                            matrix2FlatPoi(xForward, yForward, grid[i].size)
                        )
                    }
                }
            }
        }
    return uf.count
}

fun matrix2FlatPoi(x: Int, y: Int, row: Int) = x * row + y


class Node {
    var value: Int = 0
    var children: List<Node>? = null
}

/**
 * 双向 BFS
 */
fun doubleBFS() {
    // 在单向BFS基础上，选择两侧中，较少可选路径进行 扩散（相当于剪枝）
    var front = HashSet<Node>()
    var back = HashSet<Node>()
    var temp: HashSet<Node>
    // 开始
    front.add(Node())
    // 目标点
    back.add(Node())
    val visitedSet = HashSet<Node>()
    // 退出条件
    // 两个方向都没有可遍历的节点（都被 visited 了）
    var level = 1
    while (front.isNotEmpty() && back.isNotEmpty()) {
        // 交换遍历队列
        if (back.size < front.size) {
            temp = front
            front = back
            back = temp
        }
        val next = HashSet<Node>()
        for (node in front) {
            if (node !in visitedSet) {
                visitedSet.add(node)
                // 将下一层先缓存
                node.children?.let { next.addAll(it) }
            }
        }
        front = next
        level++
    }
}

fun hammingWeight(n: Int): Int {
    // 方法 1 ：将末尾 1 打掉。直到数值为0
//    var value = n
//    var count = 0
//    while (value != 0) {
//        count++
//        value = value and value - 1
//    }
//    return count
    // 方法 2：遍历二进制位，计1个数
    var count = 0
    for (i in 0..31) {
        if (n and 1.shl(i) > 0)
            count++
    }
    return count
}

fun isPowerOfTwo(n: Int): Boolean {
    if (n == 0) return false
    return (n and n - 1) == 0
}

fun reverseBits(n: Int): Int {
    var res = 0
    for (i in 0 until 32) {
        res += ((n shr i) and 1).shl(31 - i)
    }
    return res
}


/**
 * 布隆过滤器
 */
class BloomFilter(var n: Int, var m: Int) {
    var bitSet: BitSet = BitSet(m)
    val randomHelper by lazy {
        RandomHelper(n, m)
    }

    fun add(o: Any) {
        randomHelper.init(o)
        for (value in randomHelper) {
            bitSet[value] = true
        }
    }

    fun contains(o: Any): Boolean {
        randomHelper.init(o)
        for (value in randomHelper) {
            if (!bitSet[value])
                return false
        }
        return true
    }

    fun clear() {
        bitSet.clear()
    }

    class RandomHelper(var maxCount: Int, var randomArea: Int) : Iterator<Int> {
        val random = Random()
        var count = 0
        fun init(o: Any) {
            count = 0
            random.setSeed(o.hashCode().toLong())
        }

        override fun next(): Int {
            return Math.abs(random.nextInt() % randomArea)
        }

        override fun hasNext(): Boolean {
            return count++ < maxCount
        }
    }
}

fun sortArray(nums: IntArray): IntArray {
    quickSort(nums, 0, nums.size - 1)
    return nums
}

fun quickSort(nums: IntArray, left: Int, right: Int) {
    if (right <= left) return
    var pivot = right
    var slow = left
    for (i in left until pivot) {
        if (nums[i] < nums[pivot]) {
            if (i != slow) {
                nums[i] = nums[slow].apply {
                    nums[slow] = nums[i]
                }
            }
            slow++
        }
    }
    nums[pivot] = nums[slow].apply {
        nums[slow] = nums[pivot]
    }
    pivot = slow

    // 重复左右两边
    quickSort(nums, left, pivot - 1)
    quickSort(nums, pivot + 1, right)
}

fun longestCommonSubsequence(text1: String, text2: String): Int {
    val m = text1.length + 1
    val n = text2.length + 1
    val dp = Array(m) { IntArray(n) }
    for (i in 1 until m)
        for (j in 1 until n) {
            if (text1[i - 1] == text2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1] + 1
            } else {
                dp[i][j] = arrayOf(dp[i - 1][j - 1], dp[i - 1][j], dp[i][j - 1]).maxOrNull()!!
            }
        }
    return dp[m - 1][n - 1]
}

fun longestPalindrome(s: String): String {
    // 从后往前循环的原因：起终点i,j 元素相等 => dp[i][j] = dp[i+1][j-1]是回文 ，所以要先已知大于当前 i 的循环结果。从前往后的循环是无法得到这种推论
    // i,j为
    val dp = Array(s.length) { BooleanArray(s.length) }
    var res = ""
    for (i in s.length - 1 downTo 0)
        for (j in i until s.length - 1) {
            dp[i][j] = s[i] == s[j] && (j - i < 1 || dp[i + 1][j - 1])
            if (dp[i][j] && j - i + 1 > res.length) {
                res = s.substring(i, j + 1)
            }
        }
    return res
}

fun minDistance(word1: String, word2: String): Int {
    if (word1.isEmpty()) return word2.length
    if (word2.isEmpty()) return word1.length
    // 二维dp
    // 二维矩阵的两条边
    // "" 和 word2 => 0..word2.length
    // word1 和 "" => 0..word1.length
    val m = word1.length + 1
    val n = word2.length + 1
    val dp = Array(m) { IntArray(n) }
    // 初始化两条边
//    for (i in 0 until m) dp[i][0] = i
//    for (j in 0 until n) dp[0][j] = j
    for (i in 1 until m) {
        dp[i][0] = i
        for (j in 1 until n) {
            dp[0][j] = j
            if (word1[i - 1] == word2[j - 1]) {
                // 元素相等时，无须编辑
                dp[i][j] = dp[i - 1][j - 1]
            } else {
                // 元素不相等时
                // 删除任一字符串字符 : dp[i - 1][j], dp[i][j - 1]
                // 或 替换 : dp[i - 1][j - 1]
                dp[i][j] = arrayOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]).min()!! + 1
            }
        }
    }
    return dp[m - 1][n - 1]
}

fun reverseOnlyLetters(s: String): String {
    var left = 0
    var right = s.length - 1
    val array = s.toCharArray()
    while (left < right) {
        if (!array[left].isLetter()) {
            left++
            continue
        }
        if (!array[right].isLetter()) {
            right--
            continue
        }
        array[left] = array[right].apply {
            array[right] = array[left]
        }
        left++
        right--
    }
    return String(array)
}

fun reverseWords(s: String): String {
    return s.split(" ").map { it.reversed() }.joinToString(" ")
}

fun firstUniqChar(s: String): Int {
    val array = IntArray(26)
    s.forEach { array[it - 'a']++ }
    s.forEachIndexed { index, c ->
        if (array[c - 'a'] == 1) return index
    }
    return -1
}

fun myAtoi(s: String): Int {
    var startCompute = false
    var isNeg = false
    var res = 0L
    loop@ for (c in s) {
        if (startCompute && c !in '0'..'9') {
            break
        }
        when (c) {
            '+', '-' -> {
                startCompute = true
                isNeg = c == '-'
            }
            ' ' -> continue@loop
            in '0'..'9' -> {
                startCompute = true
                res = res * 10 + (c - '0')
                if (isNeg && -res <= Int.MIN_VALUE || (!isNeg && res >= Int.MAX_VALUE)) break@loop
            }
            else -> break@loop
        }
    }
    return if (isNeg) {
        Math.max(Int.MIN_VALUE.toLong(), -res).toInt()
    } else {
        Math.min(Int.MAX_VALUE.toLong(), res).toInt()
    }
}

fun reverseStr(s: String, k: Int): String {
    var reverseStart = 0
    val array = s.toCharArray()
    while (reverseStart in array.indices) {
        reverse(array, reverseStart, Math.min(array.size - 1, reverseStart + k - 1))
        reverseStart += 2 * k
    }
    return String(array)
}

fun reverse(charArray: CharArray, start: Int, end: Int) {
    var i = start
    var j = end
    while (i < j) {
        charArray[i] = charArray[j].apply {
            charArray[j] = charArray[i]
        }
        i++
        j--
    }
}

fun isIsomorphic(s: String, t: String): Boolean {
    val map = HashMap<Char, Char>()
    for (i in s.indices) {
        if ((map.contains(s[i]) || map.containsValue(t[i])) && map[s[i]] != t[i]) {
            return false
        }
        map[s[i]] = t[i]
    }
    return true
}

var isSaved = false
fun validPalindrome(s: String): Boolean {
    var left = 0
    var right = s.length - 1
    while (left < right) {
        if (s[left] != s[right]) {
            var res = false
            if (!isSaved) {
                isSaved = true
                left + 1 in s.indices && validPalindrome(s.substring(left + 1, right)).also {
                    if (it) res = it
                }
                right - 1 in s.indices && validPalindrome(s.substring(left, right - 1)).also {
                    if (it) res = it
                }
            }
            return res
        }
        left++
        right--
    }
    return true
}

fun findAnagrams(s: String, p: String): List<Int> {
    //s: "cbaebabacd" p: "abc"
    // 滑动窗口
    val sArray = IntArray(26)
    val pArray = IntArray(26)
    val sCharArray = s.toCharArray()
    val pCharArray = p.toCharArray()
    val m = pCharArray.size
    val result = ArrayList<Int>()
    if (s.length < p.length) return result

    for (i in p.indices) {
        sArray[sCharArray[i] - 'a']++
        pArray[pCharArray[i] - 'a']++
    }
    if (sArray.contentEquals(pArray)) {
        result.add(0)
    }

    for (i in m until sCharArray.size) {
        // 移动滑动窗口
        sArray[sCharArray[i - m] - 'a']--
        sArray[sCharArray[i] - 'a']++
        if (sArray.contentEquals(pArray)) {
            result.add(i - m + 1)
        }
    }
    return result
}

fun longestValidParentheses(s: String): Int {
    val cArray = s.toCharArray()
    if (cArray.size <= 1) return 0
    val dp = IntArray(cArray.size)
    var max = 0
    for (i in 1 until cArray.size) {
        if (cArray[i] == ')') {
            when (cArray[i - 1]) {
                '(' -> dp[i] = 2 + if (i >= 2) dp[i - 2] else 0
                ')' -> {
                    val lastCouldPairIndex = i - dp[i - 1] - 1
                    if (lastCouldPairIndex >= 0) {
                        if (cArray[lastCouldPairIndex] == '(') {
                            dp[i] =
                                dp[i - 1] + 2 + if (lastCouldPairIndex - 1 >= 0) dp[lastCouldPairIndex - 1] else 0
                        }
                    }
                }
            }
            max = Math.max(dp[i], max)
        }
    }
    return max
}

fun match(p: String, s: String, i: Int, j: Int): Boolean {
    // j == -1 二维中指向的是 ""
    if (j < 0) return false
    return if (p[i] == '.') true else p[i] == s[j]
}

fun toLowerCase(s: String): String {
    val array = s.toCharArray()
    for (i in array.indices) {
        if (array[i] in 'A'..'Z') {
            array[i] = array[i] + ('a' - 'A')
        }
    }
    return String(array)
}

fun lengthOfLastWord(s: String): Int {
    return s.trim().split(Regex(" +")).last().length
}

fun numJewelsInStones(jewels: String, stones: String): Int {
    val map = HashMap<Char, Int>()
    stones.forEach {
        if (map[it] == null) {
            map[it] = 1
        } else {
            map[it]?.apply {
                map[it] = this + 1
            }
        }
    }
    var result = 0
    jewels.forEach {
        map[it]?.apply {
            if (this > 0) result += this
        }
    }
    return result
}

fun maxValue(n: String, x: Int): String {
    val sb = StringBuilder(n)
    var isNeg = false
    for (i in sb.indices) {
        if (sb[i] == '-') {
            isNeg = true
            continue
        }
        if (isNeg && x < sb[i] - '0' || !isNeg && x > sb[i] - '0') {
            sb.insert(i, x)
            break
        }
    }
    return if (sb.length > n.length) sb.toString() else sb.append(x).toString()
}

fun isMatch(s: String, p: String): Boolean {
    val m = p.length + 1
    val n = s.length + 1
    val dp = Array(m) { BooleanArray(n) }
    dp[0][0] = true
    for (i in 1 until m)
        for (j in 0 until n) {
            if (j == 0) {
                if (p[i - 1] == '*') dp[i][j] = dp[i - 1][j]
                continue
            }
            when (p[i - 1]) {
                '*' -> {
                    dp[i][j] = dp[i - 1][j] || dp[i][j - 1]
                }
                '?', s[j - 1] -> {
                    dp[i][j] = dp[i - 1][j - 1]
                }
            }
        }
    return dp[m - 1][n - 1]
}

fun numDecodings(s: String): Int {
    // dp[i] ：包含i字符 从 0..i 的字符，组队数
    // 和前面是字符组队： dp[i] = dp[ i - 2] ，自己单独一个：dp[i] = dp[i - 1] => dp[i] = dp[i - 2] + dp[i - 1]
    // s[i] 是 0：dp[i] = dp[ i - 2]
    val temp = "  $s"
    val dp = IntArray(temp.length)
    dp[0] = 1
    dp[1] = 1
    for (i in 2 until temp.length) {
        if (temp[i] == '0') {
            if (temp[i - 1] in '1'..'2') {
                // 0 只能看看能不能和前面一个组队
                dp[i] = dp[i - 2]
            }
        } else {
            // 其它元素可以选择先自己一个人 (dp[i - 1] : 除开自己，你们先组好吧，我后面加入就行)
            dp[i] = dp[i - 1]
            if (temp[i - 1] == '1' || temp[i - 1] == '2' && temp[i] < '7') {
                // 选择和前面一个元素组队（dp[i - 2] : 把 i - 1 留给我跟我组队，你们组好先）
                dp[i] = dp[i] + dp[i - 2]
            }
        }
    }
    return dp[temp.length - 1]
}

fun numDistinct(s: String, t: String): Int {
    // dp[i][j] : t[0..i] 和 s[0..j] 匹配的子序列数量
    val m = t.length + 1
    val n = s.length + 1
    val dp = Array(m) { IntArray(n) }
    // 每个字符串里都有一个子序列（空串）
    for (i in 0 until n) {
        dp[0][i] = 1
    }
    for (i in 1 until m)
        for (j in 1 until n) {
            if (t[i - 1] == s[j - 1]) {
                // 可以匹配有两种组合情况 1. dp[i - 1][j - 1] : 匹配掉，包括我作为和你配对的子序列
                // 2. dp[i][j - 1] : 取前面能和你配对的子序列
                dp[i][j] = dp[i - 1][j - 1] + dp[i][j - 1]
            } else {
                // 我没办法和你配对，只能看看前面有没有人和你配对成功
                dp[i][j] = dp[i][j - 1]
            }
        }
    return dp[m - 1][n - 1]
}