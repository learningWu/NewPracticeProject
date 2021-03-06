package com.chulan.newtestproject

import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

@RequiresApi(Build.VERSION_CODES.N)
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
    val array = arrayOf(
        intArrayOf(1, 3),
        intArrayOf(2, 6),
        intArrayOf(8, 10),
        intArrayOf(15, 18)
    )

//    val array = arrayOf(
//        intArrayOf(1, 3),
//        intArrayOf(2, 6),
//        intArrayOf(8, 10),
//        intArrayOf(15, 18)
//    )
//
//    ladderLength(
//        "hot", "dog",
////        arrayOf("hot", "dot", "dog", "lot", "log", "cog").toList()
//        arrayOf("hot", "dog", "dot").toList()
//    )
//    addRungs(intArrayOf(4, 8, 12, 16), 3)
//    solve(
//        arrayOf(
//            charArrayOf('X', 'X', 'X', 'X'),
//            charArrayOf('O', 'O', 'O', 'X'),
//            charArrayOf('X', 'X', 'O', 'X'),
//            charArrayOf('X', 'O', 'X', 'X'),
//        )
//    )
//    minStoneSum(intArrayOf(5, 4, 9), 2)
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

    fun isConnect(a: Int, b: Int): Boolean {
        return find(a) == find(b)
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

fun numDecodings(s: String): Int {
    val array = s.toCharArray()
    if (array[0] == '0') return 0
    val dp = IntArray(array.size)
    dp[0] = 1
    for (i in 1 until array.size) {
        if (couldAlone(array[i])) {
            dp[i] = dp[i - 1]
        }
        if (couldCompose(array[i - 1], array[i])) {
            dp[i] = dp[i] + if (i >= 2) dp[i - 2] else 1
        }
    }
    return dp[array.size - 1]
}

private fun couldCompose(front: Char, behind: Char) =
    front == '1' && behind in '0'..'9' || front == '2' && behind in '0'..'6'

private fun couldAlone(element: Char) = element in '1'..'9'

fun isMatch(s: String, p: String): Boolean {
    // a* 可以是 空 ；a；aa(n个a)
    val sArray = s.toCharArray()
    val pArray = p.toCharArray()
    val m = pArray.size + 1
    val n = sArray.size + 1
    val dp = Array(m) { BooleanArray(n) }
    // dp[0][0]  空对空 =》true
    dp[0][0] = true
    for (i in 1 until m)
        for (j in 0 until n) {
            when (pArray[i - 1]) {
                '*' -> {
                    // a* => 空 : dp[i - 2][j]
                    dp[i][j] = dp[i - 2][j]
                    // i 在第二行之后 才有可能出现'*' ，所以 i - 2 不会越界
                    if (j > 0 && isMatchChar(pArray[i - 2], sArray[j - 1])) {
                        // a* => a : dp[i - 1][j]  ;  a* => aa[n个a] : dp[i][j - 1]
                        dp[i][j] = dp[i][j] || dp[i - 1][j] || dp[i][j - 1]
                    }
                }

                else -> {
                    if (j > 0 && isMatchChar(pArray[i - 1], sArray[j - 1])) {
                        dp[i][j] = dp[i - 1][j - 1]
                    }
                }
            }
        }
    return dp[m - 1][n - 1]
}

fun isMatchChar(p: Char, s: Char): Boolean {
    return p == s || p == '.'
}

private fun getDisFromStep(step: Int) = (1 shl step) - 1

fun longestValidParentheses(s: String): Int {
    // 枚举以当前字符为最后一个字符的最大括号数
    // 首要条件：当前字符为 )
    // 可能情况：
    // 1.上一个连续字符是 ( : 只能与上一个字符组队  dp[i] = dp[i - 2] + 2
    // 2.上一个连续字符是 ) : 只能查看上一个连续字符的配对字符前面是否是 ( => 是 -》 dp[i] = dp[i-1] + 2 + dp[i - dp[i-1] -2]  ； 否 -》0
    if (s.isEmpty()) return 0
    val array = s.toCharArray()
    val dp = IntArray(array.size)
    var result = 0
    for (i in 1 until array.size) {
        if (array[i] == ')') {
            if (array[i - 1] == '(') {
                dp[i] = 2 + if (i - 2 >= 0) dp[i - 2] else 0
            } else {
                val lastCountComposeIndex = i - dp[i - 1] - 1
                if (lastCountComposeIndex >= 0 && array[lastCountComposeIndex] == '(')
                    dp[i] =
                        dp[i - 1] + 2 + if (lastCountComposeIndex - 1 >= 0) dp[lastCountComposeIndex - 1] else 0
            }
            result = Math.max(result, dp[i])
        }
    }
    return result
}

fun largestOddNumber(num: String): String {
    val array = num.toCharArray()
    for (i in array.size - 1 downTo 0) {
        if (array[i] - '0' and 1 == 1) {
            return num.substring(0, i + 1)
        }
    }
    return ""
}

fun minCostClimbingStairs(cost: IntArray): Int {
    // dp : 到达每个阶梯的最小花费
    // 求 dp[cost.size + 1] : 顶层阶梯
    if (cost.size < 2) return 0
    val target = cost.size + 1
    val dp = IntArray(target)
    dp[0] = 0
    dp[1] = 0
    for (i in 2 until dp.size) {
        dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2])
    }
    return dp[target - 1]
}

