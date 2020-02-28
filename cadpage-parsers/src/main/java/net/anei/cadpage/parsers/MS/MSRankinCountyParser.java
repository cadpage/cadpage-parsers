package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class MSRankinCountyParser extends DispatchProQAParser {
  
  public MSRankinCountyParser() {
    this("RANKIN COUNTY");
  }
  
  MSRankinCountyParser(String county) {
    super(CITY_LIST, county, "MS", 
          "UNKNOWN? ID! TIME CALL PRI ADDR PLACE_APT PLACE_APT+? CITY! INFO/N+? CALL/SDS! ( END | PROQA_DET ) INFO/N+", true);
  }
  
  @Override
  public String getAliasCode() {
    return "MSRankinCounty";
  }
  
  @Override
  public String getFilter() {
    return "noreply@paffordems.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" / ", " - ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNKNOWN")) return new SkipField("<Unknown>", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("PRI")) return new PriorityField("(\\d) - Priority.*", true);
    if (name.equals("PLACE_APT")) return new MyPlaceAptField();
    if (name.equals("PROQA_DET")) return new SkipField("<PROQA_DET>", true); 
    return super.getField(name);
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|RM|LOT) *(.*)|\\d{1,4}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyPlaceAptField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
  private static final String[] CITY_LIST = new String[]{
    
    // Rankin County
    // Cities
    "BRANDON",
    "FLOWOOD",
    "JACKSON",
    "PEARL",
    "RICHLAND",

    // Towns
    "FLORENCE",
    "PELAHATCHIE",

    // Village
    "PUCKETT",

    // Census-designated places
    "CLEARY",
    "ROBIN HOOD",

    // Other unincorporated communities
    "CROSS ROADS",
    "FANNIN",
    "GOSHEN SPRINGS",
    "GREENFIELD",
    "GULDE",
    "JESUS",
    "JOHNS",
    "LANGFORD",
    "LEESBURG",
    "PINEY WOODS",
    "SAND HILL",
    "STAR",
    "WHITFIELD",
    "PISGAH",

    // Former communities
    "CATO",
    "COMEBY",
    "CROSSGATES FARM",
    "DOBSON",
    "ROBINHOOD/SHADY LAKES",
    "VALUE",
    
    
    // Madison County
    "CANTON",
    "MADISON",
    "RIDGELAND",

    // Towns
    "FLORA",

    // Census-designated place
    "KEARNEY PARK",
    "ANNADALE",
    "ANDERSON",

    // Other uincorporated communities
    "CAMDEN",
    "FARMHAVEN",
    "GLUCKSTADT",
    "LIVINGSTON",
    "SHARON",
    "WAY",

    // Ghost town
    "BEATTIES BLUFF",
    "STOKES",
    
    // Yazoo County
    "YAZOO CITY",

    // Towns
    "BENTONIA",

    // Villages
    "EDEN",
    "SATARTIA",

    // Unincorporated communities
    "ANDING",
    "BENTON",
    "CARTER",
    "HOLLY BLUFF",
    "HOPEWELL LANDING",
    "LITTLE YAZOO",
    "MIDWAY",
    "OIL CITY",
    "SCOTLAND",
    "TINSLEY",
    "VAUGHAN",

    // Ghost towns
    "CLAIBORNESVILLE",
    "HILTON",
    "LIVERPOOL",
    "PEARCE",
    "PLUMVILLE"
  };
}
