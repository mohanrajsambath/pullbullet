We are always told that a bullet once shot shall hit the target, anyhow.
Well...<br/>
Here we pull it!

Introducing...
PullBullet
----------

Communication between activities, fragments and services in android has always been a very tedious task. PullBullet makes it awesomely simple. All you need is a gun !
Gun
---

A gun in pullbullet can shoot bullets and can also pull them. Every bullet has a unique serial number and also carries a tailtag which could further carry messages.

Every bullet also has a target. But while a bullet is heading towards its target, we can pull it for a while! Wondering how? You'll need a magnet !

A magnet pulls a bullet between its path. But when a magnet has done its work, it has to release the bullet. So that the bullet continues on its path again.

Following code will demonstrate this concept.    

```java
//In your activity
Gun.shoot(911,new TailTag().put("mykey", "This is a 911 emergency. Back off, right now."));
//911 is the serial number of your bullet.
//Or you could also use
Bullet bullet = new Bullet(911, new TailTag().put("mykey", "Another 911 emergency."));
Gun.shoot(bullet);

//And plant a magnet in your service before the above shot to pull the bullet.

Gun.pull(911, new Magnet(){

    @Override
	public void onStuck(Bullet bullet){
	String message = bullet.getTailTag().get("mykey").toString();
	Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
```
Amazing! Isn't it?

Well, a gun also has another interesting features, which you could check out at the [wiki][1] section! 

Gun shoots and pulls everywhere within your app. No matter whether it's an activity, fragment, thread or a service, the gun will have it's shot. But...

A Gun fails to shoot bullets between two processes!
So what then? Well, we also have a rifle !!!
Rifle
-----
A rifle is much similar to the gun. Except that it can also shoot between processes, unlike gun. This means, a rifle shoots everywhere!

Rifle uses intents and broadcasts, so it needs a context.
Unlike gun, a rifle is not static. You have to make a new rifle instance for every component of your app. Like a rifle for an activity and another rifle for a service.

```java
//In your activity
Rifle rifle = new Rifle(getApplicationContext());
rifle.shoot(999, new TailTag().put("mykey", "Hi from rifle."));
//Or simply
Bullet bullet = new Bullet(999,new TailTag().put("mykey", "Hey from rifle!"));
rifle.shoot(bullet);

//And in a service running under process :rifle

Rifle rifle = new Rifle(getApplicationContext());
rifle.pull(999, new Magnet(){

    @Override
	public void onStuck(Bullet bullet){
	String message = bullet.getTailTag().get("mykey").toString();
	Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
```
Fascinating, isn't it?
Well, a rifle can do much more than this.
It has 66 power bullets. A power bullet is a bullet with power. You cannot shoot it on your own. A rifle shoots it automatically just for you! For example.

```java
rifle.pull(Bullet.SMS_IN, new Magnet(){

	//Bullet.SMS_IN has a serial of -5. All power bullets have negative serials.

    @Override
	public void onStuck(Bullet bullet){
	String number = bullet.getTailTag().get("number").toString();
	String name = bullet.getTailTag().get("name").toString();
	String message = bullet.getTailTag().get("message").toString();
	String toast = "Got a message "+message+" from "+name+" ("+number+")";
	Toast.makeText(this,toast,Toast.LENGTH_LONG).show();
    }
}
```
For more info about power bullets, head over to the [wiki][1] section.

There is one more type of bullet called as bullet di infinity. Remember, a magnet always releases a bullet when it has done its work? 

When all the magnets have released a bullet and there are no more magnets left on its path, a bullet hits its target ! In case it was fired from a gun, it bounces back with a serial of -1. This is an only power bullet of the gun. Again, if there are magnets listening for serial -1 on the return path, the bullet takes a rest at each magnet and finally it comes back to the gun! But in case of a rifle, the bullet won't bounce back. It is destroyed the moment it hits the target. 

But what does this all have to do with bullet di infinity? You might be wondering. And you are right! It has no target!! It travels infinity!!!

Bullet di infinity
-----------------    
As this bullet has no target, it can be pulled anytime!

```java
Gun.shootInfinity(111, new TailTag().put("mykey","message di infinty."));

//And pull it anywhere, anytime!
Bullet bullet = Gun.pull(111, null);
String message = bullet.getTailTag().get("mykey").toString();
//the message of infinity!!!
```
What to choose? Gun or Rifle?
----
It is noticed that (from the 1000 bullets I have shot) Gun shoots a bit faster than a rifle. Also Gun is more secure than a rifle. So if you have only one process and just communication is all you need, Gun is always preferred over a rifle. But, if you want power, well then a rifle has no choice.

For a detailed documentation of `PullBullet` refer [wiki][1].
Download
---
A jar file is attached with every release of `PullBullet`.

License
=======

	Copyright (C) 2015 Vallabh Shevate

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
[1]: https://github.com/micronic/pullbullet/wiki