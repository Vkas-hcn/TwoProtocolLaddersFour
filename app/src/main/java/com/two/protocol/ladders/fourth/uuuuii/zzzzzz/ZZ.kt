package com.two.protocol.ladders.fourth.uuuuii.zzzzzz

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.net.VpnService
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceDataStore
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager
import com.github.shadowsocks.preference.DataStore
import com.github.shadowsocks.preference.OnPreferenceDataStoreChangeListener
import com.github.shadowsocks.utils.Key
import com.google.gson.Gson
import com.two.protocol.ladders.fourth.R
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.databinding.ActivityMainBinding
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.DataUser.getSaturnImage
import com.two.protocol.ladders.fourth.uuutt.GlobalTimer
import com.two.protocol.ladders.fourth.uuutt.NetOnInfo
import com.two.protocol.ladders.fourth.uuutt.VPNGet
import com.two.protocol.ladders.fourth.uuutt.VPNGet.shareText
import com.two.protocol.ladders.fourth.uuutt.VpnServerBean
import com.two.protocol.ladders.fourth.uuuuii.eeedd.EE
import com.two.protocol.ladders.fourth.uuuuii.lll.LL
import de.blinkt.openvpn.api.ExternalOpenVPNService
import de.blinkt.openvpn.api.IOpenVPNAPIService
import de.blinkt.openvpn.api.IOpenVPNStatusCallback
import de.blinkt.openvpn.core.ICSOpenVPNApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.system.exitProcess