fun uniquePaths(m: Int, n: Int): Int {
    val dp = Array(m) { IntArray(n) }
    for (i in 0 until m)
        for (j in 0 until n) {
            if (i == 0 || j == 0) {
                dp[i][j] = 1
                continue
            }
            dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
        }
    return dp[m - 1][n - 1]
}

fun rob(nums: IntArray): Int {
    if (nums.size == 1) return nums[0]
    if (nums.size == 2) return Math.max(nums[0], nums[1])
    // dp[i] : 有 i 个房子可以去偷，偷到的最大值
    val dp = IntArray(nums.size)
    dp[0] = nums[0]
    dp[1] = Math.max(nums[0], nums[1])
    for (i in 2 until dp.size) {
        dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1])
    }
    return dp[nums.size - 1]
}

fun minPathSum(grid: Array<IntArray>): Int {
    val m = grid.size
    val n = grid[0].size
    for (i in 1 until m) grid[i][0] = grid[i - 1][0] + grid[i][0]
    for (i in 1 until n) grid[0][i] = grid[0][i - 1] + grid[0][i]
    for (i in 1 until m)
        for (j in 1 until n) {
            grid[i][j] = Math.min(grid[i - 1][j], grid[i][j - 1]) + grid[i][j]
        }
    return grid[m - 1][n - 1]
}

fun maximalRectangle(matrix: Array<CharArray>): Int {
    if (matrix.isEmpty()) return 0
    val heightMatrix = Array(matrix.size) { IntArray(matrix[0].size) }
    for (i in matrix.indices)
        for (j in matrix[i].indices) {
            val value = matrix[i][j] - '0'
            if (value == 0) continue
            heightMatrix[i][j] = if (i == 0) value else value + heightMatrix[i - 1][j]
        }
    var result = 0
    for (i in heightMatrix.indices) {
        result = Math.max(result, getMaximalRectangle(heightMatrix[i]))
    }
    return result
}

private fun getMaximalRectangle(heightArray: IntArray): Int {
    // 找出柱状图中最大矩形
    // 单调递增栈 哨兵法
    val stack = Stack<Int>()
    val newHeightArray = IntArray(heightArray.size + 2)
    heightArray.copyInto(newHeightArray, 1)
    newHeightArray[0] = -1
    newHeightArray[newHeightArray.size - 1] = -1
    stack.push(0)
    var maxArea = 0
    for (i in 1 until newHeightArray.size) {
        while (newHeightArray[i] < newHeightArray[stack.peek()]) {
            val peek = stack.peek()
            stack.pop()
            // 确定右界
            val right = i
            val left = stack.peek()
            val area = (right - left - 1) * newHeightArray[peek]
            maxArea = Math.max(area, maxArea)
        }
        stack.push(i)
    }
    return maxArea
}

fun racecar(target: Int): Int {
    val dp = IntArray(target + 1) { Int.MAX_VALUE }
    for (i in 1..target) {
        // 逐步求出每一个距离所用的最小步数
        // A 指令次数
        var ANum = 1
        var distance = 0
        var minStep = Int.MAX_VALUE
        while (getDistance(ANum).also { distance = it } <= 2 * i) {
            // 抵达目标只有几种情况：
            // 1. 直接 Anum 抵达
            // 2. 超过目标退回
            // 3. 在目标前先前进后退调整后在前进抵达
            when {
                // 相等时，直接通过 n 个 Anum 抵达，步数为 n
                distance == i -> minStep = Math.min(minStep, ANum)
                // 超过时，通过往回走 distance - i 距离抵达，步数为 Anum + 1 个转向 + dp[distance - i]( distance - i 距离 最小步数)
                distance > i -> minStep = Math.min(minStep, ANum + 1 + dp[distance - i])
                // 前进还没到先选择后退了
                distance < i -> {
                    // 后退的步子肯定要少于前进步子，否则白走
                    for (RNum in 0 until ANum) {
                        minStep = Math.min(
                            minStep,
                            ANum + 1 + RNum + 1 + dp[i - distance + getDistance(RNum)]
                        )
                    }
                }
            }
            dp[i] = minStep
            ANum++
        }
    }
    return dp[target]
}

fun getDistance(step: Int) = (1 shl step) - 1

