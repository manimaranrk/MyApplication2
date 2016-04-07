package com.purpleknot1.Adapter;


//public class SimpleAdapter extends android.widget.SimpleAdapter {
//
////    private ArrayList<HashMap<String, String>> results;
////
////    public MySimpleAdapter(Context context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
////        super(context, data, resource, from, to);
////        this.results = data;
////    }
////
////    public View getView(int position, View view, ViewGroup parent){
////
////
////        View v = view;
////        if (v == null) {
////            LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////            v = vi.inflate(R.layout.list_item, null);
////        }
////        TextView tt = (TextView) v.findViewById(R.id.item_title);
////        tt.setText(results.get(position).get("title"));
////        tt.setTypeface(localTypeface1);
////        TextView bt = (TextView) v.findViewById(R.id.item_subtitle);
////        bt.setText(results.get(position).get("date"));
////        bt.setTypeface(localTypeface1);
////        return v;
////    }
//}