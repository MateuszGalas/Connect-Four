fun solution(elements: MutableList<String>, index: Int): MutableList<String> {
    return elements.apply { removeAt(index) }
}