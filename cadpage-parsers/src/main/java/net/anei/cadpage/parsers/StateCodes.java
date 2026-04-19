package net.anei.cadpage.parsers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StateCodes {

  public static boolean isStateCode(String state) {
    return stateCodes.contains(state);
  }

  private static final Set<String> stateCodes = new HashSet<>(Arrays.asList(
      "AL", "AK", "AZ", "AR", "AS", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA",
      "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC",
      "ND", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
  ));

}
