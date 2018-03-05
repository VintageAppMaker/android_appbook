package com.psw.AppBook_Android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast

import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //----------------------------------------------------
    // lazy 초기화
    // Kotlin 프로젝트로 시작했으면 XML ID를 바로 변수로 사용할 수 있다.
    // 그런데 java로 만든 프로젝트를
    // kotlin으로 컨버트시 가끔 XML의 위젯 ID를 못가져온다.
    // 해결방법은 있지만, 할 때마다 다르다.
    private val wbMain:  WebView? by lazy{
        findViewById(R.id.wbMain) as WebView
    }

    private val toolbar: Toolbar? by lazy{
        findViewById(R.id.toolbar) as Toolbar
    }

    private val btnNext: ImageView? by lazy{
        findViewById(R.id.btnNext) as ImageView
    }

    private val btnBack: ImageView? by lazy{
        findViewById(R.id.btnBack) as ImageView
    }

    private val txtDesc: TypeWriter? by lazy{
        findViewById(R.id.txtDesc) as TypeWriter
    }
    //----------------------------------------------------

    internal var mMenu = HashMap<Int, MenuData>()
    internal var menuManager = MenuManager()

    internal var bCloseBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar!!.title = "...................."
        setSupportActionBar(toolbar)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        toolbar!!.title = "Kotlin- Learn"

        setUpMenu()
        setUpUI()
    }

    // 메뉴 및 주소연동 설정
    private fun setUpMenu() {
        try {
            mMenu = Util.processMenu(applicationContext, "desc/menu.txt")
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private inner class WebClient : WebViewClient() {

        // URL 호출하기 전...
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.contains("launch")) {
                goGithubUrl(url.replace("launch", "https"))
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        // webpage를 모두 읽었을 때,
        override fun onPageFinished(view: WebView, url: String) {
            val m = menuManager.current ?: return

            txtDesc!!.animateText(m.sDesc + " " + (menuManager.nIndex + 1) + "/" + m.nCount + "   ")
            super.onPageFinished(view, url)
        }
    }

    private fun setUpUI() {
        // 무조건해야 한다. 웹페이지 진행상황을 관리하는 클래스
        wbMain!!.setWebViewClient(WebClient())

        // 세팅을 가져오고 설정한다.
        val set = wbMain!!.settings

        // 자바스크립틀을 사용가능하게 하고 zoom을 false한다.
        set.javaScriptEnabled = true

        set.builtInZoomControls = true
        set.displayZoomControls = false

        // 화면모드
        // 이동한다.
        restoreBookMark()

        btnNext!!.setOnClickListener { menuManager.toNextPage() }
        btnBack!!.setOnClickListener { menuManager.toPrevPage() }
    }

    // 화면을 종료해도 새롭게 시작 시 원복하기 위한 메소드
    private fun restoreBookMark() {
        val nID = Util.getBookMark(applicationContext)

        val m = mMenu[nID]

        if (m == null) {
            menuManager.goMenu(
                    mMenu[Util.getResourceId(applicationContext,
                            "lect0", "id", packageName)]!!)

        } else {
            menuManager.goMenu(m)
        }

    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {

            if (bCloseBack == false) {
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
                bCloseBack = true
            } else {
                super.onBackPressed()
            }

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val m = mMenu[id]

        if (m == null) {
            if (id == R.id.nav_send) {
                sendGitHub()
            }

            if (id == R.id.nav_apps) {
                shareApps()
            }

            if (id == R.id.nav_map) {
                goOffice()
            }


        } else {
            // 현재메뉴를 저장
            Util.saveBookMark(applicationContext, id)
            menuManager.goMenu(m)
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    private fun goGithubUrl(str: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(str))
        startActivity(intent)
    }


    private fun goOffice() {
        val url = "http://www.vintageappmaker.com"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


    private fun shareApps() {

        val marketLaunch = Intent(Intent.ACTION_VIEW)
        marketLaunch.data = Uri.parse("market://search?id=Vintage appMaker")
        startActivity(marketLaunch)
    }

    private fun sendGitHub() {
        val url = "https://github.com/adsloader/AndroidDoc"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    // 메뉴관련 정보관리
    internal inner class MenuManager {
        var lst = ArrayList<MenuData>()
        var nIndex: Int = 0

        val current: MenuData?
            get() = if (lst.size - 1 < 0) null else lst[lst.size - 1]

        fun goMenu(m: MenuData) {

            nIndex = 0

            lst.add(m)
            toolbar!!.title = m.sDesc
            wbMain!!.loadUrl(m.sURL)
        }

        fun toNextPage() {
            if (lst.size - 1 < 0) return

            val m = lst[lst.size - 1]
            if (nIndex < m.nCount - 1) {
                nIndex++
                val sURL = Util.PATH + m.sName + (nIndex + 1) + ".html"
                wbMain!!.loadUrl(sURL)

            }

        }

        fun toPrevPage() {
            if (lst.size - 1 < 0) return

            val m = lst[lst.size - 1]
            if (nIndex > 0) {
                nIndex--
                val sURL = Util.PATH + m.sName + (nIndex + 1) + ".html"
                wbMain!!.loadUrl(sURL)

            }
        }

        fun pop() {
            if (lst.size - 1 < 0) return
            val m = lst[lst.size - 1]
            toolbar!!.title = m.sDesc
            nIndex = 1

            // 제거한다.
            lst.removeAt(lst.size - 1)
        }

    }
}

