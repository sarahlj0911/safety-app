package com.plusmobileapps.safetyapp.welcome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.data.dao.UserDao;
import com.plusmobileapps.safetyapp.data.entity.User;
import com.plusmobileapps.safetyapp.main.MainActivity;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.signup.SignupActivity;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager welcomeViewPager;
    private WelcomeViewPagerAdapter welcomeViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnBack, btnNext;
    private PrefManager prefManager;
    private User curUser;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (prefManager.isTutorialSeen()) {
            if (prefManager.isUserSignedUp()) {
                curUser = userDao.getUser();
                launchMainScreen();
                curUser.setLastLogin(1);
            } else {
                launchSignupScreen();
            }
        }

        setContentView(R.layout.activity_welcome);

        welcomeViewPager = findViewById(R.id.welcome_view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnBack = findViewById(R.id.welcome_btn_back);
        btnNext = findViewById(R.id.welcome_btn_next);

        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4,
                R.layout.welcome_slide5
        };

        // adding bottom dots
        addBottomDots(0);

        welcomeViewPagerAdapter = new WelcomeViewPagerAdapter();
        welcomeViewPager.setAdapter(welcomeViewPagerAdapter);
        welcomeViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnBack.setVisibility(View.INVISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(-1);
                if (current > -1) {
                    welcomeViewPager.setCurrentItem(current);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    welcomeViewPager.setCurrentItem(current);
                } else {
                    prefManager.setIsTutorialSeen(true);
                    //launchHomeScreen();
                    launchSignupScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[0]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[0]);
    }

    private int getItem(int i) {
        return welcomeViewPager.getCurrentItem() + i;
    }

    private void launchSignupScreen() {
        startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
        finish();
    }

    private void launchMainScreen() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // Don't show BACK button on first page
            if (position == 0) {
                btnBack.setVisibility(View.GONE);
            } else if (position == layouts.length - 1) {
                // On last page, change NEXT to GOT IT
                btnNext.setText(getString(R.string.got_it));
            } else {
                // Neither first nor last page, so show BACK and NEXT
                btnNext.setText(getString(R.string.next));
                btnBack.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * View pager adapter
     */
    public class WelcomeViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public WelcomeViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            if (position == 2) {
                TextView welcome2textView4 = findViewById(R.id.welcome3_textView4);
                String textToSpan = getResources().getText(R.string.welcome3_item4).toString();
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textToSpan);
                Bitmap clearIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_clear_black_trimmed_24dp);
                spannableStringBuilder.setSpan(new ImageSpan(container.getContext(), clearIcon), 71, 72, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                welcome2textView4.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
            }

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
