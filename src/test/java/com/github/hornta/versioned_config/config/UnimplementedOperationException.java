package com.github.hornta.versioned_config.config;


import org.junit.AssumptionViolatedException;

public class UnimplementedOperationException  extends AssumptionViolatedException
{
  private static final long serialVersionUID = 1L;

  public UnimplementedOperationException ()
  {
    super("Not implemented");
  }

  public UnimplementedOperationException(String message)
  {
    super(message);
  }
}
