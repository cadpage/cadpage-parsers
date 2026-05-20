package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Somerset County, NJ
 */
public class NJSomersetCountyAParser extends FieldProgramParser {

  public NJSomersetCountyAParser() {
    super("SOMERSET COUNTY", "NJ",
          "CALL ADDR ID CITY X PHONE PLACE GPS1 GPS2! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "rc.502@c-msg.net,messaging@iamresponding.com,@co.somerset.nj.us";
  }

  private static final Pattern MARKER2 = Pattern.compile("^(?:(?:/ [A-Z0-9 ]* / )?(?:[A-Za-z]+:)?(?:[[^:]*]:)?)?(?:([^: ]+):)?(\\d{8}) *:(\\d\\d/\\d\\d/\\d\\d\\d\\d) (\\d\\d:\\d\\d:\\d\\d):");
  private static final Pattern MASTER = Pattern.compile("(.*?): ([A-Z ]+)-(?! )(?:(.*) / +)?([^\\(]*)(?:\\((.*)\\))?(?: #\\d+)?");
  private static final Pattern ROUTE_HWY_PTN = Pattern.compile("\\b(STATE|COUNTY|US) (?:ROUTE|ROAD|HWY)(?: NO)? (\\d+)(?: HWY)?\\b");
  private static final Pattern APT_PTN = Pattern.compile(" +((?:APT|FL|FLR|BLDG) +[-A-Z0-9]+)$");

  @Override
  public boolean parseMsg(String body, Data data) {

    if (body.startsWith("Text Message")) {
      body = body.substring(12).trim();
      return parseFields(body.split("\\|"), data);
    }

    Matcher match = MARKER2.matcher(body);
    if (match.lookingAt()) {
      setFieldList("SRC ID DATE TIME CALL CITY PLACE ADDR APT INFO");
      data.strSource = getOptGroup(match.group(1));
      data.strCallId = match.group(2);
      data.strDate = match.group(3);
      data.strTime = match.group(4);
      body = body.substring(match.end()).trim();

      int pt = body.indexOf('\n');
      if (pt >= 0) body = body.substring(0,pt).trim();

      match = MASTER.matcher(body);
      if (!match.matches()) return false;
      data.strCall = match.group(1).trim();
      data.strCity = convertCodes(match.group(2).trim(), CITY_CODES);
      data.strPlace = null2empty(match.group(3));
      String sAddr = match.group(4).trim();
      data.strSupp = null2empty(match.group(5));

      match = ROUTE_HWY_PTN.matcher(sAddr);
      if (match.find()) {
        sAddr = sAddr.substring(0,match.start()) +
                match.group(1).substring(0,2) + " " + match.group(2) +
                sAddr.substring(match.end());
      }
      match = APT_PTN.matcher(sAddr);
      if (match.find()) {
        data.strApt = match.group(1);
        if (data.strApt.startsWith("APT ")) {
          data.strApt = data.strApt.substring(4).trim();
        }
        sAddr = sAddr.substring(0,match.start());
      }
      sAddr = stripFieldEnd(sAddr, " FL %");
      parseAddress(sAddr, data);
      data.strAddress = ROUTE_HWY_PTN.matcher(data.strAddress).replaceFirst("$1 $2");
      return true;
    }
    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(" / ");
      if (pt >= 0) {
        data.strPlace = field.substring(0, pt).trim();
        field = field.substring(pt+3).trim();
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  @Override
  public String adjustMapAddress(String sAddr) {
    sAddr = US_PTN.matcher(sAddr).replaceAll("US");
    sAddr = NO_PTN.matcher(sAddr).replaceAll(" ").trim();
    sAddr = stripFieldEnd(sAddr, " H WY ST");
    return sAddr;
  }
  private static final Pattern US_PTN = Pattern.compile("\\bU +S\\b");
  private static final Pattern NO_PTN = Pattern.compile(" *\\bNO\\b *");

  private String null2empty(String str) {
    return str == null ? "" : str.trim();
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BEDMINSTER", "BEDMINSTER TWP",
      "BERKELEY H", "BERKELEY HEIGHTS TWP",
      "BERNARD",    "BERNARD",
      "BERNARDS T", "BERNARDS TWP",
      "BERNARDSVI", "BERNARDSVILLE",
      "BOUND B",    "BOUND BROOK",
      "BOUND BROO", "BOUND BROOK",
      "BRANCHB",    "BRANCHBURG",
      "BRANCHBURG", "BRANCHBURG TWP",
      "BRIDGEW",    "BRIDGEWATER TWP",
      "BRIDGEWATE", "BRIDGEWATER TWP",
      "CHATHAM BO", "CHATHAM",
      "CHATHAM TW", "CHATHAM TWP",
      "CHESTER TW", "CHESTER TWP",
      "CLINTON TW", "CLINTON TWP",
      "DUNELLEN B", "DUNELLEN",
      "EAST AM",    "EAST AMWELL",
      "EAST AMWEL", "EAST AMWELL TWP",
      "EAST BRUNS", "EAST BRUNSWICK TWP",
      "EDISON TWP", "EDISON TWP",
      "FANWOOD BO", "FANWOOD",
      "FAR HILLS",  "FAR HILLS",
      "FLORHAM PA", "FLORHAM PARK",
      "FRANKLI",    "FRANKLIN TWP",
      "FRANKLIN T", "FRANKLIN TWP",
      "GREEN BROO", "GREEN BROOK TWP",
      "HARDING TW", "HARDING TWP",
      "HELMETTA B", "HELMETTA",
      "HIGHLAND P", "HIGHLAND PARK",
      "HILLSBO",    "HILLSBOROUGH TWP",
      "HILLSBOROU", "HILLSBOROUGH TWP",
      "HOPEWEL",    "HOPEWELL TWP",
      "HOPEWELL B", "HOPEWELL",
      "HOPEWELL T", "HOPEWELL TWP",
      "LONG HILL",  "LONG HILL TWP",
      "MADISON BO", "MADISON",
      "MANVILL",    "MANVILLE",
      "MANVILLE B", "MANVILLE",
      "MENDHAM BO", "MENDHAM",
      "MENDHAM TW", "MENDHAM TWP",
      "METUCHEN B", "METUCHEN",
      "MIDDLESEX",  "MIDDLESEX",
      "MILLSTO",    "MILLSTONE",
      "MILLSTONE",  "MILLSTONE",
      "MILLTOWN B", "MILLTOWN",
      "MONROE TWP", "MONROE TWP",
      "MONTGOM",    "MONTGOMERY TWP",
      "MONTGOMERY", "MONTGOMERY TWP",
      "MOUNTAINSI", "MOUNTAINSIDE",
      "NEW BRUNSW", "NEW BRUNSWICK CITY",
      "NEW PROVID", "NEW PROVIDENCE",
      "NORTH BRUN", "NORTH BRUNSWICK TWP",
      "NORTH PLAI", "NORTH PLAINFIELD",
      "OFFICE OF",  "OFFICE OF EMERGENCY MANAGEMENT",
      "OLD BRIDGE", "OLD BRIDGE TWP",
      "PARK COMMI", "PARK COMMISION",
      "PEAPACK-GL", "PEAPACK-GLADSTONE",
      "PISCATAWAY", "PISCATAWAY TWP",
      "PLAINFIELD", "PLAINFIELD CITY",
      "PRINCET",    "PRINCETON TWP",
      "PRINCETON",  "PRINCETON TWP",
      "PROSECUTOR", "PROSECUTORS OFFICE",
      "RARITAN",    "RARITAN",
      "RARITAN BO", "RARITAN",
      "RARITAN TW", "RARITAN TWP",
      "RARITAN",    "RARITAN",
      "READINGTON", "READINGTON TWP",
      "ROCKY H",    "ROCKY HILL",
      "ROCKY HILL", "ROCKY HILL",
      "SCOTCH PLA", "SCOTCH PLAINS TWP",
      "SOMERSET C", "SOMERSET COUNTY AGENCIES",
      "SOMERVI",    "SOMERVILLE",
      "SOMERVILLE", "SOMERVILLE",
      "SOUTH B",    "SOUTH BOUND BROOK",
      "SOUTH BOUN", "SOUTH BOUND BROOK",
      "SOUTH BRUN", "SOUTH BRUNSWICK TWP",
      "SOUTH PLAI", "SOUTH PLAINFIELD",
      "SOUTH RIVE", "SOUTH RIVER",
      "SPOTSWOOD",  "SPOTSWOOD",
      "SUMMIT CIT", "SUMMIT CITY",
      "TEWKSBURY",  "TEWKSBURY TWP",
      "WARREN TWP", "WARREN TWP",
      "WASHINGTON", "WASHINGTON TWP",
      "WATCHUNG B", "WATCHUNG",
      "WESTFIELD",  "WESTFIELD TOWN",

  });
}
