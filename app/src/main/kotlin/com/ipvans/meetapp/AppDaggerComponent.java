package com.ipvans.meetapp;

public class AppDaggerComponent {

    public AppComponent getComponent(App app) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(app))
                .build();
    }

}
