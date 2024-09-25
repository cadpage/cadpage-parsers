package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;



public class PABeaverCountyParser extends FieldProgramParser {

  private static final Pattern MISSING_LOC_PTN = Pattern.compile("^(EVENT #:[EF]\\d+)EST:  : EST ");

  public PABeaverCountyParser() {
    super(CITY_CODES, "BEAVER COUNTY", "PA",
           "EVENT_#:ID! LOC:ADDR/S? CALLER_PHONE:PHONE? TYPE:CALL CALLER_NAME:NAME? CALLER_ADDR:ADDR/S? TIME:TIME! COMMENTS:INFO");
  }

  @Override
  public String getFilter() {
    return "bc911cad@beavercountypa.gov,Beaver County 911";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("OUT OF COUNTY")) city = "";
    return super.adjustMapCity(city);
  }


  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.length() >= 1135 && body.length() < 1160) data.expectMore = true;
    body = stripFieldStart(body,"DISPATCH ALERT, ");
    body = MISSING_LOC_PTN.matcher(body).replaceFirst("$1 LOC: ");
    body = body.replace(" Type:", " TYPE:").replace("CALLER NAME:", " CALLER NAME:");
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("OUT OF COUNTY")) data.defCity = data.defState = "";
    return data.strAddress.length() > 0;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO"))  return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_ALIAS_PTN = Pattern.compile("(.*?)[ :]+ALIAS .*", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_APT_PLACE_PTN = Pattern.compile("(.*):(.*?) *@(.*)");
  private static final Pattern ADDR_EST_PTN = Pattern.compile("(.*?)(?:[ :]*\\bEST\\b)+[ :]*");
  private static final Pattern CLEAN_APT_PTN = Pattern.compile("(?:APT *)?#? *(.*)");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.length() > 0) return;

      Matcher match = ADDR_ALIAS_PTN.matcher(field);
      if (match.matches()) field = match.group(1);

      match = ADDR_EST_PTN.matcher(field);
      if (match.matches()) field = match.group(1);

      String apt = "";
      match = ADDR_APT_PLACE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = cleanApt(match.group(2).trim());
        data.strPlace = match.group(3).trim();
      }

      int pt = field.lastIndexOf(':');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        if (CITY_SET.contains(city.toUpperCase())) {
          data.strCity = city;
          field = field.substring(0,pt).trim();
        }
      }

      match = ADDR_EST_PTN.matcher(field);
      if (match.matches()) field = match.group(1);

      Parser p = new Parser(field);
      apt = append(cleanApt(p.getLastOptional(',')), "-", apt);
      field = p.get();
      StartType st = StartType.START_ADDR;
      int flags = FLAG_ANCHOR_END;
      if (field.startsWith("@")) {
        field = field.substring(1).trim();
        st = StartType.START_PLACE;
        flags |= FLAG_START_FLD_REQ;
      }
      String saveCity = data.strCity;
      parseAddress(st, flags, field, data);
      if (saveCity.length() > 0 && data.strCity.equals("OUT OF COUNTY")) data.strCity = saveCity;
      if (CITY_SET.contains(apt.toUpperCase())) {
        data.strCity = apt;
      } else {
        data.strApt = append(data.strApt, "-", apt);
      }
    }

    private String cleanApt(String apt) {
      Matcher match = CLEAN_APT_PTN.matcher(apt);
      if (match.matches()) apt = match.group(1);
      return apt;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " APT PLACE";
    }
  }

  private static final Pattern INFO_CH_PTN = Pattern.compile("((?:FIRE )?OPS CH \\d+) +");
  private static final Pattern INFO_GPS_PTN = Pattern.compile("\\b(?:WPH\\d(?: \\d{3})?|Y\\(\\d+,\\d+\\))( [-+]\\d{3}\\.\\d{6} [-+]\\d{3}\\.\\d{5,6})+ *");
  private static final Pattern INFO_TRAIL_JUNK_PTN = Pattern.compile("\\bWPH\\d\\b");
  private static final Pattern INFO_GPS_TRAILER_PTN1 = Pattern.compile("[^=:]*(?: SECTOR [_A-Z]*|_-_[NSEW][EW]?(?:_SEC?)?)\\.?(?: +|$)", Pattern.CASE_INSENSITIVE);
  private static final Pattern INFO_GPS_TRAILER_PTN2 = Pattern.compile("(?:VERIZON WIRELESS|WIRELESS-CRICKET\\(TCS\\)|SPPCS CALL|WIRELESS-AT&T MOBILITY\\(TCS\\)|SPRINT|T-Mobile USA, Inc\\.?)? *");
  private static final Pattern INFO_GPS_TRAILER_PTN3 = Pattern.compile("\\bLocation Saved by LocateCall - (?:LL\\([-+\\d:\\.,]+\\)|[ A-Z0-9]+ [-+]\\d{3}\\.\\d{6} [-+]\\d{3}\\.\\d{5,6}|) *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      int pt = field.indexOf("Duplicate Event:");
      if (pt >= 0) field = field.substring(0,pt).trim();

      Matcher match = INFO_CH_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strChannel = match.group(1);
        field = field.substring(match.end());
      }

      match = INFO_GPS_PTN.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(1), data);
        String info = field.substring(0,match.start()).trim();
        info = stripFieldEnd(info, "End of Duplicate Event data");
        super.parse(info, data);
        field = field.substring(match.end());
        match = INFO_TRAIL_JUNK_PTN.matcher(field);
        if (match.find()) field = field.substring(0,match.start()).trim();
        match = INFO_GPS_TRAILER_PTN1.matcher(field);
        if (match.lookingAt()) field = field.substring(match.end());
        match = INFO_GPS_TRAILER_PTN2.matcher(field);
        if (match.lookingAt()) field = field.substring(match.end());
        while (true) {
          match = INFO_GPS_TRAILER_PTN3.matcher(field);
          if (!match.lookingAt()) break;
          field = field.substring(match.end());
          if (field.startsWith(": EST ")) {
            field = field.substring(6).trim();
            String saveCity = data.strCity;
            data.strCity = "";
            parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, field, data);
            if (data.strCity.length() > 0) field = getLeft();
            data.strCity = saveCity;
          }
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH GPS INFO";
    }
  }


  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALIQ", "ALIQUIPPA",
      "AMBR", "AMBRIDGE",
      "BADE", "BADEN",
      "BEAV", "BEAVER",
      "BEAF", "BEAVER FALLS",
      "BELA", "BELL ACRES",
      "BIGB", "BIG BEAVER",
      "BRID", "BRIDGEWATER",
      "BRIG", "BRIGHTON TWP",
      "CENT", "CENTER TWP",
      "CHIP", "CHIPPEWA TWP",
      "CONW", "CONWAY",
      "DARB", "DARLINGTON BORO",
      "DARL", "DARLINGTON",
      "DART", "DARLINGTON TWP",
      "DAUG", "DAUGHERTY TWP",
      "EROC", "EAST ROCHESTER",
      "EVAL", "EASTVALE",
      "ECON", "ECONOMY",
      "ELLW", "ELLWOOD CITY",
      "FALS", "FALLSTON",
      "FRDM", "FREEDOM",
      "FRFK", "FRANKFORT SPRINGS",
      "FRAN", "FRANKLIN",
      "GEOR", "GEORGETOWN",
      "GLAS", "GLASGOW",
      "GREN", "GREENE TWP",
      "HANO", "HANOVER TWP",
      "HARM", "HARMONY TWP",
      "HMWD", "HOMEWOOD",
      "HKTW", "HOOKSTOWN",
      "HPWL", "HOPEWELL TWP",
      "INDE", "INDEPENDENCE TWP",
      "INDU", "INDUSTRY",
      "KOPP", "KOPPEL",
      "LETP", "LEET TWP",
      "MARI", "MARION TWP",
      "MIDL", "MIDLAND",
      "MONA", "MONACA",
      "NEWB", "NEW BRIGHTON",
      "NEWG", "NEW GALILEE",
      "NWSE", "NEW SEWICKLEY TWP",
      "NOSE", "NORTH SEWICKLEY TWP",
      "OHVL", "OHIOVILLE",
      "PRHT", "PATTERSON HEIGHTS",
      "PATT", "PATTERSON TWP",
      "POTT", "POTTER TWP",
      "PULA", "PULASKI TWP",
      "RACC", "RACCOON TWP",
      "ROCH", "ROCHESTER BORO",
      "ROCT", "ROCHESTER TWP",
      "SPPT", "SHIPPINGPORT",
      "SOBE", "SOUTH BEAVER TWP",
      "SOHT", "SOUTH HEIGHTS",
      "VANP", "VANPORT TWP",
      "WMYF", "WEST MAYFIELD",
      "WHIT", "WHITE",

      "OUTC", "OUT OF COUNTY",

      "CRANBERRY TWP",          "CRANBERRY TWP",
      "ELLWOOD CITY",           "ELLWOOD CITY",
      "WAYNE TWP",              "WAYNE TWP",
      "ZELIONOPLE",             "ZELIONOPLE",
  });

  private static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(
      "CRANBERRY TWP",
      "ELLWOOD CITY",
      "WAYNE TWP",
      "ZELIONOPLE"
  ));
}
