package com.udacity.stockhawk.ui;

import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.BounceInterpolator;
import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.view.LineChartView;
import com.udacity.stockhawk.R;


public class LinearGraph extends CardController {

	private final LineChartView mChart;
	private final Context mContext;

	private final String[] mLabels = {"Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep"};
	private final float[] mValues = {900.5f, 920.4f, 890.3f, 960f, 975.2f, 990.2f, 950f, 930f, 950.9f};
    private int mMaxValue = 1000;
    private int mMinValue = 880;
    private int selected = mValues.length-1;

	private Tooltip mTip;

	private Runnable mBaseAction;

	public LinearGraph(Activity context) {
		super(context);

		mContext = context;
		mChart = (LineChartView) context.findViewById(R.id.chart1);
	}

	@Override
	public void show(Runnable action) {
		super.show(action);

		// Tooltip
		mTip = new Tooltip(mContext, R.layout.linechart_three_tooltip, R.id.value);

//		((TextView) mTip.findViewById(R.id.value)).setTypeface(
//                Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Semibold.ttf"));

		mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
		mTip.setDimensions((int) Tools.fromDpToPx(65), (int) Tools.fromDpToPx(25));

		mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
			    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
			    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

		mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
			    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
			    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

		mTip.setPivotX(Tools.fromDpToPx(65) / 2);
		mTip.setPivotY(Tools.fromDpToPx(25));

		mChart.setTooltips(mTip);

		// Data
		LineSet dataset = new LineSet(mLabels, mValues);
		dataset.setColor(Color.parseColor("#758cbb"))
				  .setFill(Color.parseColor("#2d374c"))
				  .setDotsColor(Color.parseColor("#758cbb"))
				  .setThickness(4)
				  .setDashed(new float[] {10f, 10f})
				  .beginAt(mValues.length-2);
		mChart.addData(dataset);

		dataset = new LineSet(mLabels, mValues);
		dataset.setColor(Color.parseColor("#b3b5bb"))
				  .setFill(Color.parseColor("#2d374c"))
				  .setDotsColor(Color.parseColor("#ffc755"))
				  .setThickness(4)
				  .endAt(mValues.length-1);
		mChart.addData(dataset);

		// Chart
		mChart.setBorderSpacing(Tools.fromDpToPx(15))
				  .setAxisBorderValues(mMinValue, mMaxValue)
				  .setYLabels(AxisRenderer.LabelPosition.NONE)
				  .setLabelsColor(Color.parseColor("#6a84c3"))
				  .setXAxis(false)
				  .setYAxis(false);

		mBaseAction = action;
		Runnable chartAction = new Runnable() {
			@Override
			public void run() {
				mBaseAction.run();
				mTip.prepare(mChart.getEntriesArea(0).get(selected), mValues[selected]);
				mChart.showTooltip(mTip, true);
			}
		};

		Animation anim = new Animation().setEasing(new BounceInterpolator()).setEndAction(chartAction);

		mChart.show(anim);
	}

	@Override
	public void update() {
		super.update();

		mChart.dismissAllTooltips();
		if (firstStage) {
			mChart.updateValues(0, mValues);
			mChart.updateValues(1, mValues);
		} else {
			mChart.updateValues(0, mValues);
			mChart.updateValues(1, mValues);
		}
		mChart.getChartAnimation().setEndAction(mBaseAction);
		mChart.notifyDataUpdate();
	}

	@Override
	public void dismiss(Runnable action) {

		super.dismiss(action);

		mChart.dismissAllTooltips();
		mChart.dismiss(new Animation().setEasing(new BounceInterpolator()).setEndAction(action));
	}
}