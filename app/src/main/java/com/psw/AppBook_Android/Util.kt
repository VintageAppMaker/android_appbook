package com.psw.AppBook_Android

import android.content.Context
import android.content.SharedPreferences

import java.io.IOException
import java.io.InputStream
import java.util.HashMap

object Util {


    val PATH = "file:///android_asset/"

    // 메뉴 북마크를 위해 만든 메소드
    val MyPREFERENCES = "MyPrefs"
    val Name = "nameKey"

    fun getResourceId(ctx: Context, pVariableName: String, pResourcename: String, pPackageName: String): Int {
        try {
            return ctx.resources.getIdentifier(pVariableName, pResourcename, pPackageName)
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }

    }

    // menu txt에서 URL과 메뉴리소스 정보를 읽어온다.
    @Throws(IOException::class)
    fun processMenu(ctx: Context, sPageFile: String): HashMap<Int, MenuData> {

        val mMenu = HashMap<Int, MenuData>()

        val `is` = ctx.assets.open(sPageFile)
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()

        // 파일 인코딩이 어떤 상태인지 반드시 알아야 합니다.
        // 설정안하면 괴상한 문자를 만나게 될 것입니다.
        val sText = String(buffer)

        // 문자열파싱 리소스ID|페이지수|파일명
        // 1. \n으로 파싱
        val sLine = sText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in sLine.indices) {
            val s = sLine[i]

            // 2. "|"로 파싱 split 사용할 떄 반드시 \를 입력했어야 했나?
            // 이것이 없으면 안되더라..--;
            val sInfo = s.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val sURL = PATH + sInfo[2] + "1.html"
            val resID = Util.getResourceId(ctx, sInfo[0], "id", ctx.packageName)

            val m = MenuData()
            m.nCount = sInfo[1].toInt()
            m.sName  = sInfo[2]
            m.sURL   = sURL
            m.sDesc  = sInfo[3]
            m.nIndex = i
            mMenu[resID] = m
        }

        return mMenu
    }

    fun saveBookMark(ctx: Context, nID: Int) {
        val editor = ctx.getSharedPreferences(
                MyPREFERENCES, Context.MODE_PRIVATE).edit()
        editor.putInt(Name, nID)
        editor.commit()
    }

    fun getBookMark(ctx: Context): Int {
        val sp = ctx.getSharedPreferences(
                MyPREFERENCES, Context.MODE_PRIVATE)

        return sp.getInt(Name, 0)
    }


}
