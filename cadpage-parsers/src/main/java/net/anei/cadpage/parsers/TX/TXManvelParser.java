package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class TXManvelParser extends FieldProgramParser {
  
  private static final String[] CITY_LIST = new String[]{
    "MANVEL"
  };

  public TXManvelParser() {
    super(CITY_LIST, "MANVEL", "TX",
           "SRC CALL ADDR PLACE? CITY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@manvelpd.org";
  }
  
  @Override
  public String getSponsor() {
    return "Manvel Volunteer Fire Department";
  }
  
  @Override
  public String getSponsorDateString() {
    return "02172014";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.equals("NEW CALL")) {}
    else if (body.startsWith("NEW CALL / ")) {
      body = body.substring(10).trim();
    } else {
      return false;
    }
    
    return parseFields(body.split("\n"), data);
  }

}
