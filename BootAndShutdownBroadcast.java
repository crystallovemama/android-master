
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


public class BootAndShutdownBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			if (Api.isEnabled(context)) {
				final Handler toaster = new Handler() {
					public void handleMessage(Message msg) {
						if (msg.arg1 != 0)
							Toast.makeText(context, msg.arg1,
									Toast.LENGTH_SHORT).show();
					}
				};
				// Start a new thread to enable the firewall - this prevents ANR
				new Thread() {
					@Override
					public void run() {
						if (!Api.applySavedIptablesRules(context, false)) {
							// Error enabling firewall on boot
							final Message msg = new Message();
							msg.arg1 = R.string.toast_error_enabling;
							toaster.sendMessage(msg);
							Api.setEnabled(context, false);
						}
					}
				}.start();
			}
		} else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
			new Thread() {
				@Override
				public void run() {
					Api.storageTraffic(context);
				}
			}.start();
		}
	}
}
