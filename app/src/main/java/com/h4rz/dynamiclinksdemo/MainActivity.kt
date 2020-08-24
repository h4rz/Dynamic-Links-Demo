package com.h4rz.dynamiclinksdemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.simpleName
    private lateinit var myContext: Context

    companion object {

        private const val DEEP_LINK_URL = "https://nhancebyphoenix.com/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myContext = this
        btnShare.setOnClickListener {
            shareDeepLink(tvDeepLink.text.toString())
        }
        btnGenerate.setOnClickListener {
            var text = etDynamicLink.text.toString()
            if (text.isEmpty())
                text = DEEP_LINK_URL
            val newDeepLink = buildDeepLink(Uri.parse(text))
            tvDeepLink.text = newDeepLink.toString()
        }
        checkForDeepLinks()
    }

    private fun shareDeepLink(deepLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link")
        intent.putExtra(Intent.EXTRA_TEXT, deepLink)

        startActivity(intent)
    }

    fun buildDeepLink(deepLink: Uri): Uri {
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(deepLink.toString()))
            .setDomainUriPrefix("https://h4rz.page.link")
            // Open links with this app on Android
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            // Open links with com.example.ios on iOS
            .setIosParameters(DynamicLink.IosParameters.Builder("com.example.ios").build())
            .buildDynamicLink()

        val dynamicLinkUri = dynamicLink.uri

        return dynamicLinkUri;
    }

    private fun checkForDeepLinks() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...
                if (deepLink != null) {
                    Toast.makeText(myContext, "Found Deep Link",Toast.LENGTH_SHORT).show()
                    tvReceive.text = deepLink.toString()
                    val path = deepLink.path
                    val parts = path?.split("/")
                    if (parts != null) {
                        for (part in parts) {
                            if (part == "google")
                                DynamicLinksActivity.startActivity(myContext,deepLink.toString())
                        }
                    }
                } else
                    tvReceive.text = getString(R.string.empty_deep_link)
                // ...
            }
            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }
}