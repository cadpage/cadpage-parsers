package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Boone County, MO
 */
public class MOBooneCountyParser extends FieldProgramParser {
  
  public MOBooneCountyParser() {
    super(CITY_CODES, "BOONE COUNTY", "MO",
          "NEW_CALL ID ADDR/y APT GPS1 GPS2 ZIP PLACE X2 INFO/CS+? CALL DATE TIME SRC BOX UNIT PHONE! END");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
    };
  }

  private static final Pattern START_NOTE_PTN = Pattern.compile(",(?:(NOTES \\*?)|[ A-Za-z]*\\bHSD +HSD:|CN MED |[ A-Za-z]+$) *");
  private static final Pattern END_NOTE_PTN = Pattern.compile("\\(\\d{8}-\\d{3}\\)$");
  
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Look for 1 of 2 different NOTE indicators.  One of which should expect
    // a particular terminator
    String extraInfo = "";
    Matcher match = START_NOTE_PTN.matcher(body);
    if (match.find()) {
      extraInfo = body.substring(match.end());
      body = body.substring(0,match.start()).trim();
      if (match.group(1) != null) {
        if (!END_NOTE_PTN.matcher(extraInfo).find()) {
          data.expectMore = true;
        }
      }
    }
    
    if (!super.parseFields(body.split(",", -1), data)) return false;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("NEW_CALL")) return new SkipField("NEW CALL|RE-ENCODE", true);
    if (name.equals("ID")) return new IdField("\\d{9}", true);
    if (name.equals("ZIP")) return new MyZipCityField();
    if (name.equals("X2")) return new MyCross2Field();
    if (name.equals("CALL")) return new MyCallCodeField();
    if (name.equals("DATE"))  return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("SRC")) return new SourceField(".{2,3}|", true);
    if (name.equals("PHONE")) return new PhoneField("|\\d{7}(?:\\d{3})?|\\d{3}-(?:\\d{3}-)?\\d{4}|\\d{6}-\\d{4}|[A-F]", true);
    return super.getField(name);
  }
  
  private class MyZipCityField extends Field {
    
    public MyZipCityField() {
      super("\\d{5}|", true);
    }
  
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0) {
        data.strCity = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY";
    }
  }  
  
  private class MyCross2Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT, field);
      if (res.isValid()) {
        res.getData(data);
        field = res.getLeft();
      }
      data.strSupp = append(data.strSupp, " / ", field);
    }
    
    @Override
    public String getFieldNames() {
      return "X INFO";
    }
  }
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(\\S+)-(\\S.*)|(EMS\\.)");
  private class MyCallCodeField extends CallField {
    
    @Override
    public boolean canFail() {
      return true;
    }
  
    @Override
    public boolean checkParse(String field, Data data) {
      
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (!match.matches()) return false;
      String code = match.group(1);
      if (code == null) {
        data.strCall = match.group(3);
      } else { 
        data.strCode = code;
        data.strCall = match.group(2);
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AS",   "ASHLAND",
      "CO",   "COLUMBIA",
      "HB",   "HARTSBURG",
      "RO",   "ROCHEPORT",
      
      "BC",   ""
     });
}
