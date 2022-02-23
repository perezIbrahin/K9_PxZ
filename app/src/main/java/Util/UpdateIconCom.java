package Util;

import static com.example.k9_pxz.R.string.ble_disconnected;

import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.k9_pxz.R;

public class UpdateIconCom extends AppCompatActivity {
    ImageView imageView;
    int value;

    public UpdateIconCom() {
    }

    public UpdateIconCom(ImageView imageView, int value) {
        this.imageView = imageView;
        this.value = value;
    }

    //update icon
    public int updateIconCommunication(ImageView iv, int value) {
        if (iv != null) {
            if (value > 0) {
                switch (value) {
                    case 0:
                        iv.setImageResource(R.drawable.ic_baseline_cable_24);
                        break;
                    case R.string.ble_disconnected:
                        iv.setImageResource(R.drawable.ic_baseline_sync_disabled_24);
                        break;
                    case R.string.ble_disconnected_requested:
                        iv.setImageResource(R.drawable.ic_baseline_sync_disabled_24);
                        break;
                    case R.string.ble_discovery_finished:
                        // ivConnection.setImageResource(R.drawable.ic_bluetooth_searching_black_24dp);
                        iv.setImageResource(R.drawable.ic_baseline_sync_24);
                        break;
                    case R.string.ble_found:
                        //ivConnection.setImageResource(R.drawable.ic_bluetooth_black_24dp);
                        iv.setImageResource(R.drawable.ic_baseline_sync_alt_24);
                        break;
                    case R.string.eth_connected:
                        iv.setImageResource(R.drawable.ic_settings_ethernet_black_24dp);
                        break;
                    case R.string.eth_disconnected:
                        iv.setImageResource(R.drawable.ic_action_network_unavailable);
                        break;
                    default:
                        iv.setImageResource(R.drawable.ic_error_black_24dp);
                        break;
                }
            }
        }
        return 0;
    }


}
