package com.dp.meshinisp.koin
import com.dp.meshinisp.viewmodel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

@JvmField
val ViewModelModule = module {

    viewModel { Register1ViewModel(androidApplication()) }
    viewModel { Register2ViewModel(androidApplication()) }
    viewModel { LoginViewModel(androidApplication()) }
    viewModel { ResetPasswordViewModel(androidApplication()) }
    viewModel { PhoneActivationViewModel(androidApplication()) }
    viewModel { ChangePasswordViewModel(androidApplication()) }
    viewModel { MainActivityViewModel(androidApplication()) }
    viewModel { RequestsActivityViewModel(androidApplication()) }
    viewModel { RequestDetailsViewModel(androidApplication()) }
    viewModel { AccountActivityViewModel(androidApplication()) }
    viewModel { ChangePasswordProfileViewModel(androidApplication()) }

}