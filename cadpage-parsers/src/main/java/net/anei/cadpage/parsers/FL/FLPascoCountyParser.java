package net.anei.cadpage.parsers.FL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class FLPascoCountyParser extends DispatchOSSIParser {

  public FLPascoCountyParser() {
    super(CITY_CODES, "PASCO COUNTY", "FL",
          "CALL ADDR CITY MAP UNIT! X? X? PLACE END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField() || field.startsWith("ACCESS RD")) {
        super.parse(field, data);
        return true;
      }
      else {
        return super.checkParse(field, data);
      }
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ARP",  "ARIPEKA",
      "BEV",  "BEVERLY HILLS",
      "BKV",  "BROOKSVILLE",
      "BLT",  "BLANTON",
      "BRAN", "BRANDON",
      "BUSH", "BUSHNELL",
      "BYP",  "BAYONET POINT",
      "CENT", "CENTERHILL",
      "CIT",  "CITRUS SPRINGS",
      "CLW",  "CLEARWATER",
      "CRR",  "CRYSTAL RIVER",
      "CRS",  "CRYSTAL SPRINGS",
      "DAR",  "DARBY",
      "DC",   "DADE CITY",
      "DCI",  "DADE CITY",
      "ELF",  "ELFERS",
      "GR",   "GROTON",
      "H",    "HILLSBOROUGH COUNTY",
      "HER",  "HERNANDO BEACH",
      "HERN", "HERNANDO COUNTY",
      "HILL", "HILLSBOROUGH COUNTY",
      "HOL",  "HOLIDAY",
      "HOM",  "HOMOSASSA",
      "HUD",  "HUDSON",
      "INV",  "INVERNESS",
      "KA",   "KATHLEEN",
      "LAC",  "LACOOCHEE",
      "LAKE", "LAKELAND",
      "LEC",  "LECANTO",
      "LIT",  "LITHIA",
      "LKP",  "LAKE PANASOFKEE",
      "LOL",  "LAND O LAKES",
      "LRG",  "LARGO",
      "LUM",  "LUMBERTON",
      "LUT",  "LUTZ",
      "MSY",  "MASARYKTOWN",
      "NPR",  "NEW PORT RICHEY",
      "NPRI", "NEW PORT RICHEY",
      "ODS",  "ODESSA",
      "OLD",  "OLDSMAR",
      "ORL",  "ORLANDO",
      "OZO",  "OZONA",
      "PAH",  "PALM HARBOR",
      "PAS",  "PASCO COUNTY",
      "PIN",  "PINELLAS COUNTY",
      "PLAC", "PLANT CITY",
      "POLK", "POLK COUNTY",
      "PR",   "PORT RICHEY",
      "PRI",  "PORT RICHEY",
      "RCL",  "RICHLAND",
      "RIV",  "RIVERVIEW",
      "RM",   "RIDGE MANOR",
      "SAF",  "SAFETY HARBOR",
      "SAN",  "SAN ANTONIO",
      "SPH",  "SPRING HILL",
      "STJ",  "ST JOE",
      "STL",  "ST LEO",
      "STP",  "SAINT PETERSBURG",
      "T",    "TAMPA",
      "TALL", "TALLAHASSEE",
      "THO",  "THONOTOSASSA",
      "TPA",  "TAMPA",
      "TPS",  "TARPON SPRINGS",
      "TRI",  "TRILBY",
      "TRL",  "TRILACOOCHEE",
      "TRN",  "TRINITY",
      "VAL",  "VALRICO",
      "WEB",  "WEBSTER",
      "WES",  "WESLEY CHAPEL",
      "WW",   "WEEKI WACHEE",
      "ZHL",  "ZEPHYRHILLS",
      "ZHLI", "ZEPHYRHILLS"

  });

}
