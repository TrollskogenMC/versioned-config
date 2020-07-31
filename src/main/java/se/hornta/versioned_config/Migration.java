package se.hornta.versioned_config;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class Migration<T extends Enum<T>> implements IMigration<T> {
  private final int version;
  private final Function<Snapshot<T>, Patch<T>> applyWithSnapshot;
  private final Supplier<Patch<T>> applyWithoutSnapshot;

  public Migration(int version, Function<Snapshot<T>, Patch<T>> applyWithSnapshot) {
    Objects.requireNonNull(applyWithSnapshot);
    this.version = version;
    this.applyWithSnapshot = applyWithSnapshot;
    this.applyWithoutSnapshot = null;
  }

  public Migration(int version, Supplier<Patch<T>> applyWithoutConfiguration) {
    Objects.requireNonNull(applyWithoutConfiguration);
    this.version = version;
    this.applyWithSnapshot = null;
    this.applyWithoutSnapshot = applyWithoutConfiguration;
  }

  @Override
  public Patch<T> createPatch(Snapshot<T> configuration) {
    Patch<T> patch;
    if(applyWithSnapshot != null) {
      patch = applyWithSnapshot.apply(configuration);
    } else {
      patch = applyWithoutSnapshot.get();
    }
    Objects.requireNonNull(patch);
    return patch;
  }

  @Override
  public int getVersion() {
    return version;
  }
}
