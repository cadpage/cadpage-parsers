package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCCumberlandCountyBParser extends DispatchOSSIParser {

  public NCCumberlandCountyBParser() {
    super(CITY_CODES, "CUMBERLAND COUNTY", "NC",
          "( CANCEL ADDR CITY! PLACEX1 END " +
          "| ADDR CALL CH? PLACEX2+? UNIT! INFO/N+? ( GPS | GPS1 GPS2 ) PHONE END )");
  }

  @Override
  public String getFilter() {
    return "CAD@co.cumberland.nc.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SRC_PTN = Pattern.compile("Station .*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) {
      if (!SRC_PTN.matcher(subject).matches()) return false;
      data.strSource = subject;
      body = body.replace('\n', ';');
    }
    return super.parseMsg("CAD:" + body, data);
  }

  @Override
  public String getProgram() {
    return "SRC? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new MyCancelField();
    if (name.equals("PLACEX1")) return new MyPlaceX1Field();
    if (name.equals("PLACEX2")) return new MyPlaceX2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CH"))  return new ChannelField("(?:CC|FF)\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("GPS")) return new MyGPSField(0);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("PHONE")) return new PhoneField("\\d{10}", true);
    return super.getField(name);
  }

  private static final Pattern CANCEL_PTN = Pattern.compile("(?:\\{(\\S+)\\} *)?(CANCEL.*|SCENE SECURE|SLOW UNITS TO ROUTINE|UNDER CONTROL|UNIT WILL STAGE|TAC .*)");
  private class MyCancelField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CANCEL_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = getOptGroup(match.group(1));
        String call = match.group(2);
        if (call.startsWith("TAC ")) {
          data.strChannel = call;
        } else {
          data.strCall = call;
        }
        return true;
      }

      // We will (reluctantly) accept an empty field only if there is recognized city field
      // two places ahead
      if (field.length() == 0 && isCity(getRelativeField(+2))) return true;

      return false;
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }

  private static final Pattern PLACEX_UNIT_PTN = Pattern.compile("[A-Z]\\d{1,3}");
  private class MyPlaceX1Field extends Field {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = append(p.getOptional("(S)"), " - ", p.getOptional("(N)"));
      field = p.get();

      if (field.length() > 0) {
        if (PLACEX_UNIT_PTN.matcher(field).matches()) {
          data.strUnit = field;
        } else if (NUMERIC.matcher(field).matches()) {
          data.strApt = field;
        } else {
          data.strPlace = append(data.strPlace, " - ", field);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT? UNIT?";
    }
  }

  private class MyPlaceX2Field extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.contains(",")) {
        data.strName = field;
      } else if (field.contains("RAMP") || field.contains("EXIT") || isValidCrossStreet(field)) {
        data.strCross = field;
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "NAME PLACE X";
    }

  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}(?:[A-Z]\\d{1,2}[A-Z]?)?) +(.*)");
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCode = match.group(1);
      data.strCall = match.group(2);
      return true;
    }

    @Override
    public void parse(String field,  Data data) {
      if (checkParse(field, data)) return;
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[,A-Z0-9]+");
  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!UNIT_PTN.matcher(field).matches()) return false;
      StringBuilder src = new StringBuilder();
      StringBuilder unit = new StringBuilder(data.strUnit);
      for (String token : field.split(",")) {
        StringBuilder sb = (token.startsWith("ST") ? src : unit);
        if (sb.length() > 0) sb.append(',');
        sb.append(token);
      }
      if (src.length() > 0) data.strSource = src.toString();
      data.strUnit = unit.toString();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private static final Pattern FULL_GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}, *[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(type == 0 ? FULL_GPS_PTN : GPS_PTN, true);
    }
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FAY", "FAYETTEVILLE",
      "HM",  "HOPE MILLS",
      "PAR", "PARKTON",
      "ROS", "ROSEBORO",
      "STD", "STEDMAN",
      "WA",  "WADE"

  });

}
