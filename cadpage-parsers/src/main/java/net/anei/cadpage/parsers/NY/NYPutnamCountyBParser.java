package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYPutnamCountyBParser extends FieldProgramParser {

  public NYPutnamCountyBParser() {
    super("PUTNAM COUNTY", "NY",
          "DASH? ( MARK DATETIME! Call_Type:CALL! Location:ADDRCITY/S6! Cross_St:X! Common_Name:PLACE! " +
                      "Additional_Location_Information:PLACE/SDS! Quadrant:MAP! Narrative:INFO! " +
                "| ( DATETIME! | TIME ) ( Fire:CALL! EMS:CALL/SLS! | ) " +
                      "( Location:ADDRCITY/S6! Cross_Street:X! Common_Name:PLACE! " +
                      "| CALL ADDRCITY/ZS6 XS:X! PLACE " +
                      "| ADDRCITY/S6! X PLACE " +
                      ") ( Box:BOX! | BOX ) EMS:CALL/SDS? Fire:CALL/SDS? " +
                ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    body = body.replace("=20", "\n").trim()
               .replace("Cross Streets;", "Cross Street:")
               .replace("Cross Streets:", "Cross Street:")
               .replace("Box;", "Box:")
               .replace("Quadrant::", "Quadrant:");
    if (!parseFields(body.split("\\n+"), data)) return false;
    if (data.strCity.equals("OUTSIDE PUTNAM COUNTY")) data.strCity = "OUTSIDE COUNTY";
    return !data.strCall.isEmpty();
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARK")) return new SkipField(".* CAD Page", true);
    if (name.equals("DASH")) return new SkipField("-", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("BOX")) return new BoxField("(?:[A-Z ]+ \\()?(?:\\d\\d|[A-Z]{2})-(?:\\d\\d|[A-Z])\\)?|", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ";");
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_DATE_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d \\w+ - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_DATE_PTN.matcher(field).matches()) return;
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
}
