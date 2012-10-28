package x.x.x;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Gustavo Claramunt.
 * User: adw
 * Date: 23/01/11
 * Time: 17:53
 */
public class DockPack extends Activity implements AdapterView.OnItemClickListener{
	private static final String ACTION_ADW_PICK_RESOURCE = "org.adw.launcher.docks.ACTION_PICK_RESOURCE";
	private static final String EXTRA_ADW_RESOURCENAME = "org.adw.launcher.docks.RESOURCE_NAME";
	private static final String EXTRA_ADW_PACKAGENAME = "org.adw.launcher.docks.PACKAGE_NAME";
	private boolean mResourceMode=false;
	
    public Uri CONTENT_URI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra(ACTION_ADW_PICK_RESOURCE)){
        	mResourceMode=true;
        }
        ListView lv=new ListView(this);
        lv.setAdapter(new DocksAdapter(this));
        lv.setOnItemClickListener(this);
        setContentView(lv);
        CONTENT_URI=Uri.parse("content://"+DocksProvider.class.getCanonicalName());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    	Intent result;
    	int resourceId=(Integer)adapterView.getItemAtPosition(i);
    	if(!mResourceMode){
            String dock=String.valueOf(resourceId);
            result = new Intent(null, Uri.withAppendedPath(CONTENT_URI,dock));
    	}else{
    		String name=getResources().getResourceName(resourceId);
    		result = new Intent();
    		result.putExtra(EXTRA_ADW_PACKAGENAME, getPackageName());
    		result.putExtra(EXTRA_ADW_RESOURCENAME, name);
    	}
        setResult(RESULT_OK, result);
        finish();
    }
    private class DocksAdapter extends BaseAdapter{
        public DocksAdapter(Context mContext) {
            super();
            loadDocks();
        }

        @Override
        public int getCount() {
            return mThumbs.size();
        }

        @Override
        public Object getItem(int position) {
            return mThumbs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=new ImageView(DockPack.this);
                convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, ListView.LayoutParams.WRAP_CONTENT));
            }
            ((ImageView)convertView).setBackgroundResource(mThumbs.get(position));
            return convertView;
        }

        private ArrayList<Integer> mThumbs;
        ////////////////////////////////////////////////
        private void loadDocks() {
            mThumbs = new ArrayList<Integer>();

            final Resources resources = getResources();
            final String packageName = getApplication().getPackageName();

            addDocks(resources, packageName, R.array.dock_pack);
        }
        private void addDocks(Resources resources, String packageName, int list) {
            final String[] extras = resources.getStringArray(list);
            for (String extra : extras) {
                int res = resources.getIdentifier(extra, "drawable", packageName);
                if (res != 0) {
                    final int thumbRes = resources.getIdentifier(extra,"drawable", packageName);
                    if (thumbRes != 0) {
                        mThumbs.add(thumbRes);
                    }
                }
            }
        }

    }
}
