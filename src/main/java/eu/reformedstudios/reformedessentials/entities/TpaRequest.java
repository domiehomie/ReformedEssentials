package eu.reformedstudios.reformedessentials.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;

@Entity("reformedessentials:tparequests")
public class TpaRequest {

	@Id
	ObjectId id;
	@Property
	String sender;
	@Property
	String target;
	@Property
	boolean active;

	public TpaRequest(Player sender, Player target) {
		this.sender = sender.getUniqueId().toString();
		this.target = target.getUniqueId().toString();
		this.active = true;
	}

	public TpaRequest(Player sender, Player target, boolean active) {
		this.sender = sender.getUniqueId().toString();
		this.target = target.getUniqueId().toString();
		this.active = active;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
