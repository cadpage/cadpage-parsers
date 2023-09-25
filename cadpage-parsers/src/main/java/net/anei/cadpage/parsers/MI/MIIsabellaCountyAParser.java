package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Eaton County, MI
 */
public class MIIsabellaCountyAParser extends DispatchOSSIParser {

  public MIIsabellaCountyAParser() {
    super(CITY_CODES, "ISABELLA COUNTY", "MI",
           "( UNIT ENROUTE ADDR CITY CALL/SDS " +
           "| ( CANCEL | FYI? CALL ) ( ADDR/Z APT/Z APT/Z CITY! | ADDR/Z APT/Z CITY! | ADDR/Z CITY! | ADDR/S! ) ( X  X? | PLACE X X? | ) INFO/N+? ID UNIT " +
           ") END");
    setupCityValues(CITY_CODES);
  }

  @Override
  public String getFilter() {
    return "cad@isabellacounty.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.contains(",Enroute,")) {
      data.msgType = MsgType.GEN_ALERT;
      body = body.replace(',', ';');
    }
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ENROUTE")) return new CallField("Enroute");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("\\d{1,6}");
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*?) *-+ *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        String place = match.group(2);
        if (isCity(place)) {
          String tmp = CITY_CODES.getProperty(place);
          if (tmp != null) place = tmp;
          data.strCity = place;
        } else {
          data.strPlace = place;
        }
      }
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) *(.*)|\\d{1,4}[A-Z]?|[A-Z]|BLDG.*|.* FL .*", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String tmp = match.group(1);
        if (tmp != null) field = tmp;
        data.strApt = append(data.strApt, "-", field);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE?";
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
      "BEAV",  "BEVERTON",
      "BLA",   "BLANCHARD",
      "BRY",   "BARRYTON",
      "CLM",   "COLEMAN",
      "CLR",   "CLARE",
      "EDM",   "EDMORE",
      "FRW",   "FARWELL",
      "LKE",   "LAKE",
      "LKI",   "LAKE ISABELLA",
      "LMS",   "LOOMIS",
      "LVW",   "LAKEVIEW",
      "MP",    "MT PLEASANT",
      "RMS",   "REMUS",
      "RSB",   "ROSEBUSH",
      "RVD",   "RIVERDALE",
      "SHP",   "SHEPHERD",
      "STL",   "ST LOUIS",
      "VEST",  "VESTABURG",
      "WDM",   "WEIDMAN",
      "WNN",   "WINN",

      "CLARE",           "CLARE COUNTY",
      "MECOSTA",         "MECOSTA COUNTY",
      "MIDLAND",         "MIDLAND COUNTY",

      "CLARE CO",        "CLARE COUNTY",
      "MECOSTA CO",      "MECOSTA COUNTY",
      "MIDLAND CO",      "MIDLAND COUNTY",

      "CLARE COUNTY",    "CLARE COUNTY",
      "MECOSTA COUNTY",  "MECOSTA COUNTY",
      "MIDLAND COUNTY",  "MIDLAND COUNTY"

  });
}
