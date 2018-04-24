package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COJeffersonCountyDParser extends FieldProgramParser {
  
  public COJeffersonCountyDParser() {
    super("JEFFERSON COUNTY", "CO", 
          "CALL ADDR APT CITY X MAP GPS1/d GPS2/d UNIT UNIT/C+? CH ID TIME! END");
  }
  
  @Override
  public String getFilter() {
    return "CADPage@jeffcom911.org,messaging@iamresponding.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern DELIM = Pattern.compile("[ ,],|,(?= |\\d\\d:\\d\\d$)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Message")) data.strSource = subject;
    
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    String[] flds = body.split("\n");
    if (flds.length < 12) flds = DELIM.split(body);
    if (!parseFields(flds, data)) return false;
    if (data.strCity.equals("UNINC JEFFERSON")) data.strCity = "JEFFERSON COUNTY";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MapField("[A-Z]-\\d{1,2}-[A-Z]", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Za-z0-9]+)-(\\S.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|SUITE|LOT) *(.*)");
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("Unk Cross Street", "");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
