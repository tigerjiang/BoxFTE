package com.media.box.fte;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;


/**
 * BannerCarousel is a widget which represents a stack of {@link Thumbnail}s.
 * Maximum number of items is restricted by value returned by
 * {@link getMaxItemsNumber}. <div align="center"><img
 * src="doc-files/banner_carousel.png" width="150px"> </div>
 *
 * @version 1.7
 * @since 1.7
 * @author brladurn
 */
public class BannerCarousel extends FrameLayout  {

    public enum FlipRate {
        VIEWING,
        READING,
    }

    // Will be read from XML
    private static final int FLIP_RATES[] = {
        0,
        0
    };

    private Flipper mFlipper;
    private Indicator mIndicator;
    private int mMaxItemsNumber;
    private OnItemSelectListener mListener;

    /**
     * Constructor
     *
     * @param context
     * @see android.view.View
     */
    public BannerCarousel(Context context) {
        this(context, null);
    }

    /**
     * Constructor
     *
     * @param context
     * @see android.view.View
     */
    public BannerCarousel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor
     *
     * @param context
     * @see android.view.View
     */
    public BannerCarousel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Context ctx = getContext();
        mMaxItemsNumber = ctx.getResources().getInteger(R.integer.banner_max_items);

        LayoutInflater.from(ctx).inflate(R.layout.banner_carousel, this, true);
        mFlipper = (Flipper) findViewById(R.id.flipper);
        mIndicator = (Indicator) findViewById(R.id.indicator);

        FLIP_RATES[FlipRate.VIEWING.ordinal()] = ctx.getResources().getInteger(R.integer.banner_show_duration_view);
        FLIP_RATES[FlipRate.READING.ordinal()] = ctx.getResources().getInteger(R.integer.banner_show_duration_read);

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSelectItem();
            }
        });

        setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    return handleSelectItem();
                }
                return false;
            }
        });

        setFocusableInTouchMode(true);
        setFocusable(true);
    }

    /**
     * @return Maximum items number which can be added in a banner carousel.
     */
    public int getMaxItemsNumber() {
        return mMaxItemsNumber;
    }

    /**
     * Initializes a list of thumbnails to be shown in a banner carousel.
     *
     * @param List of {@link Thumbnail}
     */
    public void setCarouselItems(List<ImageView> thumbs) {
        int thumbsLength = thumbs.size();
        if (thumbsLength > mMaxItemsNumber) {
            throw new IllegalArgumentException(
                    "Carousel can't contain more than " + mMaxItemsNumber + " items");
        }

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mFlipper.removeAllViews();
        for (final ImageView thumb : thumbs) {
            mFlipper.addView(thumb, lp);
        }

        if (thumbsLength > 1) {
            mIndicator.setDotsNumber(thumbsLength);
            mIndicator.setVisibility(View.VISIBLE);
            mFlipper.setIndicator(mIndicator);
        } else {
            mIndicator.setVisibility(View.GONE);
            mFlipper.setIndicator(null);
        }
    }

    /**
     * How long to wait before flipping to the next view
     * @param rate predefined flip rate
     */
    public void setFlipInterval(FlipRate rate) {
        mFlipper.setFlipInterval(FLIP_RATES[rate.ordinal()]);
    }

    /**
     * Listener for getting notification that an item has been selected.
     */
    public interface OnItemSelectListener {
        void onSelectItem(ImageView thumb, int position);
    }

    /**
     * Register a callback to be invoked when current {@link Thumbnail} is
     * selected.
     *
     * @param listener
     * @see OnItemSelectListener
     */
    public void setOnItemSelectListener(BannerCarousel.OnItemSelectListener listener) {
        mListener = listener;
    }

    private static class Indicator extends LinearLayout {
        private int mDotNumber;
        private int mDotGap;
        private int mDotMargin;
        private int mCurrentDot;

        public Indicator(Context context) {
            super(context);
            init(context);
        }

        public Indicator(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public Indicator(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init(context);
        }

        private void init(Context ctx) {
            Resources res = ctx.getResources();
            mDotGap = res.getDimensionPixelOffset(R.dimen.banner_indicator_dot_gap);
            mDotMargin = res.getDimensionPixelOffset(R.dimen.banner_indicator_dot_margin);
        }

        void setDotsNumber(int num) {
            mDotNumber = num;
            mCurrentDot = 0;

            Context ctx = getContext();
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < mDotNumber; i++) {
                ImageView img = new ImageView(ctx);
                img.setAdjustViewBounds(true);
                img.setImageResource(R.drawable.banner_indicator_dot);
                if (i == 0) {
                    img.setPadding(mDotMargin, 0, mDotGap, 0);
                }
                else if ((i + 1) == mDotNumber) {
                    img.setPadding(mDotGap, 0, mDotMargin, 0);
                }
                else {
                    img.setPadding(mDotGap, 0, mDotGap, 0);
                }
                addView(img, lp);
            }
            enableDot(mCurrentDot);
        }

        void moveNext() {
            disableDot(mCurrentDot);

            int nextDot = mCurrentDot + 1;
            if (nextDot >= mDotNumber) {
                nextDot = 0;
            }

            enableDot(nextDot);
            mCurrentDot = nextDot;
        }

        private void enableDot(int index) {
            ImageView dotView = (ImageView) getChildAt(index);
            dotView.setActivated(true);
        }

        private void disableDot(int index) {
            ImageView dotView = (ImageView) getChildAt(index);
            dotView.setActivated(false);
        }
    }

    private static class Flipper extends ViewFlipper {
        private Indicator mIndicator;

        public Flipper(Context context) {
            super(context);
            init(context);
        }

        public Flipper(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context ctx) {
        }

        void setIndicator(Indicator ind) {
            mIndicator = ind;
        }

        @Override
        public void showNext() {
            if (mIndicator != null) {
                mIndicator.moveNext();
            }
            super.showNext();
        }
    }

    private boolean handleSelectItem() {
        if (mListener != null) {
            mListener.onSelectItem((ImageView) mFlipper.getCurrentView(),
                    mFlipper.getDisplayedChild());
            return true;
        }
        return false;
    }


}
