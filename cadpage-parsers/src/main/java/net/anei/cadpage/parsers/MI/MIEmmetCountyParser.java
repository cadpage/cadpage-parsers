package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MIEmmetCountyParser extends DispatchOSSIParser {

  public MIEmmetCountyParser() {
    this("EMMET COUNTY", "MI");
  }

  MIEmmetCountyParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
           "ID?:FYI+? ( CANCEL ADDR! CITY? INFO/N+ " +
                     "| CALL ( GPS1 GPS2 | ) ADDR ( CITY! ( SRC X+? ( END | PHONE END | NAME PHONE END | X/Z NAME PHONE! END | X/Z X/Z NAME PHONE! END | X/Z+? NAME! END ) " +
                                                         "| INFO/N+? SRC X+? ( END | PHONE END | NAME PHONE END | X/Z NAME PHONE! END | X/Z X/Z NAME PHONE! END | X/Z+? NAME! END ) " +
                                                         ") " +
                                                 "| X1 X/Z? CITY! INFO/N+? SRC NAME PHONE END " +
                                                 "| INFO/N+? ( CITY SRC | SRC ) X+? ( END | PHONE END | NAME PHONE END | X/Z NAME PHONE! END | X/Z X/Z NAME PHONE! END | X/Z+? NAME! END ) " +
                                                 ") " +
                     ")");
  }

  @Override
  public String getAliasCode() {
    return "MIEmmetCounty";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String getFilter() {
    return "CAD@cce911.com,8329061348";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern CAD_MARKER = Pattern.compile("(?:\\d+:)?CAD:");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!CAD_MARKER.matcher(body).lookingAt()) body = "CAD:" + body;
    body = stripFieldEnd(body, "\nText STOP to opt out");
    return super.parseMsg(body, data);
  }

  private static final Pattern SRC_PTN = Pattern.compile("(?!MALE|PAGE|POSS)[A-Z]{4}|CCE|CDA");

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("SRC")) return new SourceField(SRC_PTN, true);
    if (name.equals("X1")) return new MyCross1Field();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PHONE")) return new PhoneField("\\d{10}", true);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private static final Pattern CROSS1_PTN = Pattern.compile("[ A-Z]+");
  private class MyCross1Field extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (super.checkParse(field, data)) return true;
      if (!CROSS1_PTN.matcher(field).matches()) return false;
      if (!isCityField(+2) && !isCityField(+1)) return false;
      parse(field, data);
      return true;
    }

    private boolean isCityField(int offset) {
      return (isCity(getRelativeField(offset)) &&
              !SRC_PTN.matcher(getRelativeField(offset+1)).matches());
    }
  }

  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCity.length() > 0) return false;
      return super.checkParse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("US CG")) return false;
      return super.checkParse(field, data);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("BOYNE VALLEY TWP")) city = "BOYNE VALLEY TOWNSHIP";
    return city;
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
      "CH",   "CHEBOYGAN CITY",
      "CHB",  "CHEBOYGAN",
      "ELL",  "ELLIS TWP",
      "FRS",  "FOREST TWP",
      "GRN",  "GRANT TWP",
      "HBR",  "HEBRON TWP",
      "IND",  "INDIAN RIVER",
      "INV",  "INVERNESS TWP",
      "KHL",  "KOEHLER TWP",
      "MC",   "MACKINAW CITY",
      "MCT",  "MACKINAW TWP",
      "MLL",  "MULLET TWP",
      "MNR",  "MUNRO TWP",
      "MNT",  "MENTOR TWP",
      "NND",  "NUNDA TWP",
      "TSC",  "TUSCARORA TWP",
      "WLK",  "WALKER TWP",
      "WLV",  "WOLVERINE",
      "WLM",  "WILMOT TWP",
      "WVR",  "WAVERLY TWP",

      // Emmet County
      "BH",   "RESORT TWP",   // ???????
      "BV",   "BAY VIEW",
      "HS",   "HARBOR SPRINGS",
      "MC",   "MACKINAW CITY",

      "ALN",  "ALANSON",
      "BLS",  "BLISS TWP",
      "BRC",  "BEAR CREEK TWP",
      "CNT",  "CENTER TWP",
      "CRP",  "CARP LAKE",
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
