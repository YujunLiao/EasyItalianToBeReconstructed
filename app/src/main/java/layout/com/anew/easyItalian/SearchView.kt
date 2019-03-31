package layout.com.anew.easyItalian

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import com.lapism.searchview.Search
import com.lapism.searchview.widget.SearchView
import layout.com.anew.easyItalian.recite.DaoOpt
import layout.com.anew.easyItalian.recite.Word
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL



class SearchView : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_view)
      //  getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val searchView = findViewById<View>(R.id.searchView) as SearchView
        searchView.post(object :Runnable{
            override fun run() {
                searchView.open(null)
            }
        })
        searchView.setOnQueryTextListener(object: Search.OnQueryTextListener {
            override fun onQueryTextSubmit(query: CharSequence?): Boolean {
                StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads().detectDiskWrites().detectNetwork()
                        .penaltyLog().build())
                StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                        .penaltyLog().penaltyDeath().build())
                val text = searchView.text.toString()
                if (text.length != 0) {

                    try {
                        /*
                        *
                        *
                        * val my = DaoOpt.getInstance()
                        val mList = my.queryForWord(this@SearchView, text)

                        if (mList?.size != 0) {
                            val word: Word? = mList?.get(0)
                            val data = arrayListOf(word?.word, word?.transform, word?.translation, word?.example)
                            //Toast.makeText(this@SearchView, "search" + word?.word, Toast.LENGTH_SHORT).show()

                            val changeToSearchWordPage = Intent()
                            changeToSearchWordPage.setClass(this@SearchView, SearchWordPage::class.java)
                            changeToSearchWordPage.putStringArrayListExtra("data", data)
                            startActivity(changeToSearchWordPage)
                        } else {*/
                            val word: Word? = Word()
                            word?.setWord(text)
                            //word?.setTranslation(getTranslation(text))
                            //val data = arrayListOf(word?.word,word?.transform,word?.translation,word?.example)
                            //      val translation=getTranslation(text)
                            val translation = getDetails(text)
                            //   Toast.makeText(this@SearchView, translation, Toast.LENGTH_SHORT).show()
                            val data = arrayListOf(word?.word, translation)
                            val changeToSearchWordPage = Intent()
                            changeToSearchWordPage.setClass(this@SearchView, SearchWordPage::class.java)
                            changeToSearchWordPage.putStringArrayListExtra("data", data)
                            startActivity(changeToSearchWordPage)
                        //}
                    }catch (e:Exception){
                        Log.d("EasyItalian","Connection failed")
                        e.printStackTrace()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: CharSequence?) {

            }
        })

        searchView.setOnOpenCloseListener(object :Search.OnOpenCloseListener{
            override fun onClose() {
                finish()
            }
            override fun onOpen() {

            }
        })
    }
    fun streamToString(myis: InputStream): String? {
        try {
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = myis.read(buffer)
            while (len != -1) {
                out.write(buffer, 0, len)
                len = myis.read(buffer)
            }
            out.close()
            myis.close()
            val byteArray = out.toByteArray()
            return String(byteArray)
        } catch (e: Exception) {
            return null
        }
    }

    fun getTranslation(word :String):String{
        var result = ""
        try{
            val googleTranslate = "https://translate.google.cn/translate_a/single?client=gtx&sl=it&tl=zh-CN&dt=t&q="
            val translateUrl = googleTranslate+word
            val url = URL(translateUrl)
            val urlConn = url.openConnection() as HttpURLConnection
            // 设置连接主机超时时间
            urlConn.connectTimeout = 5 * 1000
            //设置从主机读取数据超时
            urlConn.readTimeout = 5 * 1000
            // 设置是否使用缓存  默认是true
            urlConn.useCaches = false
            // 设置为Post请求
            urlConn.requestMethod = "GET"
            //urlConn设置请求头信息
            val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"
            urlConn.setRequestProperty("User-Agent", USER_AGENT)
            urlConn.connect()
            val statusCode = urlConn.responseCode
            var googleResult = ""
            if (statusCode == 200) {
                // 获取返回的数据
                googleResult = streamToString(urlConn.getInputStream()) ?:""
            }

            val jsonArray = JSONArray(googleResult).getJSONArray(0)
            for (i in 0 until jsonArray.length()) {
                result += jsonArray.getJSONArray(i).getString(0)
            }
          //  Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
            return result
        }catch (e :Exception){
            e.printStackTrace();
            result = "";
        }
        return result
    }

    fun getDetails(word :String):String{
        val result:String
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build())
        val query = AVQuery<AVObject>("Words")
        if (query.whereEqualTo("word",word).first!=null){
            result = query.whereEqualTo("word",word).first["details"].toString()
        }else  {
            result=getTranslation(word)
        }
        return result
    }


}
