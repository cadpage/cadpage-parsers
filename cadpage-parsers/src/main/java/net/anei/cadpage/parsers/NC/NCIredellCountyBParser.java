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
          "| CALL ADDR/Z CITY/Y! INFO/N+? ( UNIT2 SRC | SRC ) END " +
          "| CALL PRI ADDR! X X INFO/N+? ( UNIT2 SRC | SRC ) END )");
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
      return parseFields(body.split("[,;]"), data);
    } else {
      if (!super.parseMsg("CAD:" + body, data)) return false;
      if (data.strCity.length() > 0) data.msgType = MsgType.RUN_REPORT;
      return true;
    }
  }
  
  @Override
  public String getProgram() {
    return "UNIT? " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ENROUTE")) return new SkipField("Enroute", true);
    if (name.equals("PRI")) return new PriorityField("[P1-9]?", true);
    if (name.equals("UNIT2")) return new MyUnit2Field();
    if (name.equals("SRC")) return new SourceField("[a-z]+\\d*", true);
    return super.getField(name);
  }
  
  private static final Pattern UNIT_PTN2 = Pattern.compile("[A-Z]+,[A-Z][A-Z0-9,]*");
  private class MyUnit2Field extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField(+2)) return false;
      if (!UNIT_PTN2.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
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
