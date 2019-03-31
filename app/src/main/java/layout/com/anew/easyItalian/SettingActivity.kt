package layout.com.anew.easyItalian


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import java.util.*
import android.util.DisplayMetrics




class SettingActivity : Activity() {
    private val data = arrayOf("语言", "主题", "壁纸")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<ListView>(R.id.list_view)
        listView.adapter = adapter


        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val showText: String
            when (position + 1) {

                1 -> {
                    showText = "执行第" + 1 + "个函数"
                    Toast.makeText(this, showText, Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    showText = "执行第" + 2 + "个函数"
                    Toast.makeText(this, showText, Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    showText = "执行第" + 3 + "个函数"
                    Toast.makeText(this, showText, Toast.LENGTH_SHORT).show()
                }
                4 -> {
                    showText = "执行第" + 4 + "个函数"
                    Toast.makeText(this, showText, Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }


        val buttonForBack = findViewById<Button>(R.id.back_)
        buttonForBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}
