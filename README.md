# Save The Ocean

Desktop version of **LibGDX** powered mini-game (or better proof-of-concept project).
The aim of the game is to collect and separate the waste floating in the ocean.

![](gamepreview.gif)

#### About LibGDX

**LibGDX** is cross-platform Java game development framework based on OpenGL (ES). It's really nice to work with because you don't have to write
platform-specific code.  
This game is also using Scene2D (not mandatory to use with LibGDX) which is library that can make development even easier.

#### Adding an AdHandler

LibGDX launcher provides default AndroidLauncher when generating project. The following code shows implementation of management of advertising banners (for Android project).

```java
public class AndroidLauncher extends AndroidApplication implements AdHandler {
    protected AdView adView;
    private final int SHOW_TOP_BANNER = 1;
    private final int HIDE_TOP_BANNER = 0;
    private final String AD_UNIT_ID = ""; // unique identifier for an ad unit from AdMob application
    private final String TEST_DEVICE_ID = ""; // also can be found in AdMob application
    
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch(message.what) {
                case SHOW_TOP_BANNER: adView.setVisibility(View.VISIBLE); break;
                case HIDE_TOP_BANNER: adView.setVisibility(View.GONE); break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout layout = new RelativeLayout(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true; // software buttons hidden after game launch
        View gameView = initializeForView(new MyGdxGame(this), config);
        layout.addView(gameView);
        
        adView = new AdView(this);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("AndroidLauncher", "Ad loaded successfully.");
            }
        });
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        
        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice(TEST_DEVICE_ID);
        
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        
        layout.addView(adView, adParams);
        adView.loadAd(builder.build());
        setContentView(layout);
    }
    
    @Override
    public void showAds(boolean show) {
        handler.sendEmptyMessage(show ? SHOW_TOP_AD : HIDE_TOP_AD);
    }
}
```

 **Note:** Don't forget to create additional constructor in `MyGdxGame` class with `AdHandler` as a parameter (also manage visibility in `MyGdxGame`).  
 **Note:** `addTestDevice` method is deprecated now, use `new RequestConfiguration.Builder() .setTestDeviceIds(testDevices).build();`


