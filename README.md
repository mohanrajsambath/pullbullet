We are always told that a bullet once shot shall hit the target, anyhow.
Well, here we pull it !

Introducing...
PullBullet
----------

Communication between activities, fragments and services in android has always been a very tedious task. PullBullet makes it awesomely simple. All you need is a gun !

A gun in pullbullet can shoot bullets and can also pull them. Every bullet has a unique serial number and also carries a tailtag which could further carry messages.

Every bullet also has a target. But while a bullet is heading towards its target, we can pull it for a while! Wondering how? You'll need a magnet !

A magnet pulls a bullet between its path. But when a magnet has done its work, it has to release the bullet. So that the bullet continues on its path again.

Following code will demonstrate this concept.    

```java
//In your activity
Gun.shoot(911,new TailTag().put("mykey","This is a 911 emergency. Back off, right now."));
//911 is the serial number of your bullet.
//Or you could also use
Bullet bullet = new Bullet(911, new TailTag().put("mykey","Another 911 emergency."));
Gun.shoot(bullet);

//And now in your service

Gun.pull(911,new Magnet(){
	public void onStuck(Bullet bullet){
	String message=bullet.getTailTag().get("mykey").toString();
	Toast.makeText(this,message,Toast.LENGTH_LONG).show();
}
}
```
Amazing! Isn't it?
