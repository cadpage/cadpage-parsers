package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class NJMorrisCountyDParser extends DispatchProQAParser {
  
  public NJMorrisCountyDParser() {
    super("MORRIS COUNTY", "NJ", 
          "ADDR APT MISC CITY CALL! CALL/L+? TIME! INFO/L+", true);
  }
  
  @Override
  public String getFilter() {
    return "autopage@monoc.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MISC")) return new MyMiscField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern MISC_APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT|SUITE|UNIT) +(.*)", Pattern.CASE_INSENSITIVE);
  private class MyMiscField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MISC_APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        return;
      }
      if (field.endsWith(" FL")) {
        data.strApt = append(data.strApt, "-", field);
        return;
      }
      if (field.startsWith("X ")) {
        data.strCross = field.substring(2).trim();
        return;
      }
      if (isValidAddress(field)) {
        data.strCross = field;
        return;
      }
      data.strPlace = field;
    }
    
    @Override
    public String getFieldNames() {
      return "APT X PLACE";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field.toUpperCase(), " BORO");
      super.parse(field, data);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("<Unknown>") || field.equals("<None>")) return;
      super.parse(field, data);
    }
  }
  
  

}
