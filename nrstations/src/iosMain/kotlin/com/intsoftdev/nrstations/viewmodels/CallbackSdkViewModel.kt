package com.intsoftdev.nrstations.viewmodels

import com.intsoftdev.nrstations.shared.FlowAdapter
import kotlinx.coroutines.flow.Flow

abstract class CallbackSdkViewModel {
    protected abstract val viewModel: NreViewModel

    /**
     * Create a [FlowAdapter] from this [Flow] to make it easier to interact with from Swift.
     */
    fun <T : Any> Flow<T>.asCallbacks() =
        FlowAdapter(viewModel.viewModelScope, this)

    @Suppress("Unused") // Called from Swift
    fun clear() = viewModel.clear()
}
