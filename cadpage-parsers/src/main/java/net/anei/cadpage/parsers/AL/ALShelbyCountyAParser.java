package net.anei.cadpage.parsers.AL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.ReverseCodeSet;

/**
 * Shelby County, AL
 */
public class ALShelbyCountyAParser extends FieldProgramParser {

  private static final Pattern PREFIX_PTN = Pattern.compile("^(?:NEW - |Arns Alert\n|Incident Time:) *", Pattern.CASE_INSENSITIVE);

  public ALShelbyCountyAParser() {
    super("SHELBY COUNTY", "AL",
          "( Unit(s)_Assigned:UNIT ID | ID DATETIME | DATETIME ID ) SRC CALL ADDR! APT X EXTRA INFO/N+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setupProtectedNames("EGG AND BUTTER");
    setupSpecialStreets("FREDERICK PASS");
    removeWords("EST", "LA");
  }

  @Override
  public String getFilter() {
    return "arns@shelby911.org,arns@shelbyal.com,ARNS Alert";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.length() == 0 && body.startsWith("/ ")) {
      int pt = body.indexOf(" / ", 2);
      if (pt < 0) return false;
      subject = body.substring(2,pt).trim();
      body = body.substring(pt+3).trim();
    }

    if (subject.startsWith("Event Closed:")) data.msgType = MsgType.RUN_REPORT;

    Matcher match = PREFIX_PTN.matcher(body);
    if (match.find()) body = body.substring(match.end());

    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X"))  return new MyCrossField();
    if (name.equals("EXTRA")) return new MyExtraField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d{4}-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = field.substring(5,7) + '/' + field.substring(8,10) + '/' + field.substring(0,4);
      data.strTime = field.substring(11);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern ADDR_SPECIAL_PTN = Pattern.compile("([A-Z]{4} [A-Z]{3}): @(.*?)(?::(.*))?");
  private static final Pattern ADDR_DELIM_PTN = Pattern.compile(" *(?:[@:;,]|: @) *");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:#|(?:APT|RM|ROOM|UNIT|SUITE?|STE|LOT)(?![A-Z])) *(.*)|(?:LOT|FL) *.*|[A-Z]?\\d{1,5}[A-Z]?|[A-Z]|\\d+(?:ST|ND|RD|TH|) *FLR?");
  private static final Pattern PLACE_APT_PTN = Pattern.compile("(.*)\\b(?:APT|RM|ROOM|UNIT|SUITE?|STE|LOT)(?![A-Z]) *(.+)|((?:LOT|FLR?) *.+)");
  private static final Pattern ADDR_PHONE_PTN = Pattern.compile("\\d{3}-\\d{3}-\\d{4}|\\(\\d{3}\\) \\d{3}[- ]\\d{4}|\\d{3}-\\d{4}");
  private static final Pattern ADDR_UNIT_PTN = Pattern.compile("^[A-Z]{2}FD\\b");
  private static final Pattern ADDR_CITY_PTN1 = Pattern.compile("(.*?) *\\b([A-Z][A-Z0-9]{3}) ([A-Z]{3})");
  private static final Pattern ADDR_CITY_PTN2 = Pattern.compile("(.*?) *\\b([A-Z][A-Z0-9]{3}) MOPT(?: (\\d+))?");
  private static final Pattern ADDR_SFX_PTN = Pattern.compile("[NSEW]B|");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      // Check for LL() GPS coordinates
      if (field.startsWith("LL(")) {
        int pt = field.indexOf(')', 2);
        if (pt < 0) pt = field.length();
        data.strAddress = field.substring(0,pt+1);
        field = field.substring(pt+1).trim();
      }

      String part1 = null;
      String part2 = null;
      List<String> aptList = new ArrayList<String>();
      String place = "";
      String alias = null;

      // Check special city src: @place:address pattern
      Matcher match = ADDR_SPECIAL_PTN.matcher(field);
      if (match.matches()) {
        String city = match.group(1);
        String city2 = stripCity(city, data);
        if (city2.length() > 0) data.strCity = city;
        place = match.group(2).trim();
        String addr = getOptGroup(match.group(3));
        match = ADDR_UNIT_PTN.matcher(place);
        if (match.lookingAt()) {
          data.strUnit = match.group();
          place = place.substring(match.end()).trim();
        }
        if (ADDR_SFX_PTN.matcher(addr).matches()) {
          addr = append(place, " ", addr);
          place = "";
        }
        addr = stripCity(addr, data);
        int pt = addr.indexOf(':');
        if (pt >= 0) {
          data.strPlace = append(data.strPlace, " - ", stripFieldStart(addr.substring(pt+1).trim(), "@"));
          addr = addr.substring(0,pt).trim();
        }

        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
        if (place.endsWith("FD")) {
          if (data.strSource.length() == 0) data.strSource = place;
          place = "";
        } else {
          match = PLACE_APT_PTN.matcher(place);
          if (match.matches()) {
            place = match.group(1).trim();
            String tmp = match.group(2);
            if (tmp == null) tmp = match.group(3);
            aptList.add(tmp);
          }
        }
      }

      else {

        // Break field up into component parts
        for (String part : ADDR_DELIM_PTN.split(field)) {

          if (part.length() == 0) continue;
          if (part.equals("NONE")) continue;

          part = stripCity(part, data);

          if (part.endsWith("FD")) {
            if (data.strSource.length() == 0) data.strSource = part;
            continue;
          }

          if (part.toUpperCase().startsWith("ALIAS ")) {
            alias = part.substring(6).trim();
            continue;
          }

          if (data.strAddress.length() == 0) {
            if (part1 == null) {
              part1 = part;
              continue;
            }
          }

          match = ADDR_APT_PTN.matcher(part);
          if (match.matches()) {
            String tmp = match.group(1);
            if (tmp == null) tmp = part;
            aptList.add(tmp);
            continue;
          }

          if (ADDR_PHONE_PTN.matcher(part).matches()) {
            data.strPhone = part;
            continue;
          }

          if (data.strAddress.length() == 0 && part2 == null) {
            part2 = part;
            continue;
          }
          place = append(place, " - ", part);
        }
      }

      // If we did not identify a GPS address earlier on, see what we can identify as an address
      // from the first two parts
      if (data.strAddress.length() == 0 && part1 != null) {
        if (part2 == null) {
          parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, part1, data);
        } else if (part2.startsWith("BETWEEN ")){
          parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, part1, data);
          data.strCross = part2.substring(8).replace('&', '/');
        } else {
          Result res1 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, part1);
          Result res2 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, part2);
          if (res2.getStatus() > res1.getStatus()) {
            res1 = res2;
            part2 = part1;
          }
          res1.getData(data);
          if (isValidAddress(part2)) {
            data.strCross = part2;
          } else {
            match = PLACE_APT_PTN.matcher(part2);
            if (match.matches()) {
              part2 = match.group(1).trim();
              String tmp = match.group(2);
              if (tmp ==  null) tmp = match.group(3);
              aptList.add(0,tmp);
            }

            place = append(part2, " - ", place);
          }
        }
      }

      // append alias
      if (alias != null) {
        data.strAddress = append(data.strAddress, " ", '(' + alias + ')');
      }

      // append apt and place
      data.strPlace = append(data.strPlace, " - ", place);

      if (data.strApt.length() > 0) {
        aptList.add(0,data.strApt);
        data.strApt = "";
      }

      if (aptList.size() == 1) {
        data.strApt = aptList.get(0);
      } else if (aptList.size() > 1) {
        Set<String> aptSet = new HashSet<String>();
        for (String apt : aptList) {
          if (aptSet.add(apt)) data.strApt = append(data.strApt, "-", apt);
        }
      }
    }

    private String stripCity(String field, Data data) {
      Matcher match = ADDR_CITY_PTN1.matcher(field);
      if (match.matches()) {
        String city = CITY_CODES.getProperty(match.group(3));
        if (city == null || city.endsWith(" COUNTY")) {
          String subcity = CITY_SUBCODES.getProperty(match.group(2));
          if (subcity != null) city = subcity;
        }
        if (city != null) {
          data.strCity = city;
          field = match.group(1);
        }
      }
      else if ((match = ADDR_CITY_PTN2.matcher(field)).matches()) {
        String city = CITY_SUBCODES.getProperty(match.group(2));
        if (city != null) {
          data.strCity = city;
          data.strApt = append(data.strApt, "-", getOptGroup(match.group(3)));
          field =  match.group(1);
        }
      }
      else {
        String city = SPECIAL_CITY_LIST.getCode(field, true);
        if (city != null) {
          data.strCity = city;
          field = field.substring(0,field.length()-city.length()).trim();
        }
      }
      return field;
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE PHONE UNIT";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("::")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace("::", "/");
      if (field.endsWith("/")) {
        field = field.substring(0,field.length()-1).trim();
      }
      data.strCross = append(data.strCross, "/", field);
    }
  }

  private static final Pattern EXTRA_PHONE_GPS_PTN = Pattern.compile("[A-Z &]+(\\(\\d{3}\\) \\d{3}-\\d{4}) ([-+]\\d+\\.\\d{6} [-+]\\d+\\.\\d{6})X?[- ]*");
  private class MyExtraField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("-")) {
        data.strName = field;
      } else {
        field = field.substring(1).trim();
        Matcher match = EXTRA_PHONE_GPS_PTN.matcher(field);
        if (match.lookingAt()) {
          data.strPhone = match.group(1);
          setGPSLoc(match.group(2), data);
          field = field.substring(match.end());
        }
        data.strSupp = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "PHONE GPS INFO";
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    sAddress = CLFS_PTN.matcher(sAddress).replaceAll("CLIFFS");
    return super.adjustMapAddress(sAddress);
  }
  private static final Pattern CLFS_PTN = Pattern.compile("\\bCLFS\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("CAHABA VALLEY")) city = "BIRMINGHAM";
    return city;
  }

  private static final ReverseCodeSet SPECIAL_CITY_LIST = new ReverseCodeSet(
      "BRIERFIELD",
      "CHILTON"
  );

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALA", "ALABASTER",
      "BES", "BESSEMER",
      "BFD", "BRIERFIELD",
      "BHM", "BIRMINGHAM",
      "BIB", "BIBB COUNTY",
      "CAL", "CALERA",
      "CHL", "CHELSEA",
      "COL", "COLUMBIANA",
      "HEL", "HELENA",
      "HOV", "HOOVER",
      "HRP", "HARPERSVILLE",
      "JEF", "JEFFERSON COUNTY",
      "LEE", "LEEDS",
      "MAY", "MAYLENE",
      "MON", "MONTEVALLO",
      "PEH", "PELHAM",
      "PEL", "PELHAM",
      "SAG", "SAGINAW",
      "SHE", "SHELBY",
      "STE", "STERRETT",
      "VAN", "VANDIVER",
      "VES", "VESTAVIA HILLS",
      "VIN", "VINCENT",
      "WES", "WESTOVER",
      "WIL", "WILSONVILLE"
  });

  private static final Properties CITY_SUBCODES = buildCodeTable(new String[]{
      "ALAB", "ALABASTER",
      "BFLD", "BRIERFIELD",
      "BHAM", "BIRMINGHAM",
      "CALE", "CALERA",
      "CALJ", "CALERA",
      "CHEJ", "CHELSEA",
      "CHEL", "CHELSEA",
      "COLU", "COLUMBIANA",
      "CVAL", "CAHABA VALLEY",
      "DUNN", "DUNNAVANT",
      "HARJ", "HARPERSVILLE",
      "HARP", "HARPERSVILLE",
      "HELN", "HELENA",
      "HOOV", "HOOVER",
      "LEED", "LEEDS",
      "MONT", "MONTEVALLO",
      "PELH", "PELHAM",
      "SAGN", "SAGINAW",
      "SHEL", "SHELBY",
      "VAND", "VANDIVER",
      "VEST", "VESTAVIA HILLS",
      "VINC", "VINCENT",
      "VINJ", "VINCENT",
      "WEST", "WESTOVER",
      "WILJ", "WILSONVILLE",
      "WILS", "WILSONVILLE"
  });

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "4151 HWY 280",                         "+33.333901,-86.421549"
  });
}
