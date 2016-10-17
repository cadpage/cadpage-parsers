package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NYNiagaraCountyBParser extends FieldProgramParser {
  
  public NYNiagaraCountyBParser() {
    super("NIAGARA COUNTY", "NY", 
          "DATETIME! Inc_Type:CALL! Location:ADDR! X:X! Name:NAME! Nature:INFO! Narrative:INFO/N! INFO/N+ Inc_#:ID! Units:UNIT! Times:TIMES+ http:SKIP");
  }
  
  @Override
  public String getFilter() {
    return "@niagaracounty.com,7127390583";
  }
  
  private String times;
  
  private static final Pattern TRI_COMM_PTN = Pattern.compile("(.*)\nOn:([^\n]+)\n(.*?)(?:,? +\nhttp:.*)?", Pattern.DOTALL);
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Fix badly scrambled calls :(
    if (subject.equals("Tri Community")) {
      data.strSource = subject;
      body = body.replace("\n@", "\nLocation:").replace("\nNam:", "\nName:").replace("\nN:", "\nNature:");
      Matcher match = TRI_COMM_PTN.matcher(body);
      if (!match.matches()) return false;
      body = match.group(2) + "\nInc Type:" + match.group(1) + '\n' + match.group(3) + "\nNarrative: \nInc #: \nUnits:";
    }
    
    // Otherwise conform subject and parse normally
    else if (!subject.startsWith("Automatic R&R Notification")) return false;
    
    
    times = "";
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d [AP]M", DATE_TIME_FMT, true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String apt = p.getLastOptional(',');
      data.strCity = p.getLastOptional(',');
      if (apt.length() == 0) {
        field = p.get();
      } else {
        String apt2 = p.getLastOptional(' ');
        field = p.get();
        if (!apt2.equals(apt)) field = append(field, " ", apt2);
      }
      field = field.replace('@', '&');
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\t")) {
        line = line.trim();
        if (data.msgType == MsgType.RUN_REPORT) {
          data.strSupp = append(data.strSupp, "\n", line);
        } else {
          times = append(times, "\n", line);
          if (line.startsWith("Cleared at:")) {
            data.msgType = MsgType.RUN_REPORT;
            data.strSupp = times;
          }
        }
      }
    }
  }
  
}
