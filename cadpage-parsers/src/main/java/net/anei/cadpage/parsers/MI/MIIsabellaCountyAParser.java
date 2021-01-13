package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Eaton County, MI
 */
public class MIIsabellaCountyAParser extends DispatchOSSIParser {

  public MIIsabellaCountyAParser() {
    super(CITY_CODES, "ISABELLA COUNTY", "MI",
           "( CANCEL | FYI? CALL ) ( ADDR/Z CITY! | ADDR2/S! ) X+? INFO/N+? ID UNIT END");
  }

  @Override
  public String getFilter() {
    return "cad@isabellacounty.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("\\d{1,6}");
    return super.getField(name);
  }

  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('-');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        city = CITY_CODES.getProperty(city);
        if (city != null) {
          data.strCity = city;
          parseAddress(field.substring(0,pt).trim(), data);
          return;
        }
      }
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
    }
  }

  private class MyCrossField extends CrossField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = stripFieldStart(field, "crosses of ");
      return super.checkParse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[\\.,]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strSupp.length() == 0) {
        String cross = field;
        Matcher match = INFO_BRK_PTN.matcher(field);
        int pt = match.find() ? match.start() : -1;
        if (pt >= 0) cross = field.substring(0,pt).trim();
        if (cross.startsWith("crosses of ")) {
          cross = cross.substring(11).trim();
        } else if (cross.endsWith(" as crosses")) {
          cross = cross.substring(0,cross.length()-11).trim();
        } else cross = null;
        if (cross != null) {
          data.strCross = append(data.strCross, " & ", cross);
          if (pt < 0) return;
          field = field.substring(pt+1).trim();
        }
      }
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALMA",  "ALMA",
      "RMS",   "REMUS",
      "BLA",   "BLANCHARD",
      "MP",    "MT PLEASANT",
      "RSB",   "ROSEBUSH",
      "CLR",   "CLARE",
      "SHP",   "SHEPHERD",
      "BEAV",  "BEVERTON",
      "BRY",   "BARRYTON",
      "CLM",   "COLEMAN",
      "EDM",   "EDMORE",
      "FRW",   "FARWELL",
      "LKE",   "LAKE",
      "LKI",   "LAKE ISABELLA",
      "LVW",   "LAKEVIEW",
      "RVD",   "RIVERDALE",
      "STL",   "ST LOUIS",
      "VEST",  "VESTABURG",
      "WDM",   "WEIDMAN",
      "WNN",   "WINN",
      "LMS",   "LOOMIS",

      "MECOSTA CO",      "MECOSTA COUNTY",
      "MIDLAND CO",      "MIDLAND COUNTY"
  });
}
