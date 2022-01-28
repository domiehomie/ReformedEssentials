package eu.reformedstudios.reformedessentials.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity("reformedessentials:player")
public class DbPlayer {

	@Id
	private ObjectId id;
	@Property("uuid")
	String uuid;
	@Property("clearInventoryConfirm")
	boolean clearInventoryConfirm = true;
	@Property("homes")
	List<DbHome> homes;

	public DbPlayer(UUID uuid) {
		this.uuid = uuid.toString();
		this.homes = Collections.emptyList();
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

	public void setClearInventoryConfirm(boolean clearInventoryConfirm) {
		this.clearInventoryConfirm = clearInventoryConfirm;
	}

	public boolean isClearInventoryConfirm() {
		return clearInventoryConfirm;
	}


}
