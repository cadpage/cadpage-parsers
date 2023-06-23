package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCHydeCountyBParser extends DispatchOSSIParser {

  public NCHydeCountyBParser() {
    super(CITY_CODES, "HYDE COUNTY", "NC",
          "CALL ADDR ( PLACE CITY! | CITY? ) INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@darepublicsafety.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      for (String part : field.split("\n")) {
        if (part.startsWith("Dispatch Code:")) {
          data.strCode = part.substring(14).trim();
          String call = CALL_CODES.getCodeDescription(data.strCode);
          if (call != null) data.strCall = call;
          continue;
        }

        if (part.startsWith("Response:")) continue;

        data.strSupp = append(data.strSupp, "\n", part);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CODE CALL";
    }
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("ROANOKE ISLAND")) return "MANTEO";
    return city;
  }

  private static CodeTable CALL_CODES = new StandardCodeTable();

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AVN",   "AVON",
      "BUX",   "BUXTON",
      "CLB",   "COLUMBIA",
      "COL",   "COLINGTON",
      "CRES",  "CRESWELL",
      "DCK",   "DUCK",
      "EDEN",  "EDENTON",
      "ELK",   "EAST LAKE",
      "ENG",   "ENGELHARD",
      "FFD",   "FAIRFIELD",
      "FRI",   "FRISCO",
      "HAT",   "HATTERAS",
      "JARV",  "JARVISBURG",
      "KDH",   "KILL DEVIL HILLS",
      "KHP",   "KITTY HAWK",
      "MAN",   "MANTEO",
      "MNH",   "MANNS HARBOR",
      "MPT",   "MARTINS POINT",
      "NGH",   "NAGS HEAD",
      "OCK",   "OCRACOKE",
      "PAN",   "PANTEGO",
      "PLY",   "PLYMOUTH",
      "PON",   "PONZER",
      "RI",    "ROANOKE ISLAND",
      "ROD",   "RODANTHE",
      "SAL",   "SALVO",
      "SCR",   "SCRANTON",
      "SSH",   "SOUTHERN SHORES",
      "STP",   "STUMPY POINT",
      "SWQ",   "SWAN QUARTER",
      "WAN",   "WANCHESE",
      "WAV",   "WAVES"

  });

}
