package layout.com.anew.easyItalian

import android.Manifest
import android.content.Intent
import android.os.*
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.avos.avoscloud.AVUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.content_main.*
import layout.com.anew.easyItalian.WordList.WordsListsPage
import layout.com.anew.easyItalian.read.ReadActivity
import layout.com.anew.easyItalian.recite.ReciteWordAcitivity
import java.io.File


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    override fun onCreate(savedInstanceState: Bundle?) {

        try{
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    2)
        }catch (e:Exception){
            Log.d("EasyItalian","permission denied")
        }
      //  val login = Intent(this,LoginActivity::class.java)
     //   startActivity(login)
        val currentUser = AVUser.getCurrentUser()
        if (currentUser == null) {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val profileFile = getExternalFilesDir("profile").absolutePath+"/"+currentUser?.objectId+".jpg"
        try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                    .penaltyLog().penaltyDeath().build())
            val myProfile =   currentUser.fetch().get("profile").toString()
            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
            val headerView = navigationView.getHeaderView(0)
            val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
            if (myProfile!="laura")
                Picasso.get().load(myProfile).into(profile)
            val nickName = headerView.findViewById<TextView>(R.id.textView)
            if(currentUser!=null){
                nickName.setText(currentUser.fetch().get("nickName").toString() )
            }
        } catch (e:Exception){
            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
            val headerView = navigationView.getHeaderView(0)
            val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
            if (File(profileFile).exists()){
                Picasso.get().load(File(profileFile)).into(profile)
                Log.d("Profile","Connection Error, Use local profile")
            }else
                Picasso.get().load(R.mipmap.laura).into(profile)
            Log.d("Profile","Can't find local profile")
        }


       val amHandler:Handler= object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when (msg?.what) {
                    1 -> {
                        try {
                            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                                    .detectDiskReads().detectDiskWrites().detectNetwork()
                                    .penaltyLog().build())
                            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                                    .penaltyLog().penaltyDeath().build())
                            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                            val headerView = navigationView.getHeaderView(0)
                            val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
                            val myProfile =  AVUser.getCurrentUser().fetch().get("profile").toString()
                            if (myProfile!="laura")
                                Picasso.get().load(myProfile).into(profile)
                            if(AVUser.getCurrentUser()!=null){
                                val nickName = headerView.findViewById<TextView>(R.id.textView)
                                nickName.setText(AVUser.getCurrentUser().fetch().get("nickName").toString() )
                            }
                        } catch (e:Exception){

                            if (File(profileFile).exists()){
                                val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                                val headerView = navigationView.getHeaderView(0)
                                val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
                                Picasso.get().load(File(profileFile)).into(profile)
                                Log.d("Profile","Connection Error, Use local profile")
                            }else
                            {
                                val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                                val headerView = navigationView.getHeaderView(0)
                                val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
                                Picasso.get().load(R.mipmap.laura).into(profile)
                            }
                            Log.d("Profile","Can't find local profile")
                        }
                    }
                    else -> {
                        val mBundle = msg?.data
                    }
                }
            }

        }
        amHandler.sendEmptyMessage(1)

        val mApp = application as BaseApplication
        mApp.setHandler(amHandler)



        // find parent view to make profile clickable on first call
        // CHOICE make the profile to a circle
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
        profile.setOnClickListener(){
       //     Toast.makeText(this,"call 个人资料 activity",Toast.LENGTH_SHORT).show()
            val changeToPersonalInfoActivity = Intent()
            changeToPersonalInfoActivity.setClass(this,PersonalInfo::class.java)
            startActivity(changeToPersonalInfoActivity)
            drawer_layout.closeDrawer(GravityCompat.START) // close side bar
        }

        //search_new_word & learn_button & read_button
        searchWordButton.setOnClickListener(){
         //   AVUser.logOut()// 清除缓存用户对象
            // use handler to refresh ui
            val intent = Intent(this,SearchView::class.java)
            startActivity(intent)

        }


        buttonForLearn.setOnClickListener(){
            val changeToRecite = Intent()
            changeToRecite.setClass(this, ReciteWordAcitivity::class.java)
            startActivity(changeToRecite)
        }
        buttonForRead.setOnClickListener(){
           val changeToRead = Intent();
            changeToRead.setClass(this, ReadActivity::class.java)
           startActivity(changeToRead)
        }
        findViewById<Button>(R.id.buttonForRead).getBackground().setAlpha(100)
        // make sure the nav_view open&close in time
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_new_word -> {
                val choice=arrayListOf("1")
                val changeToNewWords = Intent()
                changeToNewWords.setClass(this, WordsListsPage::class.java)
                changeToNewWords.putStringArrayListExtra("choice",choice);
                startActivity(changeToNewWords)
            }
            R.id.nav_finished_word-> {
                val choice=arrayListOf("2")
                val changeToFinishedWords = Intent()
                changeToFinishedWords.setClass(this, WordsListsPage::class.java)
                changeToFinishedWords.putStringArrayListExtra("choice",choice)
                startActivity(changeToFinishedWords)
            }
            R.id.nav_coming_word -> {
                // same as above
                val choice=arrayListOf("3")
                val changeToComingWords = Intent()
                changeToComingWords.setClass(this, WordsListsPage::class.java)
                changeToComingWords.putStringArrayListExtra("choice",choice)
                startActivity(changeToComingWords)
            }
            R.id.nav_setting -> {
                val changeToPersonalInfoActivity = Intent()
                changeToPersonalInfoActivity.setClass(this,PersonalInfo::class.java)
                startActivity(changeToPersonalInfoActivity)

            }
            R.id.nav_about -> {
                val changeToAbout = Intent()
                changeToAbout.setClass(this, AboutPage::class.java)
                startActivity(changeToAbout)
               // Toast.makeText(this,"call 关于 activity",Toast.LENGTH_SHORT).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}

