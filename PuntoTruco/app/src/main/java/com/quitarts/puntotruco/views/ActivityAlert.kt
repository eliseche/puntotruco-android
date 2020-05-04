package com.quitarts.puntotruco.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.quitarts.puntotruco.ApplicationMain
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.enums.AlertType
import kotlinx.android.synthetic.main.activity_alert.*
import java.io.Serializable

/**
 * Shows Alert in it's own Activity. It is not necessary to make use of Dialog and Context, so
 * it can be initialized out of a Views.
 */
class ActivityAlert : AppCompatActivity() {
    private var alertParams: AlertParams? = null
    private var alertType: AlertType = AlertType.POSITIVE

    companion object {
        const val ALERT_PARAMS = "ALERT_PARAMS"
        var completionPositive: (() -> Unit)? = null
        var completionNegative: (() -> Unit)? = null
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertParams = intent.extras?.getSerializable(ALERT_PARAMS) as AlertParams
        alertParams?.let {
            if (!it.messageNegative.isNullOrEmpty())
                alertType = AlertType.POSITIVE_NEGATIVE
        }

        setContentView(R.layout.activity_alert)
        init()
    }

    override fun onDestroy() {
        completionPositive = null
        completionNegative = null

        super.onDestroy()
    }

    override fun onBackPressed() {
    }

    //region Navigation
    fun alertPositive(view: View) {
        finish()

        completionPositive?.let {
            it()
        }
    }

    fun alertNegative(view: View) {
        finish()

        completionNegative?.let {
            it()
        }
    }
    //endregion

    //region Setup
    private fun init() {
        alertParams?.let {
            alert_title.text = it.title
            alert_message.text = HtmlCompat.fromHtml(it.message, HtmlCompat.FROM_HTML_MODE_LEGACY)
            alert_positive.text = it.messagePositive
            alert_positive.setTextColor(ContextCompat.getColor(ApplicationMain.applicationContext(), it.actionPositiveColor!!))
            if (alertType == AlertType.POSITIVE_NEGATIVE) {
                alert_negative.visibility = View.VISIBLE
                alert_negative.text = it.messageNegative
                alert_negative.setTextColor(ContextCompat.getColor(ApplicationMain.applicationContext(), it.actionNegativeColor!!))
            }
        }
    }
    //endregion

    data class AlertParams(
        val title: String = "",
        val message: String = "",
        val messagePositive: String? = null,
        val messageNegative: String? = null,
        val actionPositiveColor: Int? = R.color.black,
        val actionNegativeColor: Int? = R.color.black
    ) : Serializable
}
