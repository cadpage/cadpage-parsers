package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Oroville, CA
 */
public class CAOrovilleParser extends DispatchA20Parser {
  
  public CAOrovilleParser() {
    super(CALL_CODES, "OROVILLE", "CA", A20_UNIT_LABEL_REQ);
  }
  
  @Override
  public String getFilter() {
    return "@OROPD.ORG";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "FDASSIST", "PUBLIC ASSIST",
      "FDFIRE1",  "VEHICLE/OTHER FIRE",
      "FDFIRE2",  "VEHICLE FIRE (OCCUPIED)",
      "FDFIRE3",  "VEGETATION",
      "FDFIRE4",  "STRUCTURE",
      "FDFIRE5",  "COMMERCIAL STRUCTURE",
      "FDGEN",    "LEVEL 1 STAFFING",
      "FDHAZD3",  "FLOODING",
      "FDHAZMA1", "MINOR HAZMAT",
      "FDHAZMA2", "MAJOR HAZMAT",
      "FDHAZMAT", "HAZARDOUS CONDITION",
      "FDINVEST", "ALARM SOUNDING/SMOKE CHECK",
      "FDMED",    "MEDICAL",
      "FDMED2",   "MEDICAL (CPR)",
      "FDRESCUE", "TECHNICAL RESCUE",
      "FDTC1",    "T/C",
      "FDTC2",    "T/C POSSIBLE MCI",
      "FDW/ALAR", "WATER FLOW ALARM",
      "FDWATER",  "WATER RESCUE"
  });
}
