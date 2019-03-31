package layout.com.anew.easyItalian.WordList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import kotlinx.android.synthetic.main.activity_wods_lists.*
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R
import layout.com.anew.easyItalian.recite.DaoOpt
import layout.com.anew.easyItalian.recite.Word
import layout.com.anew.easyItalian.recite.WordDetailsActivity

class WordsListsPage : Activity() {


    private var word=Word()
    private val data = ArrayList<String>()
    //private var data= MutableList<String>()
    //private var data= mutableListOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wods_lists)
        val ins = intent
        val choice = ins.getStringArrayListExtra("choice")[0]
        when(choice){

            "1"->showNewWordsList()
            "2"->showFinishedWordsList()
            "3"->showComingWordsList()
            else->{}
        }



        val listView = findViewById<View>(R.id.new_words) as ListView
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            when(choice){

                "1"->doNewWordFunc(position)
                "2"->{
                    val text =listView.getItemAtPosition(position).toString()
                    doWordfunc(text)
                }
                "3"->{
                    val text =listView.getItemAtPosition(position).toString()
                    doWordfunc(text)
                }
                else->{}
            }
        }

        //Toast.makeText(this,"wordInList Successfully "+save.toString() , Toast.LENGTH_SHORT).show()

        val buttonForBack = findViewById<View>(R.id.back) as Button
        buttonForBack.setOnClickListener {
            val returnToMain = Intent(this, MainActivity::class.java)
            startActivity(returnToMain)
        }
    }

    private fun doWordfunc(text: String) {
      //  Toast.makeText(this, "finished : "+text, Toast.LENGTH_SHORT).show()
        val my = DaoOpt.getInstance()
        word=my.queryForWord(this,text)!!.elementAt(0)
        val detailData = arrayListOf(word.word,word.transform,word.translation,word.example)
        val showDetailsActivity = Intent()
        showDetailsActivity.setClass(this, WordListDetailPage::class.java)
        showDetailsActivity.putStringArrayListExtra("detailData",detailData)
        //finish()
        startActivity(showDetailsActivity)

    }

    private fun doNewWordFunc(i: Int) {

        var wordNewList= SQLite.select().from(WordNew::class.java).queryList()
      //  Toast.makeText(this, "new : "+wordNewList[i].word, Toast.LENGTH_SHORT).show()

        word.word=wordNewList.elementAt(i).word
        word.transform=wordNewList.elementAt(i).transform
        word.translation=wordNewList.elementAt(i).translation
        word.example=wordNewList.elementAt(i).example
        val detailData = arrayListOf(word.word,word.transform,word.translation,word.example)
        val showDetailsActivity = Intent()
        showDetailsActivity.setClass(this, WordListDetailPage::class.java)
        showDetailsActivity.putStringArrayListExtra("detailData",detailData)
        //finish()
        startActivity(showDetailsActivity)
    }


    fun showNewWordsList(){
        toolbar2.title=getString(R.string.title_new_word_list)
        var word= WordNew()
        var wordNewList= SQLite.select().from(WordNew::class.java).queryList()
        //var n=wordGraphedList.size
        //Toast.makeText(this, wordNewList[0].word, Toast.LENGTH_SHORT).show()
        for (wordNew in wordNewList){
            //Toast.makeText(this, wordNew.id.toString()+wordNew.word, Toast.LENGTH_SHORT).show()
            data.add(wordNew.word)
        }
        if(data.size==0){
            Toast.makeText(this, getString(R.string.str_nulllist), Toast.LENGTH_SHORT).show()
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<View>(R.id.new_words) as ListView
        listView.adapter = adapter

    }

    fun showFinishedWordsList(){
        toolbar2.title=getString(R.string.title_already_finished)
        val my = DaoOpt.getInstance()
        val listFinshed:MutableList<Word>?=my.queryForGrasp(this,true)

        for (wordFinished in listFinshed.orEmpty()){
            //Toast.makeText(this, wordFinished.id.toString()+wordFinished.word, Toast.LENGTH_SHORT).show()
            data.add(wordFinished.word)
        }

        if(data.size>=1)
            data.removeAt(0)

        if(data.size==0)
            Toast.makeText(this, getString(R.string.str_nulllist), Toast.LENGTH_SHORT).show()


    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<View>(R.id.new_words) as ListView
        listView.adapter = adapter
    }
    fun showComingWordsList(){
        toolbar2.title=getString(R.string.title_coming_word)
        //val title = findViewById<Button>(R.id.titleinList)
        //title.setText("尚未学习")
        val my = DaoOpt.getInstance()
        val listComing:MutableList<Word>?=my.queryForGrasp(this,false)

        for (wordFinished in listComing.orEmpty()){
            //Toast.makeText(this, wordFinished.id.toString()+wordFinished.word, Toast.LENGTH_SHORT).show()
            data.add(wordFinished.word)
        }
        if(data.size==0){
            Toast.makeText(this, getString(R.string.str_nulllist), Toast.LENGTH_SHORT).show()
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<View>(R.id.new_words) as ListView
        listView.adapter = adapter
    }

    fun getWordDetail(word:WordNew){

        val detailData: Array<String>
        if(word.transform!=" " && word.example!=" ") {
            detailData = arrayOf(word.translation, word.transform, word.example)
        }else if (word.transform!=" " && word.example==" "){
            detailData = arrayOf(word.translation, word.transform)
        }else if (word.transform==" "&& word.example!=" "){
            detailData = arrayOf(word.translation, word.example)
        }else{
            detailData = arrayOf(word.translation)
        }
    }
}