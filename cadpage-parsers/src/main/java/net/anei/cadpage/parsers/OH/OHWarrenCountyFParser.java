package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWarrenCountyFParser extends FieldProgramParser {

  public OHWarrenCountyFParser() {
    super(OHWarrenCountyParser.CITY_LIST, "WARREN COUNTY", "OH",
          "( Priority:PRI! Problem:CALL! Location:PLACE! Address:ADDR! Apt:APT! Bldg:APT? City:CITY! CrossStreets:X! CallerName:NAME! CallerPhone:PHONE! Zone:MAP? MapPage:MAP/L! RespPlan:LINFO! Units:UNIT! Comments:INFO/N! INFO/N+ https:SKIP " +
          "| PRIORITY:PRI! PROBLEM:CALL! LOCATION:PLACE! ADDRESS:ADDR! APT:APT! BLDG:APT? CITY:CITY! CROSS_STREETS:X! CALLER_NAME:NAME! CALLER_PHONE:PHONE! MAP_PAGE:MAP! RESP_PLAN:LINFO! UNITS:UNIT! COMMENTS:INFO/N! INFO/N+ " +
          "| ( PRI CALL PLACE ADDR CITY " +
            "| CALL PLACE ADDR/Z CITY " +
            "| PLACE ADDR/Z CITY " +
            "| ADDR CITY " +
            ") X NAME PHONE MAP RESP_PLAN UNIT! INFO/N+? BOX ID TIME ADD_RES? CH END " +
            ")");
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z0-9 ,]+|necc|StackHP|Greene Co.*|BC Dispatch-.*");
  @Override
  protected boolean parseMsg(String body, Data data) {

    if (body.contains("\n")) {
      if (body.startsWith("61Priority:")) body = body.substring(2);
      body = body.replace(" - MapPage:", "\nMapPage:");
      if (! parseFields(body.split("\n"), data)) return false;
    } else {
      if (! super.parseMsg(body,  data)) return false;
    }

    // If this was the labeled version, everything is cool
    if (body.startsWith("Priority:") || body.startsWith("PRIORITY:")) return true;

    //  Otherwise make some sanity checks of  different fields
    if (!data.strAddress.contains("&") && !data.strCross.contains("/")) return false;
    if (!UNIT_PTN.matcher(data.strUnit).matches()) return false;

    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("(?:Fire|Law|Medical) .*");
    if (name.equals("RESP_PLAN")) return new MyResponsePlanField();
    if (name.equals("TIME")) return new TimeField("(\\d\\d:\\d\\d)\\b[ \\d]*", true);
    if (name.equals("ADD_RES")) return new SkipField("Addt'l Resources", true);
    return super.getField(name);
  }

  private class MyResponsePlanField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse("RespPlan: " + field, data);
    }
  }
}
