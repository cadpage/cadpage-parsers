package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class SCPickensCountyAParser extends DispatchSPKParser {
  
  private static final Pattern BETWEEN_PTN = Pattern.compile("(.*) BETWEEN (.*) AND (.*)", Pattern.CASE_INSENSITIVE);
  
  public SCPickensCountyAParser() {
    super("PICKENS COUNTY", "SC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    Matcher match = BETWEEN_PTN.matcher(data.strAddress);
    if (match.matches()) {
      data.strAddress = match.group(1).trim();
      data.strCross = match.group(2).trim() + " / " + match.group(3).trim();
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("ADDR", "ADDR X"); 
  }
  
}
