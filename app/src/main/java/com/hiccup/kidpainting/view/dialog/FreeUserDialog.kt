package com.hiccup.kidpainting.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.pref.AppPreference
import com.hiccup.kidpainting.view.KidTextView

class FreeUserDialog(val context: Context, val listener: DialogListener) {
    var dialog: Dialog = Dialog(context)
    val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_share_free_user, null)

    init {
        dialog.setContentView(view)
        view.findViewById<View>(R.id.ivClose).setOnClickListener{
            dialog.dismiss()
        }

//        if (AppPreference.getInstance().canShareFacebookForOpenCollection(context)) {
//            view.findViewById<View>(R.id.groupShare).visibility = View.GONE
//        }
        view.findViewById<View>(R.id.groupShare).setOnClickListener{
            listener.onClickShare()
            dialog.dismiss()
        }
        view.findViewById<View>(R.id.groupPurchase).setOnClickListener {
            listener.onClickGotoStore()
            dialog.dismiss()
        }
        dialog.setOnDismissListener { listener.onDismiss() }
    }

    fun showDialog() {
        dialog.show()
    }

    fun showDilog(shareFacebookTitle: String, buyPremiumContent: String) {
        view.findViewById<KidTextView>(R.id.tvTitleShareFacebook).text = shareFacebookTitle
        view.findViewById<KidTextView>(R.id.tvBuyTitle).text = buyPremiumContent
        dialog.show()
    }

    interface DialogListener {
        fun onClickShare()

        fun onClickGotoStore()

        fun onDismiss()
    }
}