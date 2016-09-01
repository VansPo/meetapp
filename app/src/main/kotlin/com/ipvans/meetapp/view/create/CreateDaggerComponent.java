package com.ipvans.meetapp.view.create;

import com.ipvans.meetapp.App;

public class CreateDaggerComponent {

    public CreateComponent getComponent(App app, CreateActivity view) {
        return DaggerCreateComponent.builder()
                .appComponent(app.getComponent())
                .createModule(new CreateModule(view))
                .build();
    }

}
