package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;


public class OKClevelandCountyAParser extends FieldProgramParser {
  
  public OKClevelandCountyAParser() {
    super("CLEVELAND COUNTY", "OK",
           "ADDR/SC! ESN:ID!");
    setupMultiWordStreets(
        "CROOKED OAK",
        "RED OAK",
        "PECAN CREEK",
        "ROLLING MEADOWS",
        "VALLEY VIEW");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "dispatch.info@normanok.gov";
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "FIRE ALARM RESIDENTIAL",
      "FIRE CONTROL BURN",
      "FIRE GRASS",
      "FIRE MUTUAL AIDE",
      "FIRE RESIDENTIAL",
      "PUBLIC ASSIST",
      "SMOKE INVESTIGATION"
  );
}
