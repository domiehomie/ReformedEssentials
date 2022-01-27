package eu.reformedstudios.reformedessentials.enums;

public enum MaintenanceReason {
	CRITICAL_BUG,
	UPDATE,
	OTHER;

	@Override
	public String toString() {
		return this.name();
	}
}
