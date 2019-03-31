package layout.com.anew.easyItalian.recite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_finished.*
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R

class FinishedPage : Activity () {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)
        val my = DaoOpt.getInstance()
        my.deleteAllData(this)
        backForFinished.setOnClickListener{
            finish()
            val intent = Intent()
            intent.setClass(this,MainActivity::class.java)
            startActivity(intent)
        }
        MaterialDialog.Builder(this).title(getString(R.string.finished))
                .content(getString(R.string.finished_str))
                .negativeText(getString(R.string.no))
                .positiveText(getString(R.string.yes)).onPositive{_,_->
                    finish()
                    val intent = Intent()
                    intent.setClass(this, SetWordList::class.java)
                    startActivity(intent)
                }.onNegative{
                    _,_ ->
                    finish()
                    val intent = Intent()
                    intent.setClass(this,MainActivity::class.java)
                    startActivity(intent)
                }.show()
    }
}
