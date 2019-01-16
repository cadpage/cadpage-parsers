package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MIEmmetCountyParser extends DispatchOSSIParser {
  
  public MIEmmetCountyParser() {
    this("EMMET COUNTY", "MI");
  }
  
  MIEmmetCountyParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
           "ID?:( CANCEL ADDR! CITY? | FYI ( ADDR/Z CITY CALL | ADDRCITY CALL | CALL SRC ADDR/Z! CITY? | CALL ADDR/Z X/Z CITY | CALL ADDR/Z! CITY | CADDR1 CADDR2 ) ) SRC? INFO+");
  }
  
  @Override
  public String getAliasCode() {
    return "MIEmmetCounty";
  }
  
  @Override
  public String getFilter() {
    return "CAD@cce911.com,8329061348";
  }
  
  private static final Pattern CAD_MARKER = Pattern.compile("(?:\\d+:)?CAD:");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!CAD_MARKER.matcher(body).lookingAt()) body = "CAD:" + body;
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0, pt).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL", true);
    if (name.equals("CADDR1")) return new CallAddressField();
    if  (name.equals("CADDR2")) return new SkipField();
    if (name.equals("SRC")) return new MySourceField();
    return super.getField(name);
  }
  
  // If we do not recognize a city code, it will be processed as a source field.
  // If the real source field finds the source field is already set, reject so
  // someone will bring the missing city code to our notice
  private class MySourceField extends SourceField {
    public MySourceField() {
      super("[A-Z]{3,4}", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 0 && data.strSource.length() > 0) abort();
      super.parse(field, data);
    }
  }
  
  // Call and address fields can occur in either order.  Usually there is a city
  // field in there to positively identify which is which.  But in rare cases we
  // have nothing to go on other than the fields themselves.  When we do, that is
  // when we have to use the CallAddress1 and 2 fields.
  private static final  Pattern CALL_PTN = Pattern.compile("[^\\d&/]+");
  private class CallAddressField extends Field {
    @Override
    public void parse(String field, Data data) {
      String nextFld = getRelativeField(+1);
      if (field.length() == 0 || nextFld.length() == 0) abort();
      if (checkAddress(field) > checkAddress(nextFld)) {
        String tmp = field;
        field = nextFld;
        nextFld = tmp;
      }
      data.strCall = field;
      parseAddress(nextFld, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CALL";
    }
    
    private int checkAddress(String field) {
      int result = MIEmmetCountyParser.this.checkAddress(field);
      if (result == 0 && CALL_PTN.matcher(field).matches()) result = -1;
      return result;
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      
      // Charlevoix County
      "BC",   "BOYNE CITY",
      "BF",   "BOYNE FALLS",
      "CX",   "CHARLEVOIX",
      "EJ",   "EAST JORDAN",

      "BVT",  "BOYNE VALLEY TWP",
      "BYT",  "BOYNE CITY",
      "CHN",  "CHANDLER TWP",
      "CXT",  "CHARLEVOIX TWP",
      "EVL",  "EVELINE TWP",
      "EVG",  "EVANGELINE TWP",
      "HDS",  "HUDSON",
      "MRN",  "MARION TWP",
      "NRW",  "NORWOOD",
      "PNT",  "PEAINE TWP",
      "STH",  "SOUTH ARM TWP",
      "STJ",  "ST JAMES TWP",
      "WLS",  "WILSON TWP",
      
      // Cheboygan County
      "AFT",  "AFTON",
      "ALH",  "ALOHA TWP",
      "BGR",  "BEAUGRAND TWP",
      "BNT",  "BENTON TWP",
      "BRT",  "BURT TWP",
      "CH",  "CHEBOYGAN CITY",
      "CHB",  "CHEBOYGAN",
      "ELL",  "ELLIS TWP",
      "FRS",  "FOREST TWP",
      "GRN",  "GRANT TWP",
      "HBR",  "HEBRON TWP",
      "IND",  "INDIAN RIVER",
      "INV",  "INVERNESS TWP",
      "KHL",  "KOEHLER TWP",
      "MC",  "MACKINAW CITY",
      "MLL",  "MULLET TWP",
      "MNR",  "MUNRO TWP",
      "NND",  "NUNDA TWP",
      "TSC",  "TUSCARORA TWP",
      "WLK",  "WALKER TWP",
      "WLV",  "WOLVERINE",
      "WVR",  "WAVERLY TWP",
      
      // Emmet County
      
      "BH",   "RESORT TWP",   // ???????
      "BV",   "BAY VIEW",
      "HS",   "HARBOR SPRINGS",

      "ALN",  "ALANSON",
      "BRC",  "BEAR CREEK TWP",
      "CNT",  "CENTER TWP",
      "CRS",  "CROSS VILLAGE TWP",
      "FRN",  "FRIENDSHIP TWP",
      "HYS",  "HAYES TWP",
      "LFL",  "LITTLEFIELD TWP",
      "LTB",  "LITTLE TRAVERSE BAY",
      "LTR",  "LITTLE TRAVERSE TWP",
      "MCK",  "MCKINLEY TWP",
      "MLR",  "MELROSE TWP",
      "MPL",  "MAPLE RIVER TWP",
      "PLL",  "PELLSTON",
      "PLS",  "PLEASANTVIEW TWP",
      "PTS",  "PETOSKEY",
      "RDM",  "READMOND TWP",
      "RST",  "RESORT TWP",
      "SPR",  "SPRINGVALE TWP",
      "WST",  "WEST TRAVERSE TWP",
      
      // Otsego County
      "VANDERBILT",  "VANDERBILT"
      

  });
}
