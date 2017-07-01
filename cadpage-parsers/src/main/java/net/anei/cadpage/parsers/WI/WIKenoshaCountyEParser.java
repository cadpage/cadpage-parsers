package net.anei.cadpage.parsers.WI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WIKenoshaCountyEParser extends FieldProgramParser {
  
  public WIKenoshaCountyEParser() {
    super(CITY_LIST, "KENOSHA COUNTY", "WI", 
          "DATE_TIME_CALL! ( Address:ADDR! UNIT! Narrative:INFO! INFO/N+ " +
                          "| ADDR! CALL/SDS! ID? UNIT! INFO/N+ )");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@kenoshajs.org";
  }
  
  private static final Pattern DELIM = Pattern.compile(";?\n");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    body = stripFieldEnd(body, ";");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE_TIME_CALL")) return new MyDateTimeCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}\\b.*|");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_CALL_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)[/;](.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeCallField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
      data.strCall = match.group(3).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME CALL";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt < 0) {
        pt = field.indexOf(';');
        if (pt >= 0) {
          data.strPlace = field.substring(pt+1).trim();
          field = field.substring(0, pt).trim();
        }
        parseAddress(field, data);
      } else {
        parseAddress(field.substring(0, pt).trim(), data);
        String city = field.substring(pt+1).trim();
        pt = city.indexOf(';');
        if (pt >= 0) {
          data.strCity = city.substring(0,pt).trim();
          data.strPlace = city.substring(pt+1).trim();
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
          data.strPlace = getLeft();
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }
  
  private static final Pattern INFO_PREFIX_PTN = Pattern.compile("ProQA Paramount Medical:|CC Text:|Caller Statement:");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PREFIX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end()).trim();
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{

      // Cities
      "KENOSHA",

      // Villages
      "BRISTOL",
      "GENOA CITY",
      "PADDOCK LAKE",
      "PLEASANT PRAIRIE",
      "SILVER LAKE",
      "SOMERS",
      "TWIN LAKES",

      // Towns
      "BRIGHTON",
      "PARIS",
      "RANDALL",
      "SALEM",
      "SOMERS",
      "WHEATLAND",

      // Census-designated places
      "CAMP LAKE",
      "LILY LAKE",
      "POWERS LAKE",
      "WILMOT",

      // Unincorporated communities
      "BASSETT",
      "BENET LAKE",
      "BERRYVILLE",
      "BRIGHTON",
      "CENTRAL PARK",
      "CHAPIN",
      "FOX RIVER",
      "KELLOGG'S CORNERS",
      "KLONDIKE",
      "LAKE SHANGRILA",
      "LIBERTY CORNERS",
      "NEW MUNSTER",
      "PARIS",
      "SALEM OAKS",
      "SALEM",
      "SOMERS",
      "TREVOR",
      "VOLTZ LAKE",

      // Ghost towns
      "AURORA"
  };
}
