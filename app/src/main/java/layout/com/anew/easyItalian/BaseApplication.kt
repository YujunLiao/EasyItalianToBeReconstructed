package layout.com.anew.easyItalian

import android.app.Application
import com.avos.avoscloud.AVOSCloud
import com.raizlabs.android.dbflow.config.FlowManager
import layout.com.anew.easyItalian.gen.DaoSession
import layout.com.anew.easyItalian.gen.DaoMaster
import layout.com.anew.easyItalian.recite.GreenDaoManager


class BaseApplication : Application(){

    var mHandler :android.os.Handler? = null

    private var daoSession: DaoSession? = null
    override fun onCreate() {
        super.onCreate()
        //配置数据库
        setupDatabase()
        GreenDaoManager.getInstance(this)

        // initialize leancloud
        AVOSCloud.initialize(this, "SbkfRG1bPvdg2EWKGKa3igM5-gzGzoHsz", "iC5bfhEPRSufXzogvr4pynno")

        FlowManager.init(this);
    }

    /**
     * 配置数据库
     */
    private fun setupDatabase() {
        //创建数据库
        val helper = DaoMaster.DevOpenHelper(this, "wordList.db", null)
        //获取可写数据库
        val db = helper.writableDatabase
        //获取数据库对象
        val daoMaster = DaoMaster(db)
        //获取Dao对象管理者
        daoSession = daoMaster.newSession()
    }

    fun getDaoInstant(): DaoSession? {
        return daoSession
    }

    public fun setHandler(mhandler:android.os.Handler){
        this.mHandler = mhandler
    }

}