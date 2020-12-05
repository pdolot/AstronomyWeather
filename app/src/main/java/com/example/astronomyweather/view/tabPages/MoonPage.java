package com.example.astronomyweather.view.tabPages;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.R;
import com.example.astronomyweather.model.AstronomyInfo;
import com.example.astronomyweather.model.MoonData;

import java.util.ArrayList;
import java.util.List;

public class MoonPage extends BasePage<MoonData> {

    private final AstronomyInfoAdapter adapter = new AstronomyInfoAdapter();

    public MoonPage(MoonData data) {
        setData(data);
    }

    @Override
    public ViewType getViewType() {
        return ViewType.VIEW_MOON;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.itemView.findViewById(R.id.viewMoon_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setData(getAstronomyInfo());
    }

    private List<AstronomyInfo> getAstronomyInfo() {
        List<AstronomyInfo> astronomyInfo = new ArrayList<>();
        astronomyInfo.add(new AstronomyInfo("Ostatnia aktualizacja", getData().getUpdateDate(), R.drawable.ic_small_moon));
        astronomyInfo.add(new AstronomyInfo("Wschód księżyca", getData().getMoonrise(), R.drawable.ic_small_moon));
        astronomyInfo.add(new AstronomyInfo("Wysokość księżyca", getData().getMoonAltitude(), R.drawable.ic_small_moon));
        astronomyInfo.add(new AstronomyInfo("Azymut księżyca", getData().getMoonAzimuth(), R.drawable.ic_small_moon));
        astronomyInfo.add(new AstronomyInfo("Odległość księżyca", getData().getMoonDistance(), R.drawable.ic_small_moon));
        astronomyInfo.add(new AstronomyInfo("Kąt paralaktyczny", getData().getMoonParallacticAngle(), R.drawable.ic_small_moon));
        return astronomyInfo;
    }
}
