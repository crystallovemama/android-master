/**
	 * Show iptable rules on a dialog
	 */
	private void showRules() {
		final Resources res = getResources();
		final ProgressDialog progress = ProgressDialog.show(this,
				res.getString(R.string.working),
				res.getString(R.string.please_wait), true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				try {
					progress.dismiss();
				} catch (Exception ex) {
				}
				if (!Api.hasRootAccess(MainActivity.this, true))
					return;
				Api.showIptablesRules(MainActivity.this);
			}
		};
		handler.sendEmptyMessageDelayed(0, 100);
	}

	/**
	 * Show logs on a dialog
	 */
	private void showLog() {
		final Resources res = getResources();
		final ProgressDialog progress = ProgressDialog.show(this,
				res.getString(R.string.working),
				res.getString(R.string.please_wait), true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				try {
					progress.dismiss();
				} catch (Exception ex) {
				}
				Api.showLog(MainActivity.this);
			}
		};
		handler.sendEmptyMessageDelayed(0, 100);
	}

	/**
	 * Clear logs
	 */
	private void clearLog() {
		final Resources res = getResources();
		final ProgressDialog progress = ProgressDialog.show(this,
				res.getString(R.string.working),
				res.getString(R.string.please_wait), true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				try {
					progress.dismiss();
				} catch (Exception ex) {
				}
				if (!Api.hasRootAccess(MainActivity.this, true))
					return;
				if (Api.clearLog(MainActivity.this)) {
					Toast.makeText(MainActivity.this, R.string.log_cleared,
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		handler.sendEmptyMessageDelayed(0, 100);
	}

	/**
	 * Apply or save iptable rules, showing a visual indication
	 */
	private void applyOrSaveRules() {
		final Resources res = getResources();
		final boolean enabled = Api.isEnabled(this);
		final ProgressDialog progress = ProgressDialog.show(this, res
				.getString(R.string.working), res
				.getString(enabled ? R.string.applying_rules
						: R.string.saving_rules), true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				try {
					progress.dismiss();
				} catch (Exception ex) {
				}
				if (enabled) {
					Log.d("DroidWall", "Applying rules.");
					if (Api.hasRootAccess(MainActivity.this, true)
							&& Api.applyIptablesRules(MainActivity.this, true)) {
						Toast.makeText(MainActivity.this,
								R.string.rules_applied, Toast.LENGTH_SHORT)
								.show();
					} else {
						Log.d("DroidWall", "Failed - Disabling firewall.");
						Api.setEnabled(MainActivity.this, false);
					}
				} else {
					Log.d("DroidWall", "Saving rules.");
					Api.saveRules(MainActivity.this);
					Toast.makeText(MainActivity.this, R.string.rules_saved,
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		handler.sendEmptyMessageDelayed(0, 100);
	}

	/**
	 * Purge iptable rules, showing a visual indication
	 */
	private void purgeRules() {
		final Resources res = getResources();
		final ProgressDialog progress = ProgressDialog.show(this,
				res.getString(R.string.working),
				res.getString(R.string.deleting_rules), true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				try {
					progress.dismiss();
				} catch (Exception ex) {
				}
				if (!Api.hasRootAccess(MainActivity.this, true))
					return;
				if (Api.purgeIptables(MainActivity.this, true)) {
					Toast.makeText(MainActivity.this, R.string.rules_deleted,
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		handler.sendEmptyMessageDelayed(0, 100);
	}

	/**
	 * Called an application is check/unchecked
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		final DroidApp app = (DroidApp) buttonView.getTag();
		if (app != null) {
			switch (buttonView.getId()) {
			case R.id.itemcheck_wifi:
				app.selected_wifi = isChecked;
				break;
			case R.id.itemcheck_3g:
				app.selected_3g = isChecked;
				break;
			}
		}
	}

	private static class ListEntry {
		private CheckBox box_wifi;
		private CheckBox box_3g;
		private TextView app_text;
		private TextView upload;
		private TextView download;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.label_mode:
			selectMode();
			break;
		}
	}
}
