package eu.reformedstudios.reformedessentials.cache;

public interface ICache<T> {

  public T get(String key);

  public T store(String key, T value);

}
