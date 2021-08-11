package net.anei.cadpage.parsers.WA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class WAClallamCountyBParser extends FieldProgramParser {

  public WAClallamCountyBParser() {
    super("CLALLAM COUNTY", "WA",
          "Call_Time:DATETIME Police_Call_Type:CALL! Fire_Call_Type:CALL/L! Address:ADDRCITY! Common_Name:PLACE! Closest_Intersection:X! " +
              "Additional_Location_Info:INFO! Nature_of_Call:CALL/SDS! Assigned_Units:UNIT! Police_Priority:PRI! Police_Status:SKIP! Fire_Status:SKIP! " +
              "Quadrant:MAP! District:MAP/L! Beat:MAP/L! CFS_Number:SKIP! Primary_Incident:ID! Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "Dispatch@co.clallam.wa.us";
  }

  private static final Pattern DELIM = Pattern.compile("\n| (?=(?:Common Name|Additional Location Info|Police Status|Fire Status|District|Beat):)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Incident")) return false;

    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
