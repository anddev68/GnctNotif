package anddev68.jp.gnctnotif;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nifty.cloud.mb.core.NCMBObject;

import java.util.ArrayList;


/**
 * MainAdapter
 * MainActivityに表示する教科一覧になります
 */
public class MainAdapter  extends  RecyclerView.Adapter<MainAdapter.ViewHolder> {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<Changes> mData;

    public MainAdapter(Context context, Parcelable[] data){
        super();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
        for(Parcelable tmp : data){
            mData.add((Changes)tmp);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.row_main, parent, false);

        ViewHolder viewHolder = new ViewHolder(v,mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /* 値を代入したりする処理 */
        //holder.textView.setText(mData.get(position).name);
       // holder.textView.setText("E");
        holder.dateView.setText(mData.get(position).toDateString());
        holder.msgView.setText(mData.get(position).getMsg());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }





    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context context;
        View rootView;
        TextView msgView;
        TextView dateView;
        TextView iconTextView;

        public ViewHolder(View v,Context context) {
            super(v);
            rootView = v;
            msgView = (TextView) v.findViewById(R.id.msg);
            dateView = (TextView) v.findViewById(R.id.date);
            //iconTextView = (TextView) v.findViewById(R.id.iconText);

            this.context = context;

        }


        @Override
        public void onClick(View view) {
            /* 押されたときの処理 */

        }
    }

}
