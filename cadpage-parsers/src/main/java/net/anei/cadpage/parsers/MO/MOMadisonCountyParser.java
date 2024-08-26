package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOMadisonCountyParser extends FieldProgramParser {
  public MOMadisonCountyParser() {
    super("MADISON COUNTY", "MO",
          "Event_#:ID! Call_Date:SKIP! Address:ADDR! City:CITY! Cross_Street:X! Phone:PHONE! Police_Type:CALL! Fire_Type:CALL/SLS! EMS_Type:CALL/SLS! " +
              "EMS_Report_#:ID/L! Person_Name:NAME! Business_Name:PLACE! Unit:UNIT! Notes:EMPTY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@valorsystems.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Automated Message")) return false;
    return parseFields(body.split("\n"), data);
  }
}
