package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Franklin County, PA
 */
public class PAFranklinCountyAParser extends FieldProgramParser {
  
  public PAFranklinCountyAParser() {
    this("FRANKLIN COUNTY", "PA");
  }
  
  protected PAFranklinCountyAParser(String defCity, String defState) {
    super(defCity, defState,
          "ID? ADDR APT APT CITY EMPTY EMPTY CH EMPTY CALL EMPTY EMPTY EMPTY UNIT! BOX INFO+");
  }

  @Override
  public String getAliasCode() {
    return "PAFranklinCounty";
  }

  @Override
  public String getFilter() {
    return "PageGate@franklindes.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = stripFieldStart(body, "*");
    
    if (body.endsWith("*")) body = body + ' ';
    if (!parseFields(body.split("\\* "), data)) return false;
    if (data.strCity.endsWith(" BORO")) data.strCity = data.strCity.substring(0,data.strCity.length()-5).trim();
    if (data.strCity.equals("LEAD")) data.strCity = "CHAMBERSBURG";
    if (data.strCity.endsWith(" MD")) {
      data.strState = "MD";
      data.strCity = data.strCity.substring(0,data.strCity.length()-3).trim();
    }
    if (data.strCity.endsWith(" CO")) {
      data.strCity += "UNTY";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{11}", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOT|RM|#) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void  parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', '_').replace('-', '_');
      super.parse(field, data);
    }
  }
}
