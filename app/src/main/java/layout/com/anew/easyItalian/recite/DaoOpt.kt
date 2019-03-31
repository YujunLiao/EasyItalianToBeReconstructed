package layout.com.anew.easyItalian.recite

import android.content.Context
import layout.com.anew.easyItalian.gen.WordDao

class DaoOpt private constructor(){
    private object mHolder {
        val instance = DaoOpt()
    }

    companion object {
        fun getInstance(): DaoOpt {
            return mHolder.instance
        }
    }

    /**
     * 添加数据至数据库
     * @param context
     * *
     * @param word
     */
    fun insertData(context: Context?, word: Word) {

        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.insert(word)
    }


    /**
     * 将数据实体通过事务添加至数据库

     * @param context
     * *
     * @param list
     */
    fun insertData(context: Context?, list: List<Word>?) {
        if (null == list || list.size <= 0) {
            return
        }
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.insertInTx(list)
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；

     * @param context
     * *
     * @param word
     */
    fun saveData(context: Context?, word: Word) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.save(word)
    }

    /**
     * 删除数据至数据库

     * @param context
     * *
     * @param word 删除具体内容
     */
    fun deleteData(context: Context?, word: Word) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.delete(word)
    }

    /**
     * 根据id删除数据至数据库

     * @param context
     * *
     * @param id      删除具体内容
     */
    fun deleteByKeyData(context: Context?, id: Long) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.deleteByKey(id)
    }

    /**
     * 删除全部数据

     * @param context
     */
    fun deleteAllData(context: Context?) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.deleteAll()
    }

    /**
     * 更新数据库

     * @param context
     * *
     * @param word
     */
    fun updateData(context: Context?, word: Word) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.update(word)
    }


    /**
     * 查询所有数据

     * @param context
     * *
     * @return
     */
    fun queryAll(context: Context?): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        return builder?.build()?.list()
    }

    /**
     * 根据id，其他的字段类似

     * @param context
     * *
     * @param id
     * *
     * @return
     */
    fun queryForId(context: Context?, id: Long): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();

         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();
        return builder?.where(WordDao.Properties.Id.eq(id))?.list()
    }
    fun queryForAppearTime(context: Context?, id: Long): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();

         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();

        return builder?.where(WordDao.Properties.AppearTime.eq(id))?.list()
    }

    fun queryForCorrectTime(context: Context?, id: Long): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();

         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();

        return builder?.where(WordDao.Properties.CorrectTime.eq(id))?.list()
    }
    fun queryForIncorrectTime(context: Context?, id: Long): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();

         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();

        return builder?.where(WordDao.Properties.IncorrectTime.eq(id))?.list()
    }

    fun queryForNextAppearTime(context: Context?, id: Long): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();

         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();
        return builder?.where(WordDao.Properties.NextAppearTime.eq(id))?.list()
    }

    fun queryForNotZeroIncorrectTimes(context: Context?,id: Long): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        return builder?.where(WordDao.Properties.NextAppearTime.gt(0), WordDao.Properties.Id.notEq(id))?.list()
    }

    fun queryForIdNotEqual(context: Context?, id: MutableCollection<Long>): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();
         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();
        return builder?.where(WordDao.Properties.Id.notIn(id))?.list()
    }
    fun getNumberOfItems(context: Context?):Int {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        return builder?.build()?.list()?.size ?:0
    }

    fun queryForGrasp(context: Context?, id: Boolean): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();

         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();
        return builder?.where(WordDao.Properties.Grasp.eq(id))?.list()
    }

    fun queryForWord(context: Context?, word: String): MutableList<Word>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();

         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();
        return builder?.where(WordDao.Properties.Word.eq(word))?.list()
    }

}