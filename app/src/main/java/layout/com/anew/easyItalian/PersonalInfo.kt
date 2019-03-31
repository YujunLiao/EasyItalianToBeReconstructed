package layout.com.anew.easyItalian

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import layout.com.anew.easyItalian.recite.SetWordList
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.SaveCallback
import java.io.*


class PersonalInfo : Activity(){


    private val FILENAMEOFPIC="head.jpg"//相机
    private val FILENAMEOFPIC2="head2.jpg"//相机和相册
    private val FILENAMEOFPIC3="head3.jpg"//相机和相册
    private var imageUri: Uri?=null
    private val FILENAME="profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_info)
        val profileStr = resources.getString(R.string.profile_str)
        val nickNameStr = resources.getString(R.string.nickName_str)
        val wordListStr = resources.getString(R.string.wordList_str)
        val logOut = resources.getString(R.string.logout)
        val data = arrayOf(profileStr,nickNameStr,wordListStr,logOut)
        val listView = findViewById<View>(R.id.list_view1) as ListView
        listView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        listView.setOnItemClickListener{
            _, _, position, _ ->
            //set click listener
            when(position){
                0 -> { showImagePickDialog(this)
                    // use handler to refresh ui
                //    val mApp = application as BaseApplication
                  //  mApp.mHandler!!.sendEmptyMessageDelayed(1,2000)
                }
                1 ->{ setNickname() }
                2 -> {
                    val intent = Intent()
                    intent.setClass(this, SetWordList::class.java)
                    startActivity(intent)
                }
                3 ->{
                    logout()
                }
            }
        }
        val button_back = findViewById<View>(R.id.back_) as Button
        button_back.setOnClickListener(){
            // use handler to refresh ui
            val mApp = application as BaseApplication
            mApp.mHandler?.sendEmptyMessage(1)
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun logout(){
        Toast.makeText(this,"Bye",Toast.LENGTH_SHORT).show()
        if (AVUser.getCurrentUser()!=null)
            AVUser.logOut()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        // use handler to refresh ui
        val mApp = application as BaseApplication
        mApp.mHandler?.sendEmptyMessage(1)
    }
    fun showImagePickDialog(activity: Activity) {
        val title = getString(R.string.select_pic_resource)
        val takePicStr = getString(R.string.takePic_str)
        val fromAlbumStr = getString(R.string.fromAlbum_str)
        val items = arrayOf(takePicStr, fromAlbumStr)
        AlertDialog.Builder(activity)
                .setTitle(title)
                .setItems(items, DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    when (which) {
                        0 ->{
                            chooseCamera()
                            //Toast.makeText(this, "拍照", Toast.LENGTH_LONG).show()
                        }
                        1 ->{
                            chooseAlbum()
                           // Toast.makeText(this, "相册", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }
                }).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            startActivity(Intent(this, MainActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }



//for set profile
    fun chooseCamera(){
        var mFile= File(this.getExternalFilesDir(null),FILENAME)
        if(!mFile.exists()){
            mFile.mkdirs()
        }
        val outPutImage= File(mFile,FILENAMEOFPIC)
        try{
            if(outPutImage.exists()){
                outPutImage.delete()
            }
            outPutImage.createNewFile()
            // Toast.makeText(this@ChooseDialog, "file for pic create", Toast.LENGTH_LONG).show()
        }catch (e : IOException){
            e.printStackTrace()
        }
        if(Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(this,"com.example.cameraalbumtest.fileprovider",outPutImage)
           // Toast.makeText(this, "大于", Toast.LENGTH_LONG).show()
        }
        else
            imageUri=Uri.fromFile(outPutImage);
        try {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    2)

            val intent2=Intent("android.media.action.IMAGE_CAPTURE")
            intent2.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
            startActivityForResult(intent2, 2)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_permission), Toast.LENGTH_LONG).show()
        }

    }

    fun chooseAlbum(){
        try{
        ActivityCompat.requestPermissions(this,
                arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE),
                2)
        val intent1 = Intent(Intent.ACTION_PICK, null)//返回被选中项的URI
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")//得到所有图片的URI
        startActivityForResult(intent1, 1)
        }catch (e:Exception){
            Toast.makeText(this, getString(R.string.error_readStorage), Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    //保存传入的bitmap图片为pic2
    fun setPicToView(mBitmap : Bitmap) {
        val sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd卡是否可用
            return;
        }
        var b: FileOutputStream?  = null;
        val mFile= File(this.getExternalFilesDir(null),FILENAME)
        if(!mFile.exists()){
            mFile.mkdirs()
        }
        try {
            b= FileOutputStream(mFile.toString()+"/$FILENAMEOFPIC2")
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件（compress：压缩）
        } catch ( e: FileNotFoundException) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b!!.flush();
                b!!.close();
            } catch ( e: IOException) {
                e.printStackTrace();
            }

        }

    }

