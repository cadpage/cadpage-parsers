package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Kosciusko County, IN
 */
public class INKosciuskoCountyParser extends DispatchOSSIParser {

  public INKosciuskoCountyParser() {
    super(CITY_CODES, "KOSCIUSKO COUNTY", "IN",
          "( CANCEL | FYI? CALL ) ADDR CITY? PLACE? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@co.marshall.in.us,CAD@kcgov.local,CAD@kcgov.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern BAD_TRAIL_DATETIME = Pattern.compile(";\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d$");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    if (BAD_TRAIL_DATETIME.matcher(body).find()) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("PLACE")) return new PlaceField("\\(S\\) *\\(N\\) *(.*)", true);
    return super.getField(name);
  }

  private static final Pattern CITY_CODE_PTN = Pattern.compile("[A-Z]{1,4}|952");

  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!CITY_CODE_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      String city = CITY_CODES.getProperty(field);
      if (city == null) abort();
      data.strCity = city;
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
