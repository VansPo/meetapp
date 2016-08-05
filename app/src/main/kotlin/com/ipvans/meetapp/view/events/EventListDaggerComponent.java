package com.ipvans.meetapp.view.events;

import com.ipvans.meetapp.App;

public class EventListDaggerComponent {

    public EventListComponent getComponent(App app, EventListFragment view, String mode) {
        return DaggerEventListComponent.builder()
                .appComponent(app.getComponent())
                .eventListModule(new EventListModule(view, mode))
                .build();
    }
}
