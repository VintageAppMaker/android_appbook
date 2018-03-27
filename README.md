# Android eBook App 

> WebView를 이용한 Android ebook 예제 
> Android PlayStore에 올라간 예제소스 

- [마켓이동](https://play.google.com/store/apps/details?id=com.psw.appbook.android) 

![](androidbook.gif)

1. 초간단 구조설명

   웹뷰를 이용하여 App내의 HTML 파일을 읽어와 보여준다. 메뉴와 웹뷰와의 연동은 [메뉴정보파일](https://github.com/VintageAppMaker/android_appbook/blob/master/app/src/main/assets/desc/menu.txt)

   에 저장하여 관리한다. 


|             메뉴ID              |          페이지개수          |    HTML 파일명 템플릿     |         보여지는 제목          |
| :-----------------------------: | :--------------------------: | :-----------------------: | :----------------------------: |
| Menu Drawler에서 선택된 Menu ID | 해당 메뉴에 포함된 페이지 수 |  읽어올파일명_페이지번호  | 메뉴를 눌렀을 때 보여지는 제목 |
|              lect0              |              2               |           lec1_           |           Android란            |
|            lect0 ID             |         2개의 페이지         | lec1_1.html, lect1_2.html |    선택 시, Android란 표시     |

   ​

2. HTML 저장장소 

    [asset 폴더](https://github.com/VintageAppMaker/android_appbook/tree/master/app/src/main/assets)에 저장. 절대경로는 다음과 같이 처리. 웹뷰에서는 loadurl로 호출.
  ~~~ kotlin 
val PATH = "file:///android_asset/"
  ~~~


3. ​ HTML 만들기 


   1. 원본문서를 MarkDown(md) 문서로 만든다. sublime text나 Typora를 추천함.
   2. 내보내기를 HTML로 한다. 
   3. Android Asset 폴더에 저장한다(네이밍룰에 맞추어서) 
   4. [메뉴정보파일](https://github.com/VintageAppMaker/android_appbook/blob/master/app/src/main/assets/desc/menu.txt)를 고친다. 
   5. Android UI에서 메뉴정보파일의 정보로 메뉴를 추가한다.

   ​

