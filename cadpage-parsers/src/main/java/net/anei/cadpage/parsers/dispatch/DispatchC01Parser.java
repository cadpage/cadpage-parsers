package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchC01Parser extends FieldProgramParser {

  public DispatchC01Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchC01Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "Agency:SRC! Incident_Date:SKIP! Location:ADDR! Apartment:APT! Location_Name:PLACE! Cross_Steets:X! ITMC_Code:CODE! Inc_Type:CALL! M/C:CALL/SDS! All_Comments:INFO! INFO/N+ Units_Assigned:UNIT! City/Twp:CITY! A/S/B:MAP! Latitude:GPS1! Longitude:GPS2! Incident_Number:ID! Now_Time:DATETIME/d!");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ \\|]*\\b\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d +");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