private var count = 0
fun totalNQueens(n: Int): Int {
    recursiveSolve(0, n, 0, 0, 0)
    return count
}

fun recursiveSolve(
    m: Int,
    n: Int,
    columnSet: Int,
    pieSet: Int,
    naSet: Int
) {
    if (m == n) {
        count++
        return
    }
    var availablePoi = ((1 shl n) - 1) and ((columnSet or pieSet or naSet).inv())
    while (availablePoi != 0) {
        // 负数的补码 = 符号位不变，其它取反，+ 1
        val poi = availablePoi and -availablePoi
        availablePoi = availablePoi and (availablePoi - 1)
        // 因为二进制 和棋盘方向相反 ：所以 pie 右移，na 左移
        recursiveSolve(m + 1, n, columnSet or poi, (pieSet or poi) shr 1, (naSet or poi) shl 1)
    }
}

fun countBits(n: Int): IntArray {
    val dp = IntArray(n + 1)
    for (i in 1..n) {
        dp[i] = dp[i and i - 1] + 1
    }
    return dp
}

@RequiresApi(Build.VERSION_CODES.N)
fun relativeSortArray(arr1: IntArray, arr2: IntArray): IntArray {
    // 储存优先顺序
    val hashMap = HashMap<Int, Int>()
    for (i in arr2.indices) {
        hashMap[arr2[i]] = i
    }
    // 使用自定义 comparator 排列 arr1
    // leetcode 需要写全 (kotlin.Comparator { before, after -> })
    return arr1.sortedWith(kotlin.Comparator { before, after ->
        if (hashMap.containsKey(before) || hashMap.containsKey(after)) {
            // 没取到的给 1001 ，排到最后 (题目定义不超过 1000)
            hashMap.getOrDefault(before, 1001) - hashMap.getOrDefault(after, 1001)
        } else {
            before - after
        }
    }).toIntArray()
}

fun merge(intervals: Array<IntArray>): Array<IntArray> {
    intervals.sortWith(kotlin.Comparator { o1, o2 ->
        o1[0] - o2[0]
    })
    val merger = arrayListOf<IntArray>()
    for (i in intervals.indices) {
        if (merger.isEmpty() || merger.last()[1] < intervals[i][0]) {
            // 左边超过前一个的右边，不可合并
            merger.add(intArrayOf(intervals[i][0], intervals[i][1]))
        } else {
            merger.last()[1] = Math.max(merger.last()[1], intervals[i][1])
        }
    }
    return merger.toTypedArray()
}

fun countTriples(n: Int): Int {
    var counter = 0
    for (a in 1..n)
        for (b in a..n)
            for (c in b..n) {
                if (a * a + b * b == c * c) {
                    // 找到组合
                    if (a == b) {
                        counter++
                    } else {
                        counter += 2
                    }
                }
            }
    return counter
}

private val canAvailable = arrayOf(
    intArrayOf(1, 3),
    intArrayOf(0, 2, 4),
    intArrayOf(1, 5),
    intArrayOf(0, 4),
    intArrayOf(3, 1, 5),
    intArrayOf(4, 2)
)

private val visitedSet = HashSet<String>()

@RequiresApi(Build.VERSION_CODES.N)
fun slidingPuzzle(board: Array<IntArray>): Int {
    // BFS 状态树 扩散最短路径
    // 找到起点 0
    val boardStr = board[0].joinToString("") + board[1].joinToString("")
    val queue = PriorityQueue<AStar>(kotlin.Comparator<AStar> { o1, o2 ->
        o1.f - o2.f
    })
    queue.offer(AStar(boardStr, 0))
    while (queue.isNotEmpty()) {
        queue.poll()?.let {
            visitedSet.add(it.status)
            if (it.status == "123450") {
                return it.level
            }
            val boardArray = it.status.toCharArray()
            val indexZero = boardArray.indexOf('0')
            var newStr: String
            for (direction in canAvailable[indexZero]) {
                // 交换
                boardArray[indexZero] = boardArray[direction].apply {
                    boardArray[direction] = boardArray[indexZero]
                }
                newStr = boardArray.joinToString("")
                if (!visitedSet.contains(newStr)) {
                    queue.offer(AStar(newStr, it.level + 1))
                }
                // 还原
                boardArray[indexZero] = boardArray[direction].apply {
                    boardArray[direction] = boardArray[indexZero]
                }
            }
        }
    }
    return -1
}

/**
 * @param level BFS 层数
 * @param distance 曼哈顿距离
 */
class AStar(var status: String, var level: Int) {
    var f = 0
    var distance = 0

    // 曼哈顿距离
    val dist = arrayOf(
        intArrayOf(0, 1, 2, 1, 2, 3),
        intArrayOf(1, 0, 1, 2, 1, 2),
        intArrayOf(2, 1, 0, 3, 2, 1),
        intArrayOf(1, 2, 3, 0, 1, 2),
        intArrayOf(2, 1, 2, 1, 0, 1),
        intArrayOf(3, 2, 1, 2, 1, 0)
    )

