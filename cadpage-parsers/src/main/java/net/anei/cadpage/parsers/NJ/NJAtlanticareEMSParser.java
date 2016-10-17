package net.anei.cadpage.parsers.NJ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;


public class NJAtlanticareEMSParser extends DispatchProQAParser {
  
  public NJAtlanticareEMSParser() {
    super(CITY_LIST, "", "NJ",
           "UNKNOWN? ID! CALL CALL/L+? ADDR XPLACE+? CITY! TIME!");
  }
  
  @Override
  public String getFilter() {
    return "medcom@medcom.com,medcom@atlanticare.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Parser p = new Parser(subject);
    data.strSource = p.get(" - ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNKNOWN")) return new SkipField("<Unknown>");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("XPLACE")) return new CrossPlaceField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d");
    return super.getField(name);
  }
  
  private static final Pattern ADDR_PTN = Pattern.compile("\\d+ .*|.*[/&].*");
  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (ADDR_PTN.matcher(field).matches()) {
        super.parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("\\d+[A-Z]?");
  private class CrossPlaceField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      if (field.toUpperCase().startsWith("X ")) {
        data.strCross = append(data.strCross, " & ", field.substring(2).trim());
      } else if (APT_PTN.matcher(field).matches()) {
        data.strApt = append(data.strApt, "-", field);
      } else if (isValidAddress(field)) {
        data.strCross = append(data.strCross, " & ", field);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }
  
  private static String[] CITY_LIST = new String[] {
  // Atlantic County
    "ABSECON",    
    "ATLANTIC",
    "ATLANTIC CITY",
    "BRIGANTINE",
    "BUENA VISTA TWP",
    "BUENA VISTA TOWNSHIP",
    "COLLINGS LAKES",
    "RICHLAND",
    "BUENA",
    "CORBIN",
    "EGG HARBOR",
    "EGG HARBOR TWP",
    "EGG HARBOR TOWNSHIP",
    "ESTELL MANOR",
    "FOLSOM",
    "GALLOWAY TWP",
    "GALLOWAY TOWNSHIP",
    "POMONA",
    "HAMILTON TWP",
    "MAYS LANDING",
    "HAMMONTON",
    "LINWOOD",
    "LONGPORT",
    "MARGATE",
    "MULLICA TWP",
    "MULLICA TOWNSHIP",
    "ELWOOD-MAGNOLIA",
    "NORTHFIELD",
    "PLEASANTVILLE",
    "PORT REPUBLIC",
    "SOMERS POINT",
    "VENTNOR",
    "WEYMOUTH TWP",
    "WEYMOUTH TOWNSHIP",
    
  // Cape May County
    "AVALON",
    "CAPE MAY POINT",
    "CAPE MAY",
    "DENNIS TWP",
    "DENNIS TOWNSHIP",
    "DENNISVILLE",
    "OCEAN VIEW",
    "SOUTH DENNIS",
    "LOWER TWP",
    "LOWER TOWNSHIP",
    "DIAMOND BEACH",
    "ERMA",
    "NORTH CAPE MAY",
    "VILLAS",
    "MIDDLE TWP",
    "MIDDLE TOWNSHIP",
    "CAPE MAY COURT HOUSE",
    "GOSHEN",
    "GREEN CREEK",
    "RIO GRANDE",
    "WHITESBORO-BURLEIGH",
    "NORTH WILDWOOD",
    "OCEAN",
    "SEA ISLE",
    "STONE HARBOR",
    "UPPER TWP",
    "UPPER TOWNSHIP",
    "BEESLEYS POINT",
    "MARMORA",
    "STRATHMERE",
    "TUCKAHOE",
    "WEST CAPE MAY",
    "WEST WILDWOOD",
    "WILDWOOD CREST",
    "WILDWOOD",
    "WOODBINE",
  };
}
