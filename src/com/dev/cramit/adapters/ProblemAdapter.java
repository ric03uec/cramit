package com.dev.cramit.adapters;

import java.util.ArrayList;
import java.util.List;

import com.dev.cramit.R;
import com.dev.cramit.models.Word;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This creates an adapter to show a problem.
 * @author devashish
 *
 */
public class ProblemAdapter extends ArrayAdapter<Word> {

	protected final String TAG = "ProblemAdapter";
	protected final List<Word> currentProblem;
	protected final Activity context;
	
	RadioGroup rg;
	
	/**
	 * Default constructor for building a single problem
	 * @param context - The current context.
	 * @param resource - The resource ID for a layout file containing a TextView to use when instantiating views. 
	 * @param problem - The problem to be displayed
	 */
	public ProblemAdapter(Activity context, int resource, List<Word> currentProblem) {
		super(context, resource, currentProblem);
		
		this.context		= context;
		this.currentProblem = new ArrayList<Word>(currentProblem);
		rg =  new RadioGroup(context);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		if(null == convertView){
	
			LayoutInflater inflator = context.getLayoutInflater();
			
//			view = inflator.inflate(android.R.layout.simple_list_item_single_choice, null);
			
			final ViewHolder viewHolder = new ViewHolder();
			
			if(position > 0){
				view = inflator.inflate(R.layout.problem_layout, null);
				if(position == 1){
					viewHolder.textView = (TextView) view.findViewById(R.id.optionOneText);
					viewHolder.selection	= (RadioButton) view.findViewById(R.id.optionOneRadioButton);
				}else if(position == 2){
					viewHolder.textView = (TextView) view.findViewById(R.id.optionTwoText);
					viewHolder.selection	= (RadioButton) view.findViewById(R.id.optionTwoRadioButton);
				}else if(position == 3){
					viewHolder.textView = (TextView) view.findViewById(R.id.optionThreeText);
					viewHolder.selection	= (RadioButton) view.findViewById(R.id.optionThreeRadioButton);
				}else if(position == 4){
					viewHolder.textView = (TextView) view.findViewById(R.id.optionFourText);
					viewHolder.selection	= (RadioButton) view.findViewById(R.id.optionFourRadioButton);
				}
//				viewHolder.selection = new RadioButton(context);
//				viewHolder.selection.setText("Test text new");
//				viewHolder.selectionGroup = (RadioGroup) view.findViewById(R.id.selectionGroup);
//				viewHolder.selectionGroup.removeView(viewHolder.selection);
//				Toast.makeText(context, viewHolder.selection.getParent().getClass().getName(), Toast.LENGTH_LONG);
				
//				rg.addView(viewHolder.selection.getParent().getClass().getName());
//				viewHolder.selectionGroup.addView(viewHolder.selection);
				viewHolder.selection.setOnClickListener(radio_listener);
//				viewHolder.selection.
				viewHolder.selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						Toast.makeText(context, "checkeddd", Toast.LENGTH_LONG);
						// TODO Auto-generated method stub
						
					}
				});
			}else if(position == 0){
				view = inflator.inflate(R.layout.menu_layout, null);
				viewHolder.textView = (TextView) view.findViewById(R.id.menu);
			}
//			viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////					Problem problem = (Problem) viewHolder.checkBox.getTag();
////					problem.
//					/**
//					 * TODO: take action when box is checked
//					 */
//				}
//			});
			view.setTag(viewHolder);
			if(position > 0)
				viewHolder.selection.setTag(position);
		}else{
			view = convertView;
			((ViewHolder) view.getTag()).selection.setTag(position);
		}
		
		ViewHolder holder = (ViewHolder) view.getTag();
		if(position == 0 ){
			holder.textView.setText("===  " +currentProblem.get(position).getWord() + " ===");
		}else{
			holder.textView.setText(currentProblem.get(position).getWord());
		}
		
		return view;
	}
	
	private OnClickListener radio_listener = new OnClickListener() {
	    public void onClick(View v) {
	        // Perform action on clicks
	        RadioButton rb = (RadioButton) v;
	        Toast.makeText(context, rb.getParent().getClass().getName(), Toast.LENGTH_SHORT).show();
	    }
	};
	
	static class ViewHolder {
 		public TextView textView;
 		public RadioButton selection;
// 		public RadioGroup selectionGroup;
 	}
}
