import org.jline.keymap.KeyMap

fun Boolean.toInt() = if (this) 1 else 0

const val ESC = "\u001b"

enum class Key(vararg val seq: String) {
    Space(" "),
    Left("$ESC[D", "l"),
    Right("$ESC[C", "r"),
}

val keyMap = KeyMap<Key>().apply {
    Key.entries.map {
        bind(it, *it.seq)
    }
}
