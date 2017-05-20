package com.udacity.stockhawk.ui;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.udacity.stockhawk.R;

public class CardController {

	private final ImageButton mUpdateBtn;

	private final Runnable unlockAction = new Runnable() {
		@Override
		public void run() {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					unlock();
				}
			}, 500);
		}
	};

	protected boolean firstStage;

	private final Runnable showAction = new Runnable() {
		@Override
		public void run() {

			new Handler().postDelayed(new Runnable() {
				public void run() {

					show(unlockAction);
				}
			}, 500);
		}
	};


	protected CardController(Activity activity) {
		super();

		RelativeLayout toolbar = (RelativeLayout) activity.findViewById(R.id.chart_toolbar);
		mUpdateBtn = (ImageButton) toolbar.findViewById(R.id.update);

		mUpdateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				update();
			}
		});
	}

	public void init() {
		show(unlockAction);
	}


	protected void show(Runnable action) {
		lock();
		firstStage = false;
	}

	protected void update() {
		lock();
		firstStage = !firstStage;
	}

	protected void dismiss(Runnable action) {
		lock();
	}

	private void lock() {
		mUpdateBtn.setEnabled(false);
	}

	private void unlock() {
		mUpdateBtn.setEnabled(true);
	}
}