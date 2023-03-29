package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Kosciusko County, IN
 */
public class INKosciuskoCountyParser extends DispatchOSSIParser {

  public INKosciuskoCountyParser() {
    super(CITY_CODES, "KOSCIUSKO COUNTY", "IN",
           "( CANCEL COUNTY? | FYI CALL ) COUNTY? ( CITY ADDR | ADDR! ( COUNTY2 | CITY APTPLACE | APTPLACE? CITY/Y ) INFO+ )");
  }

  @Override
  public String getFilter() {
    return "CAD@co.marshall.in.us,CAD@kcgov.local,CAD@kcgov.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }


  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (subject.equals("Text Message")) {
      if (!body.startsWith("CAD:")) body = "CAD:" + body;
    }
    else if (subject.length() > 0 && body.startsWith("CAD:;")) {
      body = "CAD:" + subject + body.substring(3);
    }
    if (!super.parseMsg(body, data)) return false;

    // A city starting with a digit probably means this is a Marshall County page
    // In any case we don't want to accept it
    if (data.strCity.length() > 0 && Character.isDigit(data.strCity.charAt(0))) return false;

    // Rule out a special Douglas County construct that might slip through
    if (data.strCall.equals("CANCEL") &&
        data.strCity.length() == 0 &&
        data.strPlace.length() == 0 &&
        CITY_CODE_PTN.matcher(data.strApt).matches()) return false;
    return true;
  }
  private static final Pattern CITY_CODE_PTN = Pattern.compile("[A-Z]{4}");

  @Override
  public Field getField(String name) {
    if (name.equals("COUNTY")) return new MyCountyField();
    if (name.equals("COUNTY2")) return new CityField("[A-Z ]+ CO", true);
    if (name.equals("APTPLACE")) return new MyAptPlaceField();
    return super.getField(name);
  }

  private static final Pattern COUNTY_PTN = Pattern.compile("1 ([A-Z ]+) CO\\b *(.*)");
  private class MyCountyField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = COUNTY_PTN.matcher(field);
      if (!match.matches()) return false;
      String city = match.group(2).trim();
      if (city.length() == 0) city = match.group(1).trim() + " COUNTY";
      data.strCity = city;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern APT_MARK_PTN = Pattern.compile("\\(S\\) \\(N\\)|\\([NS]\\)");
  private class MyAptPlaceField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_MARK_PTN.matcher(field);
      if (match.find()) {
        data.strPlace = field.substring(match.end()).trim();
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = EXT_PTN.matcher(addr).replaceAll("EXD");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern EXT_PTN = Pattern.compile("(?<=CENTER ST )EXT\\b");

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "952",  "MARION",
      "AKRN", "AKRON",
      "ATW",  "ATWOOD",
      "BREM", "BREMEN",
      "BRK",  "BURKET",
      "CLAY", "CLAYPOOL",
      "COL",  "COLUMBIA CITY",
      "CROM", "CROMWELL",
      "EG",   "ETNA GREEN",
      "FTW",  "FT WAYNE",
      "GOSH", "GOSHEN",
      "KIM",  "KIMMELL",
      "LAR",  "LARWILL",
      "LEES", "LEESBURG",
      "M",    "MONTONE",
      "MEN",  "MENTONE",
      "MENT", "MENTONE",
      "MILF", "MILFORD",
      "NAPP", "NAPPANEE",
      "N",    "NORTH WEBSTER",
      "NMAN", "NORTH MANCHESTER",
      "NW",   "NORTH WEBSTER",
      "PI",   "PIERCETON",
      "PIE",  "PIERCETON",
      "PIER", "PIERCETON",
      "PLY",  "PLYMOUTH",
      "ROA",  "ROANN",
      "ROCH", "ROCHESTER",
      "SID",  "SIDNEY",
      "SL",   "SILVER LAKE",
      "SWHT", "SOUTH WHITLEY",
      "S",    "SYRACUSE",
      "SYR",  "SYRACUSE",
      "SYRA", "SYRACUSE",
      "TIPP", "TIPPECANOE",
      "W",    "WARSAW",
      "WA",   "WARSAW",
      "WAR",  "WARSAW",
      "WL",   "WINONA LAKE"
  });
}
