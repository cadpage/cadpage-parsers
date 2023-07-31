package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KSOttawaCountyBParser extends FieldProgramParser {

  // Date/time field appears to be missing an AM/PM indicater, so for now we ignore it

  public KSOttawaCountyBParser() {
    super(CITY_LIST, "OTTAWA COUNTY", "KS",
          "Category:CALL! Sub_Category:CALL/SDS! Opened_Date_/_Time:SKIP! Address:ADDR/S! END");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BENNINGTON",
      "CULVER",
      "DELPHOS",
      "MINNEAPOLIS",
      "TESCOTT",

      // Unincorporated communities
      "ADA†",
      "LINDSEY",
      "NILES",
      "SUMNERVILLE",
      "VERDI",
      "WELLS",

      // Ghost town
      "VINE CREEK",

      // Saline County
      "SALINA"
  };

}
