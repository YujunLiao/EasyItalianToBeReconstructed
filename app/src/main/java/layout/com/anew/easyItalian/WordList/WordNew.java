package layout.com.anew.easyItalian.WordList;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by liaoyujun on 2018/5/25.
 */

@Table(database = DBFlowDataBase.class)
public class WordNew extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String word;
    @Column
    public String transform;
    @Column
    public String translation;
    @Column
    public String example;


    public WordNew(){

    }

    public  void insertData(WordNew word){
        this.word=word.word;
        this.transform=word.transform;
        this.translation=word.translation;
        this.example=word.example;

    }

    public long getId() {
        return id;
    }
    public String getWord(){
        return word;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setWord(String word) {
        this.word = word;
    }

}
