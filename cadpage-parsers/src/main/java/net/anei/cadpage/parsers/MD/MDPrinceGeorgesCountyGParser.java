package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Prince Georges County, MD (variant E)
 */
public class MDPrinceGeorgesCountyGParser extends MDPrinceGeorgesCountyBaseParser {
  
  public MDPrinceGeorgesCountyGParser() {
    super("CALL ADDR! APT? PLACE/CS+? X! CH? BOX Units:UNIT% UNIT+ Remarks:INFO");
  }
  
  @Override
  public String getFilter() {
    return "@alert.co.pg.md.us,@c-msg.net,14100,12101,@everbridge.net,89361,87844";
  }
  
  private static final Pattern HTML_FILTER_PTN = Pattern.compile("\n {3,}<p> (.*?)</p>\n {3,}", Pattern.DOTALL);
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<!doctype html>\n")) {
      Matcher match = HTML_FILTER_PTN.matcher(body);
      if (!match.find()) return false;
      body = match.group(1).trim();  
      body = body.replace("\n<br>", " ").replace("\n", " ").replace("<br>", " ");
    }

    return super.parseHtmlMsg(subject, body, data);
  }
  
  private static final Pattern ID_PTN = Pattern.compile("(PF\\d{14}): *");

  @Override
  public boolean parseMsg(String body, Data data) {
    
    Matcher match = ID_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end()).trim();
    
    body = body.replace(" Unit:", " Units:");
    body = body.replace(". Remarks:", ", Remarks:");
    if (!parseFields(body.split(","), data)) return false;
    
    fixCity(data);
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram().replace("BOX", "BOX CITY ST");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new AptField("# *(.*)|([A-Z]?\\d{1,5}[A-Z]?)", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CH")) return new ChannelField("(?:T?G?|FX)[A-F]\\d{1,2}", true);
    if (name.equals("BOX")) return new BoxField("\\d{4}[A-Z]{0,3}|MA[A-Z]{2}");
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  // Cross field only exist if it has the correct keyword
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("btwn")) return true;
      if (!field.startsWith("btwn ")) return false;
      field = field.substring(5).trim();
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      field = field.replace(" and ", " / ");
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  // Info field drops ProQA comments
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains("ProQA recommends dispatch")) return;
      field = stripFieldStart(field, "CC TEXT:");
      super.parse(field, data);
    }
  }
  
  // Unit fields join together with comma separators
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("WI")) {
        if (!data.strCall.contains("(Working)")) data.strCall += " (Working)";
      } else {
        data.strUnit = append(data.strUnit, ",", field);
      }
    }
  }
  
  static void fixCity(Data data) {
    if (data.strCity.length() > 0) return;
    if (data.strBox.startsWith("MA")) {
      String city = MA_CITY_TABLE.getProperty(data.strBox);
      if (city != null) {
        int pt = city.lastIndexOf('/');
        if (pt >= 0) {
          data.strState = city.substring(pt+1);
          city = city.substring(0,pt);
        }
        data.strCity = city;
      }
    }
  }
  
  private static final Properties MA_CITY_TABLE = buildCodeTable(new String[]{
      "MAAA", "ANNE ARUNDEL COUNTY",
      "MACA", "CALVERT COUNTY",
      "MAAL", "ALEXANDRIA COUNTY/VA",
      "MACC", "CALVERT COUNTY",
      "MACH", "CHARLES COUNTY",
      "MADC", "DC",
      "MAFA", "FAIRFAX COUNTY/VA",
//      "MAHC", null,    // ambiguous
      "MAHO", "HOWARD COUNTY",
      "MAMO", "MONTGOMERY COUNTY",
      "MAMC", "MONTGOMERY COUNTY",
      "MASM", "ST MARYS COUNTY"

  });
}
