package com.bitshifter.wildfire;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class FamilyActivity extends Activity {

    private static final String PREFERENCE = "preference";
    private LinearLayout rowContainer;
    private static final int SCALE_DELAY = 120;
    private static EditText etName, etLocation;
    static private Button webViewButton;
    static Context context;
    public static final String EXTRA_HTML = "com.bitshifter.wildfire.familyActivity.EXTRA_HTML";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        rowContainer = (LinearLayout) findViewById(R.id.ll_container);
        //
        etLocation = (EditText) findViewById(R.id.etLocation);
        etName = (EditText) findViewById(R.id.etName);
        context = this;
        webViewButton =(Button) findViewById(R.id.bWebView);
        manageTransition();
    }

    private void manageTransition() {

        Slide slideExitTransition = new Slide(Gravity.BOTTOM);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                getWindow().getEnterTransition().removeListener(this);

                for (int i = 0; i < rowContainer.getChildCount(); i++) {
                    View rowView = rowContainer.getChildAt(i);
                    rowView.animate().setStartDelay(i * SCALE_DELAY).scaleX(1).scaleY(1);
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });

    }

    @Override
    public void onBackPressed() {


        for(int i=0; i< rowContainer.getChildCount(); i++) {
            View rowView = rowContainer.getChildAt(i);
            rowView.animate()
                    .setStartDelay(i*SCALE_DELAY)
                    .scaleX(0).scaleY(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {}

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            finishAfterTransition();
                        }
                        @Override
                        public void onAnimationCancel(Animator animation) {}
                        @Override
                        public void onAnimationRepeat(Animator animation) {}
                    });
        }
    }

    public void onFabClick(View v){
        String countryName = etLocation.getText().toString();
        String name = etName.getText().toString();
        MyDBHandler db = new MyDBHandler(this,null,null,1);
        int countryCode = FetchCountryData.getCountryCodeFromCountryName(db.getWritableDatabase(),countryName);
        Country country = FetchCountryData.getCountryByCountryCode(db.getWritableDatabase(),Integer.toString(countryCode));
        //TODO message victim
        //TODO message own contact
        RetrievePresentDistressState retrievePresentDistressState = new RetrievePresentDistressState();
        retrievePresentDistressState.currentScenario(countryCode, 1);


    }

    public static void callWebView(String html) {
        final String passHtml = html;

        webViewButton.setVisibility(View.VISIBLE);
        webViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);

                SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE, MODE_PRIVATE).edit();
                editor.putString("html",passHtml);
                editor.commit();

                context.startActivity(intent);
            }
        });

    }

}