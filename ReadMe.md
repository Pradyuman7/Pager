# Pager 

[![](https://www.jitpack.io/v/Pradyuman7/Pager.svg)](https://www.jitpack.io/#Pradyuman7/Pager)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Pager-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7537)
[![Pager](https://img.shields.io/badge/Pradyuman7-Pager-yellow.svg?style=flat)](https://github.com/Pradyuman7/Pager)
[![AndroidDevDigest](https://img.shields.io/badge/AndroidDevDigest-Pager-darkblue.svg?style=flat)](https://www.androiddevdigest.com/digest-219/)
[![AndroidDevDigest](https://img.shields.io/badge/Awesome_Android-Pager-purple.svg?style=flat)](https://android.libhunt.com/pager-alternatives)
[ ![Download](https://api.bintray.com/packages/pradyuman7/Pager/Pager/images/download.svg?version=V1.1) ](https://bintray.com/pradyuman7/Pager/Pager/V1.0/link)

<p align="center">
  <img width="250" height="250" src="https://user-images.githubusercontent.com/41565823/52990799-2c5b2980-340a-11e9-8e74-d530ee593c01.gif">
</p>

<p align="center">
  🔥 An android library to get simple menu options to 💪. Inspired by <a href="https://github.com/SpecialCyCi/AndroidResideMenu"> AndroidResideMenu</a>.
</p>

## GIF

<p align="left">
  <img width="300" height="550" src="https://user-images.githubusercontent.com/41565823/52993954-3edb6000-3416-11e9-94ef-bf9578abfe7f.gif">
</p>

## AndroidPub(Medium) Post

[You can read the AndroidPub post about this library, the perks it provides and other details here.](https://medium.com/@pradyumandixit/how-to-get-your-menu-options-on-steroids-aef1b5a40862)

## Prerequisites

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```

## Dependency

Add this to your module's `build.gradle` file (make sure the version matches the JitPack badge above):

```gradle
dependencies 
	        implementation 'com.github.Pradyuman7:Pager:V1.1'
	}
```

## Usage

- Add the layout in your XML file for the activity, like this:

```XML
<?xml version="1.0" encoding="utf-8"?>
<merge
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.pd.cards.Main2Activity"
    tools:ignore="all"
    tools:showIn="@layout/activity_main2">

    <com.pd.pager.PagerLayout
        android:id="@+id/rl_main"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center"
        app:num="five"/>
</merge>
```
***

```XMl
<com.pd.pager.PagerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.pd.pager.PagerLayout>
```
***
- Configure the layout and add the fragments like this:

```java
PagerLayout pager = findViewById(R.id.rl_main);

        List<String> titleList = new ArrayList<>();
        titleList.add("Page1");
        titleList.add("Page2");
        titleList.add("Page3");
        titleList.add("Page4");
        titleList.add("Page5");

        if(pager == null)
            Log.i("Pager_Null","Pager is null");


        pager.setTitles(titleList);


        fragments.add(new Page1());
        fragments.add(new Page2());
        fragments.add(new Page3());
        fragments.add(new Page4());
        fragments.add(new Page5());

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        pager.setAdapter(adapter);
```

- See the app code to know more about this.

## Pull Request

Have some new ideas or found a bug? Do not hesitate to open an `issue` and make a `pull request`.

## License

**Pager** is under [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0). See the [LICENSE](LICENSE.md) file for more info.

