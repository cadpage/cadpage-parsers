package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA12Parser;


public class MOCapeGirardeauCountyAParser extends DispatchA12Parser {
  
  public MOCapeGirardeauCountyAParser() {
    super("CAPE GIRARDEAU COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "CAD@CityofCape.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strPlace.equals("Resident")) data.strPlace = "";
    else if (PLACE_NAME_PTN.matcher(data.strPlace).matches()) {
      data.strName = data.strPlace;
      data.strPlace = "";
    }
    return true;
  }
    
  @Override
  public String getProgram() {
    return super.getProgram().replace("PLACE", "PLACE NAME");
  }
    
    
  private static final Pattern PLACE_NAME_PTN = Pattern.compile("[A-Z]+, +[A-Z]+", Pattern.CASE_INSENSITIVE);
}
