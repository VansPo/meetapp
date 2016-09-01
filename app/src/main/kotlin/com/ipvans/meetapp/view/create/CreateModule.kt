package com.ipvans.meetapp.view.create

import com.ipvans.meetapp.AppComponent
import com.ipvans.meetapp.data.interactors.EventsInteractor
import com.ipvans.meetapp.data.qualifiers.PerActivity
import dagger.Component
import dagger.Module
import dagger.Provides

@Module
class CreateModule(val view: CreateActivity) {

    @Provides
    @PerActivity
    fun provideCreatePresenter(interactor: EventsInteractor) = CreatePresenter(view, interactor)

}

@PerActivity
@Component (dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(CreateModule::class))
interface CreateComponent {

    fun provideCreatePresenter(): CreatePresenter

    fun inject(view: CreateActivity)

}