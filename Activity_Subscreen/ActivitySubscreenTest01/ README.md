## サブ画面アクティビティの起動方法メモ<!-- omit in toc -->

---
[Home](https://oasis3855.github.io/webpage/) > [Software](https://oasis3855.github.io/webpage/software/index.html) > [ソフトウエア開発・PC管理のメモ帳](https://oasis3855.github.io/webpage/software/software_server_memo.html) > [Workspace_Android](../README.md)  > ***Activity_Subscreen*** (this page)

<br />
<br />

Last Updated : 2025/01/03

- [概要](#概要)
- [ソースコード](#ソースコード)
- [プログラム全体の設定](#プログラム全体の設定)
- [MainActivityでの処理](#mainactivityでの処理)
- [SubActivityでの処理](#subactivityでの処理)

## 概要

メイン・アクティビティからサブ・アクティビティを起動するプログラムのスケルトン。アクティビティ間でのデータの受け渡しはしない。


<br />
<br />

## ソースコード

- [Javaソースコード : MainActivity.java](./app/src/main/java/com/example/activitysubscreentest01/MainActivity.java)
- [レイアウトXML : activity_main.xml](./app/src/main/res/layout/activity_main.xml)
- [Javaソースコード : SubActivity.java](./app/src/main/java/com/example/activitysubscreentest01/SubActivity.java)
- [レイアウトXML : activity_sub.xml](./app/src/main/res/layout/activity_sub.xml)

- [AndroidManifest.xml](./app/src/main/AndroidManifest.xml)

<br />
<br />

## プログラム全体の設定

AndroidManifest.xml でSubActivityを登録する必要がある。 

なお、actionタグでアプリのエントリーポイントとなるアクティビティを指定し、categoryタグでシステムのアプリ一覧に登録するかを指定している。

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".SubActivity"></activity>
    </application>

</manifest>
```

<br />
<br />

## MainActivityでの処理

Activityの起動方法

```java
Intent intent = new Intent(this, SubActivity.class);
startActivity(intent);
```

<br />
<br />

## SubActivityでの処理

SubActivityのクラスファイル（SubActivity.java）の新規作成は、リソースファイルの ```tools:context=".SubActivity..."```の記述部分にエラー表示されているところでコンテキストメニューを表示し、「クラスファイルの新規作成」を指示すれば自動的に作成される。

ただし、クラスはActivityから派生されているので、必要であればこの部分をAppCompatActivityからの派生に書き換えればよい。（下の例はAppCompatActivityに書き換えたもの）

SubActivityの画面を閉じるのは this.finish() メソッド。

```java
public class SubActivity extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // xmlからレイアウトリソースを読み込む
        setContentView(R.layout.activity_sub);

        // 閉じるボタン
        findViewById(R.id.buttonCloseSubActivity).setOnClickListener(this);
    }

    // *****
    // 閉じるボタンを押したときの処理（このアクティビティを終了する）
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonCloseSubActivity) {
            // このActivityを閉じる（呼び出し元のActivity画面に戻る）
            this.finish();
        }
    }
}
```