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

import java.util.ArrayList;
import java.util.List;

public class StatusArrayAdapter extends ArrayAdapter<ScheduleVenueListDto> {


    Activity context;

    ScheduleVenueListDto scheduleVenueListDto=null;
    private List<ScheduleVenueListDto> scheduleVenueListDtos = new ArrayList<ScheduleVenueListDto>();

        static class InspectionViewHolder {

            TextView statusNameTxt;

        }


        public StatusArrayAdapter(Activity context, int textViewResourceId) {
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
            row = inflater.inflate(R.layout.column_status, parent, false);
            viewHolder = new InspectionViewHolder();

            viewHolder.statusNameTxt=(TextView) row.findViewById(R.id.status_texiview);



            row.setTag(viewHolder);

        } else {
            viewHolder = (InspectionViewHolder)row.getTag();
        }
        scheduleVenueListDto = getItem(position);

        if(scheduleVenueListDto!=null){

          //  viewHolder.dailyReportVenueIdTxt.setText(Integer.toString(position+1));



            if(scheduleVenueListDto.getLastVisitDate()!=null){


                viewHolder.statusNameTxt.setText(scheduleVenueListDto.getLastVisitDate());

            }


        }
        return row;
    }
}