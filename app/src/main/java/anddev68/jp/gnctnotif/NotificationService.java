package anddev68.jp.gnctnotif;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Notification Service
 * 指定された時間に、授業変更がないかどうかを確認し、
 * あった場合orない場合は通知するようにする
 */
public class NotificationService extends IntentService {

    private static String TAG = "NotificationService";
    private int changesLen;
    private Changes[] changesList;
    private String fileUpdateDate;

    public NotificationService() {
        super(TAG);
        changesLen = -1;
        fileUpdateDate = null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        //  学科取得
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String gakka = pref.getString("gakka","Ｃ");
        String gakunen = pref.getString("gakunen","５");

        //  授業変更を確認する
        NCMB.initialize(this.getApplicationContext(),Define.APP_KEY,Define.CLIENT_KEY);
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("Changes");
        query.whereEqualTo("gakka", gakka);
        query.whereEqualTo("gakunen", gakunen);
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> list, NCMBException e) {
                changesLen =list.size();
                changesList = new Changes[changesLen];
                for(int i=0; i<changesLen; i++){
                    changesList[i] = new Changes(list.get(i));
                }
                Log.d(TAG, "done downloading changes len="+changesLen);
            }
        });

        //  最終更新日を取得する
        NCMBQuery<NCMBObject> query2 = new NCMBQuery<>("ModifiedHistory");
        query2.addOrderByAscending("createDate");
        query2.setLimit(1);
        query2.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> list, NCMBException e) {
                fileUpdateDate = list.get(0).getString("fileUpdateDate");
                Log.d(TAG, "done downloading changes update="+fileUpdateDate);
            }
        });

        //  両方取得できるまで待機
        while(changesLen==-1);
        while(fileUpdateDate==null);

        //  結果を通知する
        /*
        for(Changes changes: changesList){
            System.out.println(changes);
        }*/

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("changes",changesList);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent2 = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(android.R.drawable.ic_menu_call);
        builder.setContentTitle("授業変更通知");
        builder.setContentText(changesLen+"件の授業変更 ("+fileUpdateDate+")");
        builder.setContentIntent(intent2);
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notification);




    }
}
