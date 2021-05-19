//Sender: rc.263@c-msg.net
package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDCharlesCountyAParser extends FieldProgramParser {

  @Override
  public String getFilter() {
    return "@c-msg.net,dispatch@ccso.us,@sms.mdfiretech.com";
  }


  public MDCharlesCountyAParser() {
    super("CHARLES COUNTY", "MD",
          "CALL ADDR ( ID | INFO/N+? UNIT_INFO ID ) ID2/D TIME! CH");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  private static final Pattern COUNTY_PATTERN = Pattern.compile("[, ]*\\b([A-Z]{2,3}) CO(?:UNTY)?\\b[, ]*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Check for cancel reason
    String cancel = "";
    if (body.startsWith("Cancel Reason:")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      cancel = body.substring(0,pt).trim();
      body = body.substring(pt+1).trim();
    }

    // Kick out any wayward version B or C messages that wander this way
    if (body.contains("\nmdft.us/")) return false;
    if (body.contains("\nDISTRICT:")) return false;

    // See if this is the new dash delimited field format
    if (!parseFields(body.split(" - "), 4, data)) return false;

    data.strCall = append(cancel, " - ", data.strCall);

    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT_INFO")) return new MyUnitInfoField();
    if (name.equals("ID")) return new IdField("[EFS]\\d{9}", true);
    if (name.equals("ID2")) return new IdField("\\d+", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("\\b(?:APT|RM|ROOM|SUITE|LOT|UNIT)[ #]*(.*)$");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {

      Matcher match = COUNTY_PATTERN.matcher(field);
      if (match.lookingAt()) {
        parseCounty(match.group(1), data);
        field = field.substring(match.end());
      }

      if (field.endsWith("]")) {
        int pt = field.lastIndexOf('[');
        if (pt >= 0) {
          data.strCity = field.substring(pt+1, field.length()-1).trim();
          field = field.substring(0, pt).trim();
        }
      }

      int pt = field.indexOf(',');
      if (pt >= 0) {
        String place = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        match = APT_PTN.matcher(place);
        if (match.find()) {
          String apt = match.group(1);
          if (apt == null) apt = place;
          data.strApt = apt;
          place = place.substring(0,match.start()).trim();
        }
        data.strPlace = place;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CITY ST " + super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[,A-Z0-9]+");
  private class MyUnitInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (UNIT_PTN.matcher(field).matches()) {
        data.strUnit = field;
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " UNIT";
    }
  }

  private void parseCounty(String code, Data data) {
    String city = COUNTY_TABLE.getProperty(code);
    if (city != null) {
      int pt = city.indexOf('/');
      if (pt >= 0) {
        data.strState = city.substring(pt+1);
        city = city.substring(0,pt);
      }
      data.strCity = city;
    } else {
      data.strCity = code + " COUNTY";
    }
  }

  private static Properties COUNTY_TABLE = buildCodeTable(new String[]{
      "CAL", "CALVERT COUNTY",
      "KG",  "KING GEORGE COUNTY/VA",
      "PG",  "PRINCE GEORGES COUNTY",
      "SM",  "ST MARYS COUNTY"
  });
}