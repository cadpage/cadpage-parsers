package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMuskingumCountyDParser extends FieldProgramParser {

  public OHMuskingumCountyDParser() {
    super("MUSKINGUM COUNTY", "OH",
          "Call_Time:DATETIME! Call_Type:CALL! Incident_Number:ID! Narrative:INFO! INFO/N+ First_Name:NAME! Last_Name:NAME/S! " +
          "Location:ADDRCITY! Address:EMPTY! Common_Name:PLACE! Closest_Intersection:X! Units:UNIT! END");
  }

  @Override
  public String getFilter() {
    return "dispatch@ohiomuskingumsheriff.org";
  }

  private static final Pattern DELIM = Pattern.compile("\n|(?=(?:Call Type|Last Name|Common Name):)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Fire Run")) return false;
    body = body.replace("Call Type:", "\nCall Type:");
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
