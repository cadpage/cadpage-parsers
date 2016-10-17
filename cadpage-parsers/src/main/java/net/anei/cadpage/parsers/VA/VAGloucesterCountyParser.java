package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class VAGloucesterCountyParser extends FieldProgramParser {
  
  public VAGloucesterCountyParser() {
    super(CITIES, "GLOUCESTER COUNTY", "VA", 
        "Event_Number:ID! Category:CALL! Sub_Category:SKIP! Description:INFO! INFO+ Address:ADDR/S! ZIP? TIMES+");
    allowBadChars("()");  
  }
  
  @Override
  public String getFilter() {
    return "GCSO@GLOUCESTERVA.INFO";
  }

  @Override
  public boolean parseMsg(String head, String body, Data data) {
    return parseFields(body.split("\n"), data);    
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern ADDR_ZIP_PATTERN = Pattern.compile("(.*), *(\\d{5})");
  private static final Pattern ROUTE_PATTERN = Pattern.compile("- *\\(?(RT +\\d+\\b)\\)?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ZIP_PATTERN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      } else {
        field = stripFieldEnd(field, ",");
      }
      field = ROUTE_PATTERN.matcher(field).replaceAll("($1)");
      super.parse(field, data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" CITY";
    }
  }
  
  private static final Pattern ZIP_PATTERN = Pattern.compile("\\d{5}");
  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0 && ZIP_PATTERN.matcher(field).matches()) {
        data.strCity = field;
      }
    }
  }
  
  private static final Pattern TIMES_PTN = Pattern.compile("(.*) Date / Time: +(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (match.matches()) {
        String type = match.group(1);
        if (type.equals("Opened") || type.equals("Dispatch")) {
          data.strDate = match.group(2);
          setTime(TIME_FMT, match.group(3), data);
        }
        else if (type.equals("Arrival")) {
          data.msgType = MsgType.RUN_REPORT;
        }
      }
      data.strSupp = append(data.strSupp, "\n", field);
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
  
  private static final String[] CITIES = {
    "GLOUCESTER POINT",
    "GLOUCESTER",
    "HAYES",
    "JAMES STORE",
    "WHITE MARSH",
    "ORDINARY",
    "WICOMICO"
  };
}
