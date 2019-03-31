package layout.com.anew.easyItalian.recite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import layout.com.anew.easyItalian.gen.DaoMaster
import layout.com.anew.easyItalian.gen.DaoSession


class GreenDaoManager private constructor(mContext: Context) {

    private val DB_NAME = "wordList.db"
    private var mDevOpenHelper: DaoMaster.DevOpenHelper? = null
    private var mDaoMaster: DaoMaster? = null
    private var mDaoSession: DaoSession? = null

    init {
        // 初始化数据库信息
        mDevOpenHelper = DaoMaster.DevOpenHelper(mContext, DB_NAME)
        getDaoMaster(mContext)
        getDaoSession(mContext)
    }

    companion object {
        @Volatile
        var instance: GreenDaoManager? = null

        fun getInstance(mContext: Context): GreenDaoManager? {
            if (instance == null) {
                synchronized(GreenDaoManager::class) {
                    if (instance == null) {
                        instance = GreenDaoManager(mContext)
                    }
                }
            }
            return instance
        }
    }

    /**
     * 获取可读数据库
     *
     * @param context
     * @return
     */
    fun getReadableDatabase(context: Context): SQLiteDatabase? {
        if (null == mDevOpenHelper) {
            getInstance(context)
        }
        return mDevOpenHelper?.getReadableDatabase()
    }

    /**
     * 获取可写数据库
     *
     * @param context
     * @return
     */
    fun getWritableDatabase(context: Context): SQLiteDatabase? {
        if (null == mDevOpenHelper) {
            getInstance(context)
        }

        return mDevOpenHelper?.getWritableDatabase()
    }

    /**
     * 获取DaoMaster
     *
     * @param context
     * @return
     */
    fun getDaoMaster(context: Context): DaoMaster? {
        if (null == mDaoMaster) {
            synchronized(GreenDaoManager::class.java) {
                if (null == mDaoMaster) {
                    mDaoMaster = DaoMaster(getWritableDatabase(context))
                }
            }
        }
        return mDaoMaster
    }

    /**
     * 获取DaoSession
     *
     * @param context
     * @return
     */
    fun getDaoSession(context: Context): DaoSession? {
        if (null == mDaoSession) {
            synchronized(GreenDaoManager::class.java) {
                mDaoSession = getDaoMaster(context)?.newSession()
            }
        }

        return mDaoSession
    }
}