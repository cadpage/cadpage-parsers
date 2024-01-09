package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;
/**
 * Parker County, TX (B)
 */
public class TXParkerCountyBParser extends DispatchOSSIParser {

  public TXParkerCountyBParser() {
    super(CITY_CODES, "PARKER COUNTY", "TX",
          "( SELECT/STATUS UNIT CALL ADDR CITY/Y CALL2 " +
          "| CANCEL ADDR CITY/Y " +
          "| UNIT? CH? CALL PLACE? ADDR/Z X/Z+? CITY/Y! SRC? MAP? PRI? " +
          ") INFO/N+? GPS1 GPS2");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Text Message");
    if (body.contains(",Enroute,")) {
      setSelectValue("STATUS");
      return parseFields(body.split(","), data);
    } else {
      if (!body.startsWith("CAD:"))  body = "CAD:" + body;
      setSelectValue("");
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("CANCEL")) return new BaseCancelField("EXTRICATION STARTED|SECOND TONES|WORKING FIRE DECLARED");
    if (name.equals("CH")) return new ChannelField("TAC\\d*|WP?FD", true);
    if (name.equals("UNIT")) return new UnitField("(?!TAC)(?:\\b[A-Z]+\\d+\\b,?)+", true);
    if (name.equals("SRC")) return new SourceField("ST\\d+|TC\\d+", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("MAP")) return new MapField("\\d{4}", true);
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " for ", field);
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // The place field is optional, and determining if it
      // exists is tricky.  Whether it exists or not, it must
      // be followed by and address, zero to two cross streets,
      // and a city code.   So....

      // If we have everything, the city field will be 4 places ahead
      // of us and we must have a place field
      boolean result;
      if (isCity(getRelativeField(+4))) {
        result = true;
      }

      // Conversely, if the next field is a city, this field has to be
      // the address
      else if (isCity(getRelativeField(+1))) {
        result = false;
      }

      // OK, now we have to look for identifiable addresses.  If this
      // field is any kind of address, it cannot possibly be a place field
      else if (isValidAddress(field)) {
        result = false;
      }

      // If the next field is a simple street name, we will assume this one
      // is a mangled address
      else if (checkAddress(getRelativeField(+1)) == STATUS_STREET_NAME) {
        result = false;
      }

      // Otherwise, assume this is a place field
      else {
        result = true;
      }

      // If we have decided this is a place name, parse it
      if (result) super.parse(field, data);
      return result;
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Radio Channel:")) {
        if (data.strChannel.isEmpty()) data.strChannel = field.substring(14).trim();
        return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH? " + super.getFieldNames();
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}");

  private class MyGPSField extends GPSField {

    int endCount;

    public MyGPSField(int type) {
      super(type);
      endCount = 3-type;
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField(endCount)) return false;
      if (!GPS_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALD", "ALEDO",
      "AZL", "AZLE",
      "CRE", "CRESSON",
      "FTW", "FORT WORTH",
      "GRA", "GRANBURY",
      "LIP", "LIPAN",
      "MIL", "MILLSAP",
      "PERR","PERRIN",
      "POL", "POOLVILLE",
      "SPT", "SPRINGTOWN",
      "WFD", "WEATHERFORD",

      "BOYD", "BOYD"
  });
}
