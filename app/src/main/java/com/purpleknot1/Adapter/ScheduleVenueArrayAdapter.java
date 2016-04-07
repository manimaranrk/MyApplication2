package com.purpleknot1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.purpleknot1.activity.R;
import com.purpleknot1.DTO.ScheduleVenueListDto;
import com.purpleknot1.DTO.ScheduleVenueListDto;

import java.util.ArrayList;
import java.util.List;

public class ScheduleVenueArrayAdapter extends ArrayAdapter<ScheduleVenueListDto> {


    Activity context;

    ScheduleVenueListDto scheduleVenueListDto=null;
    private List<ScheduleVenueListDto> scheduleVenueListDtos = new ArrayList<ScheduleVenueListDto>();

        static class InspectionViewHolder {

            TextView scheduleVenueIdTxt,scheduleVenueNameTxt,scheduleVenueStatusTxt,scheduleVenueDateTxt;

        }


        public ScheduleVenueArrayAdapter(Activity context, int textViewResourceId) {
            super(context, textViewResourceId);

            this.context = context;

        }

    @Override
    public void add(ScheduleVenueListDto object) {

        scheduleVenueListDtos.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.scheduleVenueListDtos.size();
    }

    @Override
    public ScheduleVenueListDto getItem(int index) {
        return this.scheduleVenueListDtos.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final InspectionViewHolder viewHolder;
        if (row == null) {

            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.column_schedule, parent, false);
            viewHolder = new InspectionViewHolder();

            viewHolder.scheduleVenueIdTxt=(TextView) row.findViewById(R.id.schedulevenue_id_texiview);
            viewHolder.scheduleVenueNameTxt=(TextView) row.findViewById(R.id.schedulevenue_name_textview);
            viewHolder.scheduleVenueStatusTxt=(TextView) row.findViewById(R.id.schedulevenue_ststus_textview);
            viewHolder.scheduleVenueDateTxt=(TextView) row.findViewById(R.id.schedulevenue_date_textview);

            row.setTag(viewHolder);

        } else {
            viewHolder = (InspectionViewHolder)row.getTag();
        }
        scheduleVenueListDto = getItem(position);

        if(scheduleVenueListDto!=null){

            viewHolder.scheduleVenueIdTxt.setText(Integer.toString(position+1));

//            if(scheduleVenueListDto.getVenueId()!=0){
//
//                String venueName= PurpleKnotDBHelper.getInstance(getContext()).getVenueById(scheduleVenueListDto.getVenueId());
//
//                viewHolder.scheduleVenueNameTxt.setText(venueName);
//
//            }
//
//            if(scheduleVenueListDto.getVenueStatus()!=null){
//
//                if(scheduleVenueListDto.getVenueId()!=0) {
//
//                    String venueStatus = PurpleKnotDBHelper.getInstance(getContext()).getVenueStatus(scheduleVenueListDto.getVenueId());
//                    viewHolder.scheduleVenueStatusTxt.setText(venueStatus);
//                }
//            }
//
//            if(scheduleVenueListDto.getFollowUp()!=null){
//
//                String followUp=scheduleVenueListDto.getFollowUp();
//                String[] splitedFollowUpayyay = followUp.split("\\s+");
//                String[] splitedFollowUpParts = splitedFollowUpayyay[0].split("-");
//                String followDate=splitedFollowUpParts[2]+"-"+splitedFollowUpParts[1]+"-"+splitedFollowUpParts[0];
//                viewHolder.scheduleVenueDateTxt.setText(followDate);
//
//            }
        }
        return row;
    }
}