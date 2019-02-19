package com.pd.pager;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class PagerLayout extends FrameLayout {

    private Context context;
    private int num = 5;
    private int titleColor;
    private float titleSize;
    private Drawable menuIcon;

    private int[] ids = {R.id.view0, R.id.view1, R.id.view2, R.id.view3, R.id.view4};
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private ObjectAnimator[] animators;
    private ObjectAnimator[] chosenAnimator;
    private ObjectAnimator[] menuAnimator;
    private PagerAdapter adapter;
    private MenuObserver observer;
    private MenuChooser menuChoose = new MenuChooser();
    private MakeLayout.OnMenuClickListener menuListener = new MenuListener();

    private boolean open = false;
    private List<Object> objects = new ArrayList<>();
    private int chosen;

    public PagerLayout(Context context) {
        this(context, null);
    }

    public PagerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PagerLayout);
        num = array.getInteger(R.styleable.PagerLayout_num, 3);

        titleColor = array.getColor(R.styleable.PagerLayout_titleColor, getResources().getColor(android.R.color.primary_text_light));
        titleSize = array.getDimension(R.styleable.PagerLayout_titleSize, getResources().getDimension(R.dimen.cl_title_size));
        menuIcon = array.getDrawable(R.styleable.PagerLayout_titleIcon);
        array.recycle();
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setWillNotDraw(true);
            chosen = num - 1;
            for (int i = 0; i < num; i++) {
                MakeLayout frameLayout = new MakeLayout(context);
                frameLayout.setId(ids[i]);
                frameLayout.setTag(i);
                frameLayout.setOnClickListener(menuChoose);
                frameLayout.setOnMenuClickListener(menuListener);
                if(menuIcon != null) {
                    frameLayout.setMenuIcon(menuIcon);
                }
                frameLayout.setMenuTitleSize(titleSize);
                frameLayout.setMenuTitleColor(titleColor);
                if (i == num - 1) frameLayout.setMenuAlpha(1);
                LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
                frameLayout.setLayoutParams(layoutParams);
                addView(frameLayout);
            }
            animators = new ObjectAnimator[num];
            chosenAnimator = new ObjectAnimator[num];
            menuAnimator = new ObjectAnimator[num];
            initAnim();
        }
    }

    private void initAnim() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float transX = (float) ((i + 1) * 0.06);
            float transY = (float) ((i + 1) * 0.12);
            ObjectAnimator animator;

            @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("XFraction", 0, transX);
            @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("YFraction", 0, transY);
            animator = ObjectAnimator.ofPropertyValuesHolder(child, valuesHolderX, valuesHolderY);
            animator.setInterpolator(interpolator);
            animator.setDuration(300);
            animators[i] = animator;

            @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder valuesHolderXReverse = PropertyValuesHolder.ofFloat("XFraction", transX, 1);
            animator = ObjectAnimator.ofPropertyValuesHolder(child, valuesHolderXReverse);
            animator.setInterpolator(interpolator);
            animator.setDuration(300);
            chosenAnimator[i] = animator;

            @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder valuesHolderAlpha = PropertyValuesHolder.ofFloat("menuAlpha", 1, 0);
            animator = ObjectAnimator.ofPropertyValuesHolder(child, valuesHolderAlpha);
            animator.setInterpolator(interpolator);
            animator.setDuration(300);
            menuAnimator[i] = animator;
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(observer);
            this.adapter.startUpdate(this);
            for (int i = 0; i < num; i++) {
                this.adapter.destroyItem((ViewGroup) getChildAt(i), i, objects.get(i));
            }
            this.adapter.finishUpdate(this);
        }

        this.adapter = adapter;
        if (this.adapter == null) {
            return;
        }

        if (observer == null) {
            observer = new MenuObserver();
        }
        this.adapter.registerDataSetObserver(observer);

        int count = this.adapter.getCount();
        if (count != num) {
            throw new RuntimeException("Number of views should equal 'num' that's declared in xml");
        }

        for (int i = 0; i < count; i++) {
            Object object = this.adapter.instantiateItem((ViewGroup) getChildAt(i), i);
            objects.add(object);
        }
        this.adapter.finishUpdate(this);
    }

    @UiThread
    public void setTitles(@NonNull List<String> titles) {
        for (int i = 0; i < num; i++) {
            ((MakeLayout) getChildAt(i)).setTitle(titles.get(i));
        }
    }

    @UiThread
    public void setTitleByIndex(@NonNull String title, int index) {
        if (index > num -1) {
            throw new IndexOutOfBoundsException();
        }
        ((MakeLayout) getChildAt(index)).setTitle(title);
    }

    @UiThread
    public void setMenuIcon(@NonNull int resId) {
        for (int i = 0; i < num; i++) {
            ((MakeLayout) getChildAt(i)).setMenuIcon(resId);
        }
    }

    public void toggle() {
        if (open) {
            close();
        } else {
            open();
        }
    }

    private void open() {
        open = true;
        for (int i = 0; i < num; i++) {
            if (i == chosen) {
                menuAnimator[i].start();
                animators[i].start();
            } else if (i > chosen) {
                chosenAnimator[i].reverse();
            } else {
                animators[i].start();
            }
        }
        chosen = num - 1;
    }

    private void close() {
        open = false;
        for (ObjectAnimator mAnimator : animators) {
            mAnimator.reverse();
        }
    }

    private void dataSetChanged() {
        if (open) {
            close();
        }
        if (adapter != null) {
            adapter.startUpdate(this);
            for (int i = 0; i < num; i++) {
                adapter.destroyItem((ViewGroup) getChildAt(i), i, objects.get(i));
            }
            for (int i = 0; i < num; i++) {
                adapter.instantiateItem((ViewGroup) getChildAt(i), i);
            }
            adapter.finishUpdate(this);
        }
    }

    boolean isOpening() {
        return open;
    }

    private class MenuObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            dataSetChanged();
        }

        @Override
        public void onInvalidated() {
            dataSetChanged();
        }
    }

    private class MenuChooser implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (open) {
                chosen = (int) v.getTag();
                for (int i = 0; i < num; i++) {
                    if (i <= chosen) {
                        animators[i].reverse();
                    } else {
                        chosenAnimator[i].start();
                    }
                }
                menuAnimator[chosen].reverse();
            }
            open = false;
        }
    }

    private class MenuListener implements MakeLayout.OnMenuClickListener {

        @Override
        public void onMenuClick() {
            toggle();
        }
    }

}