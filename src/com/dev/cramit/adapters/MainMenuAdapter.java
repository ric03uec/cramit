package com.dev.cramit.adapters;



import com.dev.cramit.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainMenuAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final String[] items;
	
	public MainMenuAdapter(Activity context, int textViewResourceId, String[] items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
	}
	
	 public boolean areAllItemsEnabled() {
         return false;
     }

     public boolean isEnabled(int position) {
       if((0 == position) || (1 == position) || (2 == position) || (4 == position) || (6 == position) || (8 == position) || (10 == position)){
     	  return false;
       }
       return true;
     }

     public View getView(int position, View convertView, ViewGroup parent) {
     	
     	ViewHolder holder;
     	View rowView = convertView;
     	
     	if (rowView == null) {
     		LayoutInflater inflater = context.getLayoutInflater();
     		holder = new ViewHolder();
     		
     		if((0 == position) || ( 1 == position)){
     			rowView = inflater.inflate(R.layout.menu_layout, parent, false);
     			holder.textView = (TextView) rowView.findViewById(R.id.menu);
     			if(0 == position){
     	     		holder.textView.setText("~~~ "+ items[position] + " ~~~");
     	     	}else if(1 == position){
     	     		holder.textView.setText("< " + items[position] + " >");
     	     	}
     		}else {
     			rowView = inflater.inflate(R.layout.menu_items_layout, null, false);
     			holder.textView = (TextView) rowView.findViewById(R.id.menu_item);
     			holder.textView.setText(items[position]);
     		}
     		rowView.setTag(holder);
     	}else {
			holder = (ViewHolder) rowView.getTag();
		}
     	
     	return rowView;
     }
     
     static class ViewHolder {
 		public TextView textView;
 	}

}