    init {
        distance = getD()
        // 层数越少 + 曼哈顿距离越小 的越优先
        f = level + distance
    }

    private fun getD(): Int {
        var result = 0
        for (i in 0 until 6) {
            // 只要其他 5 个的都在原位，自然就是结果，距离就是 0。所以可以忽略 0 的位置
            if (status[i] != '0') {
                result += dist[i][status[i] - '1']
            }
        }
        return result
    }
}

val arrayX = arrayOf(1, 0, -1, 0, 1, -1, 1, -1)
val arrayY = arrayOf(1, 1, -1, -1, 0, 0, -1, 1)
fun shortestPathBinaryMatrix(grid: Array<IntArray>): Int {
    val rows = grid.size
    val columns = grid[0].size
    if (grid[0][0] == 1 || grid[rows - 1][columns - 1] == 1) {
        return -1
    }
    val queue = LinkedList<Int>()
    queue.offer(0)
    var path = 1
    while (queue.isNotEmpty()) {
        repeat(queue.size) {
            queue.poll()?.let {
                if (it == rows * columns - 1) {
                    return path
                }
                val i = it / columns
                val j = it % columns
                for (k in arrayX.indices) {
                    val nextI = i + arrayX[k]
                    val nextJ = j + arrayY[k]
                    if (nextI in 0 until rows && nextJ in 0 until columns && grid[nextI][nextJ] == 0) {
                        queue.offer(nextI * columns + nextJ)
                        grid[nextI][nextJ] = 1
                    }
                }
            }
        }
        path++
    }
    return -1
}

private val resultSolves = LinkedList<LinkedList<String>>()
fun solveNQueens(n: Int): List<List<String>> {
    solveQueen(IntArray(n) { -1 }, n, 0, 0, 0, 0)
    return resultSolves
}

/**
 * @param column 整型，其中位为 1 的不可放置
 * @param pie    整型，其中位为 1 的不可放置
 * @param na     整型，其中位为 1 的不可放置
 */
fun solveQueen(
    queen: IntArray,
    n: Int,
    m: Int,
    column: Int,
    pie: Int,
    na: Int
) {
    if (m == n) {
        genBoard(queen)
        return
    }
    // 得到可放置部分：availablePoi 中为 1 即可放置
    var availablePoi = ((1 shl n) - 1) and (column or pie or na).inv()
    while (availablePoi != 0) {
        // 一个数和自己的负数相与 = 只有最后一个 1 的整型数值
        val onlyOneValue = availablePoi and (-availablePoi)
        // 打掉最后一个1
        availablePoi = availablePoi and (availablePoi - 1)
        // 1的位置：二进制与棋盘位置相反
        val targetPoi = Integer.bitCount(onlyOneValue - 1)
        queen[m] = targetPoi
        // column or targetPoi : 增加 targetPoi 为 列不可放置
        // (pie or targetPoi) shr 1 : pie 不可放置
        // (na or targetPoi) shl 1 : pie 不可放置
        // 棋盘与二进制相反，所以 pie 是位右移， na 是位左移
        solveQueen(
            queen,
            n,
            m + 1,
            column or onlyOneValue,
            (pie or onlyOneValue) shr 1,
            (na or onlyOneValue) shl 1
        )
        queen[m] = -1
    }
}

fun genBoard(queen: IntArray) {
    val result = LinkedList<String>()
    for (i in queen.indices) {
        val row = CharArray(queen.size) { '.' }
        row[queen[i]] = 'Q'
        result.add(row.joinToString(""))
    }
    resultSolves.add(result)
}

//fun ladderLength(beginWord: String, endWord: String, wordList: List<String>): Int {
//    val wordSet = HashSet<String>(wordList)
//    if (!wordSet.contains(endWord)) return 0
//    // 改变单词字符，在 wordList 存在，进入下一个状态
//    val queue = LinkedList<String>()
//    var path = 1
//    queue.offer(beginWord)
//    while (queue.isNotEmpty()) {
//        repeat(queue.size) {
//            queue.poll()?.let {
//                if (it == endWord) {
//                    return path
//                }
//                for (nextStatus in getNextStatus(it)) {
//                    // 可以变换
//                    if (wordSet.contains(nextStatus)) {
//                        queue.offer(nextStatus)
//                        // 先到的层数一定比后到的快，所以不需要后到的再来了
//                        wordSet.remove(nextStatus)
//                    }
//                }
//            }
//        }
//        path++
//    }
//    return 0
//}
//
//fun getNextStatus(status: String):LinkedList<String> {
//    val result = LinkedList<String>()
//    val statusArray = status.toCharArray()
//    for (i in statusArray.indices) {
//        val originChar = statusArray[i]
//        for (j in 'a'..'z') {
//            if (originChar != j) {
//                statusArray[i] = j
//                result.add(statusArray.joinToString(""))
//            }
//        }
//        // 还原
//        statusArray[i] = originChar
//    }
//    return result
//}

