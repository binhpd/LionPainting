package com.hiccup.kidpainting.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hiccup.kidpainting.R;

class BasePanel extends FrameLayout {

    ViewGroup mPanelContainer;

    //Constructors

    BasePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPanelContainer();
    }

    /* ***************************************************************************** */
    /* ******************************** AppHelper API ******************************** */
    /* ***************************************************************************** */

    private void initPanelContainer() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mPanelContainer = (ViewGroup) inflater.inflate(R.layout.carousel_base_panel, this, true);
    }
}