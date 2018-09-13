package eu.michaelvogt.ar.author.data

class Slide(var type: Int, var contentPath: String, var secondaryPaths: List<String>?, var description: String?) {
    companion object {
        val TYPE_IMAGE = 0
        val TYPE_VR = 1
        val TYPE_COMPARISON = 2
    }
}
