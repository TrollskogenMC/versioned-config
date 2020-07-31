package se.hornta.versioned_config.operations;

import se.hornta.versioned_config.Operation;

public class AddOperation<T extends Enum<T>> extends Operation<T> {
  public AddOperation(T id) {
    super(Type.ADD, id);
  }
}
