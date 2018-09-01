package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class MOStFrancoisCountyBParser extends FieldProgramParser {
  
  public MOStFrancoisCountyBParser() {
    this("ST FRANCOIS COUNTY", "MO");
  }
  
  public MOStFrancoisCountyBParser(String defCity, String defState) {
    super(defCity, defState, 
         "( SELECT/2 ADDRCITY PLACE? UNIT2 CALL! INFO+ " +
         "| CALL:CALL1! PLACE:PLACE ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! UNIT:UNIT% INFO:INFO/N+ )");
  }
  
  @Override
  public String getAliasCode() {
    return "MOStFrancoisCountyB";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern MARKER = Pattern.compile("DISPATCH:([A-Z0-9]+:[A-Z0-9]+) - +");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("1");
      data.strSource = match.group(1);
      body = body.substring(match.end());
      return parseFields(body.split("\n"), data);
    }
    
    setSelectValue("2");
    return parseFields(body.split("//"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("DATE"))  return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT2")) return new UnitField("[A-Z0-9]+:[A-Z0-9]+(?:,[A-Z0-9]+:[A-Z0-9]+)*", true);
    return super.getField(name);
  }
  
  private class MyCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        data.strCode = field.substring(0, pt);
        field = field.substring(pt+1).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
