## Androidプログラミングのメモ<!-- omit in toc -->
---
[Home](https://oasis3855.github.io/webpage/) > [Software](https://oasis3855.github.io/webpage/software/index.html) > [ソフトウエア開発・PC管理のメモ帳](https://oasis3855.github.io/webpage/software/software_server_memo.html) > ***Workspace_Android*** (this page)

<br/>
<br/>

目次
- [アクティビティ間のデータ受け渡し方法のメモ](#アクティビティ間のデータ受け渡し方法のメモ)
- [画面の明るさ設定方法、スリープ抑止の設定方法のメモ](#画面の明るさ設定方法スリープ抑止の設定方法のメモ)
- [DialogFragmentでのダイアログ作成方法メモ](#dialogfragmentでのダイアログ作成方法メモ)
- [ユーザ入力インターフェース（ヴィジェット）のメモ](#ユーザ入力インターフェースヴィジェットのメモ)
- [画面スリープから復帰する方法のメモ](#画面スリープから復帰する方法のメモ)

<br/>
<br/>

### アクティビティ間のデータ受け渡し方法のメモ

アクティビティを利用してサブ画面を起動する方法。また、メイン画面とサブ画面間でのデータ受け渡し方法のメモ

- [サブ・アクティビティ 目次](./Activity_Subscreen/)
  - [サブ・アクティビティを起動する](./Activity_Subscreen/ActivitySubscreenTest01/) (2025/01/03)
  - [サブ・アクティビティとの間でデータの受け渡し](./Activity_Subscreen/ActivitySubscreenTest02/) (2025/01/03)

<br/>
<br/>

### 画面の明るさ設定方法、スリープ抑止の設定方法のメモ

画面の明るさ設定、画面スリープ抑止の方法をアプリ単体・システム全体で設定する方法のメモ

- [ソースコード及び説明文](./Brightness_Sleep/) (2024/12/12)

<br/>
<br/>

### DialogFragmentでのダイアログ作成方法メモ

DialogFragmentを用いてダイアログを作成表示する方法のメモ。呼び出し元のMainActivityと、呼び出される側のDialogFragmentの間で、**Bundleを用いて値を受け渡しする方法**がこの記事のメイントピック

- [ソースコード及び説明文](./DialogFragment/) (2025/01/07)

<br/>
<br/>

### ユーザ入力インターフェース（ヴィジェット）のメモ

AlertDialog（メッセージボックス、テキストボックス、リスト、シークバー等）の作成方法のメモ

- [AlertDialog 目次](./UserInterface_Widget/)
  - [その1） Ok・Yes/Noメッセージボックス、テキストボックス](./UserInterface_Widget/AlertDialogTest01/) (2024/12/31)
  - [その2）リスト、スイッチ、シークバー](./UserInterface_Widget/AlertDialogTest02/) (2025/01/09)
  - [その3） レイアウトをXMLで記述する方法](./UserInterface_Widget/AlertDialogTest03/) (2025/03/02)

<br/>
<br/>

### 画面スリープから復帰する方法のメモ

画面スリープ状態から、照度センサー値に応じスリープ解除し画面点灯する方法のメモ

- [ソースコード及び説明文](./Wakeup_From_Sleep/) (2024/12/19)

