package layout.com.anew.easyItalian.recite

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import layout.com.anew.easyItalian.R


class WordListAdapter: RecyclerView.Adapter<WordListAdapter.RecyclerHolder>{


    private var mContext: Context? =null
    private var mList: List<Wordlist>? = null
    private var item : ItemClick? = null

    constructor(context: Context, list: ArrayList<Wordlist>, item: ItemClick) {
        this.mList = list;
        this.mContext = context;
        this.item=item;
    }
    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val wordlist = mList!![position]
        holder.text.text = wordlist.wordlistName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wordlist, null)

        val holder = RecyclerHolder(view)

        holder.text.setOnClickListener(View.OnClickListener { v ->
            val position = holder.adapterPosition
            item?.OnItemClick(v,position)
        })

        return holder
    }

    class RecyclerHolder(private val wordlistView: View) : RecyclerView.ViewHolder(wordlistView) {
         var text: TextView

        init {
            text = wordlistView.findViewById<View>(R.id.text) as TextView
        }
    }


    inner class onClick:View.OnClickListener{
        override fun onClick(v: View) {
            val position = RecyclerHolder(v).adapterPosition
            if(item==null){
                Toast.makeText(mContext,"???",Toast.LENGTH_LONG).show();
            }
            item!!.OnItemClick(v,position);
        }
    }
}


