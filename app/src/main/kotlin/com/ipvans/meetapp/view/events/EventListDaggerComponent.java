package com.ipvans.meetapp.view.events;

import android.app.Activity;

import com.ipvans.meetapp.App;

public class EventListDaggerComponent {

    public EventListComponent getComponent(App app, Activity activity, EventListFragment view, String mode) {
        return DaggerEventListComponent.builder()
                .appComponent(app.getComponent())
                .eventListModule(new EventListModule(activity, view, mode))
                .build();
    }
}
