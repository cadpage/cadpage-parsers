package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Floyd County, GA
 */
public class DispatchA60Parser extends FieldProgramParser {
  
  public DispatchA60Parser(String defCity, String defState) {
    super(defCity, defState,
          "CALL ADDR INFO UNIT DATETIME!");
  }
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(": "), data);
  }
  
  private static final String
    DATE_PATTERN_S = "\\d\\d/\\d\\d/\\d\\d",
    TIME_PATTERN_S = "\\d\\d:\\d\\d",
    EXTM_PATTERN_S = TIME_PATTERN_S+":\\d\\d";
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new MyDateTimeField(DATE_PATTERN_S+" +"+TIME_PATTERN_S, true);
    return super.getField(name);
  }

  private static final Pattern CITY_PATTERN = Pattern.compile("(.*), *([A-Z][ A-Z]{2,})(?:, *([A-Z]{2})(?: +\\d{5})?)?");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      
      // Strip off leading '='
      field = stripFieldStart(field, "=");
      Matcher m = CITY_PATTERN.matcher(field);
      if (m.matches()) {
        field = m.group(1).trim();
        data.strCity = m.group(2).trim();
        data.strState = getOptGroup(m.group(3));
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" CITY ST";
    }
  }
  
  private static final Pattern LOG_PATTERN
    = Pattern.compile("("+DATE_PATTERN_S+") +("+EXTM_PATTERN_S+") - LOG -(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (!field.equals("None"))
        parseEntries(field.split("; *"), data);
    }
    
    private void parseEntries(String[]f, Data data) {
      for (String part : f) parseEntry(part.trim(), data);
    }
    
    private void parseEntry(String field, Data data) {
      Matcher m = LOG_PATTERN.matcher(field);
      if (m.matches()) {
        data.strDate = m.group(1);
        data.strTime = m.group(2);
        field = m.group(3).trim();
      }
      data.strSupp = append(data.strSupp, "\n", field);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" DATE TIME";
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace("; ", ","), data);
    }
  }

  private class MyDateTimeField extends DateTimeField {
    public MyDateTimeField(String p, boolean h) {
      super(p, h);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (data.strDate.length() == 0) {
        super.parse(field, data);
      }
    }
  }
}
