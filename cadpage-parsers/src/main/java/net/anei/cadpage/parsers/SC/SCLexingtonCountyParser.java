package net.anei.cadpage.parsers.SC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class SCLexingtonCountyParser extends DispatchOSSIParser {
  
  public SCLexingtonCountyParser() {
    super(CITY_CODES, "LEXINGTON COUNTY", "SC",
          "( CANCEL ADDR CITY | FYI SRC? CALL ADDR! ( X/Z PLACE CITY | X_PLACE CITY | CITY | ) UNIT? ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@lex-co.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD")) return false;
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("(?!MVC)[A-Z]{3,4}", true);
    if (name.equals("X_PLACE")) return new MyCrossPlaceField();
    if (name.equals("UNIT")) return new UnitField("(?:IBAT|[A-Z]+\\d+)(?:,[A-Z0-9]+)*", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCrossPlaceField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (isValidAddress(field)) {
        data.strCross = field;
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "X PLACE";
    }
  }
  
  private Pattern INFO_TRIM_PTN = Pattern.compile("[-* ]*(.*?)[-* ]*");
  private Pattern INFO_CH_PTN = Pattern.compile("OPS *\\d+", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_TRIM_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      
      if (INFO_CH_PTN.matcher(field).matches()) {
        data.strChannel = field;
      } else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BA", "BATESBURG",
      "BL", "BATESBURG LEESVILE",
      "CA", "CAYCE",
      "CH", "CHAPIN",
      "CO", "COLUMBIA",
      "GA", "GASTON",
      "GI", "GILBERT",
      "IR", "IRMO",
      "LE", "LEESVILLE",
      "LX", "LEXINGTON",
      "PE", "PELION",
      "PI", "PINE RIDGE",
      "SC", "SOUTH CONGAREE",
      "SP", "SPRINGDALE",
      "SW", "SWANSEA",
      "WC", "WEST COLUMBIA"
  }); 
}
