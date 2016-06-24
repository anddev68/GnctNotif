package anddev68.jp.gnctnotif;

import android.os.Parcel;
import android.os.Parcelable;

import com.nifty.cloud.mb.core.NCMBObject;

/**
 * Created by kano on 2016/06/24.
 */
public class Changes implements Parcelable {

    String hour;
    String gakka;
    String gakunen;
    String msg;
    String mm;
    String dd;
    String weeks;

    public Changes(Parcel in){
        gakka = in.readString();
        gakunen = in.readString();
        msg = in.readString();
        mm = in.readString();
        dd = in.readString();
        hour = in.readString();
        weeks = in.readString();
    }

    public Changes(NCMBObject obj){
        gakka = obj.getString("gakka");
        gakunen = obj.getString("gakunen");
        msg = obj.getString("msg");
        mm = obj.getString("mm");
        dd = obj.getString("dd");
        hour = obj.getString("hour");
        weeks = obj.getString("weeks");
    }

    public String getMsg(){ return msg; }
    public String toDateString(){
        return String.format("%s月%s日(%s) %s限", mm, dd, weeks, hour);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gakka);
        dest.writeString(gakunen);
        dest.writeString(msg);
        dest.writeString(mm);
        dest.writeString(dd);
        dest.writeString(hour);
        dest.writeString(weeks);
    }

    public static final Parcelable.Creator<Changes> CREATOR
            = new Parcelable.Creator<Changes>() {
        public Changes createFromParcel(Parcel in) {
            return new Changes(in);
        }

        public Changes[] newArray(int size) {
            return new Changes[size];
        }
    };

}
