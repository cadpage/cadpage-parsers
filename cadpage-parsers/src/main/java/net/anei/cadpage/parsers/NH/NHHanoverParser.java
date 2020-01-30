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



public class NHHanoverParser extends FieldProgramParser {
  
  public NHHanoverParser() {
    super(CITY_LIST, "HANOVER","NH",
          "PREFIX? CALL! ( DATETIME1! District:MAP? PLACE? ADDR/Z! CITYST1! " +
                "| PLACE? ADDR/Z CITY2! INFO2+? DATETIME2 INFO2+ )");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@HANOVER.PD,HDISPATCH@HOLLISNH.ORG,DISPATCH@LEBANONNH.GOV,messaging@iamresponding.com.hanoverpaging@hanovernh.org,amherstdispatch@amherstnh.gov,hanoverpaging@hanovernh.org,dispatch@lebnh.net,bedforddispatch@gmail.com,npd03773@gmail.com,charlestowndispatch@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Perform Page") && !subject.equals("_")) data.strSource = subject;
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
    if (name.equals("DATETIME1")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d[AP]M", DATE_TIME_FMT1, true);
    if (name.equals("ADDR")) return new AddressField("Addr:(.*)", false);
    if (name.equals("CITYST1")) return new MyCityState1Field();
    if (name.equals("CITY2")) return new MyCity2Field();
    if (name.equals("INFO2")) return new MyInfo2Field();
    if (name.equals("DATETIME2")) return new DateTimeField("\\d{1,2}/\\d{1,2}/\\d{4} \\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  private static final DateFormat DATE_TIME_FMT1 = new SimpleDateFormat("MM/dd/yyyy hh:mmaa");
  
  private static final Pattern CITY_ST1_PTN = Pattern.compile("([ A-Za-z]+), *([A-Z]{2})");
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
  
  private class MyCity2Field extends CityField {
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // Sometimes the city and info fields are combined into one
      // field separated by a slash :(
      String info = null;
      int pt1 = field.indexOf('/');
      int pt2 = field.indexOf('.');
      if (pt1 < 0 || pt2 >= 0 && pt2 < pt1) pt1 = pt2;
      if (pt1 >= 0) {
        info = field.substring(pt1+1).trim();
        field = field.substring(0,pt1).trim();
      }
      if (!super.checkParse(field, data)) return false;
      if (info != null) data.strSupp = append(data.strSupp, " / ", info);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CITY INFO";
    }
  }
  
  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("District:")) {
        data.strMap = field.substring(9).trim();
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "MAP INFO";
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
