package net.anei.cadpage.parsers.ZCAAB;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA51Parser;
/**
 * Red Deer County, AB, CA
 */
public class ZCAABRedDeerCountyAParser extends DispatchA51Parser {

  public ZCAABRedDeerCountyAParser() {
    this("RED DEER COUNTY");
  }

  protected ZCAABRedDeerCountyAParser(String defCity) {
    super(CITY_LIST, defCity, "AB");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getAliasCode() {
    return "ZCABRedDeerCounty";
  }

  @Override
  public String getFilter() {
    return "@reddeer.ca,messaging@iamresponding.com,@cremonafire.com,SCESITGroup@Strathcona.ca,ng911_notification@strathcona.ca,FireDispatch@stalbert.ca,Incident@ParklandCounty.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_CR_CRES;
  }

  private static final Pattern JUNK_PTN = Pattern.compile("\n(?:You have received|NOTICE -|This communication) ");
  private static final Pattern CLEAN_CITY_PTN = Pattern.compile("(.*?) +RURAL(?: AREA)?", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Message") && !subject.endsWith("Incident Message")) {
      data.strSource = subject;
    }

    if (body.startsWith("Sender:")) {
      int pt = body.indexOf("Message:");
      if (pt < 0) return false;
      body = body.substring(pt+8).trim();
    }

    Matcher match = JUNK_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();

    if (!super.parseMsg(body,  data)) return false;

    data.strCity = data.strCity.replace('_', ' ').replace(".", "");
    match = CLEAN_CITY_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    else data.strCity = stripFieldStart(data.strCity, "ID ");
    if (data.strCity.equals("VILLAGE OF") || data.strCity.equals("[None selected]")) data.strCity = "";
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    sAddress = RNG_PATTERN.matcher(sAddress).replaceAll("Range");
    sAddress = RANGE_RD_DASH_PTN.matcher(sAddress).replaceAll("$1$2");
    return sAddress;
  }
  private static final Pattern RNG_PATTERN = Pattern.compile("\\bRng\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern RANGE_RD_DASH_PTN = Pattern.compile("\\b(RANGE +RD +\\d+)-(\\d+)\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return HWY_NN_PTN.matcher(sAddress).replaceAll("ALBERTA $1");
  }
  private static final Pattern HWY_NN_PTN = Pattern.compile("\\bHWY +(\\d+)\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "4850 43 AVE",                          "+51.791691,-114.093236",
      "4902 44 AVE",                          "+51.791463,-114.092998",
      "5032 44 AVE",                          "+51.790321,-114.092735",
      "5082 44 AVE",                          "+51.790159,-114.092261",
      "5150 44 AVE",                          "+51.789459,-114.092203",
      "4477 47 ST",                           "+51.792728,-114.091225",
      "4470 48 ST",                           "+51.793672,-114.093157",
      "4533 48 ST",                           "+51.792578,-114.094670",
      "4534 49 ST",                           "+51.791512,-114.094278",
      "4500 50 ST",                           "+51.785962,-114.093721",
      "4450 53 ST",                           "+51.788396,-114.093292",
      "4501 53 ST",                           "+51.788254,-114.094224",
      "4550 53 ST",                           "+51.788715,-114.094699",
      "50 ST",                                "+51.788461,-114.091142"
  });

  private static final String[] CITY_LIST = new String[]{
      "ACME",
      "AIRDRIE",
      "ALCOMDALE",
      "ARDROSSAN",
      "BASSANO",
      "BENALTO",
      "BENTLEY",
      "BIG VALLEY",
      "BIRCHCLIFF",
      "BLACKFALDS",
      "BLUFFTON",
      "BON ACCORD",
      "BOWDEN",
      "BRAZEAU COUNTY",
      "BROOKS",
      "BRUDERHEIM",
      "BUFORD",
      "CALAHOO",
      "CALMAR",
      "CARBON",
      "CARBONDALE",
      "CAROLINE",
      "CARSTAIRS",
      "CHIPMAN",
      "CLEARWATER COUNTY",
      "CONDOR",
      "COUNTY OF MINBURN NO 27",
      "CREMONA",
      "CROSSFIELD",
      "DELBURNE",
      "DEVON",
      "DIDSBURY",
      "DUCHESS",
      "EDMONTON",
      "EDMONTON INTERNATIONAL AIRPORT",
      "ELK ISLAND",
      "ELNORA",
      "FORT SASKATCHEWAN",
      "GIBBONS",
      "GWYNNE",
      "INNISFAIL",
      "INUVIK",
      "JARVIS BAY",
      "HUXLEY",
      "KNEEHILL COUNTY",
      "LACOMBE",
      "LACOMBE COUNTY",
      "LAKE NEWELL RESORT",
      "LAMONT",
      "LAMONT COUNTY",
      "LAVOY",
      "LEDUC",
      "LEDUC COUNTY",
      "LEGAL",
      "LESLIEVILLE",
      "LINDEN",
      "LOUSANA",
      "MARKERVILLE",
      "MD OF TABER",
      "MINBURN",
      "MINBURN COUNTY",
      "MOUNTAIN VIEW COUNTY",
      "MORINVILLE",
      "MUNDARE",
      "NAMAO",
      "NEW SAREPTA",
      "NEWELL COUNTY",
      "NISKU",
      "NO 13 ELK ISLAND",
      "NORGLENWOLD",
      "OLDS",
      "PARKLAND BEACH",
      "PATRICIA",
      "PENHOLD",
      "PONOKA",
      "PONOKA COUNTY",
      "RED DEER",
      "RED DEER COUNTY",
      "RED_DEER_COUNTY",
      "REDWATER",
      "RIMBEY",
      "ROCKY MOUNTAIN HOUSE",
      "ROCKY VIEW COUNTY",
      "ROLLING HILLS",
      "ROSEMARY",
      "SHERWOOD PARK",
      "SPRINGBROOK",
      "SPRUCE GROVE",
      "SPRUCE VIEW",
      "STETTLER",
      "STURGEON COUNTY",
      "SUNDRE",
      "SUNRISE BEACH",
      "ST ALBERT",
      "ST MICHAEL",
      "STARLAND COUNTY",
      "STRATHCONA COUNTY",
      "SYLVAN LAKE",
      "SWALWELL",
      "THREE HILLS",
      "THORSBY",
      "TILLEY",
      "TORRINGTON",
      "TROCHU",
      "TWO HILLS COUNTY",
      "VEGREVILLE",
      "VILLENEUVE",
      "WARBURG",
      "WETASKIWIN",
      "WETASKIWIN COUNTY"
  };
}
