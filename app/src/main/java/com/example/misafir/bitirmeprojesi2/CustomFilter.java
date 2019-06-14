package com.example.misafir.bitirmeprojesi2;

/**
 * Created by MISAFIR on 18.12.2017.
 */


        import android.widget.Filter;

        import java.util.ArrayList;

/**
 * Created by Hp on 3/17/2016.
 */
public class CustomFilter extends Filter{

    RecyclerAdapter adapter;
    ArrayList<ApiModel> filterList;


    public CustomFilter(ArrayList<ApiModel> filterList,RecyclerAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;


    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<ApiModel> filteredCity=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getvisionApiData().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredCity.add(filterList.get(i));
                }

            }

            results.count=filteredCity.size();
            results.values=filteredCity;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.list= (ArrayList<ApiModel>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
