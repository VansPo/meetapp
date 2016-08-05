package com.ipvans.meetapp.view.events

import com.ipvans.meetapp.AppComponent
import com.ipvans.meetapp.data.interactors.EventsInteractor
import com.ipvans.meetapp.data.qualifiers.PerActivity
import dagger.Component
import dagger.Module
import dagger.Provides

@Module
class EventListModule(val view: EventListFragment, val mode: String) {

    @Provides
    @PerActivity
    fun provideEventListPresenter(eventsInteractor: EventsInteractor) =
            EventListPresenter(view, eventsInteractor, mode)

}

@PerActivity
@Component (dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(EventListModule::class))
interface EventListComponent {

    fun provideEventListPresenter(): EventListPresenter

    fun inject(view: EventListFragment): Unit

}