class SolutionStartEndBFS {

    /**
     * 双向BFS模板
     * 可使用双向BFS条件：起点可到终点，终点可到起点
     */
    fun start2endBfsTemplate(start: String, end: String, accessSet: Set<String>): Int {
        if (!accessSet.contains(end)) return -1

        // 定义从前遍历集合
        var beforeSet = HashSet<String>()
        // 定义从后遍历集合
        var afterSet = HashSet<String>()
        // 定义已遍历节点集合
        val visitedSet = HashSet<String>()
        // 定义起始节点数量 可能 0 或 1
        var len = 0
        while (beforeSet.isNotEmpty() && afterSet.isNotEmpty()) {
            // 选择节点少的方向遍历
            if (afterSet.size < beforeSet.size) {
                val temp = afterSet
                afterSet = beforeSet
                beforeSet = temp
            }
            // 下一层遍历的集合
            val nextLevelVisitSet = HashSet<String>()
            for (it in beforeSet) {
                // 遍历下一层可变化的数据集合  （自己的操作）
                for (nextStatus in getNextStatus(it)) {
                    if (afterSet.contains(nextStatus)) {
                        return len + 1
                    }
                    // 可以变换
                    if (accessSet.contains(nextStatus) && !visitedSet.contains(nextStatus)) {
                        nextLevelVisitSet.add(nextStatus)
                    }
                }
            }
            beforeSet = nextLevelVisitSet
            // 遍历层数+1
            len++
        }
        return -1
    }

    /**
     * 不同的变换操作
     */
    private fun getNextStatus(it: String): List<String> {
        return emptyList()
    }
}


fun solveSudoku(board: Array<CharArray>): Unit {
    if (board.isEmpty()) return
    dfsSolve(board)
}

private fun dfsSolve(board: Array<CharArray>): Boolean {
    for (i in board.indices)
        for (j in board[i].indices) {
            if (board[i][j] == '.') {
                for (k in '1'..'9') {
                    if (isValid(board, i, j, k)) {
                        board[i][j] = k
                        if (dfsSolve(board))
                            return true
                        board[i][j] = '.'
                    }
                }
                return false
            }
        }
    return true
}

fun isValid(board: Array<CharArray>, row: Int, column: Int, c: Char): Boolean {
    for (i in board.indices) {
        if (board[row][i] == c) return false
        if (board[i][column] == c) return false
        if (board[3 * (row / 3) + i / 3][3 * (column / 3) + i % 3] == c) return false
    }
    return true
}

fun canBeTypedWords(text: String, brokenLetters: String): Int {
    val words = text.split(" ")
    val brokenSet = hashSetOf<Char>()
    brokenLetters.forEach {
        brokenSet.add(it)
    }
    var counter = 0
    for (word in words) {
        if (isAvailable(word, brokenSet))
            counter++
    }
    return counter
}

private fun isAvailable(word: String, brokenSet: HashSet<Char>): Boolean {
    for (c in word) {
        if (brokenSet.contains(c)) {
            return false
        }
    }
    return true
}

fun addRungs(rungs: IntArray, dist: Int): Int {
    // dist 一个人的步幅
    var i = 0
    // 地板 0
    var accessPoi = 0
    var counter = 0
    while (i < rungs.size) {
        if (accessPoi + dist >= rungs[i]) {
            accessPoi = rungs[i]
            i++
        } else {
            // 加梯子
            counter += (Math.ceil((rungs[i] - accessPoi).toDouble() / dist.toDouble())).toInt() - 1
            accessPoi = rungs[i]
            i++
        }
    }
    return counter
}

private var max = Long.MIN_VALUE

fun maxPoints(points: Array<IntArray>): Long {
    // 暴力法
    solve(points, 0, -1, 0)
    return max
}

fun solve(points: Array<IntArray>, row: Int, lastRowColumn: Int, pickValue: Long) {
    if (row == points.size) {
        max = Math.max(pickValue, max)
        return
    }
    for (i in points[row].indices) {

        val cost = if (row == 0) 0 else Math.abs(lastRowColumn - i)
        val pickValueAmount = points[row][i] + pickValue - cost
        solve(points, row + 1, i, pickValueAmount)
    }
}

private val cacheMap = HashMap<Int, Int>()
fun climbStairs(n: Int): Int {
    // 已计算过的值进行缓存，达到剪枝目的
    if (n <= 2) return n
    if (cacheMap.containsKey(n)) {
        // 剪枝
        return cacheMap[n]!!
    }
    val amount = climbStairs(n - 1) + climbStairs(n - 2)
    cacheMap[n] = amount
    return amount
}

