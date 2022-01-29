package eu.reformedstudios.reformedessentials.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import org.bukkit.Location;


@Entity("home")
public class DbHome {

   @Property
   double x;
   @Property
   double y;
   @Property
   double z;
   @Property
   double pitch;
   @Property
   double yaw;
   @Property
   String world;
   @Property
   String name;


   public DbHome() {
   }

   public DbHome(Location l, String name) {
      this.name = name;
      this.x = l.getX();
      this.y = l.getY();
      this.z = l.getZ();
      this.pitch = l.getPitch();
      this.yaw = l.getYaw();
      this.world = l.getWorld().getName();
   }

   public double getX() {
      return x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getZ() {
      return z;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public double getPitch() {
      return pitch;
   }

   public void setPitch(double pitch) {
      this.pitch = pitch;
   }

   public double getYaw() {
      return yaw;
   }

   public void setYaw(double yaw) {
      this.yaw = yaw;
   }

   public String getWorld() {
      return world;
   }

   public void setWorld(String world) {
      this.world = world;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
