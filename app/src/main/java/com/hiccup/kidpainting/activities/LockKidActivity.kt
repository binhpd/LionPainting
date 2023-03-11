package com.hiccup.kidpainting.activities

import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.ImageView

import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.activities.store.StoreActivity
import com.hiccup.kidpainting.databinding.ActivityLockKidBinding
import com.hiccup.kidpainting.utilities.NumberToWord
import com.hiccup.kidpainting.utilities.extension.viewBinding
import com.hiccup.kidpainting.utilities.tracking.FireBaseEvent
import com.hiccup.kidpainting.utilities.tracking.FireBaseTracker
import java.util.*

class LockKidActivity : BaseActivity(), View.OnClickListener {

    private val imgPass = arrayOfNulls<ImageView>(9)
    private var mPass1: Int = 0
    private var mPass2: Int = 0
    private var mPass3: Int = 0
    private var count: Int = 0

    override val binding by viewBinding {
        ActivityLockKidBinding.inflate(layoutInflater)
    }

    private val randomKeyPass: Int
        get() = getRandomDoubleBetweenRange(1, 9)

    override fun onInitValue() {
        initView()
        populateClick()
        FireBaseTracker.sendEvent(FireBaseEvent.UNLOCK_KID_OPEN)
    }

    private fun initView() {

        //random pass unlock kid
        imgPass[0] = binding.num1
        imgPass[1] = binding.num2
        imgPass[2] = binding.num3
        imgPass[3] = binding.num4
        imgPass[4] = binding.num5
        imgPass[5] = binding.num6
        imgPass[6] = binding.num7
        imgPass[7] = binding.num8
        imgPass[8] = binding.num9

        count = 0
        //rand pass
        generatePassword()
    }

    private fun generatePassword() {
        mPass1 = randomKeyPass
        mPass2 = randomKeyPass
        mPass3 = randomKeyPass

        populateKeys()
    }

    private fun populateKeys() {
        val language = Locale.getDefault().language
        var firstInput: String
        var secondInput: String
        var thirdInput: String
        firstInput = NumberToWord.convert(language, mPass1.toLong()) + ", "
        secondInput = NumberToWord.convert(language, mPass2.toLong()) + ", "
        thirdInput = NumberToWord.convert(language, mPass3.toLong())
        if (count == 1) {
            firstInput = "<font color=\"#F2992E\">" + NumberToWord.convert(language, mPass1.toLong()) + ",</font> "
        } else if (count == 2) {
            firstInput = "<font color=\"#F2992E\">" + NumberToWord.convert(language, mPass1.toLong()) + ",</font> "
            secondInput = "<font color=\"#F2992E\">" + NumberToWord.convert(language, mPass2.toLong()) + ",</font> "
        } else if (count >= 3) {
            firstInput = "<font color=\"#F2992E\">" + NumberToWord.convert(language, mPass1.toLong()) + ",</font> "
            secondInput = "<font color=\"#F2992E\">" + NumberToWord.convert(language, mPass2.toLong()) + ",</font> "
            thirdInput = "<font color=\"#F2992E\">" + NumberToWord.convert(language, mPass3.toLong()) + "</font>"
        }
        val content = firstInput + secondInput + thirdInput
        binding.tvLockKey.text = Html.fromHtml(content)
    }

    fun populateClick() {
        binding.ivBack.setOnClickListener(this)
        binding.num1.setOnClickListener(this)
        binding.num2.setOnClickListener(this)
        binding.num3.setOnClickListener(this)
        binding.num4.setOnClickListener(this)
        binding.num5.setOnClickListener(this)
        binding.num6.setOnClickListener(this)
        binding.num7.setOnClickListener(this)
        binding.num8.setOnClickListener(this)
        binding.num9.setOnClickListener(this)
        binding.btnUnlock.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivBack -> finish()

            R.id.num1 -> setInputPass(1)

            R.id.num2 -> setInputPass(2)

            R.id.num3 -> setInputPass(3)

            R.id.num4 -> setInputPass(4)

            R.id.num5 -> setInputPass(5)

            R.id.num6 -> setInputPass(6)

            R.id.num7 -> setInputPass(7)

            R.id.num8 -> setInputPass(8)

            R.id.num9 -> setInputPass(9)

            R.id.btn_unlock -> {
                //change screen
                val intent = Intent(this, StoreActivity::class.java)
                startActivity(intent)
                finish()

                FireBaseTracker.sendEvent(FireBaseEvent.UNLOCK_KID_CLICK_UNLOCK)
            }
        }
    }

    private fun setInputPass(pass: Int) {
        if (count == 0 && mPass1 == pass) {
            count += 1
            binding.btnUnlock.setImageResource(R.drawable.btn_lock)
        } else if (count == 1 && mPass2 == pass) {
            count += 1
            binding.btnUnlock.setImageResource(R.drawable.btn_lock)
        } else if (count == 2 && mPass3 == pass) {
            count += 1
            binding.btnUnlock.setImageResource(R.drawable.bg_unlock)
            FireBaseTracker.sendEvent(FireBaseEvent.UNLOCK_KID_SUCCESS)
        }
        populateKeys()
    }

    private fun getRandomDoubleBetweenRange(min: Int, max: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }
}
