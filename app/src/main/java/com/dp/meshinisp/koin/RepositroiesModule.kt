package com.dp.meshinisp.koin

import android.os.Parcel
import com.dp.meshinisp.service.model.request.ActivationRequest
import com.dp.meshinisp.service.model.request.LoginRequest
import com.dp.meshinisp.service.model.request.RegisterRequest
import com.dp.meshinisp.service.repository.remotes.ActivationRepository
import com.dp.meshinisp.service.repository.remotes.LoginRepository
import com.dp.meshinisp.service.repository.remotes.Register1Repository
import com.dp.meshinisp.service.repository.remotes.Register2Repository
import org.koin.dsl.module.module

@JvmField
val DependencyModule = module {

    single { Register1Repository() }
    single { Register2Repository() }
    factory { RegisterRequest(Parcel.obtain()) }

    single { LoginRepository() }
    factory { LoginRequest() }

    single { ActivationRepository() }
    factory { ActivationRequest() }

}