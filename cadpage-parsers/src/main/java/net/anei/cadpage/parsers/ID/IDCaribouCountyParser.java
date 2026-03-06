package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA94Parser;

public class IDCaribouCountyParser extends DispatchA94Parser {

  public IDCaribouCountyParser() {
    this("CARIBOU COUNTY", "ID");
  }

  public IDCaribouCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }
}