fun generateParenthesis(n: Int): List<String> {
    val result = LinkedList<String>()
    generate(n, 0, 0, "", result)
    return result
}

fun generate(n: Int, leftCount: Int, rightCount: Int, s: String, result: LinkedList<String>) {
    if (leftCount == n && rightCount == n) {
        result.add(s)
        return
    }

    if (leftCount < n) {
        // 剪枝：只有这种情况可以加入左括号
        generate(n, leftCount + 1, rightCount, "$s(", result)
    }
    if (rightCount < leftCount) {
        // 剪枝：只有这种情况可以加入右括号
        generate(n, leftCount, rightCount + 1, "$s)", result)
    }
}

fun minMutation(start: String, end: String, bank: Array<String>): Int {
    // 使用 set 查的快
    val bankSet = HashSet<String>()
    bank.forEach {
        bankSet.add(it)
    }
    if (end !in bankSet) {
        return -1
    }
    var beforeSet = HashSet<String>()
    var afterSet = HashSet<String>()
    val visitedSet = HashSet<String>()
    beforeSet.add(start)
    afterSet.add(end)
    visitedSet.add(start)
    visitedSet.add(end)
    var path = 0
    while (beforeSet.isNotEmpty() && afterSet.isNotEmpty()) {
        if (afterSet.size < beforeSet.size) {
            // 从小的方向开始
            val temp = afterSet
            afterSet = beforeSet
            beforeSet = temp
        }
        val nextSet = HashSet<String>()
        for (gene in beforeSet) {
            for (nextGene in swapGene(gene)) {
                if (nextGene in afterSet) {
                    // 找到
                    return path + 1
                }
                if (nextGene !in visitedSet && nextGene in bankSet) {
                    nextSet.add(nextGene)
                    visitedSet.add(nextGene)
                }
            }
        }
        path++
        beforeSet = nextSet
    }
    return -1
}

private val geneSwapArray = arrayOf('A', 'T', 'C', 'G')
private fun swapGene(gene: String): LinkedList<String> {
    val result = LinkedList<String>()
    val array = gene.toCharArray()
    for (i in array.indices) {
        for (swap in geneSwapArray) {
            if (swap != array[i]) {
                val origin = array[i]
                array[i] = swap
                result.add(String(array))
                array[i] = origin
            }
        }
    }
    return result
}

fun getLucky(s: String, k: Int): Int {
    var value = ""
    var realValue = 0
    for (c in s) {
        if (c in 'a'..'z') {
            value += c - 'a' + 1
        }
    }
    repeat(k) {
        realValue = 0
        for (c in value) {
            realValue += c - '0'
        }
        value = "" + realValue
    }
    return realValue
}

fun maximumNumber(num: String, change: IntArray): String {
    val array = num.map {
        it - '0'
    }.toMutableList()
    var isUse = false
    for (i in array.indices) {
        if (isUse && array[i] > change[array[i]]) {
            break
        }
        if (array[i] < change[array[i]]) {
            array[i] = change[array[i]]
            isUse = true
        }
    }
    return array.joinToString("")
}

fun maxCompatibilitySum(students: Array<IntArray>, mentors: Array<IntArray>): Int {
    // 搜索 状态树
    val nextAccessStudentSet = HashSet<Int>()
    val nextAccessMentorSet = HashSet<Int>()
    for (i in students.indices) {
        nextAccessStudentSet.add(i)
        nextAccessMentorSet.add(i)
    }
    return solveDFS(0, students, mentors, nextAccessStudentSet, nextAccessMentorSet)
}

fun solveDFS(
    level: Int,
    students: Array<IntArray>,
    mentors: Array<IntArray>,
    accessStudentISet: HashSet<Int>,
    accessMentorJSet: HashSet<Int>
): Int {
    if (accessStudentISet.isEmpty() || accessMentorJSet.isEmpty()) {
        return 0
    }
    var currentMax = 0
    // TODO(wzx) : 这里重复了啊。。
    for (j in accessMentorJSet) {
        var currentStatusMaxScore = computeScore(students[level], mentors[j])
        val nextAccessStudentSet = HashSet(accessStudentISet)
        val nextAccessMentorSet = HashSet(accessMentorJSet)
        nextAccessStudentSet.remove(level)
        nextAccessMentorSet.remove(j)
        currentStatusMaxScore += solveDFS(
            level + 1,
            students,
            mentors,
            nextAccessStudentSet,
            nextAccessMentorSet
        )
        currentMax = Math.max(currentMax, currentStatusMaxScore)
    }
    return currentMax
}

fun computeScore(studentArray: IntArray, mentorArray: IntArray): Int {
    var count = 0
    for (i in studentArray.indices) {
        if (studentArray[i] == mentorArray[i]) {
            count++
        }
    }
    return count
}

