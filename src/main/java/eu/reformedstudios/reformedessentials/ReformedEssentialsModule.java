package eu.reformedstudios.reformedessentials;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import eu.reformedstudios.reformedessentials.cache.ICache;
import eu.reformedstudios.reformedessentials.cache.MessageCache;
import eu.reformedstudios.reformedessentials.util.IMessageUtil;
import eu.reformedstudios.reformedessentials.util.MessageUtil;

public class ReformedEssentialsModule extends AbstractModule {

   @Override
   protected void configure() {

      bind(new TypeLiteral<ICache<String>>() {
      })
         .annotatedWith(Names.named("message-cache"))
         .toInstance(new MessageCache());
      bind(IMessageUtil.class).toInstance(new MessageUtil());
   }
}
