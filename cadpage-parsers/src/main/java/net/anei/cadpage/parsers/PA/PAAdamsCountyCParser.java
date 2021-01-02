package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

public class PAAdamsCountyCParser extends DispatchA9Parser {

  public PAAdamsCountyCParser() {
    super("ADAMS COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "911@co.armstrong.pa.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCode = data.strCall;
    data.strCall = convertCodes(data.strCode, CALL_CODES);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "F21",      "CALL THE CENTER",
      "F7MIN",    "7 MINUTE BACKUP – TURNOVER",
      "FAFALR",   "FIRE ALARM",
      "FBOMB",    "BOMB INCIDENT",
      "FBRUSH",   "BRUSH FIRE",
      "FCOALM",   "CO ALARM",
      "FFP",      "FIRE POLICE / TRAFFIC CONTROL",
      "FGAS",     "GAS/ODOR INVESTIGATION OUTSIDE",
      "FLZ",      "LANDING ZONE",
      "FMEDAS",   "MEDICAL ASSIST – ASSIST EMS",
      "FMISC",    "MISCELLANEOUS INCIDENT",
      "FMVA",     "VEHICLE ACCIDENT NO INJURIES",
      "FOTRES",   "OTHER RESCUES",
      "FSRCH",    "SEARCH DETAIL",
      "FSTR1",    "STRUCTURE FIRE",
      "FSTR2",    "STRUCTURE FIRE – 2ND ALARM",
      "FSTR3",    "STRUCTURE FIRE – 3RD ALARM",
      "FSTR4",    "STRUCTURE FIRE – 4TH ALARM",
      "FSTR5",    "STRUCTURE FIRE – 5TH ALARM",
      "FTRENCH",  "TRENCH RESCUE",
      "FVEH",     "VEHICLE FIRE",
      "FWIRE",    "WIRES DOWN ",
      "FWORK",    "WORKING FIRE",
      "FWTRES",   "WATER RESCUE",
      "VAENT",    "VEHICLE ACCIDENT W/ ENTRAPMENT",
      "VAINJ",    "VEHICLE ACCIDENT W/ INJURIES"
  });

}
