package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIJacksonCountyBParser extends FieldProgramParser {

  public MIJacksonCountyBParser() {
    super("JACKSON COUNTY", "MI",
          "Address:ADDRCITY! Call_Time:DATETIME! Fire_Call_Type:CALL! Common_Name:PLACE! Narrative:INFO! INFO/N+ Closest_Intersection:X! " +
               "Nature_of_Call:CALL/SDS! Assigned_Units:UNIT! Fire_Priority:PRI! Quadrant:MAP! Primary_Incident:ID! " +
               "Fire_Radio_Channel:CH! Incident_Number:ID/L END");

  }

  @Override
  public String getFilter() {
    return "dispatcher@mijackson.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    if (body.startsWith("Address:")) {
      return parseFields(body.split("\n"), data);
    }
    else {
      return parseFreeFormAlert(body, data);
    }
  }

  private static final Pattern[] FF_PTNS = new Pattern[] {
      Pattern.compile("(?:REQUEST FOR JDART (?:AT )?|.* (?:AT|TO) )(.*?) (?:IN (.*?) )?(?:FOR (?:A )?|-)(.*)"),
      Pattern.compile("JDART REQ FOR (.*?) AT (.*?) IN (.*)")
  };

  private static final boolean[] FF_CALL_FIRST = new boolean[] {
      false,
      true,
  };

  private boolean parseFreeFormAlert(String body, Data data) {
    for (int jj = 0; jj<FF_PTNS.length; jj++) {
      Matcher match = FF_PTNS[jj].matcher(body);
      if (match.matches()) {
        if (FF_CALL_FIRST[jj]) {
          setFieldList("CALL ADDR APT CITY");
          data.strCall = match.group(1).trim();
          parseAddress(match.group(2).trim(), data);
          data.strCity = getOptGroup(match.group(3));
        } else {
          setFieldList("ADDR APT CITY CALL");
          parseAddress(match.group(1).trim(), data);
          data.strCity = getOptGroup(match.group(2));
          data.strCall =  match.group(3).trim();
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PRI")) return new PriorityField("(\\S*)\\s+Fire Status:.*", false);
    return super.getField(name);
  }

}