    //裁剪图片并保存为pic3
    fun cropPhoto(uri:Uri?) {
        val intent =  Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        var mFile= File(this.getExternalFilesDir(null),FILENAME)
        if(!mFile.exists()){
            mFile.mkdirs()
        }
        val file=File(mFile,FILENAMEOFPIC3)
        if(file.exists())
            file.delete()
        file.createNewFile()
        var outputUri=Uri.fromFile(file)
        //找到指定URI对应的资源图片
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
     //   Toast.makeText(this, "裁剪成功", Toast.LENGTH_LONG).show()
        //进入系统裁剪图片的界面
        startActivityForResult(intent, 3);
    }

    private fun cropBitmap(bitmap: Bitmap): Bitmap {
        val w = bitmap.width // 得到图片的宽，高
        val h = bitmap.height
        var cropWidth = if (w >= h) h else w// 裁切后所取的正方形区域边长
        cropWidth /= 2
        val cropHeight = (cropWidth / 1.2).toInt()
        return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false)
    }



    fun CopySdcardFile(fromFile: String, toFile: String): Int {

        try {
            val fosfrom = FileInputStream(fromFile)
            val fosto = FileOutputStream(toFile)
            val bt = ByteArray(1024)
            var c = fosfrom.read(bt)
            while (c > 0) {
                fosto.write(bt, 0, c)
                c = fosfrom.read(bt)
            }
            fosfrom.close()
            fosto.close()
            return 0
        } catch (ex: Exception) {
            return -1
        }

    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
        //从相册里面取相片的返回结果
            1->
                try{
                    if (resultCode == AppCompatActivity.RESULT_OK) {
                        var bitmap :Bitmap?  = null
                        //判断手机系统版本号
                        if (Build.VERSION.SDK_INT >= 19)
                            bitmap = ImgUtil.handleImageOnKitKat(this, data);
                        else
                            bitmap = ImgUtil.handleImageBeforeKitKat(this, data);
                        setPicToView(bitmap!!)
                        //data?.getData()是一个uri
                        cropPhoto(data?.getData())//裁剪图片
                    }
                }catch (e : IOException){
                    e.printStackTrace()
                }
        //相机拍照后的返回结果
            2->
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    try{
                        //BitmapFactory.decodeFile(path + "head.jpg");//从Sd中找头像，转换成Bitmap
                        var bitmap:Bitmap= BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                        //bitmap=cropBitmap(bitmap)
                        //setPicToView(bitmap)
                        cropPhoto(imageUri)//裁剪图片
                        //imageUri为camera获取
                        //Toast.makeText(this@ChooseDialog, "address:"+imageUri.toString() , Toast.LENGTH_LONG).show()
                    }catch (e : IOException){
                        e.printStackTrace()
                    }
                }
        //调用系统裁剪图片后
            3->
                if (data != null) {
                    var bitmap :Bitmap?  = null
                 //   Toast.makeText(this, "裁剪返回", Toast.LENGTH_LONG).show()
                    bitmap=data.getParcelableExtra("data")
                    //setPicToView(bitmap!!)//保存在SD卡中
                    //上传服务器代码
               //     Toast.makeText(this@PersonalInfo, "保存成功", Toast.LENGTH_LONG).show()

                    val currentUser = AVUser.getCurrentUser()
                    CopySdcardFile(this.getExternalFilesDir("profile").absolutePath+"/$FILENAMEOFPIC3",this.getExternalFilesDir("profile").absolutePath+"/"+currentUser.email+".jpg")
                    val file = AVFile.withAbsoluteLocalPath(currentUser.username+"_profile.jpg",  this.getExternalFilesDir("profile").absolutePath+"/"+currentUser.email+".jpg");
                    file.saveInBackground(object :SaveCallback() {
                        override fun done(p0: AVException?) {
                            if (p0==null){
                                currentUser.put("profile",file.url)
                                currentUser.saveInBackground()
                            }else{
                                Toast.makeText(this@PersonalInfo,"E!!",Toast.LENGTH_SHORT).show()
                            }
                        }
                    })


                }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1->{
                if (grantResults.size> 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    chooseAlbum()
                else
                    Toast.makeText(this,"can't open album",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //set nickname
    private fun setNickname(){
        try{
            val currentUser = AVUser.getCurrentUser()
            var nickName=currentUser.fetch().get("nickName").toString()
            MaterialDialog.Builder(this).title(getString(R.string.input_nickname)).widgetColor(Color.BLUE).inputType(InputType.TYPE_CLASS_TEXT).input("输入",  currentUser.fetch().get("nickName").toString() , object :MaterialDialog.InputCallback {

                override fun onInput(dialog: MaterialDialog, input: CharSequence?) {
                    currentUser.put("nickName",input)
                    currentUser.saveInBackground()
                    // use handler to refresh ui
                    val mApp = application as BaseApplication
                    mApp.mHandler?.sendEmptyMessage(1)
                }

            }).negativeText(getString(R.string.cancel)).positiveText(getString(R.string.confirm)).show()

        }catch (e:Exception){
            Toast.makeText(this,"Connection failed",Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

}
