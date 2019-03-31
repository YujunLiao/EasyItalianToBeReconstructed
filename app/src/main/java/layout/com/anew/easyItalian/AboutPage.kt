package layout.com.anew.easyItalian
import android.content.Context
import android.os.Bundle
import android.support.annotation.NonNull
import android.widget.Toast
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import layout.com.anew.easyItalian.R
import android.content.Intent
import android.net.Uri


class AboutPage : MaterialAboutActivity() {

    @NonNull
    override protected fun getMaterialAboutList(context : Context): MaterialAboutList{

        val EI = MaterialAboutCard.Builder()
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.mipmap.ic_version)
                        .text("version")
                        .subText("1.0.0")
                        .setOnClickAction{
                            //Toast.makeText(this,"version",Toast.LENGTH_SHORT).show()
                        }
                        .build())
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.mipmap.ic_version)
                        .text("SourceCode")
                        .subText("GitHub")
                        .setOnClickAction{
                            val uri = Uri.parse("https://github.com/SE-project-EasyItalian/EasyItalian-1")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                        .build())
                .title("EasyItalian")
                .build()


        val Author = MaterialAboutCard.Builder()
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.mipmap.ic_version)
                        .text("Our Group")
                        .subText("Lv Jiaying, Liao Yujun, XuShaoxun")
                        .build())
                .addItem(MaterialAboutActionItem.Builder()
                        .icon(R.mipmap.ic_version)
                        .text("Project EasyItalian")
                        .subText("Make it easy&fun for Chinese to Learn Italian")
                        .build())
                .title("About us")
                .build()
        return MaterialAboutList.Builder()
                .addCard(EI)
                .addCard(Author)
                .build() // This creates an empty screen, add cards with .addCard()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getActivityTitle(): CharSequence? {
        return getString(R.string.mal_title_about)
    }
}
