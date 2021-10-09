package software.sauce.easyledger.presentation.ui.anumati

import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import software.sauce.easyledger.presentation.components.CenterOfScreen
import software.sauce.easyledger.presentation.navigation.Screen

@Composable
fun AnumatiWebView(
    onNavigation: (String) -> Unit,
    phone: String,
    webViewModel: AnumatiViewModel
) {
    val onLoad = webViewModel.onLoad.value
    if (!onLoad) {
        webViewModel.onLoad.value = true
        webViewModel.onTriggerEvent(AnumatiStateEvent.GetConsentUrl(phone))
    }

    val consentUrl = webViewModel.consentUrl.value

    val isLoading = webViewModel.isLoading.collectAsState().value

    if (isLoading) {
        CenterOfScreen(modifier = Modifier.padding(top = 16.dp)) {
            CircularProgressIndicator()
        }
    } else {
        if (consentUrl != null) {
            AndroidView({ context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)

                            webViewModel.onPageFinished()
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            // cancel the current request if the url is the redirect url
                            return if (request?.url.toString().contains("/redirect")) {
                                onNavigation(Screen.Home.route)
                                true
                            } else {
                                false
                            }
                        }
                    }
                    webChromeClient = object : WebChromeClient(){
                        override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                            Log.d("EzLedger", "${message.message()} -- From line " +
                                    "${message.lineNumber()} of ${message.sourceId()}")
                            return true
                        }
                    }
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                    }
                    loadUrl(consentUrl)
                }
            })
        } else {
            CenterOfScreen(modifier = Modifier.padding(top = 16.dp)) {
                Text("Failed")
            }
        }
    }
}