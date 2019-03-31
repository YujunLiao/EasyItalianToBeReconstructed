package layout.com.anew.easyItalian.read


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import kotlinx.android.synthetic.main.activity_article_page.*
import android.os.StrictMode
import android.widget.Toast
import com.squareup.picasso.Picasso
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R







class ArticlePageActivity() : Activity() {
    var uid = "9900000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_page)
        try{
            getArticle()
        }catch (e:Exception){
            Toast.makeText(this,"Error!Please try it later.",Toast.LENGTH_SHORT).show()
            finish()
            val intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
            e.printStackTrace()
        }

        backForArticlePage.setOnClickListener{
            finish()
            val intent = Intent()
            intent.setClass(this, ReadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getArticle(){

        val ins = intent
        val listdata = ins.getStringArrayListExtra("data")
        uid=listdata[0]
        // necessary for getting data from internet
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build())
        val query = AVQuery<AVObject>("Articles")
        val mytitle = query.whereEqualTo("UID",uid).first["title"].toString()
        titleOfArticle.setText(mytitle)
        val mylevel =  query.whereEqualTo("UID",uid).first["level"].toString()
        //show level
        // change it
       // Toast.makeText(this,mylevel,Toast.LENGTH_SHORT).show()
        // directly use url when saving into leancloud
        val picNum = query.whereEqualTo("UID",uid).first["imageUrl"].toString()
        val imUrm = "http://avisy.ddns.net:3322/"+picNum
        Picasso.get().load(imUrm).into(image)
        val mytext = query.whereEqualTo("UID",uid).first["text"].toString()
        text.setText(mytext)
    }

}
