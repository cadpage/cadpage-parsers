package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;



public class PABeaverCountyParser extends FieldProgramParser {

  public PABeaverCountyParser() {
    super(CITY_LIST, "BEAVER COUNTY", "PA",
          "UNIT:UNIT! ACTION:ACTION! EVENT_#:ID! LOC:ADDRCITY/S! LAT:GPS1! LONG:GPS2! CALLER_NAME:NAME! CALLER_PHONE:PHONE! TYPE:CALL! SUBTYPE:CALL/SDS! PRIORITY:PRI! TIME:TIME! COMMENTS:INFO! END");
    setupCities(MISSPELLED_CITIES);
  }

  @Override
  public String getFilter() {
    return "bc911cad@beavercountypa.gov";
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("OUT OF COUNTY")) city = "";
    return super.adjustMapCity(city);
  }


  @Override
  protected boolean parseMsg(String body, Data data) {
//    body = MISSING_LOC_PTN.matcher(body).replaceFirst("$1 LOC: ");
//    body = body.replace(" Type:", " TYPE:").replace("CALLER NAME:", " CALLER NAME:");
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("OUT OF COUNTY")) data.defCity = data.defState = "";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ACTION")) return new MyActionField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d|", true);
    if (name.equals("INFO"))  return new MyInfoField();
    return super.getField(name);
  }

  private class MyActionField extends SkipField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("CLEARED")) data.msgType = MsgType.RUN_REPORT;
    }
  }

  private static final Pattern CLEAN_CITY_PTN = Pattern.compile("(.*?) (?:CITY|BORO(?:UGH)?)");
  private static final Pattern PLACE_ADDR_CITY_PTN = Pattern.compile("(.*),(.*,.*)");
  private class MyAddressCityField extends AddressCityField {

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("LL(")) {
        data.strAddress = field;
        return;
      }

      Matcher match = CLEAN_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
      } else if (field.endsWith(" TOWNSHIP")) {
        field = field.substring(0,field.length()-8)+"TWP";
      }
      int pt = field.indexOf(':');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      } else {
        match = PLACE_ADDR_CITY_PTN.matcher(field);
        if (match.matches()) {
          data.strPlace = match.group(1).trim();
          field = match.group(2).trim();
        }
      }

      String apt = "";
      String city = "";
      pt = field.lastIndexOf(',');
      if (pt >= 0) {
        city = field.substring(pt+1).replace(".", "").trim();
        field = field.substring(0,pt).trim();
        if (!isCity(city)) {
          parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, city, data);
          if (!data.strCity.isEmpty()) {
            city = data.strCity;
            data.strCity = "";
            apt = getStart();
          }
        }
      }
      int flags = FLAG_ANCHOR_END;
      if (apt.isEmpty()) flags |= FLAG_RECHECK_APT;
      if (!city.isEmpty()) flags |= FLAG_NO_CITY;
      parseAddress(StartType.START_ADDR, flags, field, data);
      if (!city.isEmpty()) data.strCity = city;
      data.strCity = convertCodes(data.strCity, MISSPELLED_CITIES);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strPhone.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile(" *[-+]?\\b\\d{2}\\.\\d{6,} [-+]?\\d{2}\\.\\d{6,}\\b *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      int pt = field.indexOf("Duplicate Event:");
      if (pt >= 0) field = field.substring(0,pt).trim();

      field = INFO_GPS_PTN.matcher(field).replaceAll(" ").trim();

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH GPS INFO";
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "ALIQUIPPA",
      "BEAVER FALLS",
      "BOROUGHS",
      "AMBRIDGE",
      "BADEN",
      "BEAVER",
      "BIG BEAVER",
      "BRIDGEWATER",
      "CONWAY",
      "DARLINGTON",
      "EAST ROCHESTER",
      "EASTVALE",
      "ECONOMY",
      "ELLWOOD CITY",
      "FALLSTON",
      "FRANKFORT SPRINGS",
      "FREEDOM",
      "GEORGETOWN",
      "GLASGOW",
      "HOMEWOOD",
      "HOOKSTOWN",
      "INDUSTRY",
      "KOPPEL",
      "MIDLAND",
      "MONACA",
      "NEW BRIGHTON",
      "NEW GALILEE",
      "OHIOVILLE",
      "PATTERSON HEIGHTS",
      "ROCHESTER",
      "SHIPPINGPORT",
      "SOUTH HEIGHTS",
      "WEST MAYFIELD",

      // Townships
      "BRIGHTON TWP",
      "BRIGHTON",
      "CENTER TWP",
      "CENTER",
      "CHIPPEWA TWP",
      "CHIPPEWA",
      "DARLINGTON TWP",
      "DARLINGTON",
      "DAUGHERTY TWP",
      "DAUGHERTY",
      "FRANKLIN TWP",
      "FRANKLIN",
      "GREENE TWP",
      "GREENE",
      "HANOVER TWP",
      "HANOVER",
      "HARMONY TWP",
      "HARMONY",
      "HOPEWELL TWP",
      "HOPEWELL",
      "INDEPENDENCE TWP",
      "INDEPENDENCE",
      "MARION TWP",
      "MARION",
      "NEW SEWICKLEY TWP",
      "NEW SEWICKLEY",
      "NORTH SEWICKLEY TWP",
      "NORTH SEWICKLEY",
      "PATTERSON TWP",
      "PATTERSON",
      "POTTER TWP",
      "POTTER",
      "PULASKI TWP",
      "PULASKI",
      "RACCOON TWP",
      "RACCOON",
      "ROCHESTER TWP",
      "ROCHESTER",
      "SOUTH BEAVER TWP",
      "SOUTH BEAVER",
      "VANPORT TWP",
      "VANPORT",
      "WHITE TWP",
      "WHITE",

      // Census-designated places
      "FRISCO",
      "HAZEN",

      // Unincorporated communities
      "BYERSDALE",
      "CANNELTON",
      "FOMBELL",
      "GRINGO",
      "HARSHAVILLE",
      "KOBUTA",
      "WEST ALIQUIPPA",

      // Allegheny County
      "NORTH FAYETTE",
      "NORTH FAYETTE TWP",

      // Butler County
      "CRANBERRY TWP",

      // Lawrence County
      "ELLWOOD CITY",
      "ELLWOOD",

      // Washington County
      // Cities
      "MONONGAHELA",
      "WASHINGTON",

      // Boroughs
      "ALLENPORT",
      "BEALLSVILLE",
      "BENTLEYVILLE",
      "BURGETTSTOWN",
      "CALIFORNIA",
      "CANONSBURG",
      "CENTERVILLE",
      "CHARLEROI",
      "CLAYSVILLE",
      "COAL CENTER",
      "COKEBURG",
      "DEEMSTON",
      "DONORA",
      "DUNLEVY",
      "EAST WASHINGTON",
      "ELCO",
      "ELLSWORTH",
      "FINLEYVILLE",
      "GREEN HILLS",
      "HOUSTON",
      "LONG BRANCH",
      "MARIANNA",
      "MCDONALD",
      "MIDWAY",
      "NEW EAGLE",
      "NORTH CHARLEROI",
      "ROSCOE",
      "SPEERS",
      "STOCKDALE",
      "TWILIGHT",
      "WEST BROWNSVILLE",
      "WEST MIDDLETOWN",

      // Townships
      "AMWELL TWP",
      "AMWELL",
      "BLAINE TWP",
      "BLAINE",
      "BUFFALO TWP",
      "BUFFALO",
      "CANTON TWP",
      "CANTON",
      "CARROLL TWP",
      "CARROLL",
      "CECIL TWP",
      "CECIL",
      "CHARTIERS TWP",
      "CHARTIERS",
      "CROSS CREEK TWP",
      "CROSS CREEK",
      "DONEGAL TWP",
      "DONEGAL",
      "EAST BETHLEHEM TWP",
      "EAST BETHLEHEM",
      "EAST FINLEY TWP",
      "EAST FINLEY",
      "FALLOWFIELD TWP",
      "FALLOWFIELD",
      "HANOVER TWP",
      "HANOVER",
      "HOPEWELL TWP",
      "HOPEWELL",
      "INDEPENDENCE TWP",
      "INDEPENDENCE",
      "JEFFERSON TWP",
      "JEFFERSON",
      "MORRIS TWP",
      "MORRIS",
      "MT PLEASANT TWP",
      "MT PLEASANT",
      "MOUNT PLEASANT TWP",
      "MOUNT PLEASANT",
      "NORTH BETHLEHEM TWP",
      "NORTH BETHLEHEM",
      "NORTH FRANKLIN TWP",
      "NORTH FRANKLIN",
      "NORTH STRABANE TWP",
      "NORTH STRABANE",
      "N STRABANE TWP",
      "NOTTINGHAM TWP",
      "NOTTINGHAM",
      "PETERS TWP",
      "PETERS",
      "ROBINSON TWP",
      "ROBINSON",
      "SMITH TWP",
      "SMITH",
      "SOMERSET TWP",
      "SOMERSET",
      "SOUTH FRANKLIN TWP",
      "SOUTH FRANKLIN",
      "SOUTH STRABANE TWP",
      "SOUTH STRABANE",
      "UNION TWP",
      "UNION",
      "WEST BETHLEHEM TWP",
      "WEST BETHLEHEM",
      "WEST FINLEY TWP",
      "WEST FINLEY",
      "WEST PIKE RUN TWP",
      "WEST PIKE RUN",

      // Census-designated places
      "AARONSBURG",
      "ATLASBURG",
      "AVELLA",
      "BAIDLAND",
      "BULGER",
      "CECIL-BISHOP",
      "CROSS CREEK",
      "EIGHTY FOUR",
      "ELRAMA",
      "FREDERICKTOWN",
      "GASTONVILLE",
      "HENDERSONVILLE",
      "HICKORY",
      "JOFFRE",
      "LANGELOTH",
      "LAWRENCE",
      "MCGOVERN",
      "MCMURRAY",
      "MEADOWLANDS",
      "MILLSBORO",
      "MUSE",
      "PARIS",
      "SLOVAN",
      "SOUTHVIEW",
      "TAYLORSTOWN",
      "THOMPSONVILLE",
      "VAN VOORHIS",
      "WEST ALEXANDER",
      "WESTLAND",
      "WICKERHAM MANOR-FISHER",
      "WOLFDALE",
      "WYLANDVILLE",

      // Unincorporated communities
      "AMITY",
      "BLAINSBURG",
      "CONDIT CROSSING",
      "COOL VALLEY",
      "COURTNEY",
      "CRACKER JACK",
      "DAISYTOWN",
      "FALLOWFIELD",
      "FLORENCE",
      "FROGTOWN",
      "GAMBLES",
      "GLYDE",
      "GOOD INTENT",
      "HAZEL KIRK",
      "LABORATORY",
      "LOG PILE",
      "LOVER",
      "MANIFOLD",
      "MURDOCKSVILLE",
      "MCADAMS",
      "NORTH FREDERICKTOWN",
      "OLD CONCORD",
      "P AND W PATCH",
      "PROSPERITY",
      "RACCOON",
      "RICHEYVILLE",
      "SCENERY HILL",
      "STUDA",
      "VENETIA",
      "VESTABURG"
  };

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[] {
      "CARROL TWP",         "CARROLL TWP",
      "CRANBRERRY TWP",     "CRANBERRY TWP",
      "ELLWOOD",            "ELLWOOD CITY"
  });
}
