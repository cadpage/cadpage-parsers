package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class CODouglasCountyBParser extends FieldProgramParser {

  public CODouglasCountyBParser() {
    super("DOUGLAS COUNTY", "CO",
          "MAP ( GPS ADDR2 | ADDR ( GPS | GPS1/d GPS2/d ) APT APT ) PLACE CALL ID! END");
  }

  @Override
  public String getFilter() {
    return "smfrrelay@smfra.com,smtprelay@smfra.onmicrosoft.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MARKER = Pattern.compile("(?:Resp\\.Info|Address Changed): *");
  private static final Pattern MASTER = Pattern.compile("RI:([A-Z]{1,2}-\\d{2}-[A-Z]) +(.*?) +(AURORA|UNINC ARAPAHOE) (.*?) ([A-Z]+\\d+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Metcom Info:") && !subject.equals("SMFR Dispatch Info:")) return false;

    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end());
      return parseFields(body.split("\\|"), data);
    } else if ((match = MASTER.matcher(body)).matches()) {
      setFieldList("MAP ADDR APT CITY CALL UNIT");
      data.strMap = match.group(1);
      parseAddress(match.group(2).trim(), data);
      data.strCity = match.group(3);
      data.strCall =  match.group(4);
      data.strUnit =  match.group(5);

      if (data.strCity.startsWith("UNINC ")) {
        data.strCity = data.strCity.substring(6).trim() + " COUNTY";
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MapField("[A-Z]{1,2}-\\d{2}-[A-Z]|NOT FOUN", true);
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("ID")) return new IdField("(?:Case#)?(\\d{2}-[A-Z]{2}-\\d{2,6})", true);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("(\\d{2,3})(\\d{6}) +(\\d{2,3})(\\d{6})");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      setGPSLoc(match.group(1)+'.'+match.group(2)+','+match.group(3)+'.'+match.group(4), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt >= 0) field = field.substring(pt+1).trim();
      super.parse(field, data);
    }
  }
}
