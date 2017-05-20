package com.udacity.stockhawk.ui;

import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.view.LineChartView;
import com.udacity.stockhawk.R;


public class LinearGraph {

	private final LineChartView mChart;
	private final Activity mActivity;

	private String[] mLabels;
	private float[] mValues;
    private int mMaxValue, mMinValue, mSelected, mValuesPerWindow;

	private Tooltip mTip;

	public LinearGraph(Activity activity) {

		mActivity = activity;
		mChart = (LineChartView) activity.findViewById(R.id.chart1);
	}

	public void show(String[] labels, float[] values) {
        mLabels = labels;
        mValues = values;

        calculateGraphParameters();
        configureGraph();
        configureTooltip();
        initializeAndShowGraph();
	}

    private void calculateGraphParameters() {
        mSelected = mValues.length-1;
        mValuesPerWindow = 10;

        int minValue = Integer.MAX_VALUE, maxValue = Integer.MIN_VALUE;

        for (float value : mValues) {
            if(value > maxValue)
                maxValue = Math.round(value);
            if(value < minValue)
                minValue = Math.round(value);
        }

        int difference = maxValue - minValue;
        difference /= 10;

        // Adjust min and max so they are not exactly the borders of the graph
        mMaxValue = maxValue + difference;
        mMinValue = minValue - difference;
    }

    private void configureGraph() {
        // Get the metrics to calculate the Graph size
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int deviceWidth = displaymetrics.widthPixels;
        int itemsBasedWidth = (mValues.length * deviceWidth / mValuesPerWindow);

        // If the items have enough space, return
        if (deviceWidth >= itemsBasedWidth)
            return;

        // If the items need more spacing, dynamically increase the graph size
        mChart.setLayoutParams(new LinearLayout.LayoutParams(itemsBasedWidth, mChart.getLayoutParams().height));

        // Scroll the view to the end
        final HorizontalScrollView hsv = (HorizontalScrollView) mActivity.findViewById(R.id.sv_chart1);
        hsv.setHorizontalScrollBarEnabled(false);
        hsv.postDelayed(new Runnable() {
            public void run() {
                hsv.setSmoothScrollingEnabled(false);
                hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 50L);
    }

    private void configureTooltip() {
        float widthPx = Tools.fromDpToPx(65), heightPx = Tools.fromDpToPx(25);

        mTip = new Tooltip(mActivity, R.layout.linechart_three_tooltip, R.id.value);

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) widthPx, (int) heightPx);

        mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);
        mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

        mTip.setPivotX(widthPx / 2);
        mTip.setPivotY(heightPx);
    }

    private void initializeAndShowGraph() {
        mChart.setTooltips(mTip);

        // Data
        mChart.addData(getFixedValues());
        mChart.addData(getDynamicValues());

        // Chart
        mChart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(mMinValue, mMaxValue)
                .setYLabels(AxisRenderer.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#6a84c3"))
                .setXAxis(false)
                .setYAxis(false);

        Runnable chartAction = new Runnable() {
            @Override
            public void run() {
                mTip.prepare(mChart.getEntriesArea(0).get(mSelected), mValues[mSelected]);
                mChart.showTooltip(mTip, true);
            }
        };

        Animation anim = new Animation().setEasing(new BounceInterpolator()).setEndAction(chartAction);

        mChart.show(anim);
    }

    private LineSet getFixedValues() {
        return new LineSet(mLabels, mValues)
                .setColor(ContextCompat.getColor(mActivity, R.color.graph_fixed_line))
                .setFill(ContextCompat.getColor(mActivity, R.color.graph_fill))
                .setDotsColor(ContextCompat.getColor(mActivity, R.color.graph_fixed_dot))
                .setThickness(4)
                .endAt(mValues.length-1);
    }

    private LineSet getDynamicValues() {
        return new LineSet(mLabels, mValues)
                .setColor(ContextCompat.getColor(mActivity, R.color.graph_dynamic_dot_line))
                .setFill(ContextCompat.getColor(mActivity, R.color.graph_fill))
                .setDotsColor(ContextCompat.getColor(mActivity, R.color.graph_dynamic_dot_line))
                .setThickness(4)
                .setDashed(new float[] {10f, 10f})
                .beginAt(mValues.length-2);
    }
}