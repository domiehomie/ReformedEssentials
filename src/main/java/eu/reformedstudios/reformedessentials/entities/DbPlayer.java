package eu.reformedstudios.reformedessentials.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity("reformedessentials:player")
public class DbPlayer {

  @Id
  ObjectId id;
  @Property("uuid")
  String uuid;
  @Property("clearInventoryConfirm")
  boolean clearInventoryConfirm = true;
  @Property("teleporting")
  boolean allowingTeleports;
  @Property("autoaccepttpa")
  boolean autoAcceptingTpa;
  @Property("socialspy")
  boolean socialspy;
  @Property("messaging")
  boolean acceptingMessages;
  @Property(value = "homes")
  List<DbHome> homes;

  public DbPlayer(UUID uuid) {
    this.uuid = uuid.toString();
    this.homes = Collections.emptyList();
    this.allowingTeleports = true;
    this.autoAcceptingTpa = false;
    this.socialspy = false;
    this.acceptingMessages = true;
  }

  public boolean isAcceptingMessages() {
    return acceptingMessages;
  }

  public void setAcceptingMessages(boolean acceptingMessages) {
    this.acceptingMessages = acceptingMessages;
  }

  public boolean isAllowingTeleports() {
    return allowingTeleports;
  }

  public void setAllowingTeleports(boolean allowingTeleports) {
    this.allowingTeleports = allowingTeleports;
  }

  public boolean isSocialspy() {
    return socialspy;
  }

  public void setSocialspy(boolean socialspy) {
    this.socialspy = socialspy;
  }

  public ObjectId getId() {
    return id;
  }

  public String getUuid() {
    return uuid;
  }

  public List<DbHome> getHomes() {
    return homes;
  }

  public void setHomes(List<DbHome> homes) {
    this.homes = homes;
  }

  public boolean isClearInventoryConfirm() {
    return clearInventoryConfirm;
  }

  public void setClearInventoryConfirm(boolean clearInventoryConfirm) {
    this.clearInventoryConfirm = clearInventoryConfirm;
  }

  public boolean isAutoAcceptingTpa() {
    return autoAcceptingTpa;
  }

  public void setAutoAcceptingTpa(boolean autoAcceptingTpa) {
    this.autoAcceptingTpa = autoAcceptingTpa;
  }

}
