package com.ipvans.meetapp.view.login

import com.ipvans.meetapp.AppComponent
import com.ipvans.meetapp.data.Store
import com.ipvans.meetapp.data.interactors.LoginInteractor
import com.ipvans.meetapp.data.model.UserState
import com.ipvans.meetapp.data.qualifiers.PerActivity
import com.ipvans.meetapp.data.qualifiers.User
import dagger.Component
import dagger.Module
import dagger.Provides

@Module
class LoginModule(val view: LoginActivity) {

    @Provides
    @PerActivity
    fun provideLoginPresenter(loginInteractor: LoginInteractor,
                              @User userState: Store<UserState>) = LoginPresenter(view, loginInteractor, userState)

}

@PerActivity
@Component (dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(LoginModule::class))
interface LoginComponent {

    fun provideLoginPresenter(): LoginPresenter

    fun inject(view: LoginActivity): Unit

}