
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PackageBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
			// Ignore application updates
			final boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
			if (!replacing) {
				final int uid = intent.getIntExtra(Intent.EXTRA_UID, -123);
				Api.applicationRemoved(context, uid);
			}
		}
	}

}
