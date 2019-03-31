package layout.com.anew.easyItalian

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import kotlinx.android.synthetic.main.activity_search_word_page.*
import kotlinx.android.synthetic.main.activity_word_imformation.*
import layout.com.anew.easyItalian.WordList.WordNew
import layout.com.anew.easyItalian.recite.Word

class SearchWordPage : Activity() {

    val word = Word()
    // getWord accepts the StringArrayList from ReciteWord or another Activity
    // and turn it to Word-info
    fun getWord() {
        val ins = intent
        val listdata = ins.getStringArrayListExtra("data")
        word.word=listdata[0]
        word.transform=listdata[1]
        word.translation=" "
        word.example=" "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_word_page)

        findViewById<Button>(R.id.addToNewWordListS).background.alpha=100
        getWord()
        wordForDetailsS.setText(word.word)
        toolbarS.title="   "+ word.word
        // set list for word details
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

        voiceS.setOnClickListener{
            //call tts
            callTTS()
        }


        // the buttons' listener has problem

        val button_back = findViewById<Button>(R.id.back)
        button_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

       addToNewWordListS.setOnClickListener{
           var judge = 1
           val wordNewList= SQLite.select().from(WordNew::class.java).queryList()
           for (wordNew in wordNewList){
               if(wordNew.word.equals(word.word)){
                   judge=0
                   break
               }
           }
           if(judge==1){
               val wordN= WordNew()
               wordN.word=word.word
               wordN.transform=word.transform
               wordN.translation=word.translation
               wordN.example=word.example


               //add to new WordsList
               val wordNew= WordNew()
               wordNew.insertData(wordN)
               wordNew.save()
               Toast.makeText(this,"add "+word.word, Toast.LENGTH_SHORT).show()
           }
           else{

               Toast.makeText(this,"added already ", Toast.LENGTH_SHORT).show()
           }
       }

    }


    //tts start
    private fun buildSpeechUrl(words: String): String {

        //tts from VoiceRss
        //val kVoiceRssServer = "http://api.voicerss.org"
        //val kVoiceRSSAppKey = "ec58f36cbbe846edb12b9fcc6e217d9f"
        //var url = ""
        //url = kVoiceRssServer + "/?key=" + kVoiceRSSAppKey + "&t=text&hl=" + "it-it" + "&src=" + words

        //tts from MARY TTS
        //http://mary.dfki.de:59125/process?INPUT_TEXT=pomodoro&INPUT_TYPE=TEXT&OUTPUT_TYPE=AUDIO&AUDIO=WAVE_FILE&LOCALE=it
        var url="http://mary.dfki.de:59125/process?INPUT_TEXT="
        val endIt="&INPUT_TYPE=TEXT&OUTPUT_TYPE=AUDIO&AUDIO=WAVE_FILE&LOCALE=it"
        url = url+words+endIt
        url = url.replace(' ','+')
        return url
    }



    private fun callTTS(){
        val txtSentence = findViewById<TextView>(R.id.wordForDetails)
        val text = txtSentence.text.toString()
        val url = buildSpeechUrl(text)
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(url)
        mediaPlayer.setOnErrorListener{_, _, _ -> false}
        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
        mediaPlayer.setOnPreparedListener {  mediaPlayer.start()}
        mediaPlayer.prepareAsync()
    }
    //tts end

}