class ZZ : AppCompatActivity(),
    ShadowsocksConnection.Callback,
    OnPreferenceDataStoreChangeListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var requestPermissionForResultVPN: ActivityResultLauncher<Intent?>
    val connection = ShadowsocksConnection(true)
    var mService: IOpenVPNAPIService? = null
    private var stopConnect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
        intVpn()
        activeFun()
        backFun()
        NetOnInfo.showDueDialog(this)
    }

    private fun intVpn() {
        bindService(
            Intent(this, ExternalOpenVPNService::class.java),
            mConnection,
            BIND_AUTO_CREATE
        )
        requestPermissionForResultVPN =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                requestPermissionForResult(it)
            }
        connection.connect(this, this)
        DataStore.publicStore.registerChangeListener(this)
        initializeServerData()
    }

    fun limitClickActions(nextFun: () -> Unit) {
        if (isConnecting()) {
            Toast.makeText(this@ZZ, "Connecting... Please wait", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (isDisConnecting()) {
            stopConnect = true
            setTypeService(2)
        }
        nextFun()
    }

    private fun activeFun() {
        binding.linNav.setOnClickListener {

        }
        binding.viewGuideLottie.setOnClickListener {

        }
        binding.tvPrivacyPolicy.setOnClickListener {
            startActivity(
                Intent(
                    "android.intent.action.VIEW", Uri.parse(DataUser.ppUrl)
                )
            )
        }
        binding.tvShare.setOnClickListener {
            shareText(
                "https://play.google.com/store/apps/details?id=${this.packageName}",
                "Share App"
            )
        }
        binding.linList.setOnClickListener {
            limitClickActions {
                startActivity(Intent(this@ZZ, LL::class.java))
            }
        }
        binding.mainMenu.setOnClickListener {
            limitClickActions {
                binding.dlHome.open()
            }
        }
        binding.imgDisconnect.setOnClickListener {
            activeTONextVpn()
        }
        binding.imgConnect.setOnClickListener {
            activeTONextVpn()
        }
        binding.laGuide.setOnClickListener {
            activeTONextVpn()
        }
        binding.tvAuto.setOnClickListener {
            limitClickActions {
                if (DataUser.protocolValueKey != "1" && ZZZ.saoState) {
                    VPNGet.switchingDialog(this) {
                        setShowProtocol(1)
                        activeTONextVpn()
                    }
                    return@limitClickActions
                }
                setShowProtocol(1)
            }
        }
        binding.tvSs.setOnClickListener {
            limitClickActions {
                if (DataUser.protocolValueKey != "2" && ZZZ.saoState) {
                    VPNGet.switchingDialog(this) {
                        setShowProtocol(2)
                        activeTONextVpn()
                    }
                    return@limitClickActions
                }
                setShowProtocol(2)
            }
        }
        binding.tvOpen.setOnClickListener {
            limitClickActions {
                if (DataUser.protocolValueKey != "3" && ZZZ.saoState) {
                    VPNGet.switchingDialog(this) {
                        setShowProtocol(3)
                        activeTONextVpn()
                    }
                    return@limitClickActions
                }
                setShowProtocol(3)
            }
        }
    }

    private fun backFun() {
        onBackPressedDispatcher.addCallback(this) {
            if (binding?.viewGuideLottie?.isVisible == true) {
                stopConnect = true
                activeGuide(false)
                return@addCallback
            }
            if (isConnecting()) {
                Toast.makeText(this@ZZ, "Connecting... Please wait", Toast.LENGTH_SHORT)
                    .show()
                return@addCallback
            }
            if (isDisConnecting()) {
                stopConnect = true
                setTypeService(2)
                return@addCallback
            }
            finish()
            exitProcess(0)
        }
    }

    private fun isConnecting(): Boolean {
        return ZZZ.saoClick == 0 && ZZZ.nowVpnUI == 1
    }

    private fun isDisConnecting(): Boolean {
        return ZZZ.saoClick == 2 && ZZZ.nowVpnUI == 1
    }

    private fun setShowProtocol(data: Int) {
        binding.showProtocol = data
        DataUser.protocolValueKey = data.toString()
    }

    private fun activeTONextVpn() {
        activeGuide(false)
        if (VPNGet.isNetworkConnected(this)) {
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            NetOnInfo.getIPInfo()
        }
        if (NetOnInfo.showDueDialog(this)) {
            return
        }
        if (checkVPNPermission()) {
            startCountdown()
        } else {
            VpnService.prepare(this).let {
                requestPermissionForResultVPN.launch(it)
            }
        }
    }

    private fun checkVPNPermission(): Boolean {
        VpnService.prepare(this).let {
            return it == null
        }
    }

    private fun startCountdown() {
        if (ZZZ.nowVpnUI == 1) {
            return
        }
        activeGuide(false)
        setTypeService(1)
        ZZZ.saoClick = if (ZZZ.saoState) {
            2
        } else {
            0
        }
        stopConnect = false
        startVpn()
    }

    private fun startVpn() {
        VPNGet.getAllData()
        lifecycleScope.launch {
            delay(2000)
            if (stopConnect) {
                return@launch
            }
            if (!ZZZ.saoState) {
                connectHowVpn()
            } else {
                disConnectHowVpn()
            }
        }
    }

    private fun connectHowVpn() {
        if (DataUser.protocolValueKey != "3") {
            Core.startService()
        } else {
            openVTool()
        }
    }

    private fun disConnectHowVpn() {
        Core.stopService()
        mService?.disconnect()
    }

    private fun initData() {
        lifecycleScope.launch(Dispatchers.IO) {
            NetOnInfo.getIPInfo()
        }
        activeGuide(true)
        binding.showProtocol = (DataUser.protocolValueKey ?: "1").toInt()
        binding.dlHome.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        GlobalTimer.onTimeUpdate = { formattedTime ->
            binding.tvDate.text = formattedTime
        }
    }

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        Log.e("TAG", "ss-stateChanged: ${state.name}")
        if (state.name == "Connected") {
            connectEndPage()
            ZZZ.saoState = true
            setTypeService(2)
            showVpnSpeed()
        }
        if (state.name == "Stopped") {
            disConnectEndPage()
            ZZZ.saoState = false
            setTypeService(0)
        }
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        val state = BaseService.State.values()[service.state]
        Log.e("TAG", "ss-初始化: ${state.name}")
        if (state.name == "Connected") {
            activeGuide(false)
            ZZZ.saoState = true
            setTypeService(2)
        }
        if (state.name == "Stopped") {
            ZZZ.saoState = false
            setTypeService(0)
        }
    }

    private val mCallback = object : IOpenVPNStatusCallback.Stub() {
        override fun newStatus(uuid: String?, state: String?, message: String?, level: String?) {
            Log.e("TAG", "open-初始化: ${state}")
            lifecycleScope.launch(Dispatchers.Main) {
                when (state) {
                    "CONNECTED" -> {
                        activeGuide(false)
                        connectEndPage()
                        ZZZ.saoState = true
                        setTypeService(2)
                        showVpnSpeed()
                    }

                    "CONNECTING" -> {
                    }

                    "RECONNECTING" -> {
                        ZZZ.saoState = false
                        mService?.disconnect()
                        setTypeService(0)
                        Toast.makeText(
                            this@ZZ,
                            "Connection failed, please try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    "NOPROCESS" -> {
                        if (ZZZ.saoClick >= 0) {
                            disConnectEndPage()
                            ZZZ.saoState = false
                            setTypeService(0)
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun requestPermissionForResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            activeTONextVpn()
        } else {
            Toast.makeText(
                this,
                "Please give permission to continue to the next step",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onPreferenceDataStoreChanged(store: PreferenceDataStore, key: String) {
        when (key) {
            Key.serviceMode -> {
                connection.disconnect(this)
                connection.connect(this, this)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        connection.bandwidthTimeout = 500
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", "onResume: ${ZZZ.saoState}")
        if (ZZZ.saoState) {
            GlobalTimer.onTimeUpdate = { formattedTime ->
                binding.tvDate.text = formattedTime
            }
        } else {
            binding.tvSpeedDown.text = "0 bit/s"
            binding.tvSpeedUp.text = "0 bit/s"
        }
        initializeServerData()
    }


    override fun onStop() {
        super.onStop()
        if (isConnecting()) {
            stopConnect = true
            setTypeService(0)
            ZZZ.saoClick = -1
            mService?.disconnect()
            Core.stopService()
        }
        if (isDisConnecting()) {
            stopConnect = true
            setTypeService(2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DataStore.publicStore.unregisterChangeListener(this)
        connection.disconnect(this)
        ZZZ.saoClick = -1
        mService?.unregisterStatusCallback(mCallback)
        GlobalTimer.stop()
    }

    private fun initializeServerData() {
        VPNGet.getAllData()
        val vpnNowBean = Gson().fromJson(DataUser.nowServiceKey, VpnServerBean::class.java)
        ProfileManager.getProfile(DataStore.profileId).let {
            if (it != null) {
                ProfileManager.updateProfile(setSkServerData(it, vpnNowBean))
            } else {
                val profile = Profile()
                ProfileManager.createProfile(setSkServerData(profile, vpnNowBean))
            }
        }
        DataStore.profileId = 1L
        updateUiWithServerData()
        if (ZZZ.jumpToHomeType != "0") {
            activeTONextVpn()
            ZZZ.jumpToHomeType = "0"
        }
    }

    private fun setSkServerData(profile: Profile, bestData: VpnServerBean): Profile {
        Log.e("TAG", "Vpn Service information:-ss =${Gson().toJson(bestData)}")
        DataUser.nowServiceKey = Gson().toJson(bestData)
        profile.name = bestData.country_name + "-" + bestData.city
        profile.host = bestData.ip
        profile.password = bestData.password
        profile.method = "chacha20-ietf-poly1305"
        profile.remotePort = bestData.port
        return profile
    }

    private fun updateUiWithServerData() {
        val connectNowVpn = Gson().fromJson(DataUser.nowServiceKey, VpnServerBean::class.java)

        if (connectNowVpn.bestState) {
            binding.imgFlag.setImageResource("Smart".getSaturnImage())
            binding.tvName.text = "Smart"
        } else {
            binding.imgFlag.setImageResource(connectNowVpn.country_name.getSaturnImage())
            binding.tvName.text = "${connectNowVpn?.country_name}-${connectNowVpn.city}"
        }
    }

    private fun setTypeService(
        stateInt: Int
    ) {
        ZZZ.nowVpnUI = stateInt
        Log.e("More", "setupViews: =====${stateInt}")
        if (stateInt == 0) {
            DataUser.timeEdKey = binding.tvDate.text.toString()
            GlobalTimer.stop()
            binding.imgDisconnect.isVisible = true
            binding.imgConnect.isVisible = false
            binding.laConnect.isVisible = false
            binding.imgLight.isVisible = false
            binding.tvDate.text = "00:00:00"
        }

        if (stateInt == 1) {
            binding.imgDisconnect.isVisible = false
            binding.imgConnect.isVisible = false
            binding.laConnect.isVisible = true
        }

        if (stateInt == 2) {
            GlobalTimer.start()
            binding.imgDisconnect.isVisible = false
            binding.imgConnect.isVisible = true
            binding.laConnect.isVisible = false
            binding.imgLight.isVisible = true
        }
    }

    private fun activeGuide(cloneState: Boolean) {
        Log.e(
            "TAG",
            "activeGuide: $cloneState==${DataUser.isCloneGuideKey}==${!ZZZ.saoState}",
        )
        if ((cloneState && DataUser.isCloneGuideKey == "1") && !ZZZ.saoState) {
            binding.laGuide.isVisible = true
            binding.viewGuideLottie.isVisible = true
        } else {
            binding.laGuide.isVisible = false
            binding.viewGuideLottie.isVisible = false
            DataUser.isCloneGuideKey = ""
        }
    }

    private fun openVTool() {
        lifecycleScope.launch(Dispatchers.IO) {
            runCatching {
                val vpnNowBean =
                    Gson().fromJson(DataUser.nowServiceKey, VpnServerBean::class.java)
                Log.e("TAG", "Vpn Service information-open: =${Gson().toJson(vpnNowBean)}")
                val conf = this@ZZ.assets.open("fast_190.ovpn")
                val br = BufferedReader(InputStreamReader(conf))
                val config = StringBuilder()
                var line: String?
                while (true) {
                    line = br.readLine()
                    if (line == null) break
                    if (line.contains("remote 195", true)) {
                        line = "remote ${vpnNowBean.ip} 443"
                    }
                    config.append(line).append("\n")
                }
                Log.e("TAG", "openVTool: $config")
                br.close()
                conf.close()
                mService?.startVPN(config.toString())
            }.onFailure {
            }
        }
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName?,
            service: IBinder?,
        ) {
            mService = IOpenVPNAPIService.Stub.asInterface(service)
            try {
                mService?.registerStatusCallback(mCallback)
            } catch (e: Exception) {
            }
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            mService = null
        }
    }

    fun connectEndPage() {
        if (isConnecting()) {
            startActivity(Intent(this@ZZ, EE::class.java))
        }
    }

    fun disConnectEndPage() {
        if (isDisConnecting()) {
            startActivity(Intent(this@ZZ, EE::class.java))
        }
    }

    fun showVpnSpeed() {
        lifecycleScope.launch {
            while (isActive) {
                if (ZZZ.saoState) {
                    binding.tvSpeedDown.text = DataUser.downValueKey ?: "0 b/s"
                    binding.tvSpeedUp.text = DataUser.upValueKey ?: "0 b/s"
                } else {
                    binding.tvSpeedDown.text = "0 bit/s"
                    binding.tvSpeedUp.text = "0 bit/s"
                }
                delay(500)
            }
        }
    }
}