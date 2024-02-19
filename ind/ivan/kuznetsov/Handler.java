package ind.ivan.kuznetsov;

import java.time.Duration;

public interface Handler {
    Duration timeout();
  
    void performOperation();
  }
