package layout.com.anew.easyItalian.read

import android.app.Activity
import android.content.Intent
import android.os.*
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R
import java.util.ArrayList
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog



class ReadActivity : Activity() {


    private val articleList = ArrayList<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        val layoutManager = LinearLayoutManager(this@ReadActivity)
        val amHandler: Handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when (msg?.what) {
                    3 -> {
                        //将articleTist里的文章展示出来
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        recyclerView.layoutManager = layoutManager
                        val adapter = ArticleAdapter(this@ReadActivity, articleList)
                        recyclerView.adapter = adapter
                        recyclerView.addItemDecoration(DividerItemDecoration(this@ReadActivity, DividerItemDecoration.VERTICAL))
                    }
                    else -> {
                        val mBundle = msg?.data
                    }
                }
            }
        }

        //初始化数据
        try {
            val mDialog = MaterialDialog.Builder(this@ReadActivity)
                    .title(R.string.progress_dialog_acquiring)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .show()
            object : Thread() {
                override fun run() {
                    try {
                        initArticle()
                        amHandler.sendEmptyMessage(3)
                        mDialog.cancel()
                    }catch (e:Exception){
                        Looper.prepare()
                        Toast.makeText(this@ReadActivity,"Connection Error",Toast.LENGTH_SHORT).show()
                        Looper.loop()
                        finish()
                    }

                }
            }.start()
          //  initArticle()
        }catch (e :Exception){
            Toast.makeText(this,"Connection Error",Toast.LENGTH_SHORT).show()
            finish()
        }




        val back = findViewById<Button>(R.id.backForRead)
        back.setOnClickListener {
            finish()
            val intent = Intent()
            intent.setClass(this@ReadActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //初始化数据
    private fun initArticle() {

        var uid = 9900000
        var article = getArticle(uid.toString())
        while (article.title !== "Error") {
            articleList.add(article)
            uid++
            article = getArticle(uid.toString())
        }
    }

    private fun getArticle(uid: String): Article {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build())

        val query = AVQuery<AVObject>("Articles")
        val thisArticle: Article
        if (query.whereEqualTo("UID", uid) != null) {
            val a = query.whereEqualTo("UID", uid).first

            val thisTitle :String?
            val thisLevel :String?
            val thisImageUrl:String?
            val thisText :String ?
                if(a != null){
                    thisTitle = a.get("title").toString()
                    thisLevel = a.get("level").toString()
                    thisImageUrl = "http://avisy.ddns.net:3322/" + a.get("imageUrl").toString()
                    thisText = a.get("text").toString()
                    thisArticle = Article(uid, thisTitle, thisLevel, thisText, thisImageUrl)
                } else thisArticle = Article("9900000", "Error", "0", "Error", "Error")
            return thisArticle

        } else {
            thisArticle = Article("9900000", "Error", "0", "Error", "Error")
            return thisArticle
        }
    }


}

