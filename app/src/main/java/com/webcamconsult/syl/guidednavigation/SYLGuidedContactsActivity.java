package com.webcamconsult.syl.guidednavigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.providers.SYLSaveValues;

/**
 * Created by Sandeep on 8/31/2015.
 */
public class SYLGuidedContactsActivity extends Activity {
    ImageView mSkipGuidedImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.guidedcontacts_layout);
        SYLSaveValues.setSylHideguidednavigationContacts("true", SYLGuidedContactsActivity.this);
        mSkipGuidedImageView=(ImageView)findViewById(R.id.img_skip);
        mSkipGuidedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
