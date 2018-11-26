package net.anei.cadpage.parsers.NY;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchRedAlertParser;



public class NYErieCountyRedAlertParser extends DispatchRedAlertParser {
  
  private static final Pattern TRAILER_MARK = Pattern.compile("(?<= \\d\\d:\\d\\d:\\d\\d)\n");
  
  public NYErieCountyRedAlertParser() {
    super("ERIE COUNTY","NY");
    setupMultiWordStreets(MWORD_STREET_LIST);
    addRoadSuffixTerms("TW");
  }

  @Override
  public String getFilter() {
    return "dispatch@townofhamburgny.com,dispatch@townofhamburg.com,hamburg@rednmxcad.com,777";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("Erie_Alert ")) return false;
    if (body.startsWith("*2")) body = body.substring(2).trim();
    
    Matcher match = TRAILER_MARK.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();
    body = body.replace('\n', ' ');
    return super.parseMsg(subject, body, data);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return TW_PTN.matcher(addr).replaceAll("THRUWAY");
  }
  private static final Pattern TW_PTN = Pattern.compile("\\bTW\\b", Pattern.CASE_INSENSITIVE);
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ARMOR DUELLS",
    "BACK CREEK",
    "BIG TREE",
    "BOSTON COLDEN",
    "BOSTON CROSS",
    "BOSTON STATE",
    "BROWN HILL",
    "CHESTNUT RIDGE",
    "EAST EDEN",
    "EAST HILL",
    "EDEN EVANS CNTR",
    "END OF",
    "GOWANDA STATE",
    "HAYES HOLLOW",
    "HIDDEN HOLLOW",
    "HOLLAND GLENWOOD",
    "LA SALLE",
    "LAKE HEIGHTS",
    "LAKE SHORE",
    "LEDGES PARK",
    "LOWER EAST HILL",
    "MURRAY HILL",
    "NEW YORK STATE",
    "PIN OAK",
    "POLISH HILL",
    "SENECA BROOK",
    "SOUTH PARK",
    "VALLEY CIRCLE",
    "VALLEY VIEW"
  };
}
