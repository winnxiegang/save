package com.android.banana.groupchat.ilistener;

import android.view.View;

/**
 * Created by zaozao on 2017/6/20.
 */

public interface onSelectClickListener {
    void  onSelectClick(View v, int position, boolean delete);

    void deleteSuccess(int position);
}
