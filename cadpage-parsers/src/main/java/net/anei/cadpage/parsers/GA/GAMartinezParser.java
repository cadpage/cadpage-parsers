package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class GAMartinezParser extends MsgParser {
  
  public GAMartinezParser() {
    super("MARTINEZ", "GA");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@mvfd.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    String[] lines = body.split("\n");
    if ( !(lines.length == 3) || ! lines[2].startsWith("DO NOT CALL DISPATCH!!!!")) return false;
    
    data.strCall = lines[0].trim();
    
    String sAddr = lines[1].trim();
    if (sAddr.startsWith("Address:")) sAddr = sAddr.substring(8);
    if (sAddr.startsWith("ACROSS FROM ")) {
      data.strPlace = sAddr.substring(0,11).trim();
      sAddr = sAddr.substring(12).trim();
    }
    int ipt = sAddr.indexOf(" OFF ");
    if (ipt >= 0) {
      data.strCross = sAddr.substring(ipt+1).trim();
      sAddr = sAddr.substring(0,ipt).trim();
    }
    parseAddress(sAddr, data);
    
    return true;
  }
}
