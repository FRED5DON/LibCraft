package ts.app.sagosoft.com.libcraft;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;


/**
 * 事件处理帮助类(otto) <br/>
 * 
 * @author zhihuxing@anjuke.com
 */
public class EventHelper {

    private static EventHelper helper;
    private static Bus bus;

    private EventHelper() {
    }

    public synchronized static EventHelper getHelper() {
        if (helper == null) {
            helper = new EventHelper();
            bus = new Bus();
        }
        return helper;
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public void post(final Object obj){
        //在其他线程调用会导致Event bus [Bus "default"] accessed from non-main thread Looper
//        bus.post(obj);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            bus.post(obj);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    bus.post(obj);
                }
            });
        }
    }
    
    public void register(Object obj) {
        bus.register(obj);
    }

    public void unregister(Object obj) {
        bus.unregister(obj);
    }

}

