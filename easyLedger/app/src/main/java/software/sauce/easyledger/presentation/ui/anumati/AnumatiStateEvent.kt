package software.sauce.easyledger.presentation.ui.anumati

sealed class AnumatiStateEvent {
    data class GetConsentUrl(
        val phone: String
    ): AnumatiStateEvent()
}