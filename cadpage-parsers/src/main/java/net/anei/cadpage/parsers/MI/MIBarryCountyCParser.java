package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIBarryCountyCParser extends FieldProgramParser {

  public MIBarryCountyCParser() {
    super("BARRY COUNTY", "MI",
          "Trip_Number:ID! Run_Number:ID/L! Unit:UNIT! Nature:CALL! Calltype:CALL/SDS! Level_of_Service:SKIP! Response_Priority:PRI! PU_Date/Time:DATETIME! " +
              "Pickup_Address:ADDR! Pickup_City,_State,_Zip:CITY_ST! Pickup_Lat,_Lon:GPS! Dropoff_City,_State,_Zip:SKIP! Dispatch_Notes:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "no_reply@rockford.traumasoft.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active 911: New Call")) return false;
    return parseFields(body.split("\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("CITY_ST")) return new MyCityStateField();
    return super.getField(name);
  }

  private static final Pattern CITY_ST_PTN = Pattern.compile("([^,]*), *([A-Z]{2})(?: +\\d{5})?");
  private class MyCityStateField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_ST_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCity = match.group(1).trim();
      data.strState = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
}
