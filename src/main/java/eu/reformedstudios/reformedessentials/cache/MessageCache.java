package eu.reformedstudios.reformedessentials.cache;

import java.util.HashMap;
import java.util.Map;

public class MessageCache implements ICache<String> {

  private final Map<String, String> cache;

  public MessageCache() {
    this.cache = new HashMap<>();
  }

  @Override
  public String get(String key) {
    return this.cache.get(key);
  }


  @Override
  public String store(String sender, String target) {
    if (this.cache.containsKey(target))
      return this.cache.replace(target, sender);
    else
      return this.cache.put(target, sender);
  }

}
