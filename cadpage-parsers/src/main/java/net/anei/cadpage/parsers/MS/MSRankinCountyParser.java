package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class MSRankinCountyParser extends DispatchProQAParser {
  
  public MSRankinCountyParser() {
    super(CITY_LIST, "RANKIN COUNTY", "MS", 
          "UNKNOWN ID! TIME CALL PRI ADDR PLACE_APT PLACE_APT+? CITY! INFO/N+? CALL/SDS! END", true);
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
    "VALUE"
  };
}