fun solve(board: Array<CharArray>): Unit {
    val rows = board.size
    val columns = board[0].size
    // 并查集
    val uf = UnionFind(rows * columns + 1)

    // 获得二维坐标
    fun node(row: Int, column: Int) = row * columns + column

    // 虚拟节点 代表 不可转换的边界
    val dummyNode = rows * columns
    for (i in board.indices)
        for (j in board[i].indices) {
            // 将四条边界先连通

            if (board[i][j] == 'O') {
                if (i == 0 || j == 0 || i == rows - 1 || j == columns - 1) {
                    uf.union(node(i, j), dummyNode)
                } else {
                    // 合并到左边的集合
                    if (j > 0 && board[i][j - 1] == 'O') {
                        uf.union(node(i, j), node(i, j - 1))
                    }
                    // 和右方合并集合
                    if (j < columns - 1 && board[i][j + 1] == 'O') {
                        uf.union(node(i, j), node(i, j + 1))
                    }
                    // 和下方合并集合
                    if (i > 0 && board[i - 1][j] == 'O') {
                        uf.union(node(i, j), node(i - 1, j))
                    }
                    // 和下方合并集合
                    if (i < columns - 1 && board[i + 1][j] == 'O') {
                        uf.union(node(i, j), node(i + 1, j))
                    }
                }

            }
        }
    // 进行转换
    for (i in board.indices)
        for (j in board[i].indices) {
            if (uf.isConnect(node(i, j), dummyNode)) {
                board[i][j] = 'O'
            } else {
                board[i][j] = 'X'
            }
        }
}

/**
 * 股票 1
 */
fun maxProfit1(prices: IntArray): Int {
    // dp[i][k] {k in 0,1}
    // k == 0 : 代表手上没股票时持有的最大现金数
    // k == 1 : 代表手上有股票时持有的最大现金数
    val dp = Array(prices.size) { IntArray(2) }
    dp[0][0] = 0
    dp[0][1] = -prices[0]
    for (i in 1 until prices.size) {
        // 股票到 i 时，没有股票的最大值为：前面没有股票的值 或者 前面有股票，现在卖了
        // dp[i - 1][0] 就相当于不操作，继续不买入   （从前面卖出的操作赚到的钱 和 持有前面股票到现在卖出 i 价格，选择赚钱更多的操作存下来）
        dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i])
        // 股票到 i 时，有股票的最大值为：继续持有前面股票的值 或者 前面没有股票，现在买了
        // 因为只允许买入一次，所以值为  - prices[i]   （从前面持有的股票和持有当前i股票做一个选择，选择值更小的股票持有）
        dp[i][1] = Math.max(dp[i - 1][1], -prices[i])
    }
    // 最大的一定是手上没股票的时候（有股票还要减值）
    return dp[prices.size - 1][0]
}

/**
 * 股票 2
 */
fun maxProfit2(prices: IntArray): Int {
    // dp[i][k] {k in 0,1}
    // k == 0 : 代表手上没股票时持有的最大现金流
    // k == 1 : 代表手上有股票时持有的最大现金流
    val dp = Array(prices.size) { IntArray(2) }
    dp[0][0] = 0
    dp[0][1] = -prices[0]
    for (i in 1 until prices.size) {
        // 股票到 i 时，没有股票的最大值为：前面没有股票的值 或者 前面有股票，现在卖了
        // dp[i - 1][0] 就相当于不操作，继续不买入   （从前面卖出的操作赚到的钱 和 持有前面股票到现在卖出 i 价格，选择更多的现金流存下来）
        dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i])
        // 股票到 i 时，有股票的最大值为：继续持有前面股票的值 或者 前面没有股票，现在买了
        // 可以允许多次购买，所以dp[i - 1][0] - prices[i]：前面赚到的钱再买入当前i价格的股票
        dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i])
    }
    // 最大的一定是手上没股票的时候（有股票还要减值）
    return dp[prices.size - 1][0]
}

/**
 * 股票 3
 */
