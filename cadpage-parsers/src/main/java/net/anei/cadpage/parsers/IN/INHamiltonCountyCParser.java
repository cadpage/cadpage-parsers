package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INHamiltonCountyCParser extends FieldProgramParser {

  public INHamiltonCountyCParser() {
    super("HAMILTON COUNTY", "IN",
          "NOC:CALL! Status:SKIP! Call_Type:CALL/SDS! Alarm_Level:PRI? Address:ADDRCITY! ( City:CITY! Common_Name:PLACE! Closest_Intersections:X! Quad:MAP! Units:UNIT! | Common_Name:PLACE! City:CITY! Units:UNIT! Quad:MAP! ) Narrative:INFO! CC_Text:CALL/SDS Caller_Statement:INFO/N Dispatch_Code:CODE INFO/N+ Coord:GPS Inc_#:ID END");
  }

  @Override
  public String getFilter() {
    return "dispatch@hamiltoncounty.in.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile("\n+|(?=Common Name:|CC Text:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CITY")) return new  MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
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

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) field = field.substring(0, pt);
      super.parse(field, data);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('/', ',');
      super.parse(field, data);
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) field = field.substring(0, pt).trim();
      super.parse(field, data);
    }
  }
}
