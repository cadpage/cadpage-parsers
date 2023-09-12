
package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TXKaufmanCountyAParser extends DispatchSouthernParser {

  public TXKaufmanCountyAParser() {
    super(CITY_LIST, "KAUFMAN COUNTY", "TX",
          DSFLG_ADDR|DSFLG_ADDR_NO_IMPLIED_APT|DSFLG_ADDR_TRAIL_PLACE|DSFLG_OPT_APT|DSFLG_OPT_BAD_PLACE|DSFLG_OPT_X|DSFLG_ID|DSFLG_TIME,
          "\\d{3}(?:FD)?|\\d[A-Z]?\\d{3}|[A-Z]\\d{1,3}");
    removeWords("BEND", "CIRCLE", "SQUARE");
  }


  private static final Pattern TIME_MARKER_PTN = Pattern.compile(", *\\d\\d:\\d\\d:\\d\\d,");
  private static final Pattern MARKER = Pattern.compile("Dispatch:|kaufmancotx911:");
  private static final Pattern VZ_PTN = Pattern.compile("\\bVZ(?= ?C[OR]\\b)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);

    // Strip off the old message prefix
    if (match.lookingAt()) {
      body = body.substring(match.end()).trim();
    } else {

      // The prefix was recently dropped, but we still need some way to reject
      // TXKaufmanCountyB alerts.  They are both  based on Southern dispatch format,
      // but TXKaufmanCountyA uses comma delimiters and  TXKaufmanCountyB used
      // semicolon delimiters, so we will check for a properly delimited time field

      if (!TIME_MARKER_PTN.matcher(body).find()) return false;
    }
    int pt = body.indexOf("\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace('@', '&');
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("VZCO") || data.strCity.equals("VAN ZANDT CO")) data.strCity = "VAN ZANDT COUNTY";
    if (data.strCity.length() == 0 && VZ_PTN.matcher(data.strAddress).find()) data.strCity = "VAN ZANDT COUNTY";
    return true;
  }

  private static final Pattern DIR_OF_PTN = Pattern.compile("\\b[NSEW]O +OF\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SVC_RD_NN_PTN = Pattern.compile("\\b(?:SVC|SERVICE) RD (\\d+)", Pattern.CASE_INSENSITIVE);

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new AptField("\\d{1,3}[A-Z]?(?: BUILDING [A-Z])?|[A-Z]", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }


  private static final Pattern ADDR_VZ_COUNTY_PTN = Pattern.compile("\\b(VZ) (CO|COUNTY)\\b");
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field= field.replace(" AT ", " @ ");
      field = field.replace(" CR ", " CO ");
      super.parse(field, data);
      Matcher match = ADDR_VZ_COUNTY_PTN.matcher(data.strAddress);
      if (match.find()) {
        if (data.strCity.length() == 0) data.strCity = "VAN ZANDT COUNTY";
        data.strAddress = match.replaceAll("$2");
      }

      if (data.strCity.length() > 0) {
        String city = MISSPELLED_CITY_TABLE.getProperty(data.strCity.toUpperCase());
        if (city != null) data.strCity = city;
      }
    }
  }

  private static final Pattern INFO_GRID_PTN = Pattern.compile("GRID +(\\d+)\\b *");
  private class MyInfoField extends BaseInfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GRID_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strMap = match.group(1);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "MAP " + super.getFieldNames();
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = DIR_OF_PTN.matcher(addr).replaceAll("&");
    addr = SVC_RD_NN_PTN.matcher(addr).replaceAll("I $1 FRONTAGE RD");
    addr = VZ_PTN.matcher(addr).replaceAll("").trim();
    return super.adjustMapAddress(addr);
  }

  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "GUN BAREL",          "GUN BARREL CITY",
      "GUN BARELL",         "GUN BARREL CITY",
      "GUN BARREL",         "GUN BARREL CITY",
      "GUN BARRELL",        "GUN BARREL CITY",
      "GUN BAREL CITY",     "GUN BARREL CITY",
      "GUN BARELL CITY",    "GUN BARREL CITY",
      "GUN BARRELL CITY",   "GUN BARREL CITY",
      "VAN ZAN CO",         "VAN ZANDT CO",
      "VAN ZAN COUNTY",     "VAN ZANDT COUNTY"
  });

  private static final String[] CITY_LIST = new String[]{

    "ABLES SPRINGS",
    "CANTON",
    "COMBINE",
    "COTTONWOOD",
    "CRANDALL",
    "DALLAS",
    "ELMO",
    "FORNEY",
    "GRAYS PRAIRIE",
    "HEARTLAND",
    "KAUFMAN",
    "KEMP",
    "MABANK",
    "MESQUITE",
    "OAK GROVE",
    "OAK RIDGE",
    "POETRY",
    "POST OAK BEND",
    "POST OAK BEND CITY",
    "ROSSER",
    "SCURRY",
    "SEAGOVILLE",
    "SEVEN POINTS",
    "TALTY",
    "TERRELL",
    "TRAVIS RANCH",

    "DALLAS COUNTY",

    "ELLIS COUNTY",

    "HUNT COUNTY",
    "QUINLAN",

    "HENDERSON COUNTY",
    "GUN BAREL",
    "GUN BARELL",
    "GUN BARREL",
    "GUN BARRELL",
    "GUN BAREL CITY",
    "GUN BARELL CITY",
    "GUN BARREL CITY",
    "GUN BARRELL CITY",

    "KAUFMAN COUNTY",

    "ROCKWALL COUNTY",

    "VAN ZANDT COUNTY",
    "VAN ZANDT CO",
    "VAN ZAN COUNTY",
    "VAN ZAN CO",
    "VZCO"

  };
}
