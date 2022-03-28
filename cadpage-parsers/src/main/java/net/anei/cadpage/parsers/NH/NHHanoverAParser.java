package net.anei.cadpage.parsers.NH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NHHanoverAParser extends FieldProgramParser {

  public NHHanoverAParser() {
    super(CITY_LIST, "HANOVER","NH",
          "CALL! CALL/SDS+? ( DATETIME! District:MAP? | District:MAP! ) PLACE? ADDR/Z! CITYST! ID? ( INFO_TAG INFO/N+ | END )");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@HANOVER.PD,HDISPATCH@HOLLISNH.ORG,DISPATCH@LEBANONNH.GOV,messaging@iamresponding.com.hanoverpaging@hanovernh.org,amherstdispatch@amherstnh.gov,hanoverpaging@hanovernh.org,dispatch@lebnh.net,bedforddispatch@gmail.com,npd03773@gmail.com,charlestowndispatch@gmail.com,donotreply@bedfordnh.org,npddispatch03773@gmail.com,hanoverdispatch@gmail.com,lebanonpaging@gmail.com,cpddispatch@claremontnh.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Perform Page") && !subject.equals("_")) data.strSource = subject;
    int pt = body.indexOf("\n-- \n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!parseFields(body.split("\n"), 3, data)) return false;
    if (VT_CITIES.contains(data.strCity)) data.defState = "VT";
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PREFIX")) return new CallField("\\*\\* *(.*)", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d[AP]M", DATE_TIME_FMT1, true);
    if (name.equals("ADDR")) return new AddressField("Addr:(.*)", false);
    if (name.equals("CITYST")) return new MyCityState1Field();
    if (name.equals("INFO_TAG")) return new SkipField("\\d\\d/\\d\\d/\\d{4} \\d{4} .*", true);
    return super.getField(name);
  }
  private static final DateFormat DATE_TIME_FMT1 = new SimpleDateFormat("MM/dd/yyyy hh:mmaa");

  private static final Pattern CITY_ST1_PTN = Pattern.compile("([ A-Za-z]+), *([A-Z]{2})(?: \\d{5})?");
  private class MyCityState1Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CITY_ST1_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCity = match.group(1).trim();
      data.strState = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private static final String[] CITY_LIST = new String[]{

    // Grafton County
    "ALEXANDRIA",
    "ASHLAND",
    "BATH",
    "MOUNTAIN LAKES",
    "BENTON",
    "BETHLEHEM",
    "BRIDGEWATER",
    "BRISTOL",
    "CAMPTON",
    "CANAAN",
    "DORCHESTER",
    "EASTON",
    "ELLSWORTH",
    "ENFIELD",
    "ENFIELD CENTER",
    "MONTCALM",
    "FRANCONIA",
    "GRAFTON",
    "GROTON",
    "HANOVER",
    "ETNA",
    "HAVERHILL",
    "MOUNTAIN LAKES",
    "NORTH HAVERHILL",
    "PIKE",
    "WOODSVILLE",
    "HEBRON",
    "EAST HEBRON",
    "HOLDERNESS",
    "LANDAFF",
    "LEBANON",
    "WEST LEBANON",
    "LINCOLN",
    "LISBON",
    "LITTLETON",
    "LIVERMORE",
    "LYMAN",
    "LYME",
    "LYME CENTER",
    "MONROE",
    "ORANGE",
    "ORFORD",
    "PIERMONT",
    "PLYMOUTH",
    "RUMNEY",
    "STINSON LAKE",
    "SUGAR HILL",
    "THORNTON",
    "WARREN",
    "GLENCLIFF",
    "WATERVILLE VALLEY",
    "WENTWORTH",
    "WOODSTOCK",
    "NORTH WOODSTOCK",

    // Hillsborough COunty
    "AMHERST",
    "ANTRIM",
    "BEDFORD",
    "BENNINGTON",
    "BROOKLINE",
    "DEERING",
    "FRANCESTOWN",
    "GOFFSTOWN",
    "GRASMERE",
    "PINARDVILLE",
    "GREENFIELD",
    "GREENVILLE",
    "HANCOCK",
    "HILLSBOROUGH",
    "HOLLIS",
    "HUDSON",
    "LITCHFIELD",
    "LYNDEBOROUGH",
    "MANCHESTER",
    "MASON",
    "MERRIMACK",
    "EAST MERRIMACK",
    "MILFORD",
    "MONT VERNON",
    "NASHUA",
    "NEW BOSTON",
    "NEW IPSWICH",
    "PELHAM",
    "PETERBOROUGH",
    "WEST PETERBOROUGH",
    "SHARON",
    "TEMPLE",
    "WEARE",
    "WILTON",
    "WINDSOR",

    // Merrimack County
    "ALLENSTOWN",
    "SUNCOOK",
    "ANDOVER",
    "EAST ANDOVER",
    "BOSCAWEN",
    "BOW",
    //  "BRADFORD",  (duplicates entry in Orange County VT)
    "CANTERBURY",
    "CHICHESTER",
    "CONCORD",
    "PENACOOK",
    "DANBURY",
    "DUNBARTON",
    "EPSOM",
    "FRANKLIN",
    "HENNIKER",
    "HILL",
    "HOOKSETT",
    "SOUTH HOOKSETT",
    "HOPKINTON",
    "CONTOOCOOK",
    "LOUDON",
    "NEW LONDON",
    "ELKINS",
    "NEWBURY",
    "BLODGETT LANDING",
    "SOUTH NEWBURY",
    "NORTHFIELD",
    "PEMBROKE",
    "SUNCOOK",
    "PITTSFIELD",
    "SALISBURY",
    "SUTTON",
    "NORTH SUTTON",
    "SOUTH SUTTON",
    "WARNER",
    "WEBSTER",
    "WILMOT",

    // Orange County (VT)
    "BRADFORD",

    // Windsor County (VT)
    "NORWICH"

  };

  private static final Set<String> VT_CITIES = new HashSet<String>(Arrays.asList(
    "BRADFORD",
    "NORWICH"
  ));
}
