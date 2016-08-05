package com.ipvans.meetapp.view.login;

import com.ipvans.meetapp.AppComponent;

public class LoginDaggerComponent {

    public LoginComponent getComponent(AppComponent appComponent, LoginActivity view) {
        return DaggerLoginComponent.builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(view))
                .build();
    }

}
