package com.psw.AppBook_Android

/**
 * Created by snake on 17. 3. 26.
 */

data class  MenuData(
    var sURL: String? = null,    // 브라우저 주소
    var sName: String? = null,   // Base가 될 이름
    var sDesc: String? = null,   // 설명
    var nCount: Int = 0,         // 전체카운트
    var nIndex: Int = 0          // 자신의 번호
)
