package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSSumnerCountyParser extends DispatchA25Parser {

  public KSSumnerCountyParser() {
    super("SUMNER COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "sumner911cad@co.sumner.ks.us,donotreply@mulvane.us,CAD@mulvane.us,notsowpdd1@hotmail.com";
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|RM|LOT)\\b[:# ]*(.*)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    
    if (data.strCity.equalsIgnoreCase("County")) data.strCity = "";
    
    data.strApt = stripFieldStart(data.strApt, data.strPlace);
    Matcher match = APT_PTN.matcher(data.strApt);
    if (match.matches()) data.strApt = match.group(1);
    return true;
  }

}
