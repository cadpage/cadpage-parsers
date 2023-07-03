package net.anei.cadpage.parsers.NC;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Alleghany County, NC
 */
public class NCAlleghanyCountyAParser extends FieldProgramParser {
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(\\d{4}-\\d{5}),.*\\(DISPATCHED\\).*");
  
  public NCAlleghanyCountyAParser() {
    super(CITY_LIST, "ALLEGHANY COUNTY", "NC",
          "ADDR/S ID TIME CALL");
  }

  @Override
  public String getFilter() {
    return "cad@alleghanycounty-nc.gov";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "RUN REPORT";
      data.strCallId = match.group(1);
      data.strPlace = body;
      return true;
    }
    
    body = stripFieldEnd(body, "-");
    return parseFields(body.split(","), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{5}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "SPARTA",

    // Townships
    "CHERRY LANE TWP",
    "CRANBERRY TWP",
    "GAP CIVIL TWP",
    "GLADE CREEK TWP",
    "PINEY CREEK TWP",
    "PRATHERS CREEK TWP",
    "WHITEHEAD TWP",

    // Unincorporated communities
    "CHERRY LANE",
    "ENNICE",
    "GLADE VALLEY",
    "LAUREL SPRINGS",
    "PINEY CREEK",
    "ROARING GAP",
    "SCOTTVILLE",
    "TWIN OAKS",
   
  };
}
