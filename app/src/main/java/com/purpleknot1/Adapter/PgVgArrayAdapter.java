package com.purpleknot1.Adapter;

////import android.app.Activity;
////import android.content.Context;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.ArrayAdapter;
////import android.widget.TextView;
////
////import com.purpleknot1.DTO.ScheduleVenueListDto;
////import com.purpleknot1.activity.R;
////
////import java.util.ArrayList;
////import java.util.List;
////
////public class PgVgArrayAdapter extends ArrayAdapter<ScheduleVenueListDto> {
////
////
////
////    Activity context;
////
////    ScheduleVenueListDto scheduleVenueListDto=null;
////    private List<ScheduleVenueListDto> scheduleVenueListDtos = new ArrayList<ScheduleVenueListDto>();
////
////        static class InspectionViewHolder {
////
////            TextView dailyReportVenueIdTxt,dailyReportVenueNameTxt,dailyReportVenueStatusTxt,dailyReportVenueDateTxt;
////
////        }
////
////
////        public PgVgArrayAdapter(Activity context, int textViewResourceId) {
////            super(context, textViewResourceId);
////
////            this.context = context;
////
////        }
////
////    @Override
////    public void add(ScheduleVenueListDto object) {
////
////        scheduleVenueListDtos.add(object);
////        super.add(object);
////    }
////
////    @Override
////    public int getCount() {
////        return this.scheduleVenueListDtos.size();
////    }
////
////    @Override
////    public ScheduleVenueListDto getItem(int index) {
////        return this.scheduleVenueListDtos.get(index);
////    }
////
////    @Override
////    public View getView(int position, View convertView, ViewGroup parent) {
////        View row = convertView;
////        final InspectionViewHolder viewHolder;
////        if (row == null) {
////
////            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////            row = inflater.inflate(R.layout.column_daily_report, parent, false);
////            viewHolder = new InspectionViewHolder();
////
////            viewHolder.dailyReportVenueIdTxt=(TextView) row.findViewById(R.id.daily_report_id_texiview);
////            viewHolder.dailyReportVenueNameTxt=(TextView) row.findViewById(R.id.daily_report_venue_name_textview);
////            viewHolder.dailyReportVenueStatusTxt=(TextView) row.findViewById(R.id.daily_report_venue_status_textview);
////            viewHolder.dailyReportVenueDateTxt=(TextView) row.findViewById(R.id.daily_report_venue_date_textview);
////
////
////            row.setTag(viewHolder);
////
////        } else {
////            viewHolder = (InspectionViewHolder)row.getTag();
////        }
////        scheduleVenueListDto = getItem(position);
////
////        if(scheduleVenueListDto!=null){
////
////            viewHolder.dailyReportVenueIdTxt.setText(Integer.toString(position+1));
////
////            if(scheduleVenueListDto.getVenueName()!=null){
////
////               // String venueName= PurpleKnotDBHelper.getInstance(getContext()).getVenueById(scheduleVenueListDto.getVenueId());
////
////                viewHolder.dailyReportVenueNameTxt.setText(scheduleVenueListDto.getVenueName());
////
////            }
////
////            if(scheduleVenueListDto.getPostVisitStatus()!=null){
////
////
////
////
////                    viewHolder.dailyReportVenueStatusTxt.setText(scheduleVenueListDto.getPostVisitStatus());
////
////            }
////
////            if(scheduleVenueListDto.getLastVisitDate()!=null){
////
////
////                viewHolder.dailyReportVenueDateTxt.setText(scheduleVenueListDto.getLastVisitDate());
////
////            }
////
////
////        }
////        return row;
////    }
//}