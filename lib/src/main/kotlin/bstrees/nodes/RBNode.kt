package bstrees.nodes

class RBNode<T : Comparable<T>>(override var data: T) : TreeNode<T, RBNode<T>>() {
    enum class Color {
        Red,
        Black
    }

    var color = Color.Black
        internal set

    internal fun flipColor() {
        color = if (color == Color.Black) Color.Red else Color.Black
    }
}
