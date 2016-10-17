package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILLakeCountyAParser extends FieldProgramParser {
  
  public ILLakeCountyAParser() {
    super(CITY_CODES, "LAKE COUNTY", "IL",
          "Incident:ID! Nat:CALL! Loc:ADDR/y! Apt:APT! Grid:MAP! Trucks:UNIT!");
  }
  
  private static final Pattern MISSING_COLON_PTN = Pattern.compile("(?<=, Trucks)(?!:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("Apt:", ", Apt:");
    body = MISSING_COLON_PTN.matcher(body).replaceFirst(":");
    return parseFields(body.split(", "), data);
  }
  
  
  @Override
  public Field getField(String name) {
    if (name.equals("NEW_CALL")) return new SkipField("New Call", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_CITY_U_PTN = Pattern.compile("(-[A-Z]{2})U\\b");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // Remove extraneous 'U' from 3 characater city codes
      field = ADDR_CITY_U_PTN.matcher(field).replaceAll("$1");
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AH", "ARLINGTON HEIGHTS",
      "AL", "ALGONQUIN",
      "AN", "ANTIOCH",
      "BA", "BARRINGTON",
      "BB", "BANNOCKBURN",
      "BG", "BUFFALO GROVE",
      "BH", "BARRINGTON HILLS",
      "BP", "BEACH PARK",
      "BV", "BULL VALLEY",
      "CA", "CARY",
      "CL", "CRYSTAL LAKE",
      "CV", "CARPENTERSVILLE",
      "DF", "DEERFIELD",
      "DP", "DEER PARK",
      "DS", "DES PLAINES",
      "DU", "DUNDEE",
      "ED", "EAST DUNDEE",
      "EL", "ELGIN",
      "EV", "EVANSTON",
      "FL", "FOX LAKE",
      "FR", "FOX RIVER GROVE",
      "GB", "GILBERTS",
      "GC", "GLENCOE",
      "GF", "GOLF",
      "GL", "GREAT LAKES",
      "GO", "GREEN OAKS",
      "GR", "GRAYSLAKE",
      "GU", "GURNEE",
      "GV", "GLENVIEW",
      "HB", "HEBRON",
      "HE", "HOFFMAN ESTATES",
      "HH", "HOLIDAY HILLS",
      "HI", "HIGHWOOD",
      "HP", "HIGHLAND PARK",
      "HR", "HARVARD",
      "HV", "HAINESVILLE",
      "HW", "HAWTHORN WOODS",
      "IC", "INDIAN CREEK",
      "IL", "ISLAND LAKE",
      "IN", "INGLESIDE",
      "IV", "INVERNESS",
      "JB", "JOHNSBURG",
      "KD", "KILDEER",
      "KN", "KENOSHA",
      "KW", "KENILWORTH",
      "LA", "LAKE BLUFF",
      "LB", "LAKE BARRINGTON",
      "LF", "LAKE FOREST",
      "LG", "LONG GROVE",
      "LH", "LINDENHURST",
      "LI", "LIBERTYVILLE",
      "LK", "LAKE IN THE HILLS",
      "LM", "LAKEMOOR",
      "LS", "LINCOLNSHIRE",
      "LV", "LAKE VILLA",
      "LZ", "LAKE ZURICH",
      "MC", "MCHENRY",
      "MG", "MORTON GROVE",
      "ML", "MCCULLOM LAKE",
      "MP", "MT PROSPECT",
      "MT", "METTAWA",
      "MU", "MUNDELEIN",
      "NB", "NORTH BARRINGTON",
      "NC", "NORTH CHICAGO",
      "NF", "NORTHFIELD",
      "NK", "NORTHBROOK",
      "NL", "NILES",
      "NU", "NUNDA",
      "OH", "OAKWOOD HILLS",
      "OM", "OLD MILL CREEK",
      "PA", "PALATINE",
      "PB", "PORT BARRINGTON",
      "PC", "PARK CITY",
      "PG", "PRAIRIE GROVE",
      "PH", "PROSPECT HEIGHTS",
      "PL", "PADDOCK LAKE",
      "PP", "PLEASANT PRAIRIE",
      "RB", "ROUND LAKE BEACH",
      "RD", "RINGWOOD",
      "RG", "RINGWOOD",
      "RH", "ROUND LAKE HEIGHTS",
      "RI", "RICHMOND",
      "RL", "ROUND LAKE",
      "RM", "ROLLING MEADOWS",
      "RP", "ROUND LAKE PARK",
      "RW", "RIVERWOODS",
      "SB", "SOUTH BARRINGTON",
      "SG", "SPRING GROVE",
      "SH", "SLEEPY HOLLOW",
      "SK", "SKOKIE",
      "SL", "SILVER LAKE",
      "SM", "SCHAUMBURG",
      "SW", "STREAMWOOD",
      "TH", "THIRD LAKE",
      "TL", "TOWER LAKES",
      "TV", "TROUT VALLEY",
      "TW", "TWIN LAKES",
      "UN", "UNION",
      "VH", "VERNON HILLS",
      "VO", "VOLO",
      "WA", "WAUCONDA",
      "WD", "WADSWORTH",
      "WH", "WINTHROP HARBOR",
      "WK", "WAUKEGAN",
      "WL", "WHEELING",
      "WM", "WILMETTE",
      "WN", "WINNETKA",
      "WO", "WOODSTOCK",
      "ZI", "ZION"

  });
}
