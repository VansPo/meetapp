package com.ipvans.meetapp.view.events

import android.content.Context
import com.ipvans.meetapp.AppComponent
import com.ipvans.meetapp.data.interactors.EventsInteractor
import com.ipvans.meetapp.data.qualifiers.PerActivity
import com.ipvans.modelsandbox.core.getModel
import dagger.Component
import dagger.Module
import dagger.Provides

@Module
class EventListModule(val context: Context, val view: EventListFragment, val mode: String) {

    @Provides
    @PerActivity
    fun provideEventListModel(eventsInteractor: EventsInteractor) =
            context.getModel("eventList#$mode") { EventListModel(eventsInteractor, mode) }

}

@PerActivity
@Component (dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(EventListModule::class))
interface EventListComponent {

    fun provideEventListModel(): EventListModel

    fun inject(view: EventListFragment): Unit

}