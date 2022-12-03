package connectfour

fun main() {
    var rowsAndColumns: String
    var rows: Int
    var columns: Int

    println("Connect Four")
    println("First player's name:")
    val firstPlayer = readln()
    println("Second player's name:")
    val secondPLayer = readln()

    while (true) {
        println("Set the board dimensions (Rows x Columns)\n" +
                "Press Enter for default (6 x 7)")
        rowsAndColumns = readln()
        rowsAndColumns = rowsAndColumns.filter { !it.isWhitespace() }
        rowsAndColumns = rowsAndColumns.lowercase()

        if (rowsAndColumns.isEmpty()) {
            rows = 6
            columns = 7
        } else if (!rowsAndColumns.matches("""\d+x\d+""".toRegex())) {
            println("Invalid input")
            continue
        } else {
            rows = rowsAndColumns.split("x").first().toInt()
            columns = rowsAndColumns.split("x").last().toInt()
        }

        if (rows !in 5..9) {
            println("Board rows should be from 5 to 9")
            continue
        } else if (columns !in 5..9) {
            println("Board columns should be from 5 to 9")
            continue
        } else {
            break
        }
    }

    println("$firstPlayer vs $secondPLayer")
    println("$rows X $columns board")
}