package com.github.hornta.versioned_config.operations;

import com.github.hornta.versioned_config.Operation;

public class RenameOperation<T extends Enum<T>> extends Operation<T> {
  private final String currentPath;
  private final String newPath;

  public RenameOperation(String currentPath, String newPath) {
    super(Type.RENAME);
    this.currentPath = currentPath;
    this.newPath = newPath;
  }

  public String getCurrentPath() {
    return currentPath;
  }

  public String getNewPath() {
    return newPath;
  }
}
