package net.anei.cadpage.parsers.ND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NDBillingsCountyParser extends FieldProgramParser {
  
  public NDBillingsCountyParser() {
    this("BILLINGS COUNTY", "ND");
  }
  
  NDBillingsCountyParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState, 
          "Loc:ADDR Type:SKIP! Time:TIME! Cross_Streets:X? Unit_Added:UNIT! Caller_Name:NAME? Caller_Phone_#:PHONE? Re:INFO");
    removeWords("UNIT");
  }
  
  @Override
  public String getAliasCode() {
    return "NDBillingsCounty";
  }
  
  @Override
  public String getFilter() {
    return "descad@911paging.us";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("([A-Z]+\\d{9,10}) - ([A-Z]+) - (.*)");
  private static final String GPS_MARKER = " /// http://maps.google.com/?q=";
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strSource = match.group(2);
    data.strCall = match.group(3).trim();
    
    int pt = body.lastIndexOf(GPS_MARKER);
    if (pt >= 0) {
      setGPSLoc(body.substring(pt+GPS_MARKER.length()).trim(), data);
      body = body.substring(0,pt).trim();
    }
    
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "ID SRC CALL " + super.getProgram() + " GPS";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern BOUND_PTN = Pattern.compile("[NSEW]B");
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field, data);
      field = getStart();
      
      field = stripFieldStart(field, "@");
      String place = null;
      if (field.startsWith("LL(")) {
        int pt = field.indexOf(')', 3);
        if (pt < 0) {
          data.strAddress = field;
        } else {
          data.strAddress = field.substring(0,pt+1);
          place = stripFieldStart(field.substring(pt+1).trim(), ":");
        }
      }
      else {
        int pt = field.indexOf(':');
        if (pt >= 0) {
          place = field.substring(pt+1).trim();
          field = field.substring(0,pt).trim(); 
        }
        if (data.strCity.length() == 0) {
          parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
        } else {
          parseAddress(field, data);
        }
      }
      
      if (place != null) {
        for (String part : place.split(":")) {
          part = part.trim();
          if (part.length() == 0) continue;
          if (data.strCity.length() == 0 && isCity(data.strAddress)) {
            data.strCity = data.strAddress;
            data.strAddress = "";
            parseAddress(part, data);
          } else if (data.strPlace.length() == 0 && BOUND_PTN.matcher(part).matches()) {
            data.strAddress = append(data.strAddress, " ", part);
          } else {
            data.strPlace = append(data.strPlace, " - ", stripFieldStart(part.trim(), "@"));
          }
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE CITY";
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("UNKNOWN")) return;
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Billings County

    // Cities
    "MEDORA",

    // Unorganized Territories
    "NORTH BILLINGS",
    "SOUTH BILLINGS",

    // Unincorporated communities
    "FAIRFIELD",
    "GORHAM",
    "FRYBURG",
    "SCORIA POINT CORNER",
    "SIX MILE CORNER",
    "SULLY SPRINGS",
    
    // Divide County
    
    // Cities
    "CROSBY",
    "NOONAN",
    "AMBROSE",
    "FORTUNA",

    // Townships
    "ALEXANDRIA",
    "AMBROSE",
    "BLOOMING PRAIRIE",
    "BLOOMING VALLEY",
    "BORDER",
    "BURG",
    "CLINTON",
    "COALFIELD",
    "DANEVILLE",
    "DE WITT",
    "ELKHORN",
    "FERTILE VALLEY",
    "FILLMORE",
    "FRAZIER",
    "FREDERICK",
    "GARNET",
    "GOOSENECK",
    "HAWKEYE",
    "HAYLAND",
    "LINCOLN VALLEY",
    "LONG CREEK",
    "MENTOR",
    "PALMER",
    "PLUMER",
    "SIOUX TRAIL",
    "SMOKY BUTTE",
    "STONEVIEW",
    "TROY",
    "TWIN BUTTE",
    "UPLAND",
    "WESTBY",
    "WRITING ROCK",

    // Unincorporated communities
    "ALKABO",
    "BOUNTY",
    "COLGAN",
    "JUNO",
    "KERMIT",
    "PAULSON",
    "STADY ",
    
    // Kidder County
    
    // Cities
    "STEELE",
    "TAPPEN",
    "TUTTLE",
    "PETTIBONE",
    "DAWSON",
    "ROBINSON",

    // Unincorporated communities
    "CRYSTAL SPRINGS",
    "LADOGA",
    "LAKE WILLIAMS",

    // Townships
    "ALLEN",
    "ATWOOD",
    "BAKER",
    "BUCKEYE",
    "BUNKER",
    "CHESTINA",
    "CLEAR LAKE",
    "CROWN HILL",
    "CRYSTAL SPRINGS",
    "EXCELSIOR",
    "FRETTIM",
    "GRAF",
    "HAYNES",
    "LAKE WILLIAMS",
    "MANNING",
    "MERKEL",
    "NORTHWEST",
    "PEACE",
    "PETERSVILLE",
    "PETTIBONE",
    "PLEASANT HILL",
    "QUINBY",
    "REXINE",
    "ROBINSON",
    "SIBLEY",
    "STEWART",
    "TANNER",
    "TAPPEN",
    "TUTTLE",
    "VALLEY",
    "VERNON",
    "WALLACE",
    "WEISER",
    "WESTFORD",
    "WILLIAMS",
    "WOODLAWN",

    // Unorganized territories
    "KICKAPOO",
    "SOUTH KIDDER",
    
    // Burleigh County
    "DRISCOLL",
    
    // Golden Valley County
    "BEACH",
    
    // LaMoure County
    "JUD",
    
    // McHenry County
    "DRAKE",
    
    // McKenzie County
    "GRASSY BUTTE",
    "WATFORD CITY",
    
    // Hettinger County
    "NEW ENGLAND",
    
    // Stark County
    "BELFIELD",
    "DISCKINSON",
    "SOUTH HEART",
    
    // Stutsman County
    "MEDINA",
    "WOODWORTH"
  }; 
}
