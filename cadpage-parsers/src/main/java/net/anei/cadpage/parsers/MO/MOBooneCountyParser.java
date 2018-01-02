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
      "( NEW_CALL ID ADDR/y APT GPS1 GPS2 ZIP PLACE X2 INFO/CS+? CALL DATE TIME SRC BOX UNIT PHONE! END" +
      "| NEWCALL ID ADDR/y APT GPS1 GPS2 ZIP PLACE X X INFO EMPTY EMPTY CALL PRI BOX DATE TIME TIME SRC MAP INFO INFO INFO UNIT NAME! NAME? EMPTY PHONE )");
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
    
    if (!super.parseFields(body.split(",", -1
        ), data)) return false;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("NEW_CALL")) return new SkipField("NEW CALL|RE-ENCODE", true);
    if (name.equals("ID")) return new IdField("\\d{9}", true);
    if (name.equals("ZIP")) return new MyZipCityField();
    if (name.equals("X2")) return new MyCross2Field();
    
    if (name.equals("NEWCALL")) return new SkipField("NEWCALL", true);
    if (name.equals("CALL")) return new MyCallCodeField();
    if (name.equals("PRI")) return new PriorityField("\\d?", true);
    if (name.equals("SRC")) return new SourceField(".{2}|", true);
    if (name.equals("MAP")) return new MapField("((\\d{2}-\\d{1,2}[A-Z]?)|SP1|)", true);
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("PHONE")) return new PhoneField("|\\d{7}(?:\\d{3})?|\\d{3}-(?:\\d{3}-)?\\d{4}|\\d{6}-\\d{4}", true);
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
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(\\S+)-(\\S.*)");
  private class MyCallCodeField extends CallField {
    
    @Override
    public boolean canFail() {
      return true;
    }
  
    @Override
    public boolean checkParse(String field, Data data) {
      
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCode = match.group(1);
      data.strCall = match.group(2);
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
  
  private class MyNameField extends NameField {
    
    @Override
    public void parse(String field, Data data) {
      data.strName = append(data.strName, ", ", field);
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
