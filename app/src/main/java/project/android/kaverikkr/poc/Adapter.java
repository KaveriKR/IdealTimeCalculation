package project.android.kaverikkr.poc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

   Context context;
   HashMap<String,String> map = new HashMap<>();
   HashMap<String,String> map2 = new HashMap<>();
    List<String> list,list2,listkeys;
   public Adapter(Context context , HashMap<String ,String> map, HashMap<String ,String> map2){

       this.context = context;
       this.map = map;
       this.map2 = map2;
       list = new ArrayList<String>(map.values());
       listkeys = new ArrayList<String>(map.keySet());
       list2 = new ArrayList<String>(map2.values());

   }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


            holder.date.setText(listkeys.get(position));
            holder.start_time.setText(list.get(position));
            holder.end_time.setText(list2.get(position));



    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView date,start_time,end_time;
        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            start_time = itemView.findViewById(R.id.start_time);
            end_time = itemView.findViewById(R.id.end_time);
        }
    }
}
