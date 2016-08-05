package com.ipvans.meetapp.view.login

import com.ipvans.meetapp.data.Store
import com.ipvans.meetapp.data.interactors.LoginInteractor
import com.ipvans.meetapp.data.model.UserState
import com.ipvans.meetapp.data.plusAssign
import com.ipvans.meetapp.data.restapi.model.request.LoginRequest
import com.ipvans.meetapp.data.restapi.model.request.SignupRequest
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

class LoginPresenter(val view: LoginActivity,
                     val loginInteractor: LoginInteractor,
                     val userState: Store<UserState>) {

    val subscriptions = CompositeSubscription()
    val screenState = BehaviorSubject.create<Boolean>(true)

    fun checkUserState() {
        userState.get()?.let { view.startMainActivity() }
    }

    fun login(request: SignupRequest) {
        view.showProgress()
        subscriptions += screenState.take(1)
                .flatMap {
                    if (it) loginInteractor.login(LoginRequest(request.email, request.password))
                    else loginInteractor.signup(request)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach { screenState.onNext(screenState.value) }
                .subscribe({
                    if (it.success) view.startMainActivity()
                    else view.showError(it.message)
                }, {
                    view.showError("Unexpected error")
                    Timber.e("Login error" , it)
                })
    }

    fun switchScreenState() = screenState.onNext(!screenState.value)

    fun onAttached() {
        subscriptions += screenState.asObservable()
                .subscribe {
                    if (it) view.showLoginForm()
                    else view.showSignupForm()
                }
    }

    fun onDetached() = subscriptions.clear()

}