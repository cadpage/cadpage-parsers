package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAFranklinCountyEParser extends FieldProgramParser {

  public PAFranklinCountyEParser() {
    super("FRANKLIN COUNTY", "PA",
          "DATE:DATE/d! TIME:TIME! TYPE:CALL! LOC:ADDRCITYST! LAT/LNG:GPS! UNITS:UNIT! BOX:BOX! REMARKS:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "alerts@apsfha.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Fayetteville VFD Notification")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?-\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("; *([^,]*),");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PLACE_PTN.matcher(field);
      if (match.find()) {
        data.strPlace = match.group(1);
        field = field.substring(0,match.start()) +',' + field.substring(match.end());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY ST";
    }
  }
}
