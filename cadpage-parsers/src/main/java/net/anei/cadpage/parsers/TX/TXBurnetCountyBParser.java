package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBurnetCountyBParser extends FieldProgramParser {

  public TXBurnetCountyBParser() {
    super(CITY_CODES, "BURNET COUNTY", "TX",
          "Msg_ID:SKIP! SRC MAP CALL ADDRCITY UNIT! INFO/N+? ID X/Z? DATETIME END");
  }

  @Override
  public String getFilter() {
    return "support@toimagine.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("INCIDENT INFO")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("MAP")) return new MapField("[A-Z]+\\d+", true);
    if (name.equals("ID")) return new IdField("[EF]\\d+", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Intersection of:")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d");
  private static final Pattern DIGIT_PTN = Pattern.compile("\\d");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (DATE_TIME_PTN.matcher(field).matches()) {
        super.parse(field,  data);
        return true;
      } else {
        field = DIGIT_PTN.matcher(field).replaceAll("N");
        return "NN/NN/NNNN NN:NN".startsWith(field);
      }

    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AD",   "ADDISON",
      "ADM",  "ANDERSON MILL",
      "AU",   "AUSTIN",
      "BA",   "BASTROP",
      "BC",   "BEE CAVES",
      "BCK",  "BARTON CREEK",
      "BD",   "BUCHANAN DAM",
      "BG",   "BRIGGS",
      "BL",   "BLUFFTON",
      "BLA",  "BLANCO",
      "BLT",  "BELTON",
      "BND",  "BEND",
      "BR",   "BRIARCLIFF",
      "BRT",  "BARTLETT",
      "BS",   "BLUFF SPRINGS",
      "BT",   "BERTRAM",
      "BTC",  "BERTRAM C",
      "BU",   "BURNET",
      "BUC",  "BURNET C",
      "BW",   "BROWNWOOD",
      "CA",   "CARROLLTON",
      "CC",   "CORPUS CHRISI",
      "CCV",  "COPPERAS COVE",
      "CE",   "CELE",
      "CK",   "CHEROKEE",
      "CL",   "CLICK",
      "CP",   "CEDAR PARK",
      "CR",   "CREEDMOOR",
      "CT",   "CASTELL",
      "CW",   "COTTONWOOD",
      "DH",   "DOUBLE HORN",
      "DNG",  "DING DONG",
      "DR",   "DEL RIO",
      "DV",   "DEL VALLE",
      "EL",   "ELGIN",
      "EP",   "EAGLE PASS",
      "ER",   "ELROY",
      "FB",   "FREDERICKSBURG",
      "FL",   "FLORENCE",
      "FM",   "FLOWER MOUND",
      "FR",   "FAIRLAND",
      "FTH",  "FORT HOOD",
      "GA",   "GANDY",
      "GAR",  "GARLAND",
      "GEO",  "GEORGETOWN",
      "GFD",  "GARFIELD",
      "GO",   "GOLIAD",
      "GR",   "GRANGER",
      "GS",   "GRANITE SHOALS",
      "GT",   "GEORGETOWN",
      "HAR",  "HARPER",
      "HB",   "HORSESHOE BAY",
      "HBC",  "HORSESHOE BAY C",
      "HDB",  "HUDSON BEND",
      "HEH",  "HEIDENHEIMER",
      "HH",   "HIGHLAND HAVEN",
      "HLD",  "HOLLAND",
      "HO",   "HOUSTON",
      "HRH",  "HARKER HEIGHTS",
      "HU",   "HUTTO",
      "HYE",  "HYE",
      "IR",   "IRVING",
      "JA",   "JARRELL",
      "JC",   "JOHNSON CITY",
      "JLV",  "JOLLYVILLE",
      "JO",   "JONAH",
      "JT",   "JONESTOWN",
      "KG",   "KINGSLAND",
      "KI",   "KIMBRO",
      "KLN",  "KILLEEN",
      "KMP",  "KEMPNER",
      "LA",   "LAKE VICTOR",
      "LCK",  "LOST CREEK",
      "LE",   "LEANDER",
      "LH",   "LIBERTY HILL",
      "LI",   "LITTIG",
      "LK",   "LAKEWAY",
      "LL",   "LLANO",
      "LMP",  "LAMPASAS",
      "LOM",  "LOMETA",
      "LRA",  "LTL RIVER ACAD",
      "LU",   "LUND",
      "LV",   "LAGO VISTA",
      "MA",   "MAHOMET",
      "MAX",  "MAXWELL",
      "MC",   "MANCHACA",
      "ME",   "MCNEIL",
      "MEQ",  "MESQUITE",
      "MF",   "MARBLE FALLS",
      "MFC",  "MARBLE FALLS C",
      "MM",   "MORMON MILL",
      "MN",   "MANOR",
      "MOF",  "MOFFAT",
      "MPR",  "MORGANS PNT RES",
      "MS",   "MARSHALL FORD",
      "MW",   "MEADOWLAKES",
      "NA",   "NARUNA",
      "NS",   "NEW SWEDEN",
      "NVL",  "NOLANVILLE",
      "OA",   "OAKALLA",
      "SP",   "SPICEWOOD"

  });

}
