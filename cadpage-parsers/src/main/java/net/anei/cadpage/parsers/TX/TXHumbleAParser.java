package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXHumbleAParser extends FieldProgramParser {
  
  // Pattern to find a single dash delimiter followed by a numeric ID token
  private static Pattern TRAIL_DELIM = Pattern.compile(" - (?=\\d+$ *)");
  
  // Pattern to identify status messages
  private static Pattern INCIDENT_PTN = Pattern.compile(" *Incident: \\d{9} *| *\\[ \\d{9} \\] *");
  
  public TXHumbleAParser() {
    super(CITY_LIST, "HUMBLE", "TX",
          "UNIT? CODE? CALL CALL2? ADDRCITY! ( Box_#:BOX Cross_STS:X | Map:MAP PLACE Xst's:X Units:UNIT ID | Xst's:X Bldg:PLACE Key_Map:MAP! Box_#:BOX | UNIT KM:MAP Xst's:X )");
  }
  
  @Override
  public String getFilter() {
    return "msg@cfmsg.com,alert@cfmsg.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Strip off leading single dash
    // If last field delimiter is a single dash, turn it to a double dash
    Parser p = new Parser(subject);
    data.strSource = p.get('|');
    if (data.strSource.equalsIgnoreCase("Chief ALT") ||  data.strSource.equalsIgnoreCase("Chief Alert")) {
      data.strSource = p.get('|');
    }
    if (!data.strSource.endsWith("FIRE") && !data.strSource.endsWith("EMS")) return false;
    body = stripFieldStart(body,  "- ");
    body = TRAIL_DELIM.matcher(body).replaceFirst(" - ");
    
    // Split line into double dash delimited fields and process them
    String[] flds = body.split(" -+ ");
    if (INCIDENT_PTN.matcher(flds[0]).matches()) return false;
    return parseFields(flds, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("(?:[A-Z]-)?[A-Z]+\\d+(?: .*)?");
    if (name.equals("CODE")) return new CodeField("\\d+[A-Z]\\d+[A-Z]?", true);
    if (name.equals("CALL2")) return new Call2Field();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
  
  private static final Pattern CALL2_EXCL_PTN = Pattern.compile("[\\d/&,]");
  private class Call2Field extends CallField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // Reject anything containing a digit, slash or ampersand
      if (CALL2_EXCL_PTN.matcher(field).find()) return false;
      
      // It still might be a street name so check to see if it is an address
      if (isValidAddress(field)) return false;
      
      // Go for it
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      field = p.get();
      if (city.equals("TX")) city = p.getLastOptional(',');
      if (city.length() > 0) {
        parseAddress(field, data);
        data.strCity = city;
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      }
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
   
    "HARRIS COUNTY",
    
    "// Cities",
    "BAYTOWN",
    "BELLAIRE",
    "BUNKER HILL VILLAGE",
    "DEER PARK",
    "EL LAGO",
    "FRIENDSWOOD",
    "GALENA PARK",
    "HEDWIG VILLAGE",
    "HILSHIRE VILLAGE",
    "HOUSTON",
    "HUMBLE",
    "HUNTERS CREEK VILLAGE",
    "JACINTO CITY",
    "JERSEY VILLAGE",
    "KATY",
    "LA PORTE",
    "LEAGUE CITY",
    "MISSOURI CITY",
    "MORGAN'S POINT",
    "NASSAU BAY",
    "PASADENA",
    "PEARLAND",
    "PINEY POINT VILLAGE",
    "SEABROOK",
    "SHOREACRES",
    "SOUTH HOUSTON",
    "SOUTHSIDE PLACE",
    "SPRING VALLEY VILLAGE",
    "STAFFORD",
    "TAYLOR LAKE VILLAGE",
    "TOMBALL",
    "WALLER",
    "WEBSTER",
    "WEST UNIVERSITY PLACE",

    "//Census-designated places",
    "ALDINE",
    "ATASCOCITA",
    "BARRETT",
    "CHANNELVIEW",
    "CINCO RANCH",
    "CLOVERLEAF",
    "CROSBY",
    "HIGHLANDS",
    "MISSION BEND",
    "SHELDON",
    "SPRING",
    "THE WOODLANDS"

  };
}
