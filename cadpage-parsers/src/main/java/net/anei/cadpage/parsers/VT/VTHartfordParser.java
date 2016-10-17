package net.anei.cadpage.parsers.VT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class VTHartfordParser extends DispatchA19Parser {
  
  public VTHartfordParser() {
    super("HARTFORD", "VT");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (NH_CITIES.contains(data.strCity.toUpperCase())) data.strState = "NH";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY",  "CITY ST");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Set<String> NH_CITIES = new HashSet<String>(Arrays.asList(new String[]{
      "CLAREMONT"
  }));
  
}
