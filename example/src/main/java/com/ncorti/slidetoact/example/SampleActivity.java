package com.ncorti.slidetoact.example;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ncorti.slidetoact.SlideToActView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SampleActivity extends AppCompatActivity {

    public static final String EXTRA_PRESSED_BUTTON = "extra_pressed_button";
    private List<SlideToActView> mSlideList;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final int pressedButton = getIntent().getExtras().getInt(EXTRA_PRESSED_BUTTON, 0);
        dateFormat = new SimpleDateFormat("HH:mm:ss", getResources().getConfiguration().locale);

        if (pressedButton == R.id.button_area_margin) {
            setContentView(R.layout.content_area_margin);
        } else if (pressedButton == R.id.button_icon_margin) {
            setContentView(R.layout.content_icon_margin);
        } else if (pressedButton == R.id.button_colors) {
            setContentView(R.layout.content_color);
        } else if (pressedButton == R.id.button_border_radius) {
            setContentView(R.layout.content_border_radius);
        } else if (pressedButton == R.id.button_elevation) {
            setContentView(R.layout.content_elevation);
        } else if (pressedButton == R.id.button_text_size) {
            setContentView(R.layout.content_text_size);
        } else if (pressedButton == R.id.button_slider_dimension) {
            setContentView(R.layout.content_slider_dimensions);
        } else if (pressedButton == R.id.button_event_callbacks) {
            setContentView(R.layout.content_event_callbacks);
            setupEventCallbacks();
        } else if (pressedButton == R.id.button_locked_slider) {
            setContentView(R.layout.content_locked_slider);
        } else if (pressedButton == R.id.button_custom_icon) {
            setContentView(R.layout.content_custom_icon);
            final SlideToActView slider = findViewById(R.id.slide_custom_icon);
            View.OnClickListener listener = view -> {
                int viewId = view.getId();
                if (viewId == R.id.button_android_icon) {
                    slider.setSliderIcon(R.drawable.ic_android);
                } else if (viewId == R.id.button_cloud_icon) {
                    slider.setSliderIcon(R.drawable.ic_cloud);
                } else if (viewId == R.id.button_complete_icon) {
                    slider.setCompleteIcon(R.drawable.custom_complete_animated);
                }
            };
            findViewById(R.id.button_android_icon).setOnClickListener(listener);
            findViewById(R.id.button_cloud_icon).setOnClickListener(listener);
            findViewById(R.id.button_complete_icon).setOnClickListener(listener);
        } else if (pressedButton == R.id.button_reversed_slider) {
            setContentView(R.layout.content_reversed_slider);
        } else if (pressedButton == R.id.button_animation_duration) {
            setContentView(R.layout.content_animation_duration);
        } else if (pressedButton == R.id.button_bump_vibration) {
            setContentView(R.layout.content_bumb_vibration);
        } else if (pressedButton == R.id.button_completed) {
            setContentView(R.layout.content_completed);
        } else if (pressedButton == R.id.button_bounce) {
            setContentView(R.layout.content_bounce_animation);
        }
        mSlideList = getSlideList();
    }

    private List<SlideToActView> getSlideList() {
        final List<SlideToActView> slideList = new ArrayList<>();
        final LinearLayout container = findViewById(R.id.slide_container);
        for (int i = 0; i < container.getChildCount(); i++) {
            final View child = container.getChildAt(i);
            if (child instanceof SlideToActView) {
                slideList.add((SlideToActView) child);
            }
        }
        return slideList;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset) {
            for (SlideToActView slide : mSlideList) {
                slide.setCompleted(false, true);
            }
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupEventCallbacks() {
        final SlideToActView slide = findViewById(R.id.event_slider);
        final TextView log = findViewById(R.id.event_log);
        slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView view) {
                log.append("\n" + getTime() + " onSlideComplete");
            }
        });
        slide.setOnSlideResetListener(new SlideToActView.OnSlideResetListener() {
            @Override
            public void onSlideReset(@NonNull SlideToActView view) {
                log.append("\n" + getTime() + " onSlideReset");
            }
        });
        slide.setOnSlideUserFailedListener(new SlideToActView.OnSlideUserFailedListener() {
            @Override
            public void onSlideFailed(@NonNull SlideToActView view, boolean isOutside) {
                log.append("\n" + getTime() + " onSlideUserFailed - Clicked outside: " + isOutside);
            }
        });
        slide.setOnSlideToActAnimationEventListener(new SlideToActView.OnSlideToActAnimationEventListener() {
            @Override
            public void onSlideCompleteAnimationStarted(@NonNull SlideToActView view, float threshold) {
                log.append("\n" + getTime() + " onSlideCompleteAnimationStarted - " + threshold + "");
            }

            @Override
            public void onSlideCompleteAnimationEnded(@NonNull SlideToActView view) {
                log.append("\n" + getTime() + " onSlideCompleteAnimationEnded");
            }

            @Override
            public void onSlideResetAnimationStarted(@NonNull SlideToActView view) {
                log.append("\n" + getTime() + " onSlideResetAnimationStarted");
            }

            @Override
            public void onSlideResetAnimationEnded(@NonNull SlideToActView view) {
                log.append("\n" + getTime() + " onSlideResetAnimationEnded");
            }
        });
        slide.setOnSlideProgressListener(new SlideToActView.OnSlideProgressListener() {
            @Override
            public void onSlideUpdated(@NonNull SlideToActView view, float progress) {
                Log.d("this here ", "onSlideUpdated ---- " + progress);
            }
        });
    }

    private String getTime() {
        return dateFormat.format(new Date());
    }
}
