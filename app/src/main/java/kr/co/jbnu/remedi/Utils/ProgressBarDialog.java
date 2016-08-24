package kr.co.jbnu.remedi.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.wang.avi.AVLoadingIndicatorView;

import kr.co.jbnu.remedi.R;

/**
 * Created by seokhyeon on 2016-08-22.
 */
public class ProgressBarDialog extends Dialog {

    private AVLoadingIndicatorView avi;

    public ProgressBarDialog(Context context){
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progressbar);

        avi = (AVLoadingIndicatorView)findViewById(R.id.progressbar);
        avi.smoothToShow();
    }


}
