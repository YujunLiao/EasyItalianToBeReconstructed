package layout.com.anew.easyItalian.read


import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso

import java.util.ArrayList

import layout.com.anew.easyItalian.R

/**
 * Created by liaoyujun on 2018/4/25.
 */

class ArticleAdapter(context: Context, private val mList: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    class ViewHolder( val articleView: View) : RecyclerView.ViewHolder(articleView) {
        val title: TextView
        val level: TextView
        val text: TextView
        val image: ImageView

        init {
            title = articleView.findViewById<View>(R.id.title) as TextView
            level = articleView.findViewById<View>(R.id.level) as TextView
            text = articleView.findViewById<View>(R.id.text) as TextView
            image = articleView.findViewById<View>(R.id.imageView) as ImageView
        }
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var level = "★"
        val article = mList[position]
        //holder.itemView.setTag(position);
        when (article.level) {
            "1" -> level = "★"
            "2" -> level = "★★"
            "3" -> level = "★★★"
            "4" -> level = "★★★★"
            "5" -> level = "★★★★★"
            "6" -> level = "★★★★★★"
        }
        holder.title.text = article.title
        holder.level.text = level
        holder.text.text = article.text
        Picasso.get().load(article.getimageUrl()).into(holder.image)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item,parent,false);

        val //view = View.inflate(parent.getContext(), R.layout.article_item, null);
                view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, null)
        val holder = ViewHolder(view)

        //点击效果
        holder.articleView.setOnClickListener { v ->
            val position = holder.adapterPosition
            val article = mList[position]
            val uid = article.id
            val data = ArrayList<String>()
            data.add(uid)
            val showArticlePageActivity = Intent()
            showArticlePageActivity.setClass(v.context, ArticlePageActivity::class.java)
            showArticlePageActivity.putStringArrayListExtra("data", data)
            v.context.startActivity(showArticlePageActivity)
        }

        //点击title
        holder.title.setOnClickListener { v ->
            val position = holder.adapterPosition
            val article = mList[position]

            // the following part is ok
            val uid = article.id
            val data = ArrayList<String>()
            data.add(uid)
            val showArticlePageActivity = Intent()
            showArticlePageActivity.setClass(v.context, ArticlePageActivity::class.java)
            showArticlePageActivity.putStringArrayListExtra("data", data)
            v.context.startActivity(showArticlePageActivity)
        }

        //点击正文
        holder.text.setOnClickListener { v ->
            val position = holder.adapterPosition
            val article = mList[position]
            val uid = article.id
            val data = ArrayList<String>()
            data.add(uid)
            val showArticlePageActivity = Intent()
            showArticlePageActivity.setClass(v.context, ArticlePageActivity::class.java)
            showArticlePageActivity.putStringArrayListExtra("data", data)
            v.context.startActivity(showArticlePageActivity)
        }
        return holder
    }


}

