fun main() {
    fun part1(input: List<String>): Long {
        val numbers = input.map { Node.construct(it) }
        val sum = numbers.drop(1).fold(numbers[0]) { acc, node -> Node.sum(acc, node) }
        return Node.magnitude(sum)
    }

    fun part2(input: List<String>): Long {
        val n = input.lastIndex
        var result = 0L
        for (i in 0 .. n) for (j in 0 .. n)  {
            if (i == j) continue

            val numbers = input.map { Node.construct(it) }
            val sum = Node.sum(numbers[j], numbers[i])
            val magnitude = Node.magnitude(sum)
            result = maxOf(result, magnitude)
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part2(testInput) == 3993L)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}

sealed interface Node {
    class Sum(var parent: Sum?, var left: Node?, var right: Node?) : Node
    data class Leaf(var parent: Sum?, var value: Int) : Node

    companion object {
        fun magnitude(node: Node): Long {
            if (node is Leaf) return node.value.toLong()

            node as Sum
            return 3 * magnitude(node.left!!) + 2 * magnitude(node.right!!)
        }

        fun sum(left: Sum, right: Sum): Sum {
            val root = Sum(null, null, null)
            left.parent = root
            right.parent = root
            root.left = left
            root.right = right
            var finised = false
            while (!finised) {
                val reduced = reduce(root)
                if (reduced) continue

                val exploded = explode(root)
                finised = !exploded
            }
            return root
        }

        fun explode(node: Node): Boolean {
            if (node is Leaf && node.value >= 10) {
                val leftValue = node.value / 2
                val rightValue = node.value - leftValue
                val newNode = Sum(node.parent, null, null)
                val leftLeaf = Leaf(newNode, leftValue)
                val rightLeaf = Leaf(newNode, rightValue)
                newNode.left = leftLeaf
                newNode.right = rightLeaf
                if (node.parent?.left == node) {
                    node.parent?.left = newNode
                } else {
                    node.parent?.right = newNode
                }

                return true
            }
            if (node is Sum) {
                val noMore = explode(node.left!!)
                if (!noMore) {
                    return explode(node.right!!)
                }
                return true
            } else return false
        }

        fun reduce(node: Sum, height: Int = 0): Boolean {
            if (node.left is Leaf && node.right is Leaf && height == 4) {
                val leftValue = (node.left as Leaf).value
                val rightValue = (node.right as Leaf).value
                var p: Sum? = node
                while (p != null && p.parent?.right != p) p = p.parent
                p = p?.parent
                if (p != null) {
                    if (p.left is Sum) {
                        p = p.left as Sum
                        while (p?.right !is Leaf) p = p?.right as Sum
                        (p.right as Leaf).value += leftValue
                    } else {
                        (p.left as Leaf).value += leftValue
                    }
                }

                var q: Sum? = node
                while (q != null && q.parent?.left != q) q = q.parent
                q = q?.parent
                if (q != null) {
                    if (q.right is Sum) {
                        q = q.right as Sum
                        while (q?.left !is Leaf) q = q?.left as Sum

                        (q.left as Leaf).value += rightValue
                    } else {
                        (q.right as Leaf).value += rightValue
                    }
                }

                if (node.parent?.left == node) {
                    node.parent?.left = Leaf(node.parent, 0)
                } else {
                    node.parent?.right = Leaf(node.parent, 0)
                }

                return true
            }


            var reducedLeft = false
            var reducedRight = false
            if (node.left is Sum) {
                reducedLeft = reduce(node.left as Sum, height + 1)
            }

            if (node.right is Sum && !reducedLeft) {
                reducedRight = reduce(node.right as Sum, height + 1)
            }
            return reducedLeft || reducedRight
        }

        fun construct(config: String): Sum {
            var root: Sum? = null
            var p: Sum? = null
            config.forEach { c ->
                when (c) {
                    '[' -> {
                        if (root == null) {
                            root = Sum(null, null, null)
                            p = root
                        } else {
                            val child = Sum(p, null, null)
                            if (p?.left == null) p?.left = child else p?.right = child
                            p = child
                        }
                    }
                    ']' -> p = p?.parent
                    in ('0'..'9') -> {
                        if (p?.left == null) {
                            p?.left = Leaf(p, c.digitToInt())
                        } else {
                            p?.right = Leaf(p, c.digitToInt())
                        }
                    }
                }
            }

            return root!!
        }
    }
}
