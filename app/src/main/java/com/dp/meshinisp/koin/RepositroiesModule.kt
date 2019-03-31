package com.dp.meshinisp.koin

import android.os.Parcel
import com.dp.meshinisp.service.model.request.ActivationRequest
import com.dp.meshinisp.service.model.request.LoginRequest
import com.dp.meshinisp.service.model.request.RegisterRequest
import com.dp.meshinisp.service.model.request.SearchRequestsRequest
import com.dp.meshinisp.service.repository.remotes.*
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

    single { MainRepository() }
    factory { SearchRequestsRequest() }

    single { RequestDetailsRepository() }
    single { AccountRepository() }

}