fun maxProfit3(prices: IntArray): Int {
    // 三维 dp ：dp[i][j][k] k 指当前操作了几次 ( 卖出算一次 )
    val dp = Array(prices.size) { Array(2) { IntArray(3) } }
    dp[0][0][0] = 0
    dp[0][1][0] = -prices[0]
    // 不管 算几次交易，只要持有都是 -prices[0]
    dp[0][0][1] = 0
    // 相当于前面有过一次操作。我又买入（但是还没卖出）
    dp[0][1][1] = -prices[0]
    dp[0][0][2] = 0
    // 相当于前面有过 2 次操作。我又买入（但是还没卖出，也没法卖出）
    dp[0][1][2] = -prices[0]
    for (i in 1 until prices.size) {
        // 手上没股票的情况
        // 1. 到 i 还是没操作
        dp[i][0][0] = dp[i - 1][0][0]
        // 2. 到 i 操作一次，前面没操作过 , 到 i 操作一次
        dp[i][0][1] = Math.max(dp[i - 1][0][1], dp[i - 1][1][0] + prices[i])
        // 3. 到 i 操作两次，前面操作过 1 次, 到 i 操作一次
        dp[i][0][2] = Math.max(dp[i - 1][0][2], dp[i - 1][1][1] + prices[i])

        // 手上有股票的情况
        // 1. 前面没操作过，还有股票，说明是有前面的股票 或者 有当前 i 价格的股票
        dp[i][1][0] = Math.max(dp[i - 1][1][0], -prices[i])
        // 2. dp[i - 1][1][1] 使用前面操作过的值和之前购入的股票  或者  dp[i - 1][0][1] - prices[i] 前面卖出操作过，再买入当前的股票
        // (其实就是选择买前面那个股票，还是买现在这个股票，来持有)
        dp[i][1][1] = Math.max(dp[i - 1][1][1], dp[i - 1][0][1] - prices[i])
        // 3. 同 2 理
        dp[i][1][2] = Math.max(dp[i - 1][1][2], dp[i - 1][0][2] - prices[i])
    }
    return dp[prices.size - 1][0].max() ?: 0
}

/**
 * 股票 冷冻期
 */
fun maxProfitCood(prices: IntArray): Int {
    if (prices.size < 2) {
        return 0
    }
    // 记录卖和没卖的状态
    val dp = Array(prices.size) { Array(2) { IntArray(2) } }
    dp[0][0][0] = 0
    dp[0][1][0] = -prices[0]
    var result = Int.MIN_VALUE
    // 列出所有情况，找出最大值
    for (i in 1 until prices.size) {
        // 手上没股票且卖了 = 前一个有股票且没卖的钱 + 这个股票赚到的钱
        dp[i][0][1] = dp[i - 1][1][0] + prices[i]
        // 手上没股票且没卖钱 = 前一个没股票且没卖钱 与 前一个没股票卖了前一个股票价格钱 的较大者（操作）
        dp[i][0][0] = Math.max(dp[i - 1][0][0], dp[i - 1][0][1])
        // 手上有股票且没卖钱 = 前一个有股票且卖钱 与 前一个没股票卖了前一个股票价格钱，然后买入当前股票 的较大者（操作）
        //（手上有股票不可能出现卖钱状态 也就是dp[i][1][1]不存在）
        dp[i][1][0] = Math.max(dp[i - 1][1][0], dp[i - 1][0][0] - prices[i])
        // 找到几种操作的最大值(最优操作)
        result = arrayOf(dp[i][0][1], dp[i][0][0], dp[i][1][0]).max()!!
    }
    return result
}

/**
 * 股票 1
 */
//fun maxProfit(prices: IntArray): Int {
//    // 买的花掉的钱
//    var firstBuy = Int.MIN_VALUE
//    // 卖的赚到的钱
//    var firstSell = 0
//    for (i in prices.indices) {
//        firstBuy = Math.max(firstBuy, -prices[i])
//        firstSell = Math.max(firstSell , firstBuy + prices[i])
//    }
//    return firstSell
//}

//fun maxProfit(prices: IntArray): Int {
//    // 买的花掉的钱
//    var buy = Int.MIN_VALUE
//    // 卖的赚到的钱
//    var sell = 0
//    for (i in prices.indices) {
//        // 能赚钱就卖了 => 累计总利润到 sell
//        sell = Math.max(sell , sell + (buy + prices[i]))
//        // 每次都买入（反正能连续买卖）后面能赚钱就卖了。不能拿着也没事
//        buy = -prices[i]
//    }
//    return sell
//}

fun isPrefixString(s: String, words: Array<String>): Boolean {
    var sIndex = 0
    for (i in words.indices) {
        val word = words[i]
        if (s.length - sIndex < word.length) {
            return false
        } else {
            for (j in word.indices) {
                if (s[sIndex] != word[j]) {
                    return false
                }
                sIndex++
            }
        }
        if (sIndex == s.length) return true
    }
    return false
}

//
//fun maxProfit(prices: IntArray): Int {
//    var firstBuy = Int.MIN_VALUE
//    var firstSell = 0
//    var secondBuy = Int.MIN_VALUE
//    var secondSell = 0
//    for (price in prices) {
//        // 找到便宜的买了
//        firstBuy = Math.max(firstBuy, -price)
//        // 找到价格高的卖了
//        firstSell = Math.max(firstSell, firstBuy + price)
//
//        // 第一次卖了后，现在买 firstSell - price (第一次卖赚的钱当作利润)
//        secondBuy = Math.max(secondBuy, firstSell - price)
//        secondSell = Math.max(secondSell, secondBuy + price)
//    }
//    return secondSell
//}
