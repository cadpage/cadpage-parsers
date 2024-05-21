package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXHillCountyParser extends DispatchA19Parser {

  public TXHillCountyParser() {
    super("HILL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "cad@co.hill.tx.us,911@co.hill.tx.us,CallOutAlert@co.hill.tx.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCall = convertCodes(data.strCall, CALL_CODES);
    return true;
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "AccidentMajor",   "Major Accident",
      "AccidentMinor",   "Minor Accident",
      "Ambulance Emerg", "Medical Emergency",
      "FIREGRASS",       "Grass Fire",
      "FireGrass",       "Grass Fire",
      "Traffic Stop",    "Assist PD"
  });

}
