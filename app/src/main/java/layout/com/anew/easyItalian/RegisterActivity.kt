package layout.com.anew.easyItalian

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.SignUpCallback
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        class signUpCallBack: SignUpCallback(){
            override fun done(e: AVException?) {
                if (e == null) {
                    // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                    // use handler to refresh ui
                    val mApp = application as BaseApplication
                    mApp.mHandler?.sendEmptyMessage(1)
                    this@RegisterActivity.finish();
                    val intent= Intent(this@RegisterActivity,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    showProgress(false);
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show();
                }
            }
        }
        button_for_sign_up.setOnClickListener {
            val username =userName.text.toString()
            val email = eMail.text.toString()
            val password = passWord.text.toString()
            val passwordR = passWordR.text.toString()
            var valid = true
            if(!isEmailValid(email)) valid=false
            if (!isPasswordValid(password,passwordR)) valid=false
            if (valid){
                val user = AVUser();// 新建 AVUser 对象实例
                user.setUsername(username);// 设置用户名
                user.put("nickName",username)
                user.setPassword(password);// 设置密码
                user.setEmail(email);//设置邮箱
                user.signUpInBackground(signUpCallBack())
            }else{
                Toast.makeText(this,isPasswordValid(password,passwordR).toString(), Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String,passwordR: String): Boolean {
       return password.length >= 6 && password.equals(passwordR)
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            register_form.visibility = if (show) View.GONE else View.VISIBLE
            register_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            register_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            register_progress.visibility = if (show) View.VISIBLE else View.GONE
            register_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            register_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            register_progress.visibility = if (show) View.VISIBLE else View.GONE
            register_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
}
