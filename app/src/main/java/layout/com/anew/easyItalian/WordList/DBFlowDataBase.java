package layout.com.anew.easyItalian.WordList;

/**
 * Created by liaoyujun on 2018/5/26.
 */

import com.raizlabs.android.dbflow.annotation.Database;



@Database(name = DBFlowDataBase.NAME, version = DBFlowDataBase.VERSION)
public class DBFlowDataBase {

    //数据库名称
    public static final String NAME = "WordListDataBase";
    //数据库版本
    public static final int VERSION = 1;
}
