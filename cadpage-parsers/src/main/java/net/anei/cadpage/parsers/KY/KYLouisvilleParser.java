package net.anei.cadpage.parsers.KY;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

/**
 * Louisville, KY
 */
public class KYLouisvilleParser extends FieldProgramParser {
  
  public KYLouisvilleParser() {
    super(CITY_CODES, "LOUISVILLE", "KY",
           "Location:ADDR/S? JTN:PLACE? TYPE_CODE:CODE! SUB_TYPE:CALL! TIME:TIME? Comments:INFO INFO+");
//    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "MetroSafeTech@louisvilleky.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      String call = CALL_CODES.getCodeDescription(field);
      if (call != null) data.strCall = call; 
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("default")) return;
      if (field.length() == 0) return;
      if (data.strCall.length() == 0) {
        data.strCall = field;
      } else {
        data.strCall = data.strCall + " (" + field + ")";
      }
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        if (data.strPlace.startsWith("@")) data.strPlace = data.strPlace.substring(1).trim();
        field = field.substring(0,pt).trim();
      }
      Parser p = new Parser(field);
      data.strApt = append(p.getLastOptional(','), "-", p.getLastOptional(';'));
      field = p.get();
      if (field.startsWith("@")) field = field.substring(1).trim();
      super.parse(field, data);
      pt = data.strCity.indexOf('/');
      if (pt >= 0) {
        data.strPlace = append(data.strPlace, " - ", data.strCity.substring(0,pt));
        data.strCity = data.strCity.substring(pt+1);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      DispatchProQAParser.parseProQAData(false, field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE INFO";
    }
  }

  private static CodeTable CALL_CODES = new StandardCodeTable();
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "LEFLORE AVE",                          "+38.130719,-85.875992",
      "MISTY LN",                             "+38.130724,-85.876780",
      "TALLAHATCHEE ST",                      "+38.130719,-85.875992",
      "YAZOO ST",                             "+38.130656,-85.876378"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANC",  "ANCHORAGE",
      "AUD",  "AUDUBON PARK",
      "BAN",  "BANCROFT",
      "BAR",  "BARBOURMEADE",
      "BELM", "BELLEMEADE",
      "BELW", "BELLEWOOD",
      "BPOI", "BROECK POINTE",
      "BRFA", "BROWNSBORO FARM",
      "BRM",  "BLUE RIDGE MANOR",
      "BRVI", "BEECHWOOD VILLAGE",
      "BRWD", "BRIARWOOD",
      "BVIL", "BEECHWOOD VILLAGE",
      "CAMB", "CAMBRIDGE",
      "COLD", "COLDSTREAM",
      "CROS", "CROSSGATE",
      "CSID", "CREEKSIDE",
      "DHIL", "DOUGLASS HILLS",
      "DRHI", "DRUID HILLS",
      "FHIL", "FOREST HILLS",
      "FINC", "FINCASTLE",
      "GCRE", "GOOSE CREEK",
      "GMOR", "GRAYMOOR-DEVONDALE",
      "GSPR", "GREEN SPRING",
      "GVEW", "GLENVIEW",
      "GVHI", "GLENVIEW HILLS",
      "GVMA", "GLENVIEW MANOR",
      "HCRE", "HOLLOW CREEK",
      "HDAL", "HILLS AND DALES",
      "HHIL", "HICKORY HILL",
      "HOAC", "HOUSTON ACRES",
      "HTCK", "HERITAGE CREEK",
      "HUAC", "HURSTBOURNE ACRES",
      "HURS", "HURSTBOURNE",
      "HVIL", "HOLLYVILLA",
      "INH",  "INDIAN HILLS",
      "JTN",  "JEFFERSONTOWN",
      "KING", "KINGSLEY",
      "LINC", "LINCOLNSHIRE",
      "LPLA", "LANGDON PLACE",
      "LVIL", "LOUISVILLE",
      "LYND", "LYNDON",
      "LYNN", "LYNNVIEW",
      "MCRE", "MANOR CREEK",
      "MEDE", "MEADOWVIEW ESTATES",
      "MEDV", "MEADOWVALE",
      "MEVL", "MEADOW VALE",
      "MFAR", "MEADOWBROOK FARM",
      "MHES", "MARYHILL ESTATES",
      "MHIL", "MURRAY HILL",
      "MOOR", "MOORLAND",
      "MOVL", "MOCKINGBIRD VALLEY",
      "MTWN", "MIDDLETOWN",
      "MVES", "MEADOWVIEW ESTATES",
      "NEST", "NORBOURNE ESTATES",
      "NORT", "NORTHFIELD",
      "NOWD", "NORWOOD",
      "OBPL", "OLD BROWNSBORO PLACE",
      "PHIL", "POPLAR HILLS",
      "PLAN", "PLANTATION",
      "PRO",  "PROSPECT",
      "PWVI", "PARKWAY VILLAGE",
      "RFIE", "ROLLING FIELDS",
      "RHIL", "ROLLING HILLS",
      "RICH", "RICHLAWN",
      "RVWD", "RIVERWOOD",
      "SGAR", "SENECA GARDENS",
      "SHV",  "SHIVELY",
      "SMAN", "STRATHMOOR MANOR",
      "SMIL", "SPRING MILL",
      "SPVW", "SOUTH PARK VIEW",
      "SRPK", "ST REGIS PARK",
      "STM",  "ST MATTHEWS",
      "SVAL", "SPRING VALLEY",
      "SVIL", "STRATHMOOR VILLAGE",
      "SYCA", "SYCAMORE",
      "TBRO", "TEN BROECK",
      "THIL", "THORNHILL",
      "UNK",  "UNKNOWN",
      "WB",   "WEST BUECHEL",
      "WDHI", "WOODLAND HILLS",
      "WDPK", "WOODLAWN PARK",
      "WDWD", "WILDWOOD",
      "WELL", "WELLINGTON",
      "WHIL", "WINDY HILLS",
      "WORH", "WORTHINGTON HILLS",
      "WTPK", "WATTERSON PARK",
      "WTWD", "WESTWOOD"


  });
}
