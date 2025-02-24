package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBurnetCountyBParser extends FieldProgramParser {

  public TXBurnetCountyBParser() {
    super(CITY_CODES, "BURNET COUNTY", "TX",
          "Msg_ID:ID! SRC MAP CALL ADDRCITY UNIT! INFO/N+");
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
    return super.getField(name);
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
