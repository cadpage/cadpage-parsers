package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MNAnokaCountyBParser extends FieldProgramParser {

  public MNAnokaCountyBParser() {
    super("ANOKA COUNTY", "MN",
          "Call_For_Service_Number:ID! Police_Call_Type:CALL! Fire_Call_Type:CALL/L! Address:ADDRCITY! Common_Name:PLACE! " +
                "City:CITY! Lat:GPS1! Long:GPS2! Caller_Name:NAME! Caller_Phone:PHONE! Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CADExchange@acw-psds.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile("\n;?\n?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Paging")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strCall)) return;
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      super.parse(field, data);
    }
  }
}
