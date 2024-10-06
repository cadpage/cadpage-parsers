package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PABerksCountyDParser extends FieldProgramParser {

  public PABerksCountyDParser() {
    super("BERKS COUNTY", "PA",
          "Type:CALL! Add:ADDRCITY! X-Sts:X! INFO/N+ NOC:CALL/SDS? Unit:UNIT% DATETIMEGPS% GPS? END");
  }

  @Override
  public String getFilter() {
    return "berksalert@countyofberks.com,89361,99538";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern OPT_MARKER_PTN = Pattern.compile("(Berks County DES|CAD Incident Page): +");
  private static final Pattern MISSING_BRK_PTN = Pattern.compile("(?<!\n)(Add:|NOC:|X-Sts:|Unit:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match = OPT_MARKER_PTN.matcher(body);
    if (match.lookingAt()) {
      subject = "Incident";
      body = body.substring(match.end()).trim();
      body = MISSING_BRK_PTN.matcher(body).replaceAll("\n$1");
    }

    int pt = body.indexOf("\nhttps:");
    if (pt >= 0) {
      data.strInfoURL = body.substring(pt+1);
      body = body.substring(0,pt).trim();
    }

    if (!subject.equals("Incident")) return false;
    body = stripFieldEnd(body, "\n.");
    if (parseFields(body.split("\n"), data)) return true;
    setFieldList("INFO");
    data.parseGeneralAlert(this, body);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " URL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIMEGPS")) return new MyDateTimeGpsField();
    if (name.equals("GPS")) return new MyGpsField();
    return super.getField(name);
  }

  private static final Pattern ADDR_DELIM_PTN = Pattern.compile(" - ");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE) +(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("-")) field += ' ';
      for (String part : ADDR_DELIM_PTN.split(field)) {
        part = part.trim();
        if (data.strAddress.length() == 0) {
          part = part.replace('@', '&');
          super.parse(part, data);
          continue;
        }
        Matcher match = ADDR_APT_PTN.matcher(part);
        if (match.matches()) {
          data.strApt = append(data.strApt, "-", match.group(1));
          continue;
        }
        if (part.startsWith("BLDG ")) {
          data.strApt = append(data.strApt, "-", part);
          continue;
        }
        data.strPlace = append(data.strPlace, " - ", part);
      }

      int pt = data.strAddress.indexOf(" BLDG ");
      if (pt >= 0) {
        data.strApt = append(data.strAddress.substring(pt+1).trim(), "-", data.strApt);
        data.strAddress = data.strAddress.substring(0, pt).trim();
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ";");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_GPS_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)(?:; +(.*))?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeGpsField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
      String gps = match.group(3);
      if (gps != null) setGPSLoc(gps, data);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME GPS";
    }
  }

  private class MyGpsField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override public boolean checkParse(String field, Data data) {
      if (data.strGPSLoc.length() > 0) return false;
      super.parse(field, data);
      return true;
    }
  }


}
