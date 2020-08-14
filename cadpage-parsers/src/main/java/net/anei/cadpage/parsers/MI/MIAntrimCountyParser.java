package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MIAntrimCountyParser extends DispatchOSSIParser {

  public MIAntrimCountyParser() {
    this("ANTRIM COUNTY", "MI");
  }

  MIAntrimCountyParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
          "FYI? CALL PRI? ADDR! ( GPS1 GPS2 CITY! X+? | CITY! X+? | X/Z CITY! | X/Z X/Z CITY! | X+? ) INFO/N+? ( GPS1 GPS2 PRI |  PRI ) END");
  }

  @Override
  public String getFilter() {
    return "CAD@antrimcounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("[1-9P]");
    if (name.equals("GPS1")) return new GPSField(1, "[-+]?\\d{2}\\.\\d{6,}", true);
    if (name.equals("GPS2")) return new GPSField(2, "[-+]?[0-9.]*", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)");
  private static final String DATE_TIME_STR = "NN/NN/NNNN NN:NN:NN";
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      // Look for wayward city codes
      if (data.strCity.length() == 0) {
        String city = CITY_CODES.getProperty(field);
        if (city != null) {
          data.strCity = city;
          return;
        }
      }

      // See if this is a radio channel
      if (field.startsWith("Radio Channel:")) {
        data.strChannel = field.substring(14).trim();
        return;
      }

      // See if this is a date time field
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (match.matches()) {
        if (data.strTime.length() == 0) {
          data.strDate = match.group(1);
          data.strTime = match.group(2);
        }
        return;
      }

      // Or if it is a truncated field at end of message
      else if (getRelativeField(+1).length() == 0) {
        if (DATE_TIME_STR.startsWith(field.replaceAll("\\d", "N"))) return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH DATE TIME " + super.getFieldNames() + " CITY";
    }
  }


  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BEV",   "BELLAIRE",
      "BKT",   "BANKS TWP",
      "CLT",   "CENTRAL LAKE TWP",
      "CLV",   "CENTRAL LAKE",
      "CHT",   "CHESTONIA TWP",
      "CUT",   "CUSTER TWP",
      "ECT",   "ECHO TWP",
      "ELL",   "ELLSWORTH",
      "ERT",   "ELK RAPIDS TWP",
      "ERV",   "ELK RAPIDS",
      "FHT",   "FOREST HOME TWP",
      "HLT",   "HELENA TWP",
      "JDT",   "JORDAN TWP",      // Guess to fill in missing entry
      "KRT",   "KEARNEY TWP",
      "MIT",   "MILTON TWP",
      "MNT",   "MANCELONA TWP",
      "MNV",   "MANCELONA",
      "STT",   "STAR TWP",
      "TLT",   "TORCH LAKE TWP",
      "WRT",   "WARNER TWP",

      // old codes
      "BANK",  "BANKS TWP",
      "CL",    "CENTRAL LAKE",
      "CUST",  "CUSTER TWP",
      "ECHO",  "ECHO TWP",
      "ELLV",  "ELLSORTH",
      "HELE",  "HELENA TWP",
      "JORD",  "JORDAN TWP",
      "KERN",  "KEARNEY TWP",
      "MAN",   "MANCELONA TWP",
      "MANV",  "MANCEOLNA",
      "MILT",  "MILTON TWP",
      "STAR",  "STAR TWP",
      "VBL",   "BELLAIRE",
      "VER",   "ELK RAPIDS",
      "WARN",  "WARNER TWP"
  });
}
