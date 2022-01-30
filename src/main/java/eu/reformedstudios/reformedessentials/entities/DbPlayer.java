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

	@Property("uuid")
	String uuid;
	@Property("clearInventoryConfirm")
	boolean clearInventoryConfirm = true;
	@Property("teleporting")
	boolean allowingTeleports;
	@Property(value = "homes")
	List<DbHome> homes;
	@Id
	private ObjectId id;

	public DbPlayer(UUID uuid) {
		this.uuid = uuid.toString();
		this.homes = Collections.emptyList();
		this.allowingTeleports = true;
	}

	public boolean isAllowingTeleports() {
		return allowingTeleports;
	}

	public void setAllowingTeleports(boolean allowingTeleports) {
		this.allowingTeleports = allowingTeleports;
	}

	public ObjectId getId() {
		return id;
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


}
