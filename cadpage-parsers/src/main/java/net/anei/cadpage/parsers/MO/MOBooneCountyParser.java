package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Boone County, MO
 */
public class MOBooneCountyParser extends DispatchOSSIParser {

  public MOBooneCountyParser() {
    super(CITY_CODES, "BOONE COUNTY", "MO",
          "( SELECT/2 UNIT CALL ADDR CITY CALL2/S! END " +
          "| CANCEL ADDR CITY! INFO/N+ " +
          "| FYI? DATETIME ID ( MAP ADDR? | ADDR ) PLACE1? ( CODE | PLACE CODE | CITY/Z PLACE CODE | CITY/Z X/Z X/Z CODE | CITY PLACE X X CODE ) CALL1 SRC! UNIT PHONE INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "CAD@boonecountymo.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private String gps = null;

  protected boolean parseMsg(String subject, String body, Data data) {
    gps = null;
    if (body.contains(",Enroute,")) {
      setSelectValue("2");
      return parseFields(body.split(","), data);
    } else {
      setSelectValue("1");
      return parseMsg("CAD:"+body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("MAP")) return new MapField("\\d\\d [A-Z]\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE1")) return new PlaceField("\\(S\\) *(.*?) *\\(N\\)", true);
    if (name.equals("CODE"))  return new CodeField("\\d{1,3}[A-EO]\\d{1,2}[A-Z]?|(?!CHA)[A-Z]{2,3}|TEST", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{2,5}", true);
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9,]+", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.endsWith(" COUNTY")) return false;
      super.parse(field, data);
      return true;
    }
  }

  private class MyCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, data.strCode);
      super.parse(field, data);
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+) +(.*)");
  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (GPS_PTN.matcher(field).matches()) {
        if (gps == null) gps = field;
        else {
          setGPSLoc(gps+','+field, data);
          gps = null;
        }
        return;
      }
      for (String line : field.split("\n")) {
        line = line.trim();
        if (line.startsWith("Radio Channel:")) {
          data.strChannel = field.substring(14).trim();
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CH GPS";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AS",   "ASHLAND",
      "CE",   "CENTRALIA",
      "CL",   "CLARK",
      "CO",   "COLUMBIA",
      "HA",   "HALLSVILLE",
      "HB",   "HARTSBURG",
      "HR",   "HARRISBURG",
      "RO",   "ROCHEPORT",

      "BC",   ""
     });
}
