package layout.com.anew.easyItalian.read


import java.io.Serializable

class Article(val id: String, val title: String, val level: String, val text: String, private val imageUrl: String) : Serializable {

    fun getimageUrl(): String {
        return this.imageUrl
    }

}
