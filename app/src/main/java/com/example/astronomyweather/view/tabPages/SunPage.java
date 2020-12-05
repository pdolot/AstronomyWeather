package com.example.astronomyweather.view.tabPages;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.R;
import com.example.astronomyweather.model.AstronomyInfo;
import com.example.astronomyweather.model.SunData;

import java.util.ArrayList;
import java.util.List;

public class SunPage extends BasePage<SunData> {

    private final AstronomyInfoAdapter adapter = new AstronomyInfoAdapter();

    public SunPage(SunData data) {
        setData(data);
    }

    @Override
    public ViewType getViewType() {
        return ViewType.VIEW_SUN;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.itemView.findViewById(R.id.viewSun_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setData(getAstronomyInfo());
    }

    private List<AstronomyInfo> getAstronomyInfo() {
        List<AstronomyInfo> astronomyInfo = new ArrayList<>();
        astronomyInfo.add(new AstronomyInfo("Ostatnia aktualizacja", getData().getUpdateDate(), R.drawable.ic_more));
        astronomyInfo.add(new AstronomyInfo("Wschód słońca", getData().getSunrise(), R.drawable.ic_more));
        astronomyInfo.add(new AstronomyInfo("Zachód słońca", getData().getSunset(), R.drawable.ic_more));
        astronomyInfo.add(new AstronomyInfo("Wysokość słońca", getData().getSunAltitude(), R.drawable.ic_more));
        astronomyInfo.add(new AstronomyInfo("Azymut słońca", getData().getSunAzimuth(), R.drawable.ic_more));
        astronomyInfo.add(new AstronomyInfo("Odległość słońca", getData().getSunDistance(), R.drawable.ic_more));
        return astronomyInfo;
    }
}
