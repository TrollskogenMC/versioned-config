package se.hornta.versioned_config.operations;

import se.hornta.versioned_config.Operation;

public class RemoveOperation<T extends Enum<T>> extends Operation<T> {
  public RemoveOperation(T id) {
    super(Type.REMOVE, id);
  }
}
