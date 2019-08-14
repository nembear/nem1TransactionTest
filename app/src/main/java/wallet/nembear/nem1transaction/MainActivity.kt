package wallet.nembear.nem1transaction

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ryuta46.nemkotlin.account.AccountGenerator
import com.ryuta46.nemkotlin.client.RxNemApiClient
import com.ryuta46.nemkotlin.transaction.TransactionHelper
import com.ryuta46.nemkotlin.util.ConvertUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = RxNemApiClient("http://62.75.251.134:7890")


//      タイムスタンプ取得
        client.networkTime()
            .subscribeOn(Schedulers.newThread())
            .subscribe { timeStamp ->
                val nowTime = timeStamp.receiveTimeStamp
                nowTimeStamp(nowTime)

            }

        button.setOnClickListener {

            try {
                val transaction = TransactionHelper.createXemTransferTransaction(
                    ACCOUNT,
                    RCVADDRESS,
                    100000000,
                    timeStamp = getNowTimeStamp()
                )
                val result = client.transactionAnnounce(transaction)
                Log.d("TAG", result.toString())

                }catch (e: NumberFormatException) {
                Log.d("TAG", e.message)

                }
        }

    }

//   タイムスタンプ情報保存
    private fun nowTimeStamp(nowTime :Long) {
        val data = getSharedPreferences(SHAREDPREFERENCES_PACKAGE_NAME, Context.MODE_PRIVATE)
        val editor = data.edit()
        editor.putInt(NOW_TIME, nowTime.toInt())
        editor.apply()
    }

    private fun getNowTimeStamp(): Int {
        val data = getSharedPreferences(SHAREDPREFERENCES_PACKAGE_NAME, Context.MODE_PRIVATE)
        val getTimeStamp = data.getInt(NOW_TIME, 1)
        return getTimeStamp
    }

//tracsaction用アカウント情報＆送信先アドレス
    companion object {
        private val ACCOUNT = AccountGenerator.fromSeed(ConvertUtils.toByteArray("24b5aa82d62a606edf1e866a01e7a5295d8610f15da4b953783bb0de9591a2f9"),com.ryuta46.nemkotlin.enums.Version.Main)
        private const val RCVADDRESS = "ND2OPDYXXW7FQZE6IOZAC4NPNWU5A2GYBHJH5Q4U"

        private const val SHAREDPREFERENCES_PACKAGE_NAME = "DataSave"
        private const val NOW_TIME = "nowtime"

    }


}
