package net.anei.cadpage.parsers.OR;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;



public class ORJeffersonCountyAParser extends DispatchA22Parser {

  public ORJeffersonCountyAParser() {
    this("JEFFERSON COUNTY", "OR");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  protected ORJeffersonCountyAParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "ORJeffersonCounty";
  }

  @Override
  public String getFilter() {
    return "april.steam@tcdispatch.org,eis@wstribes.org,cad@frontier911.org";
  }

  private static final Pattern HWY_MP_PTN = Pattern.compile("(?:I|US|HWY|ST|OR) (\\d+)[NSEW]?[& ]+MP (\\d+)(?: .*)");
  private static final Pattern TRAIL_DIR_PTN = Pattern.compile(" *[NESW]B?$");
  private static final Pattern MPNNN_PTN = Pattern.compile("(MP)(\\d+ +.*)");
  private static final Pattern MP_HWY_PTN = Pattern.compile("(MP \\d+ )(?:I|US|HWY|ST|OR|MP)[- ]?(\\d+)(?: +HWY)?");

  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = address.toUpperCase();
    Matcher match = HWY_MP_PTN.matcher(address);
    if (match.matches()) address = match.group(2) + " MP " + match.group(1);
    address = TRAIL_DIR_PTN.matcher(address).replaceFirst("");
    match = MPNNN_PTN.matcher(address);
    if (match.matches()) address = match.group(1) + ' ' + match.group(2);
    match = MP_HWY_PTN.matcher(address);
    if (match.matches()) address = match.group(1) + match.group(2);
    return address;
  }



  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{

      "MP 82 20",                             "+44.424612,-121.824489",
      "MP 83 20",                             "+44.423390,-121.804864",
      "MP 84 20",                             "+44.419774,-121.786221",
      "MP 85 20",                             "+44.418713,-121.766178",
      "MP 86 20",                             "+44.423689,-121.747217",
      "MP 87 20",                             "+44.428324,-121.728293",
      "MP 88 20",                             "+44.426523,-121.710519",
      "MP 89 20",                             "+44.414837,-121.698439",
      "MP 90 20",                             "+44.403124,-121.686453",
      "MP 91 20",                             "+44.392117,-121.673393",
      "MP 92 20",                             "+44.382136,-121.658532",
      "MP 93 20",                             "+44.376176,-121.640243",
      "MP 94 20",                             "+44.369207,-121.622787",
      "MP 95 20",                             "+44.356841,-121.612124",
      "MP 96 20",                             "+44.344400,-121.601506",

      "MP 1 26",                              "+44.594549,-121.133028",
      "MP 2 26",                              "+44.580726,-121.127017",
      "MP 3 26",                              "+44.566486,-121.123692",
      "MP 4 26",                              "+44.554313,-121.113253",
      "MP 5 26",                              "+44.542900,-121.100793",
      "MP 6 26",                              "+44.531473,-121.088362",
      "MP 7 26",                              "+44.518909,-121.078773",
      "MP 8 26",                              "+44.507087,-121.067643",
      "MP 9 26",                              "+44.495396,-121.056057",
      "MP 10 26",                             "+44.482569,-121.047045",
      "MP 11 26",                             "+44.469515,-121.038707",
      "MP 12 26",                             "+44.455227,-121.035702",
      "MP 13 26",                             "+44.440854,-121.033540",
      "MP 14 26",                             "+44.427050,-121.028947",
      "MP 15 26",                             "+44.417464,-121.014072",
      "MP 16 26",                             "+44.413036,-120.994870",
      "MP 17 26",                             "+44.406379,-120.977340",
      "MP 18 26",                             "+44.395336,-120.964326",
      "MP 19 26",                             "+44.384155,-120.951496",
      "MP 20 26",                             "+44.372972,-120.938671",
      "MP 21 26",                             "+44.361788,-120.925850",
      "MP 22 26",                             "+44.350603,-120.913035",
      "MP 92 26",                             "+44.640351,-121.129732",
      "MP 93 26",                             "+44.863260,-121.420408",
      "MP 94 26",                             "+44.853049,-121.406227",
      "MP 95 26",                             "+44.842432,-121.392211",
      "MP 96 26",                             "+44.831784,-121.378237",
      "MP 97 26",                             "+44.821167,-121.364151",
      "MP 98 26",                             "+44.810518,-121.350116",
      "MP 99 26",                             "+44.799797,-121.336191",
      "MP 100 26",                            "+44.788728,-121.322999",
      "MP 101 26",                            "+44.781366,-121.306903",
      "MP 102 26",                            "+44.773976,-121.290632",
      "MP 103 26",                            "+44.765755,-121.274830",
      "MP 104 26",                            "+44.763707,-121.254704",
      "MP 105 26",                            "+44.763523,-121.234264",
      "MP 106 26",                            "+44.752052,-121.224329",
      "MP 107 26",                            "+44.739662,-121.234358",
      "MP 108 26",                            "+44.727483,-121.235637",
      "MP 109 26",                            "+44.718401,-121.222836",
      "MP 110 26",                            "+44.721555,-121.203893",
      "MP 111 26",                            "+44.723011,-121.186292",
      "MP 112 26",                            "+44.714632,-121.172059",
      "MP 113 26",                            "+44.701702,-121.162683",
      "MP 114 26",                            "+44.688814,-121.153274",
      "MP 115 26",                            "+44.675866,-121.143933",
      "MP 116 26",                            "+44.662888,-121.134679",
      "MP 117 26",                            "+44.649301,-121.129178",

      "MP 71 97",                             "+44.869349,-120.927505",
      "MP 72 97",                             "+44.854787,-120.926467",
      "MP 73 97",                             "+44.840325,-120.929992",
      "MP 74 97",                             "+44.826821,-120.928226",
      "MP 75 97",                             "+44.813269,-120.932063",
      "MP 76 97",                             "+44.800069,-120.939806",
      "MP 77 97",                             "+44.789894,-120.954397",
      "MP 78 97",                             "+44.778504,-120.966991",
      "MP 79 97",                             "+44.764525,-120.972573",
      "MP 80 97",                             "+44.759303,-120.985587",
      "MP 81 97",                             "+44.765812,-121.003559",
      "MP 82 97",                             "+44.759352,-121.019942",
      "MP 83 97",                             "+44.746605,-121.029409",
      "MP 84 97",                             "+44.735528,-121.042054",
      "MP 85 97",                             "+44.721660,-121.047977",
      "MP 86 97",                             "+44.708886,-121.057472",
      "MP 87 97",                             "+44.696642,-121.068484",
      "MP 88 97",                             "+44.684425,-121.079545",
      "MP 89 97",                             "+44.672202,-121.090596",
      "MP 90 97",                             "+44.660273,-121.102125",
      "MP 91 97",                             "+44.651655,-121.118525",
      "MP 93 97",                             "+44.625386,-121.130772",
      "MP 97 97",                             "+44.610803,-121.137393",
      "MP 98 97",                             "+44.597332,-121.144895",
      "MP 99 97",                             "+44.584125,-121.153520",
      "MP 100 97",                            "+44.570428,-121.160410",
      "MP 101 97",                            "+44.556494,-121.166384",
      "MP 102 97",                            "+44.542559,-121.172354",
      "MP 103 97",                            "+44.528623,-121.178322",
      "MP 104 97",                            "+44.514687,-121.184287",
      "MP 105 97",                            "+44.500751,-121.190249",
      "MP 106 97",                            "+44.486530,-121.193549",
      "MP 107 97",                            "+44.472170,-121.194888",
      "MP 108 97",                            "+44.458305,-121.199479",
      "MP 109 97",                            "+44.443887,-121.199375",
      "MP 110 97",                            "+44.429469,-121.199127",
      "MP 111 97",                            "+44.415051,-121.198924",
      "MP 112 97",                            "+44.400636,-121.198646",
      "MP 113 97",                            "+44.388604,-121.188634",
      "MP 114 97",                            "+44.376993,-121.178103",
      "MP 115 97",                            "+44.362577,-121.178523",
      "MP 116 97",                            "+44.348160,-121.178741",

      "MP 1 293",                             "+44.827689,-120.921464",
      "MP 2 293",                             "+44.838265,-120.913661",
      "MP 3 293",                             "+44.845566,-120.899126",
      "MP 4 293",                             "+44.846283,-120.882580",
      "MP 5 293",                             "+44.852452,-120.867271",

      "MP 1 361",                             "+44.623083,-121.140823",
      "MP 2 361",                             "+44.609459,-121.145446",
      "MP 3 361",                             "+44.600636,-121.161629",
      "MP 4 361",                             "+44.590057,-121.175102",
      "MP 5 361",                             "+44.578012,-121.186357",
      "MP 6 361",                             "+44.565613,-121.196975",
      "MP 7 361",                             "+44.553042,-121.207238",
      "MP 8 361",                             "+44.539027,-121.210128",
      "MP 9 361",                             "+44.524471,-121.210063",
      "MP 10 361",                            "+44.512331,-121.200607",
      "MP 11 361",                            "+44.499195,-121.195006"

  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARL",  "ARLINGTON",
      "CON",  "CONDON",
      "LON",  "LONEROCK",
      "GV",   "GRASS VALLEY",
      "WS",   "WARM SPRINGS"
  });
}
