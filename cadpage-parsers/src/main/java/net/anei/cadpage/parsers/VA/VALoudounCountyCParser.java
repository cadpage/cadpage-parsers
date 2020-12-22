package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VALoudounCountyCParser extends FieldProgramParser {

  public VALoudounCountyCParser() {
    super("LOUDOUN COUNTY", "VA",
          "( SELECT/1 ( CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! MAP:MAP! UNIT:UNIT! INFO:INFO! INFO/N+ B:BOX! ( CROSS_STREETS:X! | LOW_CROSS_STREET:X! HIGH_CROSS_STREET:X! ) RADIO_CHANNELS:CH! FDIDs:SKIP! LATITUDE:GPS1 LONGITUDE:GPS2 BUILDING:APT APARTMENT:APT END " +
                     "| Incident_Number:ID! Incident_Type:CALL! Location:ADDR! City:CITY! Building:APT! Apartment:APT! Box_Area:BOX! Cross_Streets:X! Units_Involved:UNIT! Radio_Channels:CH! Incident_Time:DATETIME3! FDIDs:SKIP! Incident_Location_on_Map:GPS3! ) " +
          "| CALL ADDR ( X2! | APT X2! | APT PLACE X2! ) BOX! Units:UNIT! )");
  }

  @Override
  public String getFilter() {
    return "LCe911@loudoun.gov,@everbridge.net,dispatchpager@webserver.sterlingfire.org,87844,88911,89361";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern MARKER2 = Pattern.compile("([A-Z]{4}-\\d{4}-\\d{8}):");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?: +|(?<! ))(?=TEXT:|PROBLEM:|CAD RESPONSE:|DISPATCH LEVEL:)|(?<=[\\.\\)])(?=\\d{1,2}\\.)");
  private static final Pattern QUAL_PTN = Pattern.compile("^([A-Za-z ]+:) *\\[[^\\]]+\\]");

  @Override
  protected boolean parseMsg(String body, Data data) {

    int pt = body.indexOf("\\n\\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = MARKER2.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("2");
      data.strCallId = match.group(1);
      body = body.substring(match.end()).trim();
      String info = null;
      pt = body.indexOf(". Remarks:");
      if (pt >= 0) {
        info = body.substring(pt+10).trim();
        body = body.substring(0,pt).trim();
      }
      if (!parseFields(body.split(","), data)) return false;
      if (info != null) {
        info = INFO_BRK_PTN.matcher(info).replaceAll("\n");
        data.strSupp = append(data.strSupp, "\n", info);
      }
      return true;
    } else {
      setSelectValue("1");
      body = body.replace(" City:", "\nCity:");
      body = QUAL_PTN.matcher(body).replaceFirst("$1");
      return parseFields(body.split("\n+"), data);
    }
  }

  @Override
  public String getProgram() {
    return "ID? " + super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("X2")) return new CrossField("btwn\\b[ /]*(.*)", true);
    if (name.equals("DATETIME3")) return new MyDateTime3Field();
    if (name.equals("GPS3")) return new MyGPS3Field();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("\\?center=([-+]?\\d{2,3}\\.\\d{6,},[-+]?\\d{2,3}\\.\\d{6,})&");
  private class MyGPS3Field extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.find()) {
        super.parse(match.group(1), data);
      }
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d)T(\\d{1,2}:\\d\\d:\\d\\d)\\.\\d*-0[45]:00");
  private class MyDateTime3Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+':'+match.group(3)+':'+match.group(1);
      data.strTime = match.group(4);
    }
  }
}
