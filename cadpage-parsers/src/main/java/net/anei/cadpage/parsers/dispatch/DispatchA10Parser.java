package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA10Parser extends FieldProgramParser {

  private MyCrossField crossField; 

  public DispatchA10Parser(String[] cityList, String defCity, String defState, String program) {
    super(cityList, defCity, defState, program);
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    crossField.reset();
    return parseFields(body.split(",", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return (crossField != null ? crossField : (crossField = new MyCrossField()));
    if (name.equals("X2")) return new BaseCross2Field();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  // Complicated cross field processing results from the unfortunate fact that
  // the separate up to two cross streets into a cross street and city name
  // separated by a comma.  But we use comma as a field separator, so what should
  // be one fairly simple cross street field becomes up two 3 different state fields
  // that must be processed as a state engine
  private class MyCrossField extends CrossField {
    private int state = 0;
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    public void reset() {
      state = 0;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // Switch on current state
      switch (state) {
      
      case 0:  // First cross field

        // Normally the first field contains the first cross
        // street and does not contain a double //
        if (!field.contains("//")) {
          data.strCross = field;
          state = 1;
          break;
        }

        // Otherwise, this really the second field
        // so drop into second field processing
        state = 1;
      
      case 1:  // Second cross field
        
        // This has to contain a double // that splits this field into two parts
        int pt = field.indexOf("//");
        if (pt < 0) abort();
        String part1 = field.substring(0,pt).trim();
        String part2 = field.substring(pt+2).trim();
        
        // If we have a cross street, part1 is the cross street city and can be skipped
        // If we don't, part 1 is the first cross street
        if (part1.length() > 0) {
          if (data.strCross.length() == 0) data.strCross = part1;
          else if (data.strCity.length() == 0) data.strCity = part1;
        }
        
        // If there is no part2, then there is no second part street and we are finished
        if (part2.length() == 0) {
          state = 3;
        }
        
        // Otherwise part2 is the cross street and we have to skip over another  city field
        data.strCross = append(data.strCross, " & ", part2);
        state = 2;
        break;
        
      case 2: // Third cross field
        
        // We expect the second street city to be the same as the main address city
        // If it isn't, treat this as not being a cross street
        state = 3;
        if (!field.equals(data.strCity)) return false;
        break;
        
      case 3: // All fields processed
        return false;
      }
      
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern CROSS2_PTN = Pattern.compile("(.*?) */{2,} *(.*)");
  private class BaseCross2Field extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CROSS2_PTN.matcher(field);
      if (!match.matches()) return false;
      field = append(match.group(1), " & ", match.group(2));
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, ", ", field);
    }
  }
}
