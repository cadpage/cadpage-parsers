package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA30Parser extends FieldProgramParser {
  
  private static final Pattern INCIDENT_NO_PTN = Pattern.compile("YIncident No.: *(\\d+) +");
  private static final Pattern MISSED_DELIM_PTN = Pattern.compile(" (?=Complaint No.:|Category:|Fire:|EMS:)");

  protected DispatchA30Parser(String[] cityList, String defCity, String defState) {
    this(cityList, "", defCity, defState);
  }

  protected DispatchA30Parser(String[] cityList, String type, String defCity, String defState) {
    super(cityList, defCity, defState,
           "Incident_No.:ID! Complaint_No.:SKIP? Category:CALL! Address:ADDR/SXXa! Open_DateTime_/_DSN:DATETIME? Dispatch_DateTime:DATETIME? Police:" + calcSrcType("P", type) + " Fire:" + calcSrcType("F", type) + " EMS:" + calcSrcType("E", type) + " Business_Name:PLACE? Incident_Notes:INFO INFO+?");
  }
  
  private static String calcSrcType(String srcType, String defType) {
    return (srcType.equals(defType) ? "DEF_SRC?" : "SRC?");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Call")) return false;
    
    if (body.contains("\nArrival DateTime: ")) {
      data.strCall = "RUN REPORT";
      Matcher match = INCIDENT_NO_PTN.matcher(body);
      if (match.find()) {
        data.strCallId = match.group(1);
        body = body.substring(match.end());
      }
      data.strPlace = body;
      return true;
    }
    
    body = MISSED_DELIM_PTN.matcher(body).replaceAll("\n");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("DEF_SRC")) return new MySourceField(true);
    if (name.equals("SRC")) return new MySourceField(false);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("/")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private class MySourceField extends SourceField {
    
    private boolean defSource;
    
    public MySourceField(boolean defSource) {
      this.defSource = defSource;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!defSource && data.strSource.length() > 0) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_SKIP_PTN = Pattern.compile("Call Received on .*|");
  private static final Pattern INFO_END_PTN = Pattern.compile("Log Date Time .*|City: .*");
  private class MyInfoField extends InfoField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (INFO_END_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
    
    @Override 
    public void parse(String field, Data data) {
      if (INFO_SKIP_PTN.matcher(field).matches()) return;
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
