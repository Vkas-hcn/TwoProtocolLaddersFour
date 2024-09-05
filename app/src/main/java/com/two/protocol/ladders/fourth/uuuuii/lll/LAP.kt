package com.two.protocol.ladders.fourth.uuuuii.lll

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.two.protocol.ladders.fourth.R
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.DataUser.getSaturnImage
import com.two.protocol.ladders.fourth.uuutt.VpnServerBean

class LAP(private val vpnList: List<VpnServerBean>, private val activity: LL) :
    RecyclerView.Adapter<LAP.AppViewHolder>() {

    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgFlag: AppCompatImageView = view.findViewById(R.id.img_flag)
        val tvCou: TextView = view.findViewById(R.id.tv_cou)
        val imgCheck: AppCompatImageView = view.findViewById(R.id.img_check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lllll_iiiiii_ttttt, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val item = vpnList[position]
        val vpnCharBean = DataUser.nowServiceKey?.let {
            Gson().fromJson(it, VpnServerBean::class.java)
        }

        holder.tvCou.text = if (item.bestState) {
            "Faster Server"
        } else {
            "${item.country_name}-${item.city}-0$position"
        }

        holder.imgFlag.setImageResource(
            if (item.bestState) R.drawable.fast else item.country_name.getSaturnImage()
        )

        val isSele = vpnCharBean?.let { vpnCharBean.ip == item.ip && vpnCharBean.bestState == item.bestState } == true

        holder.imgCheck.setImageResource(
            if (isSele && ZZZ.saoState) R.drawable.list_check else R.drawable.list_dis
        )

        holder.itemView.setOnClickListener {
            if (!(isSele && ZZZ.saoState)) {
                clickFun(item)
            }
        }
    }

    override fun getItemCount(): Int = vpnList.size

    private fun clickFun(jsonBean: VpnServerBean) {
        if (ZZZ.saoState) {
            showDisConnectFun {
                DataUser.chooneServiceKey = Gson().toJson(jsonBean)
                endThisPage()
            }
        } else {
            DataUser.nowServiceKey = Gson().toJson(jsonBean)
            endThisPage()
        }
    }

    private fun endThisPage() {
        activity.finish()
        ZZZ.jumpToHomeType = "1"
    }

    private fun showDisConnectFun(nextFun: () -> Unit) {
        AlertDialog.Builder(activity)
            .setTitle("Tip")
            .setMessage("Whether To Disconnect The Current Connection")
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton("YES") { _, _ -> nextFun() }
            .setNegativeButton("NO", null)
            .show()
    }
}
