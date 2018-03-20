package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCIredellCountyBParser extends DispatchOSSIParser {
  
  public NCIredellCountyBParser() {
    super(CITY_CODES, "IREDELL COUNTY", "NC", 
          "( UNIT/Z ENROUTE ADDR CITY CALL SRC END " + 
          "| CALL ADDR/Z CITY/Y! INFO/N+? SRC END " +
          "| CALL PRI ADDR! X X INFO/N+? SRC END )");
  }
  
  @Override
  public String getFilter() {
    return "@co.iredell.nc.us";
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z0-9]+");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (UNIT_PTN.matcher(subject).matches()) data.strUnit = subject;
    if (body.contains(",Enroute,")) {
      return parseFields(body.split(","), data);
    } else {
      if (!super.parseMsg("CAD:" + body, data)) return false;
      if (data.strCity.length() > 0) data.msgType = MsgType.RUN_REPORT;
      return true;
    }
  }
  
  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ENROUTE")) return new SkipField("Enroute", true);
    if (name.equals("PRI")) return new PriorityField("[P1-9]?", true);
    if (name.equals("SRC")) return new SourceField("[a-z]+\\d*", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CLE", "CLEVELANE",
      "HAR", "HARMONY",
      "MOC", "MOCKSVILLE",
      "MOR", "MOORESVILLE",
      "MTUL","MT ULLA",
      "STA", "STATESVILLE",
      "STO", "STONEY POINT",
      "TRO", "TROUTMAN",
      "OLI", "OLIN TWP"
  });

}
