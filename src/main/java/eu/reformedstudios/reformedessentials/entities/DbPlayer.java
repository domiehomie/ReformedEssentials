package eu.reformedstudios.reformedessentials.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import org.bson.types.ObjectId;

import java.util.UUID;

@Entity("reformedessentials:player")
public class DbPlayer {

	@Id
	private ObjectId id;
	@Property("uuid")
	String uuid;
	@Property("clearInventoryConfirm")
	boolean clearInventoryConfirm = true;

	public DbPlayer(UUID uuid) {
		this.uuid = uuid.toString();
	}

	public void setClearInventoryConfirm(boolean clearInventoryConfirm) {
		this.clearInventoryConfirm = clearInventoryConfirm;
	}

	public boolean isClearInventoryConfirm() {
		return clearInventoryConfirm;
	}


}
