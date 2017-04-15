package com.ts.tsadmobtest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ts.tsadmobtest.network.AsyncHttpIMGRequest;
import com.ts.tsadmobtest.network.AsyncHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LockScreenActivity extends Activity {
    private static final int SPOON_UP_EXPORSE   =   1;
    private static final int SPOON_UP_LINK      =   2;

    private static final String URL_SPOON_UP    =   "/api/v1/spoon/rice/add/{accountId}/{rice}";
    private static final String URL_SPOON_GET   =   "/api/v1/spoon/{accountId}";

    private static final String ADMOB_AD_UNIT_ID    = "ca-app-pub-3940256099942544/2247696110";
    private static final String ADMOB_APP_ID        = "ca-app-pub-3940256099942544~3347511713";

    private TextView poemTextView;

    private ActionBarDrawerToggle mDrawerToggle;
    private Button mRefresh;

    private Button installBut;
    private TextView adLinkText;

    private TextView dayText;
    private TextView hourText;

    private SeekBar adUnlockSeekbar;
    private CheckBox mRequestAppInstallAds;
    private CheckBox mRequestContentAds;

    TextView sideRiceProgTxView;
    ImageView donationIMGView;
    ProgressBar sideRiceProgress;
    TextView sideContentsTextView;
    TextView sideTitleTextView;

    Thread tTask;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        poemTextView    =   (TextView) findViewById(R.id.poemTextView);

        String poem =   "해당화\n\n\n\n 이육사 \n\n\n 당신은 봄이 오기 전에 오신다고 하였습니다. \n 봄은 이미 지났습니다.";
        SpannableStringBuilder poemStrBuilder = new SpannableStringBuilder(poem);

        poemStrBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        poemTextView.setText(poemStrBuilder);
//        setContentView(R.layout.ad_express);

//        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);
////        adView.setAdSize(AdSize.SMART_BANNER);
//        adView.loadAd(new AdRequest.Builder().build());

//        frameLayout.removeAllViews();


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawLayout = (DrawerLayout) inflater.inflate(R.layout.lock_screen_control, null);
        RelativeLayout.LayoutParams paramRelative = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        addContentView(drawLayout, paramRelative);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dayText     =   (TextView) findViewById(R.id.clock_date);
        hourText    =   (TextView) findViewById(R.id.clock_hour);

        tTask = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    Date rightNow = new Date();

                    SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm a");
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM.dd(E)");

                    final String dateString = dateFormatter.format(rightNow);
                    final String hourString = hourFormatter.format(rightNow);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            dayText.setText(dateString);
//                            hourText.setText(hourString);
                        }
                    });

                    try {

                        Thread.sleep(2000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        tTask.start();


        mDrawerToggle = new ActionBarDrawerToggle(this, drawLayout,
                null, R.string.drawer_open, R.string.drawer_close) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                setSlideviewData();
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawLayout.addDrawerListener(mDrawerToggle);

        ListView listView = (ListView) findViewById(R.id.left_drawer);

        // 아이템을 추가
        ArrayList<String> items = new ArrayList<>();
        items.add("item1");
        items.add("item2");
        items.add("item3");
        items.add("item4");
        items.add("item5");

        CustomAdapter adapter = new CustomAdapter(this, 0, items);
        listView.setAdapter(adapter);

        // 아이템을 [클릭]시의 이벤트 리스너를 등록
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ListView listView = (ListView) parent;
//                // TODO 아이템 클릭시에 구현할 내용은 여기에.
//                String item = (String) listView.getItemAtPosition(position);
//                Toast.makeText(LockScreenActivity.this, item, Toast.LENGTH_LONG).show();
//            }
//        });

        // 아이템을 [선택]시의 이벤트 리스너를 등록
//        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                ListView listView = (ListView) parent;
//                // TODO 아이템 선택시에 구현할 내용은 여기에.
//                String item = (String) listView.getSelectedItem();
//                Toast.makeText(LockScreenActivity.this, item, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, ADMOB_APP_ID);

        adUnlockSeekbar = (SeekBar) findViewById(R.id.ad_unlock_seekbar);
        adUnlockSeekbar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
        adUnlockSeekbar.setOnTouchListener(new MyOnTouchListener());


        refreshAd(true, false);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                Toast.makeText(this, "Can't Back Action.", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    /**
     * Populates a {@link NativeAppInstallAdView} object with data from a given
     * {@link NativeAppInstallAd}.
     *
     * @param nativeAppInstallAd the object containing the ad's assets
     * @param adView             the view to be populated
     */
    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                          NativeAppInstallAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setImageView(adView.findViewById(R.id.appinstall_image));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
        ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon()
                .getDrawable());

        installBut = (Button) adView.getCallToActionView();

        List<NativeAd.Image> images = nativeAppInstallAd.getImages();

        String imageInfo    =   "";
        if (images.size() > 0)
        {
            NativeAd.Image adImage  =   images.get(0);
            ImageView adImageView   =   (ImageView) adView.getImageView();
            adImageView.setImageDrawable(adImage.getDrawable());

            int imgWidth       =   adImage.getDrawable().getIntrinsicWidth();
            int imgHeight      =   adImage.getDrawable().getIntrinsicHeight();

            adImageView.setScaleType(ImageView.ScaleType.FIT_END);

            if(imgHeight > imgWidth)
            {
                adImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                setVerticalAdImageArea(adImageView);
            }
//            adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            adImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


            for(NativeAd.Image img: images){
                imageInfo   =   imageInfo+img.getDrawable().getIntrinsicWidth()+"/";
                imageInfo   =   imageInfo+img.getDrawable().getIntrinsicHeight();
            }
        }

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());

        dayText.setText(width+"/"+height);
        hourText.setText(imageInfo);

        // Some aren't guaranteed, however, and should be checked.
        if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText("◁ "+nativeAppInstallAd.getPrice());
        }

        if (nativeAppInstallAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        }

        if (nativeAppInstallAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

    /**
     * Populates a {@link NativeContentAdView} object with data from a given
     * {@link NativeContentAd}.
     *
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       final NativeContentAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText("◁ "+nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        adLinkText = (TextView) adView.getCallToActionView();
        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ImageView adImageView   =   (ImageView) adView.getImageView();
            adImageView.setImageDrawable(images.get(0).getDrawable());

            adImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     * @param requestAppInstallAds indicates whether app install ads should be requested
     * @param requestContentAds    indicates whether content ads should be requested
     */
    private void refreshAd(boolean requestAppInstallAds, boolean requestContentAds) {
        if (!requestAppInstallAds && !requestContentAds) {
            Toast.makeText(this, "At least one ad format must be checked to request an ad.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);

        if (requestAppInstallAds) {
            builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                @Override
                public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                    FrameLayout frameLayout =
                            (FrameLayout) findViewById(R.id.fl_adplaceholder);
                    NativeAppInstallAdView adView = (NativeAppInstallAdView) getLayoutInflater()
                            .inflate(R.layout.ad_app_install, null);
                    populateAppInstallAdView(ad, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            });
        }

        if (requestContentAds) {
            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                @Override
                public void onContentAdLoaded(NativeContentAd ad) {
                    FrameLayout frameLayout =
                            (FrameLayout) findViewById(R.id.fl_adplaceholder);
                    NativeContentAdView adView = (NativeContentAdView) getLayoutInflater()
                            .inflate(R.layout.ad_content, null);
                    populateContentAdView(ad, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            });
        }

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(LockScreenActivity.this, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void adAction() {
//        adLinkText.performClick();
        installBut.performClick();
    }

    private void increaseExposeCnt() {
        boolean netState   =   checkNetwork();

        if(netState)
        {
            int accountId   =   2;

            String requestURL   =   URL_SPOON_UP.replace("{accountId}",""+accountId);

            requestURL          =   requestURL.replace("{rice}",""+SPOON_UP_EXPORSE);

            new AsyncHttpRequest(requestURL).execute();
        }
    }

    private void increaseLinkCnt() {
        boolean netState   =   checkNetwork();

        if(netState)
        {
            int accountId   =   2;

            String requestURL   =   URL_SPOON_UP.replace("{accountId}",""+accountId);

            requestURL          =   requestURL.replace("{rice}",""+SPOON_UP_LINK);


            new AsyncHttpRequest(requestURL).execute();
        }
    }

    private boolean checkNetwork() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // 네트워크가 연결되어 있을 때 -> HttpURLConnection
        if (networkInfo != null &&  networkInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    private void distoryAdview() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
        frameLayout.removeAllViews();
        tTask.interrupt();
        this.finish();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("LockScreen Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private void setVerticalAdImageArea(ImageView adImageView) {
//        adImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1500));
        adImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void setSlideviewData() {
        //Step 1. Spoon list 요청(현재는 한개의 Spoon만 리스트)
        //Step 2. Spoon data View 설정

        int accountID   =   2;
        JSONArray jsonArr =   null;
        JSONObject jsonSpoon = null;
        JSONObject jsonBowl = null;


        try
        {
            String requestParam =   URL_SPOON_GET.replace("{accountId}",""+accountID);

            String result       =   new AsyncHttpRequest(requestParam).execute().get();


            jsonArr     =   new JSONArray(result);

            jsonSpoon   =   jsonArr.getJSONObject(0);
            jsonBowl    =   jsonSpoon.getJSONObject("bowl");

            String donIMGPath    =   jsonBowl.getString("imgPath");

            int riceTol     =   jsonBowl.getInt("riceTol");
            int riceAim     =   jsonBowl.getInt("riceAim");


            sideTitleTextView.setText(jsonBowl.getString("title"));


            donationIMGView.setImageBitmap(new AsyncHttpIMGRequest(donIMGPath).execute().get());

            sideRiceProgTxView.setText(riceTol+"/"+riceAim+"(원)");

            sideRiceProgress.setMax(riceAim);
            sideRiceProgress.setProgress(riceTol);

            sideContentsTextView.setText(jsonBowl.getString("contents"));

        }
        catch (InterruptedException e) {e.printStackTrace();}
        catch (ExecutionException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}



    }


    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View sideLayout = vi.inflate(R.layout.ad_side_view, null);


                if ("item1".equals(items.get(position))) {
                    sideTitleTextView = (TextView) sideLayout.findViewById(R.id.side_total_title);
                    v = sideTitleTextView;

                } else if ("item2".equals(items.get(position))) {
                    donationIMGView = (ImageView) sideLayout.findViewById(R.id.side_img);
                    v = donationIMGView;

                } else if ("item3".equals(items.get(position))) {
                    sideRiceProgTxView = (TextView) sideLayout.findViewById(R.id.side_total_text);
                    v = sideRiceProgTxView;

                } else if ("item4".equals(items.get(position))) {
                    sideRiceProgress = (ProgressBar) sideLayout.findViewById(R.id.side_total_progress);

                    v = sideRiceProgress;
                } else if ("item5".equals(items.get(position))) {
                    sideContentsTextView = (TextView) sideLayout.findViewById(R.id.side_donation_detail);
                    v = sideContentsTextView;

                    sideContentsTextView.setMovementMethod(new ScrollingMovementMethod());
                }
            }

            // ImageView 인스턴스


//            TextView textView = (TextView)v.findViewById(R.id.textView);
//            textView.setText(items.get(position));
//
//            final String text = items.get(position);
//            Button button = (Button)v.findViewById(R.id.button);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(LockScreenActivity.this, text, Toast.LENGTH_SHORT).show();
//                }
//            });

            return v;
        }
    }

    class MyOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 터치 했다가 손을 떼면 progress 를 다시 50 으로 초기화
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                int position    =   adUnlockSeekbar.getProgress();

                if (10 <= position || position <= 90)
                {
                    adUnlockSeekbar.setProgress(50);
                }

                adUnlockSeekbar.setBackgroundResource(R.drawable.unlock_bg);
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                adUnlockSeekbar.setBackgroundResource(R.drawable.unlock_bg_touch);
            }

            return false;
        }
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            if (progress >= 90)
            {
                seekBar.setProgress(100);
            }else if(seekBar.getProgress() <= 10)
            {
                seekBar.setProgress(0);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 80 % 이상 간격에 들어오면 잠금을 해제한다. // 여기서 80 이라는 상수는 잠금해제 버튼의 크기에 따라 변경이 필요하다

            int position = seekBar.getProgress();


            if (seekBar.getProgress() >= 90)
            {
                try {
                    increaseExposeCnt();
                }catch (Exception e){e.printStackTrace();}
                finally {   distoryAdview();   }
            } else if (seekBar.getProgress() <= 10)
            {
                try {
                    adAction();
                    increaseLinkCnt();
                }catch (Exception e){e.printStackTrace();}
                finally {distoryAdview();}
            } else {
                seekBar.setProgress(50);
            }
        }
    }
}
