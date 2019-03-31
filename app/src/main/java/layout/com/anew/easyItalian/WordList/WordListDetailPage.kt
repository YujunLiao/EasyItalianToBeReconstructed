package layout.com.anew.easyItalian.WordList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_word_imformation.*
import layout.com.anew.easyItalian.R
import layout.com.anew.easyItalian.recite.Word

class WordListDetailPage : Activity() {

    private var word = Word()
    // getWord accepts the StringArrayList from ReciteWord or another Activity
    // and turn it to Word-info

    fun getWord() {
        val ins = intent
        val listdata = ins.getStringArrayListExtra("detailData")
        word.word=listdata[0]
        word.transform=listdata[1]
        word.translation=listdata[2]
        word.example=listdata[3]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list_imformation)

        getWord()
        wordForDetails.setText(word.word)

     //   Toast.makeText(this, "WordListDetailPage", Toast.LENGTH_SHORT).show()
        val data: Array<String>


        if(word.transform!=" " && word.example!=" ") {
            data = arrayOf(word.translation, word.transform, word.example)
        }else if (word.transform!=" " && word.example==" "){
            data = arrayOf(word.translation, word.transform)
        }else if (word.transform==" "&& word.example!=" "){
            data = arrayOf(word.translation, word.example)
        }else{
            data = arrayOf(word.translation)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<View>(R.id.wordDetails) as ListView
        listView.adapter = adapter

        val button_back = findViewById<Button>(R.id.back)
        button_back.setOnClickListener {
            val intent = Intent(this, WordsListsPage::class.java)
            finish()
            startActivity(intent)
        }
    }


}
