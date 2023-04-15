package bstrees

import bstrees.nodes.TreeNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AVLTreeTest {
    private val randomizer = Random(72)
    private val values = Array(1000) { randomizer.nextInt() }.distinct()
    private val elementsCount = values.size // 3 is minimum possible count of elements

    private lateinit var tree: AVLTree<Int>

    private fun checkAVLInvariant(tree: BinarySearchTree<*, *>): Boolean {
        /** Returns height of [node] or null if tree is not balanced */
        fun isBalanced(node: TreeNode<*, *>?): Int? {
            if (node == null) return 0

            val leftHeight = isBalanced(node.left) ?: return null
            val rightHeight = isBalanced(node.right) ?: return null

            if (abs(leftHeight - rightHeight) > 1) return null
            return max(leftHeight, rightHeight) + 1
        }

        return isBalanced(tree.root) != null
    }

    @BeforeEach
    fun recreateTree() {
        tree = AVLTree()
    }

    @Test
    fun `newly instantiated tree has null root`() {
        assertEquals(null, tree.root, "Tree must have null root after initialization")
    }

    @Test
    fun `invariant after insertion`() {
        values.forEach {
            tree.insert(it)

            assertTrue(checkBSTInvariant(tree), "BST invariant is not held")
            assertTrue(checkAVLInvariant(tree), "AVL invariant is not held")
        }
    }

    @Test
    fun `invariant after insertion of duplicates`() {
        values.forEach(tree::insert)

        values.slice(elementsCount / 3 until 2 * elementsCount / 3).forEach {
            tree.insert(it)

            assertTrue(checkBSTInvariant(tree), "BST invariant is not held")
            assertTrue(checkAVLInvariant(tree), "AVL invariant is not held")
        }
    }

    @Test
    fun `tree contains all inserted elements`() {
        values.forEach(tree::insert)

        val treeElems = traverseInOrder(tree)
        assertTrue(
            values.size == treeElems.size,
            "Tree doesn't have the same number of elements as inserted"
        )
        assertTrue(
            values.containsAll(treeElems) && treeElems.containsAll(values),
            "Elements in the tree are not the ones that were inserted"
        )
    }

    @Test
    fun `tree doesnt contain duplicate elements`() {
        values.forEach(tree::insert)
        values.slice(elementsCount / 3 until 2 * elementsCount / 3).forEach(tree::insert) // insert some elements again

        val treeElems = traverseInOrder(tree)
        assertTrue(
            values.size == treeElems.size,
            "Tree doesn't have the same number of elements as inserted"
        )
        assertTrue(
            values.containsAll(treeElems) && treeElems.containsAll(values),
            "Elements in the tree are not the ones that were inserted"
        )
    }

    @Test
    fun `invariant after deletion`() {
        values.forEach(tree::insert)

        values.slice(elementsCount / 4 until 3 * elementsCount / 4).forEach {
            tree.delete(it)

            assertTrue(checkBSTInvariant(tree), "BST invariant is not held")
            assertTrue(checkAVLInvariant(tree), "AVL invariant is not held")
        }
    }

    @Test
    fun `tree doesnt contain deleted elements`() {
        values.forEach(tree::insert)
        val toDelete = values.slice(elementsCount / 4 until 3 * elementsCount / 4)

        toDelete.forEach(tree::delete) // delete some elements

        val treeElems = traverseInOrder(tree)
        val expectedElems = values.subtract(toDelete.toSet())
        assertTrue(
            expectedElems.size == treeElems.size,
            "Tree doesn't have the same number of elements as expected"
        )
        assertTrue(
            expectedElems.containsAll(treeElems) && treeElems.containsAll(expectedElems),
            "Elements in the tree are not the ones that were expected"
        )
    }

    @Test
    fun `root is null after deletion of everything`() {
        values.forEach(tree::insert)

        values.forEach(tree::delete) // delete everything

        assertEquals(
            null, tree.root,
            "Tree must have null root after deletion of every element"
        )
    }

    @Test
    fun `delete returns deleted value`() {
        values.forEach(tree::insert)

        values.forEach {
            assertEquals(it, tree.delete(it), "Delete method must return deleted value")
        }
    }

    @Test
    fun `delete returns null when not found`() {
        values.take(elementsCount / 3).forEach(tree::insert)

        values.takeLast(elementsCount / 3).forEach {
            assertEquals(
                null, tree.delete(it),
                "Delete method must return null if value to delete not found"
            )
        }
    }

    @Test
    fun `search returns found value`() {
        values.forEach(tree::insert)

        values.forEach {
            assertEquals(it, tree.search(it), "Search method must return found value")
        }
    }

    @Test
    fun `search returns null when not found`() {
        values.take(elementsCount / 3).forEach(tree::insert)

        values.takeLast(elementsCount / 3).forEach {
            assertEquals(
                null, tree.search(it),
                "Search method must return null if value not found"
            )
        }
    }

    @Test
    fun `insert, delete, search`() {
        val dataSize = 1e6.toInt()
        val bigData = List(dataSize) { randomizer.nextInt() }
        val expected = mutableSetOf<Int>()
        var currentIndex = 0

        while (currentIndex + 2 < dataSize) {
            val insertIndex1 = currentIndex + randomizer.nextInt(3)
            tree.insert(bigData[insertIndex1])
            expected.add(bigData[insertIndex1])

            val insertIndex2 = currentIndex + randomizer.nextInt(3)
            tree.insert(bigData[insertIndex2])
            expected.add(bigData[insertIndex2])

            val searchIndex1 = currentIndex + randomizer.nextInt(3)
            assertEquals(
                tree.search(bigData[searchIndex1]) != null,
                expected.contains(bigData[searchIndex1]),
                "Search must return not null if element is in the tree and null otherwise"
            )

            val deleteIndex1 = currentIndex + randomizer.nextInt(3)
            tree.delete(bigData[deleteIndex1])
            expected.remove(bigData[deleteIndex1])

            val searchIndex2 = currentIndex + randomizer.nextInt(3)
            assertEquals(
                tree.search(bigData[searchIndex2]) != null,
                expected.contains(bigData[searchIndex2]),
                "Search must return not null if element is in the tree and null otherwise"
            )

            val insertIndex3 = currentIndex + randomizer.nextInt(3)
            tree.insert(bigData[insertIndex3])
            expected.add(bigData[insertIndex3])

            val deleteIndex2 = currentIndex + randomizer.nextInt(3)
            tree.delete(bigData[deleteIndex2])
            expected.remove(bigData[deleteIndex2])

            currentIndex += 3
        }
    }
}
