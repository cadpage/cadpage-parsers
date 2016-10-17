package net.anei.cadpage.parsers.PA;


import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBParser;




public class PAClarionCountyAParser extends DispatchBParser {
 
  public PAClarionCountyAParser() {
    super(PAClarionCountyParser.CITY_LIST, "CLARION COUNTY", "PA");
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "911-CENTER@oes.clarion.pa.us";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return body.startsWith("911-CENTER:");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    PAClarionCountyParser.fixCity(data);
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT ENTRAPMENT",
      "ACCIDENT HIGH MECHANISM",
      "ACCIDENT INJURIES",
      "ACCIDENT MAJOR INCIDENT",
      "ACCIDENT OTHER HAZARDS",
      "ACCIDENT UNK STATUS",
      "ELECTRICAL HAZARD",
      "ELECTRO/LIGHTNING UNK STATUS",
      "FIRE ALARM",
      "FIRE ALARM COMMERCIAL/INDUST",
      "FUEL SPILL UNKNOWN",
      "GAS LEAK OUTSIDE COMM LINE",
      "PUBLIC SERVICE",
      "REFINERY/TANK FARM/FUEL STORAG",
      "SELF DISPATCH",
      "SERVICE CALL",
      "SMOKE INVEST HEAVY SMOKE",
      "STRUCTURE FIRE",
      "TRAFFIC CONTROL",
      "TRAFFIC /TRANS ACCIDENTS",
      "TREE DOWN",
      "UNCONTAINED HAZMAT",
      "VEHICLE FIRE"
  );